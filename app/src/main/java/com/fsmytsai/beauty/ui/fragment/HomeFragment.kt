package com.fsmytsai.beauty.ui.fragment

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fsmytsai.beauty.R
import com.fsmytsai.beauty.ui.activity.MainActivity
import android.support.transition.*
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import com.fsmytsai.beauty.ui.activity.AnalysisActivity
import com.fsmytsai.beauty.ui.activity.RankActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.top_section.view.*


class HomeFragment : Fragment() {
    private val mMainActivity: MainActivity  by lazy { activity!! as MainActivity }
    private var isFirstIn = true
    private val REQUEST_EXTERNAL_STORAGE = 18

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        view.tv_toolBar.text = "BeautyRANK"
        mMainActivity.setSupportActionBar(view.findViewById(R.id.toolbar))
        mMainActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        mMainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        if (isFirstIn) {
            isFirstIn = false
            mMainActivity.getVotes()
        } else {
            view.iv_home_vote.setImageBitmap(mMainActivity.getBitmap(0))
            view.pb_home_load.visibility = View.GONE
            view.iv_home_rank.setImageBitmap(mMainActivity.firstRankBitmap)
            view.pb_home_rank.visibility = View.GONE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.iv_home_vote.transitionName = "imageTransition"
        }
        view.iv_home_vote.setOnClickListener {
            val voteFragment = VoteFragment()

            if (mMainActivity.userData != null) {
                voteFragment.sharedElementEnterTransition = ImageTransition()
                voteFragment.enterTransition = Explode()
                voteFragment.exitTransition = Explode()
                voteFragment.sharedElementReturnTransition = ImageTransition()

                mMainActivity.supportFragmentManager
                        .beginTransaction()
                        .addSharedElement(view.iv_home_vote, "imageTransition")
                        .replace(R.id.fl_main_container, voteFragment, "VoteFragment")
                        .addToBackStack(null)
                        .commit()
            } else {
                mMainActivity.login()
            }
        }

        view.iv_home_rank.setOnClickListener {
            val intent = Intent(mMainActivity, RankActivity::class.java)
            intent.putExtra("rank", Gson().toJson(mMainActivity.rank))
//            val bs = ByteArrayOutputStream()
//            mMainActivity.firstRankBitmap.compress(Bitmap.CompressFormat.PNG, 50, bs)
//            intent.putExtra("firstRankBitmap", bs.toByteArray())
            startActivity(intent)
        }

        view.bt_home_upload.setOnClickListener {
            val permission = ActivityCompat.checkSelfPermission(mMainActivity, READ_EXTERNAL_STORAGE)
            if (permission == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(mMainActivity, AnalysisActivity::class.java)
                startActivity(intent)
            } else {
                requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE)
            }
        }

    }

    private class ImageTransition : TransitionSet() {
        init {
            ordering = ORDERING_TOGETHER
            addTransition(ChangeBounds()).addTransition(ChangeTransform()).addTransition(ChangeImageTransform())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(mMainActivity, AnalysisActivity::class.java)
                    startActivity(intent)
                } else {
                    mMainActivity.showErrorMessage("您拒絕選取檔案")
                }
                return
            }
        }
    }


}