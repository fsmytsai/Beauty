package com.fsmytsai.beauty.ui.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.Snackbar
import android.support.v4.util.LruCache
import android.support.v7.app.AlertDialog
import android.view.ViewGroup
import com.fsmytsai.beauty.service.app.SharedService

abstract class BaseActivity : AppCompatActivity() {

    override fun onDestroy() {
        super.onDestroy()
        mMemoryCaches?.evictAll()
    }

    private var mMemoryCaches: LruCache<String, Bitmap>? = null

    protected fun initCache() {
        val maxMemory = Runtime.getRuntime().maxMemory() / 3
        mMemoryCaches = object : LruCache<String, Bitmap>(maxMemory.toInt()) {
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                return value!!.byteCount
            }
        }
    }

    fun getBitmapFromLrucache(imageName: String): Bitmap? {
        return mMemoryCaches?.get(imageName)
    }

    fun addBitmapToLrucaches(imageName: String, bitmap: Bitmap) {
        if (getBitmapFromLrucache(imageName) == null) {
            mMemoryCaches?.put(imageName, bitmap)
        }
    }

    fun removeBitmapFromLrucaches(imageName: String) {
        if (getBitmapFromLrucache(imageName) == null) {
            mMemoryCaches?.remove(imageName)
        }
    }

    fun handleErrorMessage(errorList: ArrayList<String>) {
        if (errorList.size == 1) {
            showErrorMessage(errorList[0])
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

    fun showMessage(message: String) {
        val rootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        val snackBar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
        SharedService.setSnackbarColor(snackBar, Color.WHITE, Color.BLUE)
        snackBar.show()
    }

    fun showErrorMessage(errorMessage: String) {
        val rootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        val snackBar = Snackbar.make(rootView, errorMessage, Snackbar.LENGTH_LONG)
        SharedService.setSnackbarColor(snackBar, Color.WHITE, Color.RED)
        snackBar.show()
    }
}
