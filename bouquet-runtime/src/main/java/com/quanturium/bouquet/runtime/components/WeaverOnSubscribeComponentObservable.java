package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Observable;

public class WeaverOnSubscribeComponentObservable extends WeaverOnSubscribeComponentAbstract<Observable> {

	WeaverOnSubscribeComponentObservable(ProceedingJoinPoint joinPoint, WeaverComponent<Observable> parentWeaverComponent) {
		super(joinPoint, parentWeaverComponent);
	}

	@Override
	public Observable build() throws Throwable {
		return buildComponentInternal();
	}

	@SuppressWarnings("unchecked")
	private <T> Observable<T> buildComponentInternal() throws Throwable {
		if (getParentWeaverComponent() == null)
			return (Observable<T>) getJoinPoint().proceed();

		return ((Observable<T>) getJoinPoint().proceed())
				.doFinally(() -> {
					if (getParentWeaverComponent().getComponentInfo().subscribeOnThread() == null)
						getParentWeaverComponent().getComponentInfo().setSubscribeOnThread(Thread.currentThread().getName());
				});
	}
}
