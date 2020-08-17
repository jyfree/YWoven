package com.jy.woven.plugin;

import com.android.SdkConstants;
import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;


public class WTransform extends Transform {

    private static final String TRANSFORM = "Transform: ";


    @Override
    public String getName() {
        return Const.NAME;
    }

    // 输入类型，可以使class文件，也可以是源码文件 ，这是表示输入的class文件
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    // 作用范围
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    //是否支持增量编译
    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation invocation) throws TransformException, InterruptedException, IOException {
        super.transform(invocation);
        Logger.info(TRANSFORM + "start...");
        long ms = System.currentTimeMillis();
        //遍历Transform 要消费的输入流
        TransformOutputProvider outputProvider = invocation.getOutputProvider();
        for (TransformInput input : invocation.getInputs()) {
            //JarInput 代表以 jar 包方式参与项目编译的所有本地 jar 包或远程 jar 包，可以借助它来动态添加 jar 包。
            //outputProvider 是用来获取输出目录的
            //getContentLocation方法相当于创建一个对应名称表示的目录
            //是从0 、1、2开始递增。如果是目录，名称就是对应的数字，如果是jar包就类似0.jar

            //DirectoryInput 代表以源码方式参与项目编译的所有目录结构及其目录下的源码文件
            // 可以借助于它来修改输出文件的目录结构以及目标字节码文件。


            //遍历jarInputs
            input.getJarInputs().forEach(jarInput -> {
                //处理jarInputs
                try {
                    handleJarInputs(jarInput, outputProvider);
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.error(TRANSFORM + "getJarInputs error ：%s", e.getMessage());
                }
            });

            //遍历directoryInputs
            input.getDirectoryInputs().forEach(directoryInput -> {
                //处理directoryInputs
                try {
                    handleDirectoryInput(directoryInput, outputProvider);
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.error(TRANSFORM + "getDirectoryInputs error : %s", e.getMessage());
                }
            });
        }

        Logger.info(TRANSFORM + "cost %s ms", System.currentTimeMillis() - ms);

    }


    /**
     * 处理Jar中的class文件
     */
    private void handleJarInputs(JarInput jarInput, TransformOutputProvider outputProvider) throws IOException {
        if (jarInput.getFile().getAbsolutePath().endsWith(".jar")) {
            //重名名输出文件,因为可能同名,会覆盖
            String jarName = jarInput.getName();
            String md5Name = DigestUtils.md5Hex(jarInput.getFile().getAbsolutePath());
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4);
            }
            JarFile jarFile = new JarFile(jarInput.getFile());
            Enumeration enumeration = jarFile.entries();
            File tmpFile = new File(jarInput.getFile().getParent() + File.separator + "classes_temp.jar");
            //避免上次的缓存被重复插入
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile));
            //用于保存
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement();
                String entryName = jarEntry.getName();
                ZipEntry zipEntry = new ZipEntry(entryName);
                InputStream inputStream = jarFile.getInputStream(jarEntry);

                //插桩class
                if (MatchUtils.matchJarFile(entryName)) {
                    //class文件处理
                    jarOutputStream.putNextEntry(zipEntry);
                    ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream));
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
                    WClassVisitor classVisitor = new WClassVisitor(classWriter);
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
                    byte[] code = classWriter.toByteArray();
                    jarOutputStream.write(code);
                } else {
                    jarOutputStream.putNextEntry(zipEntry);
                    jarOutputStream.write(IOUtils.toByteArray(inputStream));
                }
                jarOutputStream.closeEntry();
            }
            //结束
            jarOutputStream.close();
            jarFile.close();

            File dest = outputProvider.getContentLocation(jarName + md5Name,
                    jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR);
            FileUtils.copyFile(tmpFile, dest);
            tmpFile.delete();
        }
    }

    /**
     * 处理文件目录下的class文件
     */
    private void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) throws IOException {
        scanDir(directoryInput.getFile());

        //处理完输入文件之后，要把输出给下一个任务
        File dst = outputProvider.getContentLocation(
                directoryInput.getName(), directoryInput.getContentTypes(),
                directoryInput.getScopes(), Format.DIRECTORY);

        FileUtils.copyDirectory(directoryInput.getFile(), dst);
    }


    /**
     * 扫描dir下的文件，并修改对应的字节码
     */
    private void scanDir(File dir) throws IOException {
        if (dir.exists() && dir.isDirectory()) {
            Collection<File> files = FileUtils.listFiles(dir,
                    new SuffixFileFilter(SdkConstants.DOT_CLASS, IOCase.INSENSITIVE), TrueFileFilter.INSTANCE);
            for (File file : files) {
                String name = file.getName();
                //插桩class
                if (MatchUtils.matchDirFile(dir, file)) {

                    ClassReader classReader = new ClassReader(WFileUtils.toByte(file));
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
                    WClassVisitor classVisitor = new WClassVisitor(classWriter);
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
                    byte[] code = classWriter.toByteArray();
                    FileOutputStream fos = new FileOutputStream(
                            Objects.requireNonNull(file.getParentFile()).getAbsolutePath() + File.separator + name);
                    fos.write(code);
                    fos.close();
                }
            }
        }
    }
}
