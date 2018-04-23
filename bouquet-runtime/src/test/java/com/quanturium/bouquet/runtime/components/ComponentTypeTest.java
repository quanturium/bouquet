package com.quanturium.bouquet.runtime.components;

import org.junit.Test;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ComponentTypeTest {

	@Test
	public void toStringTest() {

		assertEquals(ComponentType.OBSERVABLE.toString(), "Observable");
		assertEquals(ComponentType.FLOWABLE.toString(), "Flowable");
		assertEquals(ComponentType.SINGLE.toString(), "Single");
		assertEquals(ComponentType.MAYBE.toString(), "Maybe");
		assertEquals(ComponentType.COMPLETABLE.toString(), "Completable");

	}

	@Test
	public void fromClass() {

		ComponentType componentType;

		componentType = ComponentType.fromClass(Observable.class);
		assertEquals(componentType, ComponentType.OBSERVABLE);

		componentType = ComponentType.fromClass(Flowable.class);
		assertEquals(componentType, ComponentType.FLOWABLE);

		componentType = ComponentType.fromClass(Single.class);
		assertEquals(componentType, ComponentType.SINGLE);

		componentType = ComponentType.fromClass(Maybe.class);
		assertEquals(componentType, ComponentType.MAYBE);

		componentType = ComponentType.fromClass(Completable.class);
		assertEquals(componentType, ComponentType.COMPLETABLE);

		componentType = ComponentType.fromClass(String.class); // random class here
		assertNull(componentType);

	}
}