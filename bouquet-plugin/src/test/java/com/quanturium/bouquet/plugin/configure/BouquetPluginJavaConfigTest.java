package com.quanturium.bouquet.plugin.configure;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.tasks.TaskContainer;
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
public class BouquetPluginJavaConfigTest {

	Project project;
	ProjectType projectType;
	BouquetPluginJavaConfig bouquetPluginJavaConfig;

	@Before
	public void setUp() {
		project = mock(Project.class);
		projectType = ProjectType.ANDROID_APP;
		bouquetPluginJavaConfig = Mockito.spy(new BouquetPluginJavaConfig(project, projectType));
	}


	@Test
	public void configurePlugin() {
		DependencyHandler dependencyHandler = mock(DependencyHandler.class);
		TaskContainer taskContainer = mock(TaskContainer.class);
		Task compileJava = mock(Task.class);
		when(project.getDependencies()).thenReturn(dependencyHandler);
		when(project.getTasks()).thenReturn(taskContainer);
		when(taskContainer.getByName("compileJava")).thenReturn(compileJava);

		bouquetPluginJavaConfig.configurePlugin();

		verify(dependencyHandler).add("implementation","org.aspectj:aspectjrt:" + ASPECTJ_VERSION);
		verify(dependencyHandler).add("implementation", "com.quanturium.bouquet:bouquet-runtime:" + BOUQUET_VERSION);
		verify(dependencyHandler).add("implementation", "com.quanturium.bouquet:bouquet-runtime-java:" + BOUQUET_VERSION);
		verifyNoMoreInteractions(dependencyHandler);
		verify(compileJava).doLast(Matchers.<Action<Task>>any());
	}
}