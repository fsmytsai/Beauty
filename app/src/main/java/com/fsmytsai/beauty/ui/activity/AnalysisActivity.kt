package com.fsmytsai.beauty.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.fsmytsai.beauty.R
import com.fsmytsai.beauty.service.app.FileChooser
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_analysis.*
import kotlinx.android.synthetic.main.top_section.*
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.regex.Pattern

class AnalysisActivity : BaseActivity() {
    private val mFileChooser = FileChooser(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)
        initViews()
        if (!mFileChooser.showFileChooser("image/*", null, false, true)) {
            showErrorMessage("您沒有適合的檔案選取器")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        tv_toolBar.text = "Rank"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FileChooser.ACTIVITY_FILE_CHOOSER -> {
                if (mFileChooser.onActivityResult(requestCode, resultCode, data)) {
                    val files = mFileChooser.chosenFiles
                    iv_analysis_show.setImageURI(mFileChooser.oneUri)
                    upload(files!![0])
                } else {
                    finish()
                }
                return
            }
        }
    }

    //    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            FileChooser.ACTIVITY_FILE_CHOOSER -> {
//                if (mFileChooser.onActivityResult(requestCode, resultCode, data)) {
//                    val files = mFileChooser.chosenFiles
//                    iv_analysis_show.setImageURI(mFileChooser.oneUri)
//                    upload(files!![0])
//                } else {
////                    finish()
//                }
//                return
//            }
//        }
//    }

    fun upload(file: File) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        val fileName = file.name
        val type = fileName.split(Pattern.quote(".").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        if (type[type.size - 1].equals("jpg", ignoreCase = true))
            type[type.size - 1] = "jpeg"
        builder.addFormDataPart("image", file.name, RequestBody.create(MediaType.parse("image/" + type[type.size - 1]), file))

        val body = builder.build()

        val request = Request.Builder()
                .url("http://beauty.southeastasia.cloudapp.azure.com/api/upload")
                .post(body)
                .build()

        showMessage("圖片上傳中...")

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { showErrorMessage("請檢察網路連線") }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val statusCode = response.code()
                val resMsg = response.body()!!.string()
                runOnUiThread {
                    if (statusCode == 200) {
                        AlertDialog.Builder(this@AnalysisActivity).setTitle("辨識結果")
                                .setMessage(resMsg)
                                .setPositiveButton("確定", null)
                                .show()
                    } else {
                        handleErrorMessage(Gson().fromJson(resMsg, ArrayList<String>()::class.java))
                    }
                }
            }
        })
    }
}
