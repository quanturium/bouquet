package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Completable;

public class WeaverOnSubscribeComponentCompletable extends WeaverOnSubscribeComponentAbstract<Completable> {

	WeaverOnSubscribeComponentCompletable(ProceedingJoinPoint joinPoint, WeaverComponent<Completable> parentWeaverComponent) {
		super(joinPoint, parentWeaverComponent);
	}

	@Override
	public Completable build() throws Throwable {
		return buildComponentInternal();
	}

	private Completable buildComponentInternal() throws Throwable {
		if (getParentWeaverComponent() == null)
			return (Completable) getJoinPoint().proceed();

		return ((Completable) getJoinPoint().proceed())
				.doFinally(() -> {
					if (getParentWeaverComponent().getComponentInfo().subscribeOnThread() == null)
						getParentWeaverComponent().getComponentInfo().setSubscribeOnThread(Thread.currentThread().getName());
				});
	}
}
