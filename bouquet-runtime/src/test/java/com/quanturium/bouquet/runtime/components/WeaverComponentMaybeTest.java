package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class WeaverComponentMaybeTest {

	@Mock
	ProceedingJoinPoint proceedingJoinPoint;
	@Mock
	MessageManager messageManager;

	private TestObserver observer;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		observer = new TestObserver();
	}

	@Test
	public void buildRx() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.just(1));

		WeaverComponentMaybe weaverComponentMaybe = new WeaverComponentMaybe(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager);
		Maybe maybe = weaverComponentMaybe.buildRx();

		maybe.subscribe(observer);
		observer.dispose();

		observer.assertValue(1);
		observer.assertNoErrors();
		observer.assertComplete();
	}

	@Test
	public void buildRxAll() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.just(1));

		WeaverComponentMaybe weaverComponentMaybe = new WeaverComponentMaybe(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager);
		Maybe maybe = weaverComponentMaybe.buildRx();

		maybe.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSource(weaverComponentMaybe.getRxComponentInfo());
		verify(messageManager).printEvent(weaverComponentMaybe.getRxComponentInfo(), RxEvent.SUBSCRIBE);
		verify(messageManager).printEvent(weaverComponentMaybe.getRxComponentInfo(), RxEvent.SUCCESS, 1);
		verify(messageManager).printEvent(weaverComponentMaybe.getRxComponentInfo(), RxEvent.DISPOSE);
		verify(messageManager).printSummary(weaverComponentMaybe.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxSource() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.just(1));

		WeaverComponentMaybe weaverComponentMaybe = new WeaverComponentMaybe(RxLogger.Scope.SOURCE, proceedingJoinPoint, messageManager);
		Maybe maybe = weaverComponentMaybe.buildRx();

		maybe.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSource(weaverComponentMaybe.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxLifecycle() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.just(1));

		WeaverComponentMaybe weaverComponentMaybe = new WeaverComponentMaybe(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager);
		Maybe maybe = weaverComponentMaybe.buildRx();

		maybe.subscribe(observer);
		observer.dispose();

		verify(messageManager).printEvent(weaverComponentMaybe.getRxComponentInfo(), RxEvent.SUBSCRIBE);
		verify(messageManager).printEvent(weaverComponentMaybe.getRxComponentInfo(), RxEvent.SUCCESS, 1);
		verify(messageManager).printEvent(weaverComponentMaybe.getRxComponentInfo(), RxEvent.DISPOSE);
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxSummary() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.just(1));

		WeaverComponentMaybe weaverComponentMaybe = new WeaverComponentMaybe(RxLogger.Scope.SUMMARY, proceedingJoinPoint, messageManager);
		Maybe maybe = weaverComponentMaybe.buildRx();

		maybe.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSummary(weaverComponentMaybe.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxNone() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.just(1));

		WeaverComponentMaybe weaverComponentMaybe = new WeaverComponentMaybe(RxLogger.Scope.NONE, proceedingJoinPoint, messageManager);
		Maybe maybe = weaverComponentMaybe.buildRx();

		maybe.subscribe(observer);
		observer.dispose();

		verifyZeroInteractions(messageManager);
	}
}