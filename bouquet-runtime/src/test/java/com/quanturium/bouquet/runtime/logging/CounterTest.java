package com.quanturium.bouquet.runtime.logging;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CounterTest {

	@Test
	public void increment() {

		Counter counter = new Counter();
		counter.increment();
		assertEquals(counter.get(), 1);
	}
}