package com.fsmytsai.beauty.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fsmytsai.beauty.R
import com.fsmytsai.beauty.ui.activity.MainActivity
import android.os.Build
import android.support.transition.*

class HomeFragment : BaseFragment() {
    private val mMainActivity: MainActivity  by lazy { activity!! as MainActivity }
    private lateinit var ivMainVote: ImageView
    private var mIsFirstIn = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initViews(view)

        return view
    }

    private fun initViews(view: View) {
        val tvToolBar = view.findViewById<TextView>(R.id.tv_toolBar)
        tvToolBar.text = "Beauty"
        mMainActivity.setSupportActionBar(view.findViewById(R.id.toolbar))
        mMainActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        mMainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        ivMainVote = view.findViewById(R.id.iv_home_vote)
        if (mIsFirstIn) {
            mIsFirstIn = false
            ivMainVote.setImageResource(R.drawable.i1)
        } else
            ivMainVote.setImageResource(R.drawable.i2)

        ivMainVote.setOnClickListener {
            val voteFragment = VoteFragment()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                voteFragment.sharedElementEnterTransition = ImageTransition()
//                voteFragment.enterTransition = Slide()
//                voteFragment.exitTransition = Slide()
                voteFragment.sharedElementReturnTransition = ImageTransition()
            }

            mMainActivity.supportFragmentManager
                    .beginTransaction()
                    .addSharedElement(ivMainVote, "imageTransition")
                    .replace(R.id.fl_main_container, voteFragment, "VoteFragment")
                    .addToBackStack(null)
                    .commit();
        }

    }

    private class ImageTransition : TransitionSet() {
        init {
            ordering = ORDERING_TOGETHER
            addTransition(ChangeBounds()).addTransition(ChangeTransform()).addTransition(ChangeImageTransform())
        }
    }
}