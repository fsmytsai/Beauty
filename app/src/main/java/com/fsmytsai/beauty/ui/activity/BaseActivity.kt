package com.fsmytsai.beauty.ui.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.Snackbar
import android.support.v4.util.LruCache
import android.support.v7.app.AlertDialog
import android.view.ViewGroup
import com.fsmytsai.beauty.service.app.SharedService
import com.fsmytsai.beauty.service.presenter.BasePresenter

abstract class BaseActivity<out P : BasePresenter> : AppCompatActivity() {
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
        mMemoryCaches?.evictAll()
        mPresenter.onDestroy()
    }

    private var mMemoryCaches: LruCache<String, Bitmap>? = null

    protected fun initCache() {
        val maxMemory = Runtime.getRuntime().maxMemory().toInt() / 1024
        val maxCache = maxMemory / 3 //使用 1/3 內存
        mMemoryCaches = object : LruCache<String, Bitmap>(maxCache) {

            override fun sizeOf(key: String?, value: Bitmap?): Int {
                return value!!.byteCount
            }
        }
    }

    protected fun getBitmapFromLrucache(imageName: String): Bitmap? {
        return mMemoryCaches?.get(imageName)
    }

    protected fun addBitmapToLrucaches(imageName: String, bitmap: Bitmap) {
        if (getBitmapFromLrucache(imageName) == null) {
            mMemoryCaches?.put(imageName, bitmap)
        }
    }

    protected fun removeBitmapFromLrucaches(imageName: String) {
        if (getBitmapFromLrucache(imageName) == null) {
            mMemoryCaches?.remove(imageName)
        }
    }

    protected fun handleErrorMessage(errorList: ArrayList<String>) {
        if (errorList.size == 1) {
            val rootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
            val snackBar = Snackbar.make(rootView, errorList[0], Snackbar.LENGTH_LONG)
            SharedService.setSnackbarColor(snackBar, Color.WHITE, Color.RED)
            snackBar.show()
        } else {
            var msg = ""

            for (i in 0 until errorList.size) {
                msg += errorList[i]
                if (i != errorList.size - 1) {
                    msg += "\n"
                }
            }

            AlertDialog.Builder(this)
                    .setTitle("錯誤訊息")
                    .setMessage(msg)
                    .setPositiveButton("知道了", null)
                    .show()
        }
    }
}
