package com.quanturium.bouquet.plugin.configure;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.ApplicationVariant;
import com.quanturium.bouquet.plugin.BouquetExtension;

import org.gradle.api.DomainObjectSet;
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

import static com.quanturium.bouquet.plugin.BouquetPlugin.ASPECTJ_VERSION;
import static com.quanturium.bouquet.plugin.BouquetPlugin.BOUQUET_VERSION;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BouquetPluginAndroidConfigTest {

	@Mock
	Project project;
	@Mock
	DependencyHandler dependencyHandler;
	@Mock
	ExtensionContainer extensionContainer;
	@Mock
	AppExtension appExtension;
	@Mock
	DomainObjectSet<ApplicationVariant> domainObjectSet;
	ProjectType projectType;
	BouquetPluginAndroidConfig bouquetPluginAndroidConfig;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		projectType = ProjectType.ANDROID_APP;
		bouquetPluginAndroidConfig = Mockito.spy(new BouquetPluginAndroidConfig(project, projectType));
	}

	@Test
	public void configurePlugin() {

		when(project.getDependencies()).thenReturn(dependencyHandler);
		when(project.getExtensions()).thenReturn(extensionContainer);
		when(extensionContainer.getByName("android")).thenReturn(appExtension);
		BouquetExtension bouquetExtension = new BouquetExtension();
		bouquetExtension.setEnabled(true);
		when(extensionContainer.getByName("bouquet")).thenReturn(bouquetExtension);
		when(appExtension.getApplicationVariants()).thenReturn(domainObjectSet);

		bouquetPluginAndroidConfig.configurePlugin();

		verify(dependencyHandler).add("implementation","org.aspectj:aspectjrt:" + ASPECTJ_VERSION);
		verify(dependencyHandler).add("implementation", "com.quanturium.bouquet:bouquet-runtime:" + BOUQUET_VERSION);
		verify(dependencyHandler).add("implementation", "com.quanturium.bouquet:bouquet-runtime-android:" + BOUQUET_VERSION);
		verify(appExtension).registerTransform(any(AspectJAndroidTransform.class));
		verifyNoMoreInteractions(dependencyHandler);
	}
}