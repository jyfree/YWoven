package com.jy.woven.api.exception;

public class NoSuchAdviceException extends Exception {

    private static final long serialVersionUID = 3256444698657634352L;
    private String name;

    public NoSuchAdviceException(String name) {
        this.name = name;
    }

    /**
     * The advice name that could not be found.
     */
    public String getName() {
        return name;
    }

}