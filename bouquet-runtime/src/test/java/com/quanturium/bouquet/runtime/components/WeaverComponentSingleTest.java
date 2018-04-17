package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class WeaverComponentSingleTest {

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
		when(proceedingJoinPoint.proceed()).thenReturn(Single.just(1));

		WeaverComponentSingle weaverComponentSingle = new WeaverComponentSingle(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager);
		Single single = weaverComponentSingle.buildRx();

		single.subscribe(observer);
		observer.dispose();

		observer.assertValue(1);
		observer.assertNoErrors();
		observer.assertComplete();
	}

	@Test
	public void buildRxAll() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Single.just(1));

		WeaverComponentSingle weaverComponentSingle = new WeaverComponentSingle(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager);
		Single single = weaverComponentSingle.buildRx();

		single.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSource(weaverComponentSingle.getRxComponentInfo());
		verify(messageManager).printEvent(weaverComponentSingle.getRxComponentInfo(), RxEvent.SUBSCRIBE);
		verify(messageManager).printEvent(weaverComponentSingle.getRxComponentInfo(), RxEvent.SUCCESS, 1);
		verify(messageManager).printEvent(weaverComponentSingle.getRxComponentInfo(), RxEvent.DISPOSE);
		verify(messageManager).printSummary(weaverComponentSingle.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxSource() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Single.just(1));

		WeaverComponentSingle weaverComponentSingle = new WeaverComponentSingle(RxLogger.Scope.SOURCE, proceedingJoinPoint, messageManager);
		Single single = weaverComponentSingle.buildRx();

		single.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSource(weaverComponentSingle.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxLifecycle() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Single.just(1));

		WeaverComponentSingle weaverComponentSingle = new WeaverComponentSingle(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager);
		Single single = weaverComponentSingle.buildRx();

		single.subscribe(observer);
		observer.dispose();

		verify(messageManager).printEvent(weaverComponentSingle.getRxComponentInfo(), RxEvent.SUBSCRIBE);
		verify(messageManager).printEvent(weaverComponentSingle.getRxComponentInfo(), RxEvent.SUCCESS, 1);
		verify(messageManager).printEvent(weaverComponentSingle.getRxComponentInfo(), RxEvent.DISPOSE);
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxSummary() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Single.just(1));

		WeaverComponentSingle weaverComponentSingle = new WeaverComponentSingle(RxLogger.Scope.SUMMARY, proceedingJoinPoint, messageManager);
		Single single = weaverComponentSingle.buildRx();

		single.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSummary(weaverComponentSingle.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxNone() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Single.just(1));

		WeaverComponentSingle weaverComponentSingle = new WeaverComponentSingle(RxLogger.Scope.NONE, proceedingJoinPoint, messageManager);
		Single single = weaverComponentSingle.buildRx();

		single.subscribe(observer);
		observer.dispose();

		verifyZeroInteractions(messageManager);
	}
}