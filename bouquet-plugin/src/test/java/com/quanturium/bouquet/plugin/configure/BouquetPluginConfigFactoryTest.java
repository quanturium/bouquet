package com.quanturium.bouquet.plugin.configure;

import org.gradle.api.Project;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.tasks.TaskInstantiationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static com.quanturium.bouquet.plugin.configure.ProjectType.ANDROID_APP;
import static com.quanturium.bouquet.plugin.configure.ProjectType.ANDROID_LIBRARY;
import static com.quanturium.bouquet.plugin.configure.ProjectType.JAVA_APP;
import static com.quanturium.bouquet.plugin.configure.ProjectType.JAVA_LIBRARY;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BouquetPluginConfigFactoryTest {

	@Mock
	private Project project;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void create() {

		PluginManager pluginManager = mock(PluginManager.class);
		when(project.getPluginManager()).thenReturn(pluginManager);
		when(pluginManager.hasPlugin(ANDROID_APP.getPluginName())).thenReturn(true).thenReturn(false).thenReturn(false).thenReturn(false);
		when(pluginManager.hasPlugin(ANDROID_LIBRARY.getPluginName())).thenReturn(true).thenReturn(false).thenReturn(false);
		when(pluginManager.hasPlugin(JAVA_APP.getPluginName())).thenReturn(true).thenReturn(false);
		when(pluginManager.hasPlugin(JAVA_LIBRARY.getPluginName())).thenReturn(true);

		BouquetPluginConfig bouquetPluginConfig = BouquetPluginConfigFactory.create(project);
		assertEquals(bouquetPluginConfig.getClass(), BouquetPluginAndroidConfig.class);

		bouquetPluginConfig = BouquetPluginConfigFactory.create(project);
		assertEquals(bouquetPluginConfig.getClass(), BouquetPluginAndroidConfig.class);

		bouquetPluginConfig = BouquetPluginConfigFactory.create(project);
		assertEquals(bouquetPluginConfig.getClass(), BouquetPluginJavaConfig.class);

		bouquetPluginConfig = BouquetPluginConfigFactory.create(project);
		assertEquals(bouquetPluginConfig.getClass(), BouquetPluginJavaConfig.class);
	}

	@Test(expected = TaskInstantiationException.class)
	public void createInvalidProjectType() {
		PluginManager pluginManager = mock(PluginManager.class);
		when(project.getPluginManager()).thenReturn(pluginManager);
		when(pluginManager.hasPlugin(anyString())).thenReturn(false);

		BouquetPluginConfigFactory.create(project);
	}
}