package com.fsmytsai.beauty.service.presenter

import android.graphics.BitmapFactory
import com.fsmytsai.beauty.model.Votes
import com.fsmytsai.beauty.service.retrofit.ApiCallback
import com.fsmytsai.beauty.service.view.MainView
import okhttp3.ResponseBody

class MainPresenter(private val mainView: MainView) : BasePresenter() {
    private val loadedImageNameList = ArrayList<String>()

    fun getVotes() {
        addSubscription(mApiStores.getVotes(), object : ApiCallback<Votes>() {
            override fun onSuccess(model: Votes) {
                mainView.getVoteDataSuccess(model)
            }

            override fun onFailure(errorList: ArrayList<String>) {
                mainView.onFailure(errorList)
            }

            override fun onFinish() {

            }

        })
    }

    fun loadImage(url: String, imageName: String) {
        if (loadedImageNameList.contains(imageName))
            return
        loadedImageNameList.add(imageName)
        addSubscription(mApiStores.loadImage(url), object : ApiCallback<ResponseBody>() {
            override fun onSuccess(model: ResponseBody) {
                val bitmap = BitmapFactory.decodeStream(model.byteStream())
                mainView.loadImageSuccess(bitmap, imageName)
            }

            override fun onFailure(errorList: ArrayList<String>) {
                mainView.onFailure(errorList)
            }

            override fun onFinish() {
            }

        })
    }
}