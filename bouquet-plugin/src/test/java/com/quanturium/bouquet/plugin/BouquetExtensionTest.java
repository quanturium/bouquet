package com.quanturium.bouquet.plugin;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BouquetExtensionTest {

	@Test
	public void setEnabled() {
		BouquetExtension bouquetExtension = new BouquetExtension();

		bouquetExtension.setEnabled(true);
		assertTrue(bouquetExtension.isEnabled());

		bouquetExtension.setEnabled(false);
		assertFalse(bouquetExtension.isEnabled());
	}

}
