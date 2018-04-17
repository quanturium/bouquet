package com.quanturium.bouquet.plugin.configure;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProjectTypeTest {

	private static final int PROJECT_TYPES_SUPPORT = 4;

	@Test
	public void supportedProjectTypesCount(){
		assertEquals(ProjectType.values().length, PROJECT_TYPES_SUPPORT);
	}

	@Test
	public void getPluginName() {

		assertEquals(ProjectType.ANDROID_APP.getPluginName(), "com.android.application");
		assertEquals(ProjectType.ANDROID_LIBRARY.getPluginName(), "com.android.library");
		assertEquals(ProjectType.JAVA_APP.getPluginName(), "application");
		assertEquals(ProjectType.JAVA_LIBRARY.getPluginName(), "java");

	}
}