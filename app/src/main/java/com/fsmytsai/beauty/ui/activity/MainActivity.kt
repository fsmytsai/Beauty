package com.fsmytsai.beauty.ui.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import com.fsmytsai.beauty.R
import com.fsmytsai.beauty.model.Vote
import com.fsmytsai.beauty.service.presenter.MainPresenter
import com.fsmytsai.beauty.service.view.MainView
import com.fsmytsai.beauty.ui.fragment.HomeFragment

class MainActivity : BaseActivity<MainPresenter>(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_main_container, HomeFragment(), "HomeFragment")
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun createPresenter(): MainPresenter {
        return MainPresenter(this)
    }

    override fun onFailure(errorList: ArrayList<String>) {
        handleErrorMessage(errorList)
    }

    override fun getVoteDataSuccess(voteData: Vote) {
    }

    override fun loadImageSuccess(bitmap: Bitmap) {
    }
}
