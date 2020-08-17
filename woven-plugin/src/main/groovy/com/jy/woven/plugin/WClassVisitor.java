package com.jy.woven.plugin;


import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class WClassVisitor extends ClassVisitor implements Opcodes {

    private static final String TAG = "WClassVisitor: ";

    private String mClassName;
    private WAnnotationVisitor wAnnotationVisitor;

    public WClassVisitor(ClassVisitor classVisitor) {
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
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        Logger.info(TAG + "visitAnnotation -------->%s", desc);
        AnnotationVisitor annotationVisitor = super.visitAnnotation(desc, visible);
        if (annotationVisitor != null && desc != null) {
            wAnnotationVisitor = new WAnnotationVisitor(Opcodes.ASM6, annotationVisitor, desc);
            return wAnnotationVisitor;
        }
        return annotationVisitor;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
//        YLogger.info(TAG + "visitMethod : %s", name);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (mv == null) return null;

        if (MatchUtils.matchClass(mClassName)) {
            if ("onCreate".equals(name)) {
                //处理onCreate
                return new WMethodVisitor(mv, name);
            } else if ("onDestroy".equals(name)) {
                //处理onDestroy
                return new WMethodVisitor(mv, name);
            } else if (wAnnotationVisitor != null) {

                return new WAdviceAdapter(Opcodes.ASM6, mv, access, name, desc);
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
