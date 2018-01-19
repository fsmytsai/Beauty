package com.fsmytsai.beauty.ui.activity

import com.fsmytsai.beauty.service.presenter.BasePresenter

abstract class MVPActivity<out P : BasePresenter>:BaseActivity() {
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