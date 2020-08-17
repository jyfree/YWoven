package com.jy.woven.api.itf;

import com.jy.woven.api.AdviceKind;
import com.jy.woven.api.exception.NoSuchAdviceException;
import com.jy.woven.api.exception.NoSuchPointcutException;

import java.lang.reflect.Method;

public interface WovenType<T> {
    /**
     * The name of this type, in the same format as returned by Class.getName()
     */
    public String getName();

    /**
     * The package in which this type is declared
     */
    public Package getPackage();

    /**
     * The modifiers declared for this type. The return value can be interpreted
     * using java.lang.reflect.Modifier
     */
    public int getModifiers();

    /**
     * The java.lang.Class that corresponds to this WovenType
     */
    public Class<T> getJavaClass();

    /**
     * Return the method object for the specified method declared in this type
     */
    public Method[] getDeclaredMethods();

    /**
     * Return the pointcut object representing the specified pointcut declared by this type
     */
    public Pointcut getDeclaredPointcut(String name) throws NoSuchPointcutException;

    /**
     * Returns all of the pointcuts declared by this type
     */
    public Pointcut[] getDeclaredPointcuts();

    /**
     * Returns all of the advice declared by this type, of an advice kind contained in the
     * parameter list.
     */
    public Advice[] getDeclaredAdvice(AdviceKind... ofTypes);

    /**
     * Returns the advice declared in this type with the given name. For an @AspectJ declared advice member,
     * this is the name of the annotated method. For a code-style advice declaration, this
     * is the name given in the @AdviceName annotation if present.
     */
    public Advice getDeclaredAdvice(String name) throws NoSuchAdviceException;
}
