package com.fsmytsai.beauty.service.retrofit

import com.fsmytsai.beauty.model.Rank
import com.fsmytsai.beauty.model.Votes
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiStores {
    @GET("api/getVotes")
    fun getVotes(): Observable<Votes>

    @GET("api/getRank")
    fun getRank(): Observable<Rank>

    @GET
    fun loadImage(@Url url: String): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("api/vote")
    fun vote(@Field("image_id") imageId: Int,
             @Field("feature_id") featureId: Int,
             @Field("user_id") userId: String,
             @Field("is_agree") isAgree: Int): Observable<String>
}