package com.jy.woven.api.itf;

import com.jy.woven.api.AdviceKind;

public interface Advice {
    AdviceKind getKind();

    String getName();

    String getPointcut();

    int getModifiers();

    Class<?>[] getParameterTypes();
}
