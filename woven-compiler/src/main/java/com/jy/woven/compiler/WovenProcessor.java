package com.jy.woven.compiler;

import com.google.auto.service.AutoService;
import com.jy.woven.annotation.After;
import com.jy.woven.annotation.Before;
import com.jy.woven.annotation.Pointcut;
import com.jy.woven.annotation.Woven;
import com.jy.woven.annotation.common.Const;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class WovenProcessor extends BaseProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        if (annotations == null || annotations.isEmpty()) {
            return false;
        }
        messager.printMessage(Diagnostic.Kind.NOTE, "WovenProcessor--processing...");
        for (Element classElement : env.getElementsAnnotatedWith(Woven.class)) {
            if (!(classElement instanceof Symbol.ClassSymbol)) {
                continue;
            }
            Symbol.ClassSymbol cls = (Symbol.ClassSymbol) classElement;
            String className = cls.getSimpleName().toString();
            String classPath = cls.packge().fullname + "." + className;

            messager.printMessage(Diagnostic.Kind.NOTE, "WovenProcessor--className--" + className);


            //实体类型， 如：LogWoven
            TypeName entitiesTypeArguments = className(classPath);
            //父类 ，如：BaseWovenImpl<LogWoven>
            TypeName superName = ParameterizedTypeName.get(className(Const.WOVEN_SUPER_NAME), entitiesTypeArguments);
            ClassName pointcutImplClass = className(Const.POINTCUT_IMPL_CLASS);
            ClassName actionImplClass = className(Const.ACTION_IMPL_CLASS);
            ClassName actionKindClass = className(Const.ACTION_KIND_CLASS);
            ClassName arrayList = ClassName.get("java.util", "ArrayList");

            List<MethodSpec> methodSpecList = new ArrayList<>();//方法集合

            CodeBlock.Builder initMethodCode = CodeBlock.builder();
            initMethodCode.addStatement("return null");

            CodeBlock.Builder initDeclaredPointcutsCode = CodeBlock.builder();
            initDeclaredPointcutsCode.addStatement("List<Pointcut> pointcutList = new $T()", arrayList);

            CodeBlock.Builder initDeclaredActionCode = CodeBlock.builder();
            initDeclaredActionCode.addStatement("List<Action> actionList  = new $T()", arrayList);

            Iterable<Symbol> symbols = cls.members().getElements();
            for (Symbol symbol : symbols) {
                //过滤构造函数，如<init>
                if (symbol.isConstructor()) {
                    continue;
                }
                //过滤非方法
                if (!(symbol instanceof Symbol.MethodSymbol)) {
                    continue;
                }
                Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol) symbol;

                //过来静态方法
                if (methodSymbol.isStatic()) {
                    continue;
                }
                //获取方法名
                String methodName = methodSymbol.name.toString();
                //返回类型
                Type returnType = methodSymbol.getReturnType();
                //参数
                List<ParameterSpec> result = new ArrayList<>();
                for (VariableElement parameter : methodSymbol.getParameters()) {
                    result.add(ParameterSpec.get(parameter));
                }

                Pointcut pointcut = methodSymbol.getAnnotation(Pointcut.class);
                if (pointcut != null) {
                    initDeclaredPointcutsCode.addStatement("pointcutList.add(new $T(declaredMethodMap.get($S),$S))", pointcutImplClass, methodName, pointcut.value());
                }
                After after = methodSymbol.getAnnotation(After.class);
                if (after != null) {
                    initDeclaredActionCode.addStatement("actionList.add(new $T(declaredMethodMap.get($S),$S,$T.AFTER))", actionImplClass, methodName, after.value(), actionKindClass);
                }
                Before before = methodSymbol.getAnnotation(Before.class);
                if (before != null) {
                    initDeclaredActionCode.addStatement("actionList.add(new $T(declaredMethodMap.get($S),$S,$T.BEFORE))", actionImplClass, methodName, before.value(), actionKindClass);
                }
            }

            initDeclaredPointcutsCode.addStatement("return pointcutList");
            initDeclaredActionCode.addStatement("return actionList");

            //创建父类方法
            buildSuperAbstractMethod(methodSpecList, initMethodCode, initDeclaredPointcutsCode, initDeclaredActionCode);

            //添加构造函数
            methodSpecList.add(builderConstructor(entitiesTypeArguments));
            methodSpecList.add(builderDefaultConstructor(entitiesTypeArguments));

            //生成实现类
            buildClass(Const.GEN_PKG, className + Const.GEN_CLASS_IMPL_NAME, superName, null, methodSpecList, null);
        }
        messager.printMessage(Diagnostic.Kind.NOTE, "WovenProcessor--finish...");
        messager.printMessage(Diagnostic.Kind.NOTE, "...");
        messager.printMessage(Diagnostic.Kind.NOTE, "...");
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        List<String> list = new ArrayList<>();
        list.add(Woven.class.getName());
        list.add(Pointcut.class.getName());
        list.add(After.class.getName());
        list.add(Before.class.getName());
        return new HashSet<>(list);
    }

    /**
     * 创建父类抽象方法
     *
     * @param methodSpecList
     * @param initMethodCode
     * @param initDeclaredPointcutsCode
     * @param initDeclaredActionCode
     */
    private void buildSuperAbstractMethod(List<MethodSpec> methodSpecList, CodeBlock.Builder initMethodCode,
                                          CodeBlock.Builder initDeclaredPointcutsCode, CodeBlock.Builder initDeclaredActionCode) {

        ClassName hashMap = ClassName.get("java.util", "HashMap");
        ClassName stringClass = ClassName.get("java.lang", "String");
        ClassName method = ClassName.get("java.lang.reflect", "Method");
        ClassName list = ClassName.get("java.util", "List");
        ClassName pointcutClass = className(Const.POINTCUT_CLASS);
        ClassName actionClass = className(Const.ACTION_CLASS);

        TypeName mapOfMethod = ParameterizedTypeName.get(hashMap, stringClass, method);
        TypeName listOfPointcut = ParameterizedTypeName.get(list, pointcutClass);
        TypeName listOfAction = ParameterizedTypeName.get(list, actionClass);


        //initMethod方法
        methodSpecList.add(buildMethod("initMethod", mapOfMethod, null, getOverrideClassName(), initMethodCode.build()));

        //initDeclaredPointcuts方法
        methodSpecList.add(buildMethod("initDeclaredPointcuts", listOfPointcut, null, getOverrideClassName(), initDeclaredPointcutsCode.build()));

        //initDeclaredAction方法
        methodSpecList.add(buildMethod("initDeclaredAction", listOfAction, null, getOverrideClassName(), initDeclaredActionCode.build()));
    }

    /**
     * 创建构造函数，如：
     * <pre>
     * public LogWoven_Impl(Class<LogWoven> subClass) {
     *       super(subClass);
     * }
     * </pre>
     *
     * @param typeName
     * @return
     */
    private MethodSpec builderConstructor(TypeName typeName) {
        ClassName strClass = ClassName.get("java.lang", "Class");
        //父类，如：Class<LogWoven>
        TypeName superName = ParameterizedTypeName.get(strClass, typeName);

        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(superName, "subClass")
                .addCode("super(subClass);")
                .build();
    }

    /**
     * 创建构造函数，如：
     * <pre>
     * public LogWoven_Impl() {
     *        super(LogWoven.class);
     * }
     * </pre>
     *
     * @param typeName
     * @return
     */
    private MethodSpec builderDefaultConstructor(TypeName typeName) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addCode("super($T.class);", typeName)
                .build();
    }
}
