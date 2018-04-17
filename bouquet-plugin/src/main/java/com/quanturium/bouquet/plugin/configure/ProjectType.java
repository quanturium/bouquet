package com.quanturium.bouquet.plugin.configure;

public enum ProjectType {
	ANDROID_APP("com.android.application"),
	ANDROID_LIBRARY("com.android.library"),
	JAVA_APP("application"),
	JAVA_LIBRARY("java");

	private final String pluginName;

	ProjectType(String pluginName) {
		this.pluginName = pluginName;
	}

	public String getPluginName() {
		return pluginName;
	}
}