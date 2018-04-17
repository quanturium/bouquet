package com.quanturium.bouquet.plugin.configure;

import com.quanturium.bouquet.plugin.BouquetExtension;

import org.gradle.api.Project;
import org.gradle.api.tasks.compile.JavaCompile;

import static com.quanturium.bouquet.plugin.BouquetExtension.BOUQUET_EXTENSION;
import static com.quanturium.bouquet.plugin.BouquetPlugin.ASPECTJ_VERSION;
import static com.quanturium.bouquet.plugin.BouquetPlugin.BOUQUET_VERSION;

public class BouquetPluginJavaConfig extends BouquetPluginConfig {

	public BouquetPluginJavaConfig(Project project, ProjectType projectType) {
		super(project, projectType);
	}

	@Override
	protected void configurePlugin() {

		getProject().getDependencies().add("implementation", "org.aspectj:aspectjrt:" + ASPECTJ_VERSION);
		getProject().getDependencies().add("implementation", "com.quanturium.bouquet:bouquet-runtime:" + BOUQUET_VERSION);
		getProject().getDependencies().add("implementation", "com.quanturium.bouquet:bouquet-runtime-java:" + BOUQUET_VERSION);

		getProject().getTasks().getByName("compileJava").doLast(task -> configureTask(getProject(), (JavaCompile) task));

	}

	private void configureTask(Project project, JavaCompile javaCompile) {
		if (!((BouquetExtension) project.getExtensions().getByName(BOUQUET_EXTENSION)).isEnabled()) {
			return;
		}

		final String[] args = new String[]{
				"-showWeaveInfo",
				"-1.5",
				"-XnoInline",
				"-Xlint:warning",
				"-inpath", javaCompile.getDestinationDir().toString(),
				"-inpath", javaCompile.getInputs().getFiles().getAsPath(),
				"-aspectpath", javaCompile.getClasspath().getAsPath(),
				"-d", javaCompile.getDestinationDir().toString(),
				"-classpath", javaCompile.getClasspath().getAsPath()
		};
		applyArgs(args);
	}
}
