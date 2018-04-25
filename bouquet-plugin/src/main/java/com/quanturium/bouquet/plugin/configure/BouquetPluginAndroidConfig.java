package com.quanturium.bouquet.plugin.configure;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.LibraryExtension;
import com.quanturium.bouquet.plugin.BouquetExtension;

import org.gradle.api.Project;

import static com.quanturium.bouquet.plugin.BouquetExtension.BOUQUET_EXTENSION;
import static com.quanturium.bouquet.plugin.BouquetPlugin.ASPECTJ_VERSION;
import static com.quanturium.bouquet.plugin.BouquetPlugin.BOUQUET_VERSION;

public class BouquetPluginAndroidConfig extends BouquetPluginConfig {

	private static final String CONFIGURATION = "implementation";

	public BouquetPluginAndroidConfig(Project project, ProjectType projectType) {
		super(project, projectType);
	}

	@Override
	protected void configurePlugin() {

		getProject().getDependencies().add(CONFIGURATION, "org.aspectj:aspectjrt:" + ASPECTJ_VERSION);
		getProject().getDependencies().add(CONFIGURATION, "com.quanturium.bouquet:bouquet-runtime:" + BOUQUET_VERSION);
		getProject().getDependencies().add(CONFIGURATION, "com.quanturium.bouquet:bouquet-runtime-android:" + BOUQUET_VERSION);

		BaseExtension androidExtension = (BaseExtension) getProject().getExtensions().getByName("android");

		if (!((BouquetExtension) getProject().getExtensions().getByName(BOUQUET_EXTENSION)).isEnabled()) {
			getProject().getLogger().debug("Skipping weaving. Bouquet is not enabled");
			return;
		}

		AspectJAndroidTransform aspectJAndroidTransform = new AspectJAndroidTransform(getProject(), androidExtension);
		androidExtension.registerTransform(aspectJAndroidTransform);

		// Add variants that we want to weave
		if (getProjectType() == ProjectType.ANDROID_APP) {
			((AppExtension) androidExtension).getApplicationVariants().all(applicationVariant -> {
				if (applicationVariant.getBuildType().isDebuggable()) {
					aspectJAndroidTransform.addVariant(applicationVariant);
				}
			});
		} else if (getProjectType() == ProjectType.ANDROID_LIBRARY) {
			((LibraryExtension) androidExtension).getLibraryVariants().all(libraryVariant -> {
				if (libraryVariant.getBuildType().isDebuggable()) {
					aspectJAndroidTransform.addVariant(libraryVariant);
				}
			});
		}
	}

}
