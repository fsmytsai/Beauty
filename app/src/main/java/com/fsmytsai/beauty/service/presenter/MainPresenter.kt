package com.fsmytsai.beauty.service.presenter

import android.graphics.BitmapFactory
import com.fsmytsai.beauty.model.Vote
import com.fsmytsai.beauty.service.retrofit.ApiCallback
import com.fsmytsai.beauty.service.view.MainView
import okhttp3.ResponseBody

/**
 * Created by fsmytsai on 2017/12/15.
 */
class MainPresenter(private val mainView: MainView) : BasePresenter() {

    fun getVoteData() {
        addSubscription(mApiStores.getVoteData(), object : ApiCallback<Vote>() {
            override fun onSuccess(model: Vote) {
                mainView.getVoteDataSuccess(model)
            }

            override fun onFailure(errorList: ArrayList<String>) {
                mainView.onFailure(errorList)
            }

            override fun onFinish() {

            }

        })
    }

    fun loadImage(url: String) {
        addSubscription(mApiStores.loadImage(url), object : ApiCallback<ResponseBody>() {
            override fun onSuccess(responseBody: ResponseBody) {
                val bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                mainView.loadImageSuccess(bitmap)
            }

            override fun onFailure(errorList: ArrayList<String>) {
                mainView.onFailure(errorList)
            }

            override fun onFinish() {
            }

        })
    }
}