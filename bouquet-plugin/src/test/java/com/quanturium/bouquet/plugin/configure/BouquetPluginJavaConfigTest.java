package com.quanturium.bouquet.plugin.configure;

import com.quanturium.bouquet.plugin.BouquetExtension;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.TaskContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static com.quanturium.bouquet.plugin.BouquetPlugin.ASPECTJ_VERSION;
import static com.quanturium.bouquet.plugin.BouquetPlugin.BOUQUET_VERSION;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BouquetPluginJavaConfigTest {

	@Mock
	Project project;
	@Mock
	DependencyHandler dependencyHandler;
	@Mock
	TaskContainer taskContainer;
	@Mock
	Task compileJava;
	@Mock
	ConfigurationContainer configurations;
	@Mock
	ExtensionContainer extensionContainer;
	@Mock
	Logger logger;

	ProjectType projectType;
	BouquetPluginJavaConfig bouquetPluginJavaConfig;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		projectType = ProjectType.ANDROID_APP;
		bouquetPluginJavaConfig = Mockito.spy(new BouquetPluginJavaConfig(project, projectType));
	}


	@Test
	public void configurePlugin() {
		when(project.getConfigurations()).thenReturn(configurations);
		when(project.getDependencies()).thenReturn(dependencyHandler);
		when(project.getTasks()).thenReturn(taskContainer);
		when(project.getExtensions()).thenReturn(extensionContainer);
		BouquetExtension bouquetExtension = new BouquetExtension();
		bouquetExtension.setEnabled(true);
		when(extensionContainer.getByName("bouquet")).thenReturn(bouquetExtension);
		when(taskContainer.getByName("compileJava")).thenReturn(compileJava);

		bouquetPluginJavaConfig.configurePlugin();

		String configName = "aspectj";
		verify(configurations).create(configName);
		verify(dependencyHandler).add("implementation", "org.aspectj:aspectjrt:" + ASPECTJ_VERSION);
		verify(dependencyHandler).add("implementation", "com.quanturium.bouquet:bouquet-runtime:" + BOUQUET_VERSION);
		verify(dependencyHandler).add("implementation", "com.quanturium.bouquet:bouquet-runtime-java:" + BOUQUET_VERSION);
		verify(dependencyHandler).add(configName, "com.quanturium.bouquet:bouquet-runtime-java:" + BOUQUET_VERSION);
		verify(compileJava).doLast(Matchers.<Action<Task>>any());
		verifyNoMoreInteractions(dependencyHandler);
	}

	@Test
	public void configurePluginDisabled() {
		when(project.getConfigurations()).thenReturn(configurations);
		when(project.getDependencies()).thenReturn(dependencyHandler);
		when(project.getExtensions()).thenReturn(extensionContainer);
		when(project.getLogger()).thenReturn(logger);
		BouquetExtension bouquetExtension = new BouquetExtension();
		bouquetExtension.setEnabled(false);
		when(extensionContainer.getByName("bouquet")).thenReturn(bouquetExtension);
		when(taskContainer.getByName("compileJava")).thenReturn(compileJava);

		bouquetPluginJavaConfig.configurePlugin();

		String configName = "aspectj";
		verify(configurations).create(configName);
		verify(dependencyHandler).add("implementation", "org.aspectj:aspectjrt:" + ASPECTJ_VERSION);
		verify(dependencyHandler).add("implementation", "com.quanturium.bouquet:bouquet-runtime:" + BOUQUET_VERSION);
		verify(dependencyHandler).add("implementation", "com.quanturium.bouquet:bouquet-runtime-java:" + BOUQUET_VERSION);
		verify(dependencyHandler).add(configName, "com.quanturium.bouquet:bouquet-runtime-java:" + BOUQUET_VERSION);
		verify(compileJava, times(0)).doLast(Matchers.<Action<Task>>any());
		verifyNoMoreInteractions(dependencyHandler);
	}
}