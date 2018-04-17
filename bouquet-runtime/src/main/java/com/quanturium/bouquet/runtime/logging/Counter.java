package com.quanturium.bouquet.runtime.logging;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter extends AtomicInteger {

	public void increment() {
		incrementAndGet();
	}

}
