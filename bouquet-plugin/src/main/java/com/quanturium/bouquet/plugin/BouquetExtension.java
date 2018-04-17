package com.quanturium.bouquet.plugin;

public class BouquetExtension {

	public static final String BOUQUET_EXTENSION = "bouquet";

	private boolean enabled = true;

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
