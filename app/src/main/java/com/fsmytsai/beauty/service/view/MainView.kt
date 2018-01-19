package com.fsmytsai.beauty.service.view

import android.graphics.Bitmap
import com.fsmytsai.beauty.model.Vote

/**
 * Created by fsmytsai on 2017/12/15.
 */
interface MainView : BaseView {
    fun getVoteDataSuccess(voteData: Vote)
    fun loadImageSuccess(bitmap: Bitmap)
}