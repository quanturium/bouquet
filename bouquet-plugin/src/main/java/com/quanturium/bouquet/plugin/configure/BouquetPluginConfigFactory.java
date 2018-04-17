package com.quanturium.bouquet.plugin.configure;

import org.gradle.api.Project;
import org.gradle.api.tasks.TaskInstantiationException;

import static com.quanturium.bouquet.plugin.configure.ProjectType.ANDROID_APP;
import static com.quanturium.bouquet.plugin.configure.ProjectType.ANDROID_LIBRARY;
import static com.quanturium.bouquet.plugin.configure.ProjectType.JAVA_APP;
import static com.quanturium.bouquet.plugin.configure.ProjectType.JAVA_LIBRARY;

public class BouquetPluginConfigFactory {

	public static BouquetPluginConfig create(Project project) {

		if (project.getPluginManager().hasPlugin(ANDROID_APP.getPluginName())) {
			return new BouquetPluginAndroidConfig(project, ANDROID_APP);
		} else if (project.getPluginManager().hasPlugin(ANDROID_LIBRARY.getPluginName())) {
			return new BouquetPluginAndroidConfig(project, ANDROID_LIBRARY);
		} else if (project.getPluginManager().hasPlugin(JAVA_APP.getPluginName())) {
			return new BouquetPluginJavaConfig(project, JAVA_APP);
		} else if (project.getPluginManager().hasPlugin(JAVA_LIBRARY.getPluginName())) {
			return new BouquetPluginJavaConfig(project, JAVA_LIBRARY);
		} else {
			throw new TaskInstantiationException("Android or Java plugins required");
		}
	}

}
