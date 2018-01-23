package com.fsmytsai.beauty.model


data class Rank(
		val images: Images,
		val base_url: String //http://beauty.southeastasia.cloudapp.azure.com/images/
)

data class Images(
		val feature_1: List<Feature1>,
		val feature_2: List<Feature2>,
		val feature_3: List<Feature3>,
		val feature_4: List<Feature4>
)

data class Feature4(
		val sub_image_id: Int, //349
		val image_name: String, //2197_17_23.png
		val real_count: Int //4
)

data class Feature1(
		val sub_image_id: Int, //678
		val image_name: String, //2194_15_11.jpg
		val real_count: Int //4
)

data class Feature2(
		val sub_image_id: Int, //609
		val image_name: String, //2194_7_22.jpg
		val real_count: Int //5
)

data class Feature3(
		val sub_image_id: Int, //297
		val image_name: String, //2197_5_9.jpg
		val real_count: Int //5
)