package com.quanturium.bouquet.plugin;

import com.quanturium.bouquet.plugin.configure.BouquetPluginConfig;
import com.quanturium.bouquet.plugin.configure.BouquetPluginConfigFactory;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class BouquetPlugin implements Plugin<Project> {

	public static final String BOUQUET_VERSION = "0.0.1";
	public static final String ASPECTJ_VERSION = "1.8.6";

	@Override
	public void apply(Project project) {
		BouquetPluginConfig bouquetPluginConfig = BouquetPluginConfigFactory.create(project);
		bouquetPluginConfig.configure();
	}
}
