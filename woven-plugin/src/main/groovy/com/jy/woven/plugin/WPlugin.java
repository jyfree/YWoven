package com.jy.woven.plugin;


import com.android.build.gradle.BaseExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @description 插件
 * @date: 2020/4/26 11:58
 * @author: jy
 */
public class WPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        //创建额外配置，用于gradle配置
        Extension extension = project.getExtensions().create(Const.NAME, Extension.class);

        Logger.info("register YWoven transform");
        project.getExtensions().findByType(BaseExtension.class).registerTransform(new WTransform());

        project.afterEvaluate(p -> Logger.setConfig(extension));

    }
}
