package com.fsmytsai.beauty.service.app

import android.support.design.widget.Snackbar
import android.widget.TextView
import com.fsmytsai.beauty.R

class SharedService {
    companion object {

        fun setSnackbarColor(snackbar: Snackbar, messageColor: Int, backgroundColor: Int) {
            val view = snackbar.view
            view.setBackgroundColor(backgroundColor)
            view.findViewById<TextView>(R.id.snackbar_text).setTextColor(messageColor)
        }
    }
}