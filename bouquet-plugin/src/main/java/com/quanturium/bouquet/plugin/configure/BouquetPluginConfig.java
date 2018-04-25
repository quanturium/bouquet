package com.quanturium.bouquet.plugin.configure;

import com.quanturium.bouquet.plugin.BouquetExtension;

import org.gradle.api.Project;

import static com.quanturium.bouquet.plugin.BouquetExtension.BOUQUET_EXTENSION;
import static com.quanturium.bouquet.plugin.BouquetPlugin.BOUQUET_VERSION;

public abstract class BouquetPluginConfig {

	private final Project project;
	private final ProjectType projectType;

	public BouquetPluginConfig(Project project, ProjectType projectType) {
		this.project = project;
		this.projectType = projectType;
	}

	public Project getProject() {
		return project;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public final void configure() {

		// Create the extension
		project.getExtensions().create(BOUQUET_EXTENSION, BouquetExtension.class);

		// Adding annotation dependency
		getProject().getDependencies().add("implementation", "com.quanturium.bouquet:bouquet-annotations:" + BOUQUET_VERSION);

		// Configure the plugin
		configurePlugin();
	}

	protected abstract void configurePlugin();
}
