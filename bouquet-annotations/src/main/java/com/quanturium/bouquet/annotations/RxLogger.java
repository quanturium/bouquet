package com.quanturium.bouquet.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RxLogger annotation.
 * <p>
 * Add this to any RxJava2 reactive component (Observable, Flowable, Single, Maybe, Completable)
 * to output some debug informations:
 * <p>
 * Example:
 * <pre>
 *     {@code
 * Main: Bouquet => [Source] io.reactivex.Observable<java.lang.String> Main#getObservableExample()
 * Main: Bouquet => [Event] getObservableExample -> onSubscribe()
 * Main: Bouquet => [Event] getObservableExample -> onNext() -> String 1
 * Main: Bouquet => [Event] getObservableExample -> onNext() -> String 2
 * Main: Bouquet => [Event] getObservableExample -> onNext() -> String 3
 * Main: Bouquet => [Event] getObservableExample -> onNext() -> String 4
 * Main: Bouquet => [Event] getObservableExample -> onComplete()
 * Main: Bouquet => [Event] getObservableExample -> onTerminate()
 * Main: Bouquet => [Summary] getObservableExample -> Count: 4 items | Time: 2 ms | Subscription: asynchronous
 * Main: Bouquet => [Summary] getObservableExample -> Subscribe: IO (RxCachedThreadScheduler-1) | Observe: NEW_THREAD (RxNewThreadScheduler-1)
 *     }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RxLogger {

	Scope value() default Scope.ALL;

	enum Scope {ALL, SOURCE, LIFECYCLE, SUMMARY, NONE}
}
