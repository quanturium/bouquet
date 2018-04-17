package com.quanturium.bouquet.plugin.configure;

import com.quanturium.bouquet.plugin.BouquetExtension;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageHandler;
import org.aspectj.tools.ajc.Main;
import org.gradle.api.Project;

import static com.quanturium.bouquet.plugin.BouquetExtension.BOUQUET_EXTENSION;
import static com.quanturium.bouquet.plugin.BouquetPlugin.BOUQUET_VERSION;
import static org.aspectj.bridge.IMessage.DEBUG;
import static org.aspectj.bridge.IMessage.INFO;
import static org.aspectj.bridge.IMessage.WARNING;

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

		// Adding annotation dependency
		getProject().getDependencies().add("implementation", "com.quanturium.bouquet:bouquet-annotations:" + BOUQUET_VERSION);

		// Create the extension
		project.getExtensions().create(BOUQUET_EXTENSION, BouquetExtension.class);

		configurePlugin();

	}

	protected abstract void configurePlugin();

	protected void applyArgs(String[] args) {
		final MessageHandler handler = new MessageHandler(true);
		new Main().run(args, handler);
		for (IMessage message : handler.getMessages(null, true)) {
			if (message.getKind().isSameOrLessThan(INFO)) {
				project.getLogger().info(message.getMessage(), message.getThrown());
			} else if (message.getKind().isSameOrLessThan(DEBUG)) {
				project.getLogger().debug(message.getMessage(), message.getThrown());
			} else if (message.getKind().isSameOrLessThan(WARNING)) {
				project.getLogger().warn(message.getMessage(), message.getThrown());
			} else {
				project.getLogger().error(message.getMessage(), message.getThrown());
			}
		}
	}
}
