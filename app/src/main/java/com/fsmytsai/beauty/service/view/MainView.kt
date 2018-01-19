package com.fsmytsai.beauty.service.view

import android.graphics.Bitmap
import com.fsmytsai.beauty.model.Vote

interface MainView : BaseView {
    fun getVoteDataSuccess(voteData: ArrayList<Vote>)
    fun loadImageSuccess(bitmap: Bitmap)
}