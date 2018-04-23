package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Flowable;

public class WeaverOnSubscribeComponentFlowable extends WeaverOnSubscribeComponentAbstract<Flowable> {

	WeaverOnSubscribeComponentFlowable(ProceedingJoinPoint joinPoint, WeaverComponent<Flowable> parentWeaverComponent) {
		super(joinPoint, parentWeaverComponent);
	}

	@Override
	public Flowable build() throws Throwable {
		return buildComponentInternal();
	}

	@SuppressWarnings("unchecked")
	private <T> Flowable<T> buildComponentInternal() throws Throwable {
		if (getParentWeaverComponent() == null)
			return (Flowable<T>) getJoinPoint().proceed();

		return ((Flowable<T>) getJoinPoint().proceed())
				.doFinally(() -> {
					if (getParentWeaverComponent().getComponentInfo().subscribeOnThread() == null)
						getParentWeaverComponent().getComponentInfo().setSubscribeOnThread(Thread.currentThread().getName());
				});
	}
}
