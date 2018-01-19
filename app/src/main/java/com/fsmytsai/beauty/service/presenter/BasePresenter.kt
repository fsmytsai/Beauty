package com.fsmytsai.beauty.service.presenter

import com.fsmytsai.beauty.service.retrofit.ApiClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by tsaiminyuan on 2017/12/15.
 */
open class BasePresenter {
    protected val mApiStores = ApiClient.instance.getServer()
    private val mCompositeDisposable = CompositeDisposable()

    fun onStop(){
        mCompositeDisposable.clear()
    }

    fun onDestroy() {
        mCompositeDisposable.dispose()
    }

    fun <M> addSubscription(observable: Observable<M>, subscriber: DisposableObserver<M>) {
        mCompositeDisposable.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber))
    }
}