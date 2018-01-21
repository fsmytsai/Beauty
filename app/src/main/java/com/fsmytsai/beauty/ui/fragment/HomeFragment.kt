package com.fsmytsai.beauty.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fsmytsai.beauty.R
import com.fsmytsai.beauty.ui.activity.MainActivity
import android.support.transition.*
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {
    private val mMainActivity: MainActivity  by lazy { activity!! as MainActivity }
    private var isFirstIn = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        val tvToolBar = view.findViewById<TextView>(R.id.tv_toolBar)
        tvToolBar.text = "BeautyRANK"
        mMainActivity.setSupportActionBar(view.findViewById(R.id.toolbar))
        mMainActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        mMainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        if (isFirstIn) {
            isFirstIn = false
            mMainActivity.getVotes()
        } else {
            view.iv_home_vote.setImageBitmap(mMainActivity.getBitmap(0))
            view.pb_home_load.visibility = View.GONE
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

    }

    private class ImageTransition : TransitionSet() {
        init {
            ordering = ORDERING_TOGETHER
            addTransition(ChangeBounds()).addTransition(ChangeTransform()).addTransition(ChangeImageTransform())
        }
    }
}