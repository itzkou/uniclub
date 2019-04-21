package com.kou.uniclub.Activities


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.kou.uniclub.Extensions.Connectivity
import android.content.Intent
import android.os.Handler
import android.support.design.widget.Snackbar
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import kotlinx.android.synthetic.main.activity_splash.*


class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //No Status Bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        if (PrefsManager.getFirstTime(this@Splash)) {
            PrefsManager.setFirstime(this, false)

            setContentView(R.layout.activity_splash)


            val r = Runnable {
                startActivity(Intent(this@Splash, Intro::class.java))
                finish()
            }
            val h = Handler()
            h.postDelayed(r, 2000) // will be delayed for 2 seconds

        }

        else{
            //TODO("getLogged_status -> Home else -> Sign in")
            setContentView(R.layout.activity_splash)

            if (Connectivity.isConnected(this@Splash)) {

                //startActivity(Intent(this@Splash, Auth::class.java))
            }
            else {
                Snackbar.make(swiperefresh, "No internet! Swipe to refresh", Snackbar.LENGTH_LONG).show()

                swiperefresh.setOnRefreshListener {

                    if (Connectivity.isConnected(this@Splash))
                        //startActivity(Intent(this@Splash, Auth::class.java))
                    else {
                        Snackbar.make(swiperefresh, "No internet", Snackbar.LENGTH_SHORT).show()
                        swiperefresh.isRefreshing = false
                    }

                }

            }

        }
    }
}
