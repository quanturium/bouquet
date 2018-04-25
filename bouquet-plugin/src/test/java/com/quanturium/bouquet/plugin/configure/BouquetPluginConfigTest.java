package com.quanturium.bouquet.plugin.configure;

import com.quanturium.bouquet.plugin.BouquetExtension;

import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.plugins.ExtensionContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static com.quanturium.bouquet.plugin.BouquetExtension.BOUQUET_EXTENSION;
import static com.quanturium.bouquet.plugin.BouquetPlugin.BOUQUET_VERSION;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BouquetPluginConfigTest {

	@Mock
	Project project;
	ProjectType projectType;
	BouquetPluginConfig bouquetPluginConfig;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		projectType = ProjectType.ANDROID_APP;
		bouquetPluginConfig = Mockito.spy(new BouquetPluginConfig(project, projectType) {
			@Override
			protected void configurePlugin() {

			}
		});
	}

	@Test
	public void getProject() {
		assertEquals(bouquetPluginConfig.getProject(), project);
	}

	@Test
	public void getProjectType() {
		assertEquals(bouquetPluginConfig.getProjectType(), projectType);
	}

	@Test
	public void configure() {
		DependencyHandler dependencyHandler = mock(DependencyHandler.class);
		ExtensionContainer extensionContainer = mock(ExtensionContainer.class);
		when(project.getDependencies()).thenReturn(dependencyHandler);
		when(project.getExtensions()).thenReturn(extensionContainer);

		bouquetPluginConfig.configure();

		verify(dependencyHandler).add("implementation", "com.quanturium.bouquet:bouquet-annotations:" + BOUQUET_VERSION);
		verify(extensionContainer).create(BOUQUET_EXTENSION, BouquetExtension.class);
		verify(bouquetPluginConfig).configurePlugin();
	}

	@Test
	public void applyArgs() {
	}
}
