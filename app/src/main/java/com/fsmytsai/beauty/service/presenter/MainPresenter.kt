package com.fsmytsai.beauty.service.presenter

import android.graphics.BitmapFactory
import android.widget.ImageView
import com.fsmytsai.beauty.model.Rank
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

    fun getRank() {
        addSubscription(mApiStores.getRank(), object : ApiCallback<Rank>() {
            override fun onSuccess(model: Rank) {
                mainView.getRankDataSuccess(model)
            }

            override fun onFailure(errorList: ArrayList<String>) {
                mainView.onFailure(errorList)
            }

            override fun onFinish() {

            }

        })
    }

    fun loadImage(url: String, imageName: String, imageView: ImageView?) {
        //跳過圖片載入過慢的重複請求
        if (loadedImageNameList.contains(imageName))
            return
        loadedImageNameList.add(imageName)
        addSubscription(mApiStores.loadImage(url), object : ApiCallback<ResponseBody>() {
            override fun onSuccess(model: ResponseBody) {
                val bitmap = BitmapFactory.decodeStream(model.byteStream())
                loadedImageNameList.remove(imageName)
                if (imageView != null)
                    imageView.setImageBitmap(bitmap)
                else
                    mainView.loadImageSuccess(bitmap, imageName)
            }

            override fun onFailure(errorList: ArrayList<String>) {
                mainView.onFailure(errorList)
            }

            override fun onFinish() {
            }

        })
    }

    fun loadFirstRankImage(url: String) {
        addSubscription(mApiStores.loadImage(url), object : ApiCallback<ResponseBody>() {
            override fun onSuccess(model: ResponseBody) {
                val bitmap = BitmapFactory.decodeStream(model.byteStream())
                mainView.loadImageSuccess(bitmap, "firstRankImage")
            }

            override fun onFailure(errorList: ArrayList<String>) {
                mainView.onFailure(errorList)
            }

            override fun onFinish() {
            }

        })
    }
}