package com.quanturium.bouquet.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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
 * Main: Bouquet => [Summary] getObservableExample -> Count: 4 items | Time: 2 ms | SubscribeOn: RxCachedThreadScheduler-1 | ObservingOn: main
 *     }
 * </pre>
 */
@Retention(RUNTIME)
@Target({METHOD})
public @interface RxLogger {

	Scope value() default Scope.ALL;

	enum Scope {ALL, SOURCE, LIFECYCLE, SUMMARY, NONE}
}
