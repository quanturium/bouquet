package com.quanturium.bouquet.plugin;

import com.quanturium.bouquet.plugin.configure.BouquetPluginConfig;
import com.quanturium.bouquet.plugin.configure.BouquetPluginConfigFactory;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class BouquetPlugin implements Plugin<Project> {

	public static final String BOUQUET_VERSION = "1.1.0";
	public static final String ASPECTJ_VERSION = "1.9.1";

	@Override
	public void apply(Project project) {
		BouquetPluginConfig bouquetPluginConfig = BouquetPluginConfigFactory.create(project);
		bouquetPluginConfig.configure();
	}
}
