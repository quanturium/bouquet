package com.quanturium.bouquet.plugin.configure;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static com.quanturium.bouquet.plugin.BouquetPlugin.ASPECTJ_VERSION;
import static com.quanturium.bouquet.plugin.BouquetPlugin.BOUQUET_VERSION;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BouquetPluginAndroidConfigTest {

	Project project;
	ProjectType projectType;
	BouquetPluginAndroidConfig bouquetPluginAndroidConfig;

	@Before
	public void setUp() {
		project = mock(Project.class);
		projectType = ProjectType.ANDROID_APP;
		bouquetPluginAndroidConfig = Mockito.spy(new BouquetPluginAndroidConfig(project, projectType));
	}

	@Test
	public void configurePlugin() {
		DependencyHandler dependencyHandler = mock(DependencyHandler.class);
		when(project.getDependencies()).thenReturn(dependencyHandler);

		bouquetPluginAndroidConfig.configurePlugin();

		verify(dependencyHandler).add("debugImplementation","org.aspectj:aspectjrt:" + ASPECTJ_VERSION);
		verify(dependencyHandler).add("debugImplementation", "com.quanturium.bouquet:bouquet-runtime:" + BOUQUET_VERSION);
		verify(dependencyHandler).add("debugImplementation", "com.quanturium.bouquet:bouquet-runtime-android:" + BOUQUET_VERSION);
		verifyNoMoreInteractions(dependencyHandler);
		verify(project).afterEvaluate(Matchers.<Action<Project>>any());
	}
}