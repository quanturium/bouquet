package com.quanturium.bouquet.runtime.components;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.internal.schedulers.ComputationScheduler;
import io.reactivex.internal.schedulers.IoScheduler;
import io.reactivex.internal.schedulers.NewThreadScheduler;
import io.reactivex.internal.schedulers.SingleScheduler;
import io.reactivex.internal.schedulers.TrampolineScheduler;

public enum RxScheduler {

	SINGLE(SingleScheduler.class),
	COMPUTATION(ComputationScheduler.class),
	IO(IoScheduler.class),
	TRAMPOLINE(TrampolineScheduler.class),
	NEW_THREAD(NewThreadScheduler.class);

	private Class aClass;

	RxScheduler(Class aClass) {
		this.aClass = aClass;
	}

	private static Map<Class, RxScheduler> map = new HashMap<>();

	static {
		for (RxScheduler p : RxScheduler.values())
			map.put(p.aClass, p);
	}

	public static RxScheduler fromClass(Class klass) {
		return map.get(klass);
	}
}
