package com.fsmytsai.beauty.ui.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import com.fsmytsai.beauty.R
import com.fsmytsai.beauty.model.Votes
import com.fsmytsai.beauty.service.presenter.MainPresenter
import com.fsmytsai.beauty.service.view.MainView
import com.fsmytsai.beauty.ui.fragment.HomeFragment
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : MVPActivity<MainPresenter>(), MainView {
    private var mIsFirstIn = true
    lateinit var votes: Votes
    var bitmapCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_main_container, HomeFragment(), "HomeFragment")
                .commit()

        initCache()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getVotes() {
        mPresenter.getVotes()
    }

    override fun createPresenter(): MainPresenter {
        return MainPresenter(this)
    }

    override fun onFailure(errorList: ArrayList<String>) {
        handleErrorMessage(errorList)
    }

    override fun getVoteDataSuccess(votes: Votes) {
        this.votes = votes
        if (mIsFirstIn)
            mPresenter.loadImage("${votes.base_url}${votes.voteList[0].image_name}", votes.voteList[0].image_name)
    }

    override fun loadImageSuccess(bitmap: Bitmap, imageName: String) {
        if (mIsFirstIn && imageName == votes.voteList[0].image_name) {
            mIsFirstIn = false


            iv_home_vote?.setImageBitmap(bitmap)
            loadImages(1, 5)
        }
        bitmapCount++
        addBitmapToLrucaches(imageName, bitmap)
    }

    fun loadImages(startIndex: Int, count: Int) {
        for (i in startIndex..startIndex + count)
            mPresenter.loadImage("${votes.base_url}${votes.voteList[i].image_name}", votes.voteList[i].image_name)
    }

    fun getBitmap(index: Int): Bitmap? {
        return getBitmapFromLrucache(votes.voteList[index].image_name)
    }

    fun removeFirstBitmap() {
        removeBitmapFromLrucaches(votes.voteList[0].image_name)
    }
}
