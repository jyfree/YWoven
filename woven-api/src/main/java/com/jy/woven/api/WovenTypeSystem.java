package com.jy.woven.api;

import com.jy.woven.api.impl.WovenImpl;
import com.jy.woven.api.itf.WovenType;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class WovenTypeSystem {
    private static Map<Class, WeakReference<WovenType>> wovenType =
            Collections.synchronizedMap(new WeakHashMap<Class, WeakReference<WovenType>>());

    public static <T> WovenType<T> getWovenType(Class<T> fromClass) {
        WeakReference<WovenType> weakRefToWovenType = wovenType.get(fromClass);
        if (weakRefToWovenType != null) {
            WovenType<T> theWovenType = weakRefToWovenType.get();
            if (theWovenType != null) {
                return theWovenType;
            } else {
                theWovenType = new WovenImpl<>(fromClass);
                wovenType.put(fromClass, new WeakReference<WovenType>(theWovenType));
                return theWovenType;
            }
        }
        // neither key nor value was found
        WovenType<T> theWovenType = new WovenImpl<T>(fromClass);
        wovenType.put(fromClass, new WeakReference<WovenType>(theWovenType));
        return theWovenType;
    }
}
