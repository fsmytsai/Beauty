package com.fsmytsai.beauty.service.view

import android.graphics.Bitmap
import com.fsmytsai.beauty.model.Votes

interface MainView : BaseView {
    fun getVoteDataSuccess(votes: Votes)
    fun loadImageSuccess(bitmap: Bitmap, imageName: String)
}