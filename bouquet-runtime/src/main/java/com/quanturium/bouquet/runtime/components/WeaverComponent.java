package com.quanturium.bouquet.runtime.components;

public interface WeaverComponent<T> {

	T buildRx() throws Throwable;

	RxComponent getRxComponent();
}
