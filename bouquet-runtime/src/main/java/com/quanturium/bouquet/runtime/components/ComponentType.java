package com.quanturium.bouquet.runtime.components;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public enum ComponentType {

	FLOWABLE(Flowable.class),
	OBSERVABLE(Observable.class),
	SINGLE(Single.class),
	MAYBE(Maybe.class),
	COMPLETABLE(Completable.class);

	private Class aClass;

	ComponentType(Class aClass) {
		this.aClass = aClass;
	}

	private String capitalize(String string) {
		String lowerCase = string.toLowerCase();
		return lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
	}

	@Override
	public String toString() {
		return capitalize(name());
	}

	private static Map<Class, ComponentType> map = new HashMap<>();

	static {
		for (ComponentType p : ComponentType.values())
			map.put(p.aClass, p);
	}

	public static ComponentType fromClass(Class klass) {
		return map.get(klass);
	}
}
