package com.fsmytsai.beauty.model

data class Votes(val voteList: ArrayList<Vote>, val base_url: String) {
    data class Vote(
            val image_id: Int,
            val image_name: String,
            val feature_id: Int,
            val feature_topic: String
    )
}