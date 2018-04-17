package com.quanturium.bouquet.runtime.components;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public enum RxComponent {

	FLOWABLE(Flowable.class),
	OBSERVABLE(Observable.class),
	SINGLE(Single.class),
	MAYBE(Maybe.class),
	COMPLETABLE(Completable.class);

	private Class aClass;

	private static Map<Class, RxComponent> map = new HashMap<>();

	static {
		for (RxComponent p : RxComponent.values())
			map.put(p.aClass, p);
	}

	RxComponent(Class aClass) {
		this.aClass = aClass;
	}

	@Override
	public String toString() {
		return capitalize(name());
	}

	public static RxComponent fromClass(Class klass) {
		return map.get(klass);
	}

	private String capitalize(String string) {
		String lowerCase = string.toLowerCase();
		return lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
	}
}
