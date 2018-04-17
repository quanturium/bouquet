package com.quanturium.bouquet.runtime.components;

import org.junit.Test;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RxComponentTest {

	@Test
	public void toStringTest() {

		assertEquals(RxComponent.OBSERVABLE.toString(), "Observable");
		assertEquals(RxComponent.FLOWABLE.toString(), "Flowable");
		assertEquals(RxComponent.SINGLE.toString(), "Single");
		assertEquals(RxComponent.MAYBE.toString(), "Maybe");
		assertEquals(RxComponent.COMPLETABLE.toString(), "Completable");

	}

	@Test
	public void fromClass() {

		RxComponent rxComponent;

		rxComponent = RxComponent.fromClass(Observable.class);
		assertEquals(rxComponent, RxComponent.OBSERVABLE);

		rxComponent = RxComponent.fromClass(Flowable.class);
		assertEquals(rxComponent, RxComponent.FLOWABLE);

		rxComponent = RxComponent.fromClass(Single.class);
		assertEquals(rxComponent, RxComponent.SINGLE);

		rxComponent = RxComponent.fromClass(Maybe.class);
		assertEquals(rxComponent, RxComponent.MAYBE);

		rxComponent = RxComponent.fromClass(Completable.class);
		assertEquals(rxComponent, RxComponent.COMPLETABLE);

		rxComponent = RxComponent.fromClass(String.class); // random class here
		assertNull(rxComponent);

	}
}