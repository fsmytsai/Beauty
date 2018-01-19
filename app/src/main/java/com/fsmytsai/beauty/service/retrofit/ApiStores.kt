package com.fsmytsai.beauty.service.retrofit

import com.fsmytsai.beauty.model.Vote
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiStores {
    @GET("api/getVoteData")
    fun getVoteData(): Observable<ArrayList<Vote>>

    @GET
    fun loadImage(@Url url: String): Observable<ResponseBody>
}