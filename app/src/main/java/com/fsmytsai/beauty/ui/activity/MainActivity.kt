package com.fsmytsai.beauty.ui.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import com.fsmytsai.beauty.R
import com.fsmytsai.beauty.model.Votes
import com.fsmytsai.beauty.service.presenter.MainPresenter
import com.fsmytsai.beauty.service.view.MainView
import com.fsmytsai.beauty.ui.fragment.HomeFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.fragment_home.*
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.fsmytsai.beauty.ui.fragment.VoteFragment
import com.google.android.gms.common.api.ApiException

class MainActivity : MVPActivity<MainPresenter>(), MainView {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    var userData: GoogleSignInAccount? = null
    private var mIsFirstIn = true
    lateinit var votes: Votes
    var bitmapCount = 0
    private val GOOGLE_LOGIN_CODE = 80

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        userData = GoogleSignIn.getLastSignedInAccount(this)

        val userName = when (userData) {
            null -> "遊客"
            else -> userData!!.displayName
        }

        showMessage("$userName 您好!")

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_main_container, HomeFragment(), "HomeFragment")
                .commit()

        initCache()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getVotes() {
        mPresenter.getVotes()
    }

    override fun createPresenter(): MainPresenter {
        return MainPresenter(this)
    }

    override fun onFailure(errorList: ArrayList<String>) {
        handleErrorMessage(errorList)
    }

    override fun getVoteDataSuccess(votes: Votes) {
        if (mIsFirstIn && votes.voteList != null) {
            this.votes = votes
            mPresenter.loadImage("${votes.base_url}${votes.voteList[0].image_name}", votes.voteList[0].image_name, null)
        } else {
            this.votes.voteList.addAll(votes.voteList)
        }
    }

    override fun loadImageSuccess(bitmap: Bitmap, imageName: String) {
        if (mIsFirstIn && imageName == votes.voteList[0].image_name) {
            mIsFirstIn = false
            pb_home_load.visibility = View.GONE
            iv_home_vote?.setImageBitmap(bitmap)
            loadImages(1, 5)
        }
        bitmapCount++
        addBitmapToLrucaches(imageName, bitmap)
    }

    fun loadImages(startIndex: Int, count: Int) {
        for (i in startIndex until startIndex + count)
            mPresenter.loadImage("${votes.base_url}${votes.voteList[i].image_name}", votes.voteList[i].image_name, null)
    }

    fun getMissImage(index: Int, imageView: ImageView) {
        mPresenter.loadImage("${votes.base_url}${votes.voteList[index].image_name}", votes.voteList[index].image_name, imageView)
    }

    fun getBitmap(index: Int): Bitmap? {
        return getBitmapFromLrucache(votes.voteList[index].image_name)
    }

    fun removeFirstBitmap() {
        removeBitmapFromLrucaches(votes.voteList[0].image_name)
    }

    fun login() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_LOGIN_CODE) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                userData = task.getResult(ApiException::class.java)
                showMessage("${userData?.displayName} 您好!")
            } catch (e: ApiException) {
                showErrorMessage("登入失敗")
                Log.e("BeautyRANK", e.message)
            }
        }
    }

    override fun onBackPressed() {
        val voteFragment = supportFragmentManager.findFragmentByTag("VoteFragment")
        if (voteFragment != null)
            (voteFragment as VoteFragment).dragImageViewList[1].visibility = View.GONE

        super.onBackPressed()
    }
}
