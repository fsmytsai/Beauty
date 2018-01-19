package com.fsmytsai.beauty.ui.fragment

import android.support.v4.app.Fragment
import com.fsmytsai.beauty.service.presenter.BasePresenter

abstract class BaseFragment<out P : BasePresenter> : Fragment() {
    protected val mPresenter: P

    init {
        mPresenter = this.createPresenter()
    }

    protected abstract fun createPresenter(): P

    override fun onStop() {
        super.onStop()
        mPresenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }
}