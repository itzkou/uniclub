package com.kou.uniclub.Activities


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*


class Splash : AppCompatActivity() {


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (PrefsManager.getFirstTime(this@Splash)) {
            setContentView(R.layout.activity_splash)
            val rotation = AnimationUtils.loadAnimation(this@Splash, R.anim.loading_icon)
            rotation.repeatCount = Animation.INFINITE
            imLoading.animation = rotation
            PrefsManager.setFirstime(this, false)

            val r = Runnable {
                startActivity(Intent(this@Splash, Intro::class.java))
                finish()
            }
            val h = Handler()
            h.postDelayed(r, 1500) // will be delayed for 2 seconds

        } else {

            setContentView(R.layout.activity_splash)
            val rotation = AnimationUtils.loadAnimation(this@Splash, R.anim.loading_icon)
            rotation.repeatCount = Animation.INFINITE
            imLoading.animation = rotation

            ReactiveNetwork
                .observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isConnectedToInternet ->
                    if (isConnectedToInternet) {

                        val r = Runnable {
                            startActivity(Intent(this@Splash, Home::class.java))
                            finish()
                        }
                        val h = Handler()
                        h.postDelayed(r, 2000)
                    } else {

                        val snacko = Snackbar.make(imLogo, "Verify your network status", Snackbar.LENGTH_INDEFINITE)
                        snacko.config(this@Splash)
                        snacko.setAction("REFRESH") {
                            imLoading.visibility = View.VISIBLE
                            imLoading.animation = rotation

                        }
                        snacko.show()
                    }
                }


        }
    }

    fun Snackbar.config(context: Context) {
        val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(12, 24, 12, 12)
        this.view.layoutParams = params
        this.view.background = ContextCompat.getDrawable(context, R.drawable.bg_snackbar)

        this.setActionTextColor(ContextCompat.getColor(context, R.color.orange))
        val text = this.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        text.setTextColor(ContextCompat.getColor(context, R.color.white))
        text.maxLines = 1
        text.textSize = 12f

        ViewCompat.setElevation(this.view, 6f)
    }
}
