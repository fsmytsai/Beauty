package com.fsmytsai.beauty.service.view

import android.graphics.Bitmap
import com.fsmytsai.beauty.model.Rank
import com.fsmytsai.beauty.model.Votes

interface MainView : BaseView {
    fun getVoteDataSuccess(votes: Votes)
    fun getRankDataSuccess(votes: Rank)
    fun loadImageSuccess(bitmap: Bitmap, imageName: String)
}