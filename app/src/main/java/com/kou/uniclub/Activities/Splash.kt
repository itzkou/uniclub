package com.kou.uniclub.Activities


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import kotlinx.android.synthetic.main.activity_splash.*


class Splash : AppCompatActivity() {

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val ni = intent.extras?.get(ConnectivityManager.EXTRA_NETWORK_INFO) as NetworkInfo
            if (ni.state == NetworkInfo.State.CONNECTED) {

                connected()

            } else

                disconnected()


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val rotation = AnimationUtils.loadAnimation(this@Splash, R.anim.loading_icon)
        rotation.repeatCount = Animation.INFINITE
        imLoading.animation = rotation


    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
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

    private fun connected() {
        if (PrefsManager.getFirstTime(this@Splash)) {


            PrefsManager.setFirstime(this, false)

            val r = Runnable {
                startActivity(Intent(this@Splash, Intro::class.java))
                finish()
            }
            val h = Handler()
            h.postDelayed(r, 1500) // will be delayed for 2 seconds

        } else {
            val r = Runnable {
                startActivity(Intent(this@Splash, Home::class.java))
                finish()
            }
            val h = Handler()
            h.postDelayed(r, 1500)
        }
    }

    private fun disconnected() {
        val snacko = Snackbar.make(imLogo, "Verify your network status", Snackbar.LENGTH_INDEFINITE)
        snacko.config(this@Splash)
        snacko.setAction("REFRESH") {
        }
        snacko.show()
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager //1
        val networkInfo = connectivityManager.activeNetworkInfo //2
        return networkInfo != null && networkInfo.isConnected //3
    }
}
