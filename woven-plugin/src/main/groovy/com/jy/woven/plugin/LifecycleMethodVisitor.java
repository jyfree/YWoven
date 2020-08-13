package com.jy.woven.plugin;

import com.jy.woven.annotation.RunTimeTrace;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LifecycleMethodVisitor extends MethodVisitor {

    private static final String TAG = "LifecycleMethodVisitor: ";

    private String name;

    public LifecycleMethodVisitor(MethodVisitor methodVisitor, String name) {
        super(Opcodes.ASM6, methodVisitor);
        this.name = name;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        Logger.info(TAG + "visitAnnotation ----%s", desc);
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public void visitCode() {

//        Logger.info(TAG + "visitCode ----> %s", name);
        //方法执行前插入
        mv.visitLdcInsn(Const.NAME);
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitLdcInsn("-------> " + name + ": ");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getSimpleName", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);

        super.visitCode();
        //方法执行后插入
    }
}
