package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.annotations.NonNull;

public class WeaverComponentFactory {

	public WeaverComponentFactory() {
	}

	public WeaverComponent build(@NonNull RxComponent rxComponent, @NonNull RxLogger.Scope scope, @NonNull ProceedingJoinPoint joinPoint, @NonNull MessageManager messageManager) {

		switch (rxComponent) {
			case FLOWABLE:
				return new WeaverComponentFlowable(scope, joinPoint, messageManager);
			case OBSERVABLE:
				return new WeaverComponentObservable(scope, joinPoint, messageManager);
			case SINGLE:
				return new WeaverComponentSingle(scope, joinPoint, messageManager);
			case MAYBE:
				return new WeaverComponentMaybe(scope, joinPoint, messageManager);
			case COMPLETABLE:
				return new WeaverComponentCompletable(scope, joinPoint, messageManager);
		}

		return null;
	}

}
