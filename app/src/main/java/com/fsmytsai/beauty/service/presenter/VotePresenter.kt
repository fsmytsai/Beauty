package com.fsmytsai.beauty.service.presenter

import com.fsmytsai.beauty.service.retrofit.ApiCallback
import com.fsmytsai.beauty.service.view.VoteView

class VotePresenter(private val voteView: VoteView) : BasePresenter() {
    fun vote(imageId: Int, featureId: Int, userId: String, isAgree: Boolean) {
        addSubscription(mApiStores.vote(imageId, featureId, userId, isAgree), object : ApiCallback<String>() {
            override fun onSuccess(model: String) {
                voteView.voteSuccess()
            }

            override fun onFailure(errorList: ArrayList<String>) {
                voteView.onFailure(errorList)
            }

            override fun onFinish() {

            }

        })
    }
}