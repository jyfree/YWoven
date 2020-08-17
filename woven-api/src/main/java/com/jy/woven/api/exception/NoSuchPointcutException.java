package com.jy.woven.api.exception;

public class NoSuchPointcutException extends Exception {

    private static final long serialVersionUID = 3256444698657634352L;
    private String name;

    public NoSuchPointcutException(String name) {
        this.name = name;
    }

    /**
     * 找不到的切入点的名称
     */
    public String getName() {
        return name;
    }
}
