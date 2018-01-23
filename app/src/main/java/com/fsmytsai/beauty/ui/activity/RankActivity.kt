package com.fsmytsai.beauty.ui.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.fsmytsai.beauty.R
import com.fsmytsai.beauty.model.Rank
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_rank.*
import kotlinx.android.synthetic.main.top_section.*
import okhttp3.*
import java.io.IOException

class RankActivity : BaseActivity() {

    private lateinit var mRank: Rank
    private var mRankAdapter = RankAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)
        initViews()
        mRank = Gson().fromJson(intent.getStringExtra("rank"), Rank::class.java)
    }

    private fun initViews() {
        tv_toolBar.text = "Rank"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rv_rank.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_rank.adapter = mRankAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class RankAdapter : RecyclerView.Adapter<RankAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val context = parent.context

            val view = LayoutInflater.from(context).inflate(R.layout.block_rank, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            when (position) {
                in 0..9 -> {
                    holder.tvBlockTopic.text = "可愛 No. ${position % 10 + 1} 票數差：${mRank.images.feature_1[position % 10].real_count}"
                    holder.ivBlockRank.tag = mRank.images.feature_1[position % 10].image_name
                    showImage(holder.ivBlockRank, mRank.images.feature_1[position % 10].image_name, holder.pbBlockRank)
                }
                in 10..19 -> {
                    holder.tvBlockTopic.text = "性感 No. ${position % 10 + 1} 票數差：${mRank.images.feature_2[position % 10].real_count}"
                    holder.ivBlockRank.tag = mRank.images.feature_2[position % 10].image_name
                    showImage(holder.ivBlockRank, mRank.images.feature_2[position % 10].image_name, holder.pbBlockRank)
                }
                in 20..29 -> {
                    holder.tvBlockTopic.text = "氣質 No. ${position % 10 + 1} 票數差：${mRank.images.feature_3[position % 10].real_count}"
                    holder.ivBlockRank.tag = mRank.images.feature_3[position % 10].image_name
                    showImage(holder.ivBlockRank, mRank.images.feature_3[position % 10].image_name, holder.pbBlockRank)
                }
                in 30..39 -> {
                    holder.tvBlockTopic.text = "陽光 No. ${position % 10 + 1} 票數差：${mRank.images.feature_4[position % 10].real_count}"
                    holder.ivBlockRank.tag = mRank.images.feature_4[position % 10].image_name
                    showImage(holder.ivBlockRank, mRank.images.feature_4[position % 10].image_name, holder.pbBlockRank)
                }
            }
        }

        override fun getItemCount(): Int {
            return 40
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvBlockTopic: TextView = itemView.findViewById(R.id.tv_block_topic)
            val pbBlockRank: ProgressBar = itemView.findViewById(R.id.pb_block_rank)
            val ivBlockRank: ImageView = itemView.findViewById(R.id.iv_block_rank)
        }
    }

    private val wImageViewList = ArrayList<ImageView>()
    private val loadingImgNameList = ArrayList<String>()
    private val mOkHttpClient = OkHttpClient()

    private fun showImage(imageView: ImageView?, ImgName: String, pb: ProgressBar) {
        val bitmap = getBitmapFromLrucache(ImgName)
        if (bitmap == null) {
            loadImgByOkHttp(
                    imageView,
                    ImgName,
                    mRank.base_url + ImgName,
                    pb)
        } else imageView?.setImageBitmap(bitmap)
    }

    private fun loadImgByOkHttp(imageView: ImageView?, ImgName: String, url: String, pb: ProgressBar) {
        if (imageView != null) {
            for (mImgView in wImageViewList) {
                if (mImgView === imageView)
                    return
            }
            wImageViewList.add(imageView)
        }

        for (imgName in loadingImgNameList) {
            if (imgName == ImgName)
                return
        }
        loadingImgNameList.add(ImgName)

        val request = Request.Builder()
                .url(url)
                .build()

        mOkHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                loadingImgNameList.remove(ImgName)
                var i = 0
                while (i < wImageViewList.size) {
                    if (wImageViewList[i].tag == ImgName) {
                        wImageViewList.removeAt(i)
                        i--
                    }
                    i++
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val inputStream = response.body()!!.byteStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                try {
                    runOnUiThread {
                        if (bitmap != null) {
                            addBitmapToLrucaches(ImgName, bitmap)
                            loadingImgNameList.remove(ImgName)
                            var i = 0
                            while (i < wImageViewList.size) {
                                if (wImageViewList[i].tag == ImgName) {
                                    pb?.visibility = View.GONE
                                    wImageViewList[i].setImageBitmap(bitmap)
                                    wImageViewList.removeAt(i)
                                    i--
                                }
                                i++
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }
}
