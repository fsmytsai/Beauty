package com.fsmytsai.beauty.ui.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.SharedElementCallback
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.fsmytsai.beauty.R
import com.fsmytsai.beauty.service.presenter.VotePresenter
import com.fsmytsai.beauty.service.view.VoteView
import com.fsmytsai.beauty.ui.activity.MainActivity
import com.fsmytsai.beauty.ui.view.DragImageView
import kotlinx.android.synthetic.main.fragment_vote.*
import kotlinx.android.synthetic.main.fragment_vote.view.*
import kotlinx.android.synthetic.main.top_section.view.*

class VoteFragment : BaseFragment<VotePresenter>(), VoteView {

    private val mMainActivity: MainActivity  by lazy { activity!! as MainActivity }
    private val mDragImageViewList = ArrayList<DragImageView>()
    private var mScreenWidth = 0
    private var mIsOpening = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_vote, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        view.tv_toolBar.text = "Vote"
        mMainActivity.setSupportActionBar(view.findViewById(R.id.toolbar))
        mMainActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        mMainActivity.supportActionBar?.setHomeButtonEnabled(true)
        mMainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val dm = DisplayMetrics()
        mMainActivity.windowManager.defaultDisplay.getMetrics(dm)
        mScreenWidth = dm.widthPixels

        val firstDragImageView = DragImageView(mMainActivity)
        mDragImageViewList.add(firstDragImageView)
        mDragImageViewList[0].layoutParams = getFirstLayoutParams()
        mDragImageViewList[0].adjustViewBounds = true
        mDragImageViewList[0].setImageBitmap(getCompleteBitmap(0))
        mDragImageViewList[0].setMyDragListener(mMyDragListener)
        mDragImageViewList[0].transitionName = "imageTransition"
        view.rl_vote_container.addView(mDragImageViewList[0])

        setEnterSharedElementCallback(object : SharedElementCallback() {

            override fun onSharedElementEnd(sharedElementNames: MutableList<String>?, sharedElements: MutableList<View>?, sharedElementSnapshots: MutableList<View>?) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)

                if (!mIsOpening)
                    return

                mIsOpening = false
                val secondDragImageView = DragImageView(mMainActivity)
                mDragImageViewList.add(secondDragImageView)
                mDragImageViewList[1].layoutParams = getSecondLayoutParams()
                mDragImageViewList[1].adjustViewBounds = true
                mDragImageViewList[1].setImageBitmap(getCompleteBitmap(1))
                mDragImageViewList[1].setMyDragListener(mMyDragListener)
                view.rl_vote_container.addView(mDragImageViewList[1], 0)
            }
        })
    }

    private fun getFirstLayoutParams(): RelativeLayout.LayoutParams {
        val firstLayoutParams = RelativeLayout.LayoutParams((mScreenWidth * 0.8).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT)
        firstLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        return firstLayoutParams
    }

    private fun getSecondLayoutParams(): RelativeLayout.LayoutParams {
        val secondLayoutParams = RelativeLayout.LayoutParams((mScreenWidth * 0.4).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT)
        secondLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        return secondLayoutParams
    }

    override fun createPresenter(): VotePresenter {
        return VotePresenter(this)
    }

    private val mMyDragListener = object : DragImageView.MyDragListener {

        override fun rollBack() {
        }

        override fun dragging() {
        }

        override fun finish(isAgree: Boolean) {
            mPresenter.vote(mMainActivity.votes.voteList[0].image_id,
                    mMainActivity.votes.voteList[0].feature_id,
                    mMainActivity.userData!!.id!!,
                    isAgree)

            if (isAgree)
                iv_vote_agree.setImageResource(R.drawable.yes)
            else
                iv_vote_agree.setImageResource(R.drawable.no)

            showAgree()

            if (mDragImageViewList.size <= 1)
                return

            mDragImageViewList[1].scaleTo(2f)
        }

        override fun finished() {
            mMainActivity.bitmapCount--
            mMainActivity.removeFirstBitmap()
            if (mMainActivity.bitmapCount < 5)
                mMainActivity.loadImages(5, 10)

            rl_vote_container.removeView(mDragImageViewList[0])
            mDragImageViewList.removeAt(0)
            mDragImageViewList[0].transitionName = "imageTransition"

            if (mMainActivity.votes.voteList.size < 20)
                mMainActivity.getVotes()

            val dragImageView = DragImageView(mMainActivity)
            mDragImageViewList.add(dragImageView)

            mDragImageViewList[1].layoutParams = getSecondLayoutParams()
            mDragImageViewList[1].adjustViewBounds = true
            mDragImageViewList[1].setImageBitmap(getCompleteBitmap(1))
            mDragImageViewList[1].setMyDragListener(this)
            rl_vote_container.addView(mDragImageViewList[1], 0)
        }
    }

    private fun getCompleteBitmap(index: Int): Bitmap? {
        var bitmap = mMainActivity.getBitmap(index)
        while (bitmap == null) {
            if (mMainActivity.votes.voteList.size > index) {
                mMainActivity.votes.voteList.removeAt(index)
                bitmap = mMainActivity.getBitmap(index)
            } else {
                mMainActivity.showErrorMessage("取得圖片失敗")
                break
            }

        }
        return bitmap
    }

    private fun showAgree() {
        iv_vote_agree.animate().alpha(1f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        iv_vote_agree.animate().alpha(0f).duration = 300
                    }
                })
    }

    override fun voteSuccess() {

    }

    override fun onFailure(errorList: ArrayList<String>) {
        mMainActivity.handleErrorMessage(errorList)
    }

}