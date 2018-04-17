package com.quanturium.bouquet.runtime.logging;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StopWatchTest {

	StopWatch stopWatch;

	@Before
	public void setUp() {
		stopWatch = new StopWatch();
	}

	@Test
	public void getElapsedTime() throws InterruptedException {
		stopWatch.start();
		Thread.sleep(100);
		stopWatch.stop();
		assertTrue(stopWatch.getElapsedTime() < 110 && stopWatch.getElapsedTime() > 90);
	}
}