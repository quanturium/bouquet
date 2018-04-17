package com.quanturium.bouquet.runtime.logging;


public class StopWatch {

	private long startTime;
	private long elapsedTime;

	/**
	 * Start the stop watch
	 */
	public synchronized void start() {
		startTime = System.currentTimeMillis();
	}

	/**
	 * Stop the stop watch
	 */
	public synchronized void stop() {
		elapsedTime = System.currentTimeMillis() - startTime;
	}

	/**
	 * Return the difference of time in milliseconds between {@link #start()} and {@link #stop()}
	 * @return the elapsed time in milliseconds
	 */
	public final synchronized long getElapsedTime() {
		return elapsedTime;
	}
}
