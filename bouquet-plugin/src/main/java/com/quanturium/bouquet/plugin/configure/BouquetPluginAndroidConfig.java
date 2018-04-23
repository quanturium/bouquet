package com.quanturium.bouquet.plugin.configure;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.LibraryExtension;
import com.android.build.gradle.api.BaseVariant;
import com.quanturium.bouquet.plugin.BouquetExtension;

import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.tasks.compile.JavaCompile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.quanturium.bouquet.plugin.BouquetExtension.BOUQUET_EXTENSION;
import static com.quanturium.bouquet.plugin.BouquetPlugin.BOUQUET_VERSION;

public class BouquetPluginAndroidConfig extends BouquetPluginConfig {

	private static final String CONFIGURATION = "debugImplementation";

	public BouquetPluginAndroidConfig(Project project, ProjectType projectType) {
		super(project, projectType);
	}

	@Override
	protected void configurePlugin() {

//		getProject().getDependencies().add(CONFIGURATION, "org.aspectj:aspectjrt:" + ASPECTJ_VERSION);
		getProject().getDependencies().add(CONFIGURATION, "com.quanturium.bouquet:bouquet-runtime:" + BOUQUET_VERSION);
		getProject().getDependencies().add(CONFIGURATION, "com.quanturium.bouquet:bouquet-runtime-android:" + BOUQUET_VERSION);
		getProject().getDependencies().add("aspectj", "com.quanturium.bouquet:bouquet-runtime-android:" + BOUQUET_VERSION);

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

	private void configureVariant(Project project, BaseVariant variant, BaseExtension androidExtension) {
		if (!((BouquetExtension) project.getExtensions().getByName(BOUQUET_EXTENSION)).isEnabled()) {
			project.getLogger().debug("Skipping build type " + variant.getBuildType().getName() + ". Bouquet is not enabled");
			return;
		}

		if (!variant.getBuildType().isDebuggable()) {
			project.getLogger().debug("Skipping build type " + variant.getBuildType().getName() + ". Buld type not debuggable");
			return;
		}

		JavaCompile javaCompile = (JavaCompile) variant.getJavaCompiler();


		javaCompile.doLast(task -> {

			ConfigurableFileCollection inpathFileCollection = project.files(javaCompile.getDestinationDir());
//			project.getLogger().warn(inpathFileCollection.getAsPath());

			final String[] args = new String[]{
					"-showWeaveInfo",
					"-1.5",
					"-inpath", inpathFileCollection.getAsPath(),
					"-aspectpath", javaCompile.getClasspath().getAsPath(),
					"-d", javaCompile.getDestinationDir().toString(),
					"-classpath", javaCompile.getClasspath().getAsPath(),
					"-bootclasspath", getBootClassPathString(androidExtension)
			};

			applyArgs(args);
		});
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
