package com.quanturium.bouquet.android.sample

import com.quanturium.bouquet.annotations.RxLogger
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class KotlinSample {

    @RxLogger
    fun getKotlinCompletableExample(): Completable {
        return Completable.complete() // Useless, just for demo purposes
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
