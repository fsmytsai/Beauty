package com.fsmytsai.beauty.service.retrofit

import com.fsmytsai.beauty.model.Vote
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by fsmytsai on 2017/12/15.
 */
interface ApiStores {
    @GET("api/getVoteData")
    fun getVoteData(): Observable<Vote>

    @GET
    fun loadImage(@Url url: String): Observable<ResponseBody>
}