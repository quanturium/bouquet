package com.quanturium.bouquet.runtime.java.logging;

import com.quanturium.bouquet.runtime.logging.Logger;

public final class JavaLogger implements Logger {

	@Override
	public void log(String tag, String message) {
		System.out.println(tag + ": " + message);
	}
}
