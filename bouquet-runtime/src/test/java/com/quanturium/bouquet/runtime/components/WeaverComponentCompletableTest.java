package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class WeaverComponentCompletableTest {

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
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete());

		WeaverComponentCompletable weaverComponentCompletable = new WeaverComponentCompletable(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager);
		Completable completable = weaverComponentCompletable.buildRx();

		completable.subscribe(observer);
		observer.dispose();

		observer.assertNoValues();
		observer.assertNoErrors();
		observer.assertComplete();
	}

	@Test
	public void buildRxAll() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete());

		WeaverComponentCompletable weaverComponentCompletable = new WeaverComponentCompletable(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager);
		Completable completable = weaverComponentCompletable.buildRx();

		completable.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSource(weaverComponentCompletable.getRxComponentInfo());
		verify(messageManager).printEvent(weaverComponentCompletable.getRxComponentInfo(), RxEvent.SUBSCRIBE);
		verify(messageManager).printEvent(weaverComponentCompletable.getRxComponentInfo(), RxEvent.COMPLETE);
		verify(messageManager).printEvent(weaverComponentCompletable.getRxComponentInfo(), RxEvent.DISPOSE);
		verify(messageManager).printSummary(weaverComponentCompletable.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxSource() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete());

		WeaverComponentCompletable weaverComponentCompletable = new WeaverComponentCompletable(RxLogger.Scope.SOURCE, proceedingJoinPoint, messageManager);
		Completable completable = weaverComponentCompletable.buildRx();

		completable.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSource(weaverComponentCompletable.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxLifecycle() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete());

		WeaverComponentCompletable weaverComponentCompletable = new WeaverComponentCompletable(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager);
		Completable completable = weaverComponentCompletable.buildRx();

		completable.subscribe(observer);
		observer.dispose();

		verify(messageManager).printEvent(weaverComponentCompletable.getRxComponentInfo(), RxEvent.SUBSCRIBE);
		verify(messageManager).printEvent(weaverComponentCompletable.getRxComponentInfo(), RxEvent.COMPLETE);
		verify(messageManager).printEvent(weaverComponentCompletable.getRxComponentInfo(), RxEvent.DISPOSE);
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxSummary() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete());

		WeaverComponentCompletable weaverComponentCompletable = new WeaverComponentCompletable(RxLogger.Scope.SUMMARY, proceedingJoinPoint, messageManager);
		Completable completable = weaverComponentCompletable.buildRx();

		completable.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSummary(weaverComponentCompletable.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxNone() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete());

		WeaverComponentCompletable weaverComponentCompletable = new WeaverComponentCompletable(RxLogger.Scope.NONE, proceedingJoinPoint, messageManager);
		Completable completable = weaverComponentCompletable.buildRx();

		completable.subscribe(observer);
		observer.dispose();

		verifyZeroInteractions(messageManager);
	}
}