package com.fsmytsai.beauty.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fsmytsai.beauty.R
import com.fsmytsai.beauty.ui.activity.MainActivity
import android.os.Build
import android.support.transition.*
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private val mMainActivity: MainActivity  by lazy { activity!! as MainActivity }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initViews(view)
        mMainActivity.getVotes()
        return view
    }

    private fun initViews(view: View) {
        val tvToolBar = view.findViewById<TextView>(R.id.tv_toolBar)
        tvToolBar.text = "Beauty"
        mMainActivity.setSupportActionBar(view.findViewById(R.id.toolbar))
        mMainActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        mMainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        iv_home_vote.setOnClickListener {
            val voteFragment = VoteFragment()

            if (mMainActivity.userData != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    voteFragment.sharedElementEnterTransition = ImageTransition()
//                voteFragment.enterTransition = Slide()
//                voteFragment.exitTransition = Slide()
                    voteFragment.sharedElementReturnTransition = ImageTransition()
                }

                mMainActivity.supportFragmentManager
                        .beginTransaction()
                        .addSharedElement(iv_home_vote, "imageTransition")
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