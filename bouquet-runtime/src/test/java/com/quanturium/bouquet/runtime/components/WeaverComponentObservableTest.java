package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class WeaverComponentObservableTest {

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
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.just(1));

		WeaverComponentObservable weaverComponentObservable = new WeaverComponentObservable(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager);
		Observable observable = weaverComponentObservable.buildRx();

		observable.subscribe(observer);
		observer.dispose();

		observer.assertValue(1);
		observer.assertNoErrors();
		observer.assertComplete();
	}

	@Test
	public void buildRxAll() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.just(1));

		WeaverComponentObservable weaverComponentObservable = new WeaverComponentObservable(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager);
		Observable observable = weaverComponentObservable.buildRx();

		observable.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSource(weaverComponentObservable.getRxComponentInfo());
		verify(messageManager).printEvent(weaverComponentObservable.getRxComponentInfo(), RxEvent.SUBSCRIBE);
		verify(messageManager).printEvent(weaverComponentObservable.getRxComponentInfo(), RxEvent.NEXT, 1);
		verify(messageManager).printEvent(weaverComponentObservable.getRxComponentInfo(), RxEvent.COMPLETE);
		verify(messageManager).printEvent(weaverComponentObservable.getRxComponentInfo(), RxEvent.TERMINATE);
		verify(messageManager).printEvent(weaverComponentObservable.getRxComponentInfo(), RxEvent.DISPOSE);
		verify(messageManager).printSummary(weaverComponentObservable.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxSource() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.just(1));

		WeaverComponentObservable weaverComponentObservable = new WeaverComponentObservable(RxLogger.Scope.SOURCE, proceedingJoinPoint, messageManager);
		Observable observable = weaverComponentObservable.buildRx();

		observable.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSource(weaverComponentObservable.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxLifecycle() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.just(1));

		WeaverComponentObservable weaverComponentObservable = new WeaverComponentObservable(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager);
		Observable observable = weaverComponentObservable.buildRx();

		observable.subscribe(observer);
		observer.dispose();

		verify(messageManager).printEvent(weaverComponentObservable.getRxComponentInfo(), RxEvent.SUBSCRIBE);
		verify(messageManager).printEvent(weaverComponentObservable.getRxComponentInfo(), RxEvent.NEXT, 1);
		verify(messageManager).printEvent(weaverComponentObservable.getRxComponentInfo(), RxEvent.COMPLETE);
		verify(messageManager).printEvent(weaverComponentObservable.getRxComponentInfo(), RxEvent.TERMINATE);
		verify(messageManager).printEvent(weaverComponentObservable.getRxComponentInfo(), RxEvent.DISPOSE);
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxSummary() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.just(1));

		WeaverComponentObservable weaverComponentObservable = new WeaverComponentObservable(RxLogger.Scope.SUMMARY, proceedingJoinPoint, messageManager);
		Observable observable = weaverComponentObservable.buildRx();

		observable.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSummary(weaverComponentObservable.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxNone() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.just(1));

		WeaverComponentObservable weaverComponentObservable = new WeaverComponentObservable(RxLogger.Scope.NONE, proceedingJoinPoint, messageManager);
		Observable observable = weaverComponentObservable.buildRx();

		observable.subscribe(observer);
		observer.dispose();

		verifyZeroInteractions(messageManager);
	}
}