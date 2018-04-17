package com.quanturium.bouquet.runtime.android.logging;

import android.util.Log;

import com.quanturium.bouquet.runtime.logging.Logger;

public final class AndroidLogger implements Logger {

	@Override
	public void log(String tag, String message) {
		Log.d(tag, message);
	}
}
