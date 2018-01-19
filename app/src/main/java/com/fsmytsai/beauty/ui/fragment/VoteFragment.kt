package com.fsmytsai.beauty.ui.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.SharedElementCallback
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.fsmytsai.beauty.R
import com.fsmytsai.beauty.ui.activity.MainActivity
import com.fsmytsai.beauty.ui.view.DragImageView

class VoteFragment : BaseFragment() {
    private val mMainActivity: MainActivity  by lazy { activity!! as MainActivity }
    private val mBitmapList = ArrayList<Bitmap>()
    private val mDragImageViewList = ArrayList<DragImageView>()
    private lateinit var rlVoteContainer: RelativeLayout
    private lateinit var ivVoteAgree: ImageView
    private var mScreenWidth = 0
    private var mIsOpening = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_vote, container, false)

        mBitmapList.add(BitmapFactory.decodeResource(resources, R.drawable.i1))
        mBitmapList.add(BitmapFactory.decodeResource(resources, R.drawable.i2))
//        mBitmapList.add(BitmapFactory.decodeResource(resources, R.drawable.i3))
//        mBitmapList.add(BitmapFactory.decodeResource(resources, R.drawable.i4))
//        mBitmapList.add(BitmapFactory.decodeResource(resources, R.drawable.i5))
//        mBitmapList.add(BitmapFactory.decodeResource(resources, R.drawable.i6))

        initViews(view)

        return view
    }

    private fun initViews(view: View) {
        val tvToolBar = view.findViewById<TextView>(R.id.tv_toolBar)
        tvToolBar.text = "Vote"
        mMainActivity.setSupportActionBar(view.findViewById(R.id.toolbar))
        mMainActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        mMainActivity.supportActionBar?.setHomeButtonEnabled(true)
        mMainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ivVoteAgree = view.findViewById(R.id.iv_vote_agree)
        rlVoteContainer = view.findViewById(R.id.rl_vote_container)

        val dm = DisplayMetrics()
        mMainActivity.windowManager.defaultDisplay.getMetrics(dm)
        mScreenWidth = dm.widthPixels

        val firstDragImageView = DragImageView(mMainActivity)
        mDragImageViewList.add(firstDragImageView)

        mDragImageViewList[0].layoutParams = getFirstLayoutParams()

        mDragImageViewList[0].adjustViewBounds = true

        mDragImageViewList[0].setImageBitmap(mBitmapList[0])

        mDragImageViewList[0].setMyDragListener(mMyDragListener)

        mDragImageViewList[0].transitionName = "imageTransition"

        rlVoteContainer.addView(mDragImageViewList[0])

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
                mDragImageViewList[1].setImageBitmap(mBitmapList[1])
                mDragImageViewList[1].setMyDragListener(mMyDragListener)
                rlVoteContainer.addView(mDragImageViewList[1], 0)
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

    private val mMyDragListener = object : DragImageView.MyDragListener {

        override fun rollBack() {
        }

        override fun dragging() {
        }

        override fun finish(isAgree: Boolean) {
            if (isAgree)
                ivVoteAgree.setImageResource(R.drawable.yes)
            else
                ivVoteAgree.setImageResource(R.drawable.no)

            showAgree()

            if (mDragImageViewList.size <= 1)
                return

            mDragImageViewList[1].scaleTo(2f)
        }

        override fun finished() {
            mBitmapList.removeAt(0)
            rlVoteContainer.removeView(mDragImageViewList[0])
            mDragImageViewList.removeAt(0)

            mDragImageViewList[0].transitionName = "imageTransition"

            if (mBitmapList.size <= 1)
                return

            val dragImageView = DragImageView(mMainActivity)
            mDragImageViewList.add(dragImageView)

            mDragImageViewList[1].layoutParams = getSecondLayoutParams()
            mDragImageViewList[1].adjustViewBounds = true
            mDragImageViewList[1].setImageBitmap(mBitmapList[1])
            mDragImageViewList[1].setMyDragListener(this)
            rlVoteContainer.addView(mDragImageViewList[1], 0)
        }
    }


    private fun showAgree() {
        ivVoteAgree.animate().alpha(1f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        ivVoteAgree.animate().alpha(0f).duration = 300
                    }
                })
    }
}