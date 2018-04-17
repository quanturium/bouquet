package com.quanturium.bouquet.plugin.configure;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.LibraryExtension;
import com.android.build.gradle.api.BaseVariant;
import com.quanturium.bouquet.plugin.BouquetExtension;

import org.gradle.api.Project;
import org.gradle.api.tasks.compile.JavaCompile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.quanturium.bouquet.plugin.BouquetExtension.BOUQUET_EXTENSION;
import static com.quanturium.bouquet.plugin.BouquetPlugin.ASPECTJ_VERSION;
import static com.quanturium.bouquet.plugin.BouquetPlugin.BOUQUET_VERSION;

public class BouquetPluginAndroidConfig extends BouquetPluginConfig {

	public BouquetPluginAndroidConfig(Project project, ProjectType projectType) {
		super(project, projectType);
	}

	@Override
	protected void configurePlugin() {

		getProject().getDependencies().add("debugImplementation", "org.aspectj:aspectjrt:" + ASPECTJ_VERSION);
		getProject().getDependencies().add("debugImplementation", "com.quanturium.bouquet:bouquet-runtime:" + BOUQUET_VERSION);
		getProject().getDependencies().add("debugImplementation", "com.quanturium.bouquet:bouquet-runtime-android:" + BOUQUET_VERSION);

		getProject().afterEvaluate(project -> configureVariants(project, getProjectType()));
	}

	private void configureVariants(Project project, ProjectType projectType) {
		BaseExtension androidExtension = (BaseExtension) project.getExtensions().getByName("android");

		if (projectType == ProjectType.ANDROID_APP) {
			((AppExtension) androidExtension).getApplicationVariants().all(applicationVariant -> configureVariant(project, applicationVariant, androidExtension));
		} else if (projectType == ProjectType.ANDROID_LIBRARY) {
			((LibraryExtension) androidExtension).getLibraryVariants().all(libraryVariant -> configureVariant(project, libraryVariant, androidExtension));
		}
	}

	private void configureVariant(Project project, BaseVariant variant, BaseExtension baseExtension) {
		if (!((BouquetExtension) project.getExtensions().getByName(BOUQUET_EXTENSION)).isEnabled()) {
			project.getLogger().debug("Skipping build type " + variant.getBuildType().getName() + ". Bouquet is not enabled");
			return;
		}

		if (!variant.getBuildType().isDebuggable()) {
			project.getLogger().debug("Skipping build type " + variant.getBuildType().getName() + ". Buld type not debuggable");
			return;
		}

		variant.getJavaCompiler().doLast(task -> {
			JavaCompile javaTask = getJavaTask(variant);
			final String[] args = new String[]{
					"-showWeaveInfo",
					"-1.5",
					"-inpath", javaTask.getDestinationDir().toString(),
					"-aspectpath", javaTask.getClasspath().getAsPath(),
					"-d", javaTask.getDestinationDir().toString(),
					"-classpath", javaTask.getClasspath().getAsPath(),
					"-bootclasspath", getBootClassPathString(baseExtension)
			};
			applyArgs(args);
		});
	}

	@SuppressWarnings("deprecation")
	private JavaCompile getJavaTask(BaseVariant variantData) {
		// just get actual javac we don't support jack.
		return variantData.getJavaCompile();
	}

	private String getBootClassPathString(BaseExtension baseExtension) {
		List<File> fileList = baseExtension.getBootClasspath();
		List<String> stringList = new ArrayList<>();
		for (File file : fileList) {
			stringList.add(file.toString());
		}
		return String.join(File.pathSeparator, stringList);
	}


}
