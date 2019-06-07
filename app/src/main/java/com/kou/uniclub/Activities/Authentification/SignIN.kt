package com.kou.uniclub.Activities.Authentification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kou.uniclub.Activities.Home
import com.kou.uniclub.Extensions.Validation
import com.kou.uniclub.Model.Auth.LoginResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SignIN : AppCompatActivity(), Validation {


    /******* SOCIAL AUTH *******/
    private val GOOGLE_SIGN = 4
    /******* Social MANAGERS *******/
    private lateinit var callbackManager: CallbackManager
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        btnFb.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        btnSignin.isEnabled = false
        var mail = false
        var pass = false
        edEmail.afterTextChanged {
            mail = it.isValidEmail()
            btnSignin.isEnabled = mail && pass
        }
        edPassword.afterTextChanged {
            pass = it.isValidPassword()
            btnSignin.isEnabled = mail && pass

        }


        btnSignin.setOnClickListener {
            uniSignIn(edEmail.text.toString(), edPassword.text.toString())
        }
        /******************* with facebook *****************/
        callbackManager = CallbackManager.Factory.create()
        btnFb.setOnClickListener {
            facebook()

        }


        /******************* with google *****************/

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()//request email id
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        // Build a GoogleSignInClient with the options specified by gso.
        GooUi()
        tvSignUp.setOnClickListener {
            startActivity(Intent(this@SignIN, UserCategory::class.java))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN && resultCode == RESULT_OK) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
        //facebook
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun uniSignIn(m: String, p: String) {
        val service = UniclubApi.create()
        service.signIN(m, p).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@SignIN, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    PrefsManager.seToken(this@SignIN, response.body()!!.accessToken)
                    startActivity(Intent(this@SignIN, Home::class.java))
                    finish()
                } else {
                    val snacko=Snackbar.make(rootSignIN, "User doesn't exist", Snackbar.LENGTH_LONG)
                        snacko.config(this@SignIN)

                        snacko.show()
                    //facebook log out
                    LoginManager.getInstance().logOut()
                    //google sign out
                    mGoogleSignInClient.signOut().addOnSuccessListener {
                    }
                }


            }

        })

    }

    private fun facebook() {
        btnFb.setReadPermissions(Arrays.asList("email", "public_profile"))
        btnFb.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    val request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken()
                    ) { me, _ ->
                        val email = me.get("email").toString()
                        val fn = me.get("first_name").toString()
                        val ln = me.get("last_name").toString()
                        val pic = me.getJSONObject("picture").getJSONObject("data").get("url").toString()


                        uniSignIn(email, "123social")
                        //TODO("response caching")
                        PrefsManager.setPicture(this@SignIN, pic)


                    }

                    val parameters = Bundle()
                    parameters.putString("fields", "id,first_name,last_name,picture.type(large), email")
                    request.parameters = parameters
                    request.executeAsync()


                }

            }

            override fun onCancel() {
                Log.e("FBLOGIN_FAILD", "Cancel")
            }

            override fun onError(error: FacebookException) {
                Log.e("FBLOGIN_FAILD", "ERROR", error)
            }
        })


    }

    private fun GooUi() {

        btnGoogle.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_SIGN)//pass the declared request code here

        }

        for (i in 0 until btnGoogle.childCount) {
            val v = btnGoogle.getChildAt(i)
            if (v is TextView) {
                val tv = v
                tv.setBackgroundResource(R.drawable.ic_gmail)
                val params = tv.layoutParams
                params.width = 64
                params.height = 64
                tv.layoutParams = params
                return
            }

        }
    }


    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {

        val account = task.getResult(ApiException::class.java)!!
        uniSignIn(account.email.toString(), "123social")
        PrefsManager.setPicture(this@SignIN, account.photoUrl.toString())
    }

    fun Snackbar.config(context: Context) {
        val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(12, 24, 12, 12)
        this.view.layoutParams = params
        this.view.background = ContextCompat.getDrawable(context, R.drawable.bg_snackbar)

        this.setActionTextColor(ContextCompat.getColor(context, R.color.orange))
        val text = this.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        text.setTextColor(ContextCompat.getColor(context, R.color.white))

        text.textAlignment=TextView.TEXT_ALIGNMENT_CENTER
        text.maxLines = 1
        text.textSize = 12f

        ViewCompat.setElevation(this.view, 6f)
    }


}
