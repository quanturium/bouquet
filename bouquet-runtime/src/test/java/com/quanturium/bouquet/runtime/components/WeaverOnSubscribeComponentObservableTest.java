package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeaverOnSubscribeComponentObservableTest {

	@Mock
	ProceedingJoinPoint proceedingJoinPoint;
	@Mock
	WeaverComponent parentWeaverComponent;
	@Mock
	ComponentInfo componentInfo;

	private TestObserver observer;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		observer = new TestObserver();
	}

	@Test
	public void build() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.just(1));
		when(parentWeaverComponent.getComponentInfo()).thenReturn(componentInfo);

		WeaverOnSubscribeComponentObservable weaverOnSubscribeComponentObservable = new WeaverOnSubscribeComponentObservable(proceedingJoinPoint, parentWeaverComponent);
		Observable observable = weaverOnSubscribeComponentObservable.build();

		observable.subscribe(observer);
		observer.dispose();

		observer.assertComplete();
		verify(componentInfo).setSubscribeOnThread(anyString());
	}

	@Test
	public void buildWithoutParent() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.just(1));

		WeaverOnSubscribeComponentObservable weaverOnSubscribeComponentObservable = new WeaverOnSubscribeComponentObservable(proceedingJoinPoint, null);
		Observable observable = weaverOnSubscribeComponentObservable.build();

		observable.subscribe(observer);
		observer.dispose();

		observer.assertComplete();
		verify(componentInfo, times(0)).setSubscribeOnThread(anyString());
	}
}