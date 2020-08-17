package com.jy.woven.plugin;

import org.objectweb.asm.AnnotationVisitor;

public class WAnnotationVisitor extends AnnotationVisitor {

    public String desc;
    public String name;
    public String value;

    public WAnnotationVisitor(int api, AnnotationVisitor av, String desc) {
        super(api, av);
        this.desc = desc;
    }

    @Override
    public void visit(String name, Object value) {
        super.visit(name, value);
        this.name = name;
        this.value = value.toString();
    }
}
