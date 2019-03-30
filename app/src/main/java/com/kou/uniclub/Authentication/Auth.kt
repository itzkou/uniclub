package com.kou.uniclub.Authentication

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.widget.Toast
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_auth.*

class Auth : AppCompatActivity() {

    private val appPermissions= arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION)
    companion object {
        private  const val PERMIS_REQUEST=1997
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        checkPermis()


    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== PERMIS_REQUEST)
        { val permisResults=HashMap<String,Int>()
            var deniedCount=0

            Log.d("gsize",grantResults.size.toString())

            // gather granted results
            for(i in 0 until grantResults.size)
            {
                if (grantResults[i]==PackageManager.PERMISSION_DENIED)
                {
                    permisResults[permissions[i]] = grantResults[i]
                    deniedCount++
                }


                 if(deniedCount==0) {
                    //Toast.makeText(this, "All permissions are granted", Toast.LENGTH_SHORT).show()
                    //init()

                }
                else
                {
                    for (j in permisResults.entries)
                    {  val key=j.key


                        if(ActivityCompat.shouldShowRequestPermissionRationale(this,key))
                        {
                            Toast.makeText(this, "Enable all permissions in order to use our services", Toast.LENGTH_SHORT).show()


                        }
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_SHORT).show()


                        }
                    }

                }

            }

            if (deniedCount==0) {
                Toast.makeText(this, "All permissions are granted", Toast.LENGTH_SHORT).show()
                init()
            }




        }


    }

    fun checkPermis():Boolean{
        val listPermis=ArrayList<String>()

        for (i in appPermissions){
            if (ContextCompat.checkSelfPermission(this,i)!= PackageManager.PERMISSION_GRANTED){
                listPermis.add(i)

            }
        }

        if (listPermis.isNotEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermis.toArray(arrayOfNulls(listPermis.size)), PERMIS_REQUEST)
            return false
        }

        return true
    }

    fun init(){

        btn_signin.setOnClickListener {
            startActivity(Intent(this@Auth,SignIN::class.java))
        }

        btn_signup.setOnClickListener {
            startActivity(Intent(this@Auth,SignUP::class.java))

        }
    }
}
