package com.quanturium.bouquet.runtime.java.logging;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class JavaLoggerIntegTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@Before
	public void setUp() {
		System.setOut(new PrintStream(outContent));
	}

	@Test
	public void log() {
		JavaLogger javaLogger = new JavaLogger();
		javaLogger.log("tag", "message");
		assertEquals("tag: message\n", outContent.toString());
	}
}