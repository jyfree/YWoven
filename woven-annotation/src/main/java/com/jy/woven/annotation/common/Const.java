package com.jy.woven.annotation.common;

public class Const {

    public static final String SPLITTER = "_";
    public static final String GEN_CLASS_IMPL_NAME = SPLITTER + "Impl";


    public static final String PKG = "com.jy.woven.";
    public static final String GEN_PKG = PKG + "generated";
    public static final String API_PKG = PKG + "api";
    public static final String IMPL_PKG = API_PKG + ".impl";
    public static final String ITF_PKG = API_PKG + ".itf";


    public static final String WOVEN_SUPER_NAME = IMPL_PKG + ".BaseWovenImpl";

    public static final String ACTION_CLASS = ITF_PKG + ".Action";
    public static final String ACTION_IMPL_CLASS = IMPL_PKG + ".ActionImpl";
    public static final String ACTION_KIND_CLASS = API_PKG + ".ActionKind";

    public static final String POINTCUT_CLASS = ITF_PKG + ".Pointcut";
    public static final String POINTCUT_IMPL_CLASS = IMPL_PKG + ".PointcutImpl";

}
