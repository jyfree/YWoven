package com.jy.woven.plugin;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LifecycleClassVisitor extends ClassVisitor implements Opcodes {

    private static final String TAG = "LifecycleClassVisitor: ";

    private String mClassName;

    public LifecycleClassVisitor(ClassVisitor classVisitor) {
        super(ASM6, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces) {
        Logger.info(TAG + "visit -----> started ---->%s", name);
        this.mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
//        YLogger.info(TAG + "visitMethod : %s", name);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (mv == null) return null;

        if (MatchUtils.matchLifecycleClassName(mClassName)) {
            if ("onCreate".equals(name)) {
                //处理onCreate
                return new LifecycleMethodVisitor(mv, name);
            } else if ("onDestroy".equals(name)) {
                //处理onDestroy
                return new LifecycleMethodVisitor(mv, name);
            }
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        Logger.info(TAG + "visit -----> end");
        super.visitEnd();
    }
}
