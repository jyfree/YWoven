package com.jy.woven.api.exception;

/**
 * @description 切入行为异常
 * @date: 2020/8/18 10:24
 * @author: jy
 */
public class NoSuchActionException extends Exception {

    private static final long serialVersionUID = 3256444698657634352L;
    private String name;

    public NoSuchActionException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}