package com.quanturium.bouquet.runtime.android.logging;

import android.util.Log;

import com.google.common.collect.Iterables;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class AndroidLoggerIntegTest {

	@Test
	public void log() {
		AndroidLogger androidLogger = new AndroidLogger();
		androidLogger.log("tag", "message");
		ShadowLog.LogItem lastLog = Iterables.getLast(ShadowLog.getLogs());
		assertEquals(Log.DEBUG, lastLog.type);
		assertEquals("tag", lastLog.tag);
		assertEquals("message", lastLog.msg);
	}

}