package com.fsmytsai.beauty.ui.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.SharedElementCallback
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
    val dragImageViewList = ArrayList<DragImageView>()
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

        val firstDragImageView = DragImageView(mMainActivity)
        dragImageViewList.add(firstDragImageView)

        val firstLayoutParams = RelativeLayout.LayoutParams((dm.widthPixels * 0.8).toInt(), (dm.heightPixels * 0.6).toInt())
        firstLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

        dragImageViewList[0].layoutParams = firstLayoutParams
        dragImageViewList[0].adjustViewBounds = true
        dragImageViewList[0].setBackgroundColor(Color.WHITE)
        dragImageViewList[0].setImageBitmap(getCompleteBitmap(0, dragImageViewList[0]))
        dragImageViewList[0].setMyDragListener(mMyDragListener)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dragImageViewList[0].transitionName = "imageTransition"
        }
        view.rl_vote_container.addView(dragImageViewList[0])

        view.tv_vote_topic.text = mMainActivity.votes.voteList[0].topic

        setEnterSharedElementCallback(object : SharedElementCallback() {

            override fun onSharedElementEnd(sharedElementNames: MutableList<String>?, sharedElements: MutableList<View>?, sharedElementSnapshots: MutableList<View>?) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)

                if (!mIsOpening)
                    return

                mIsOpening = false

                view.rl_vote_container.post {
                    val secondDragImageView = DragImageView(mMainActivity)
                    dragImageViewList.add(secondDragImageView)
                    dragImageViewList[1].layoutParams = getSecondLayoutParams()
                    dragImageViewList[1].adjustViewBounds = true
                    dragImageViewList[1].setBackgroundColor(Color.WHITE)
                    dragImageViewList[1].setImageBitmap(getCompleteBitmap(1, dragImageViewList[1]))
                    dragImageViewList[1].setMyDragListener(mMyDragListener)
                    view.rl_vote_container.addView(dragImageViewList[1], 0)
                }

            }
        })
    }

    private fun getSecondLayoutParams(): RelativeLayout.LayoutParams {
        val secondLayoutParams = RelativeLayout.LayoutParams((rl_vote_container.width * 0.4).toInt(), (rl_vote_container.height * 0.4).toInt())
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

            if (dragImageViewList.size <= 1)
                return

            dragImageViewList[1].scaleTo(2f)
        }

        override fun finished() {
            mMainActivity.bitmapCount--
            mMainActivity.removeFirstBitmap()
            if (mMainActivity.bitmapCount < 5)
                mMainActivity.loadImages(4, 5)

            rl_vote_container.removeView(dragImageViewList[0])
            mMainActivity.votes.voteList.removeAt(0)
            tv_vote_topic.text = mMainActivity.votes.voteList[0].topic
            dragImageViewList.removeAt(0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dragImageViewList[0].transitionName = "imageTransition"
            }

            if (mMainActivity.votes.voteList.size < 25)
                mMainActivity.getVotes()

            val dragImageView = DragImageView(mMainActivity)
            dragImageViewList.add(dragImageView)

            dragImageViewList[1].layoutParams = getSecondLayoutParams()
            dragImageViewList[1].adjustViewBounds = true
            dragImageViewList[1].setBackgroundColor(Color.WHITE)
            dragImageViewList[1].setImageBitmap(getCompleteBitmap(1, dragImageViewList[1]))
            dragImageViewList[1].setMyDragListener(this)
            rl_vote_container.addView(dragImageViewList[1], 0)
        }
    }

    private fun getCompleteBitmap(index: Int, imageView: ImageView): Bitmap? {
        val bitmap = mMainActivity.getBitmap(index)
        if (bitmap == null) {
            Log.d("Beauty", "Image null")
            mMainActivity.getMissImage(index, imageView)
        }
        return bitmap
    }

    private fun showAgree() {
        iv_vote_agree.animate().alpha(1f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        iv_vote_agree?.animate()?.alpha(0f)?.duration = 300
                    }
                })
    }

    override fun voteSuccess() {

    }

    override fun onFailure(errorList: ArrayList<String>) {
        mMainActivity.handleErrorMessage(errorList)
    }

}