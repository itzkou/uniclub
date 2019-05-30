package com.kou.uniclub.Activities.Authentification

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kou.uniclub.Activities.Home
import com.kou.uniclub.Extensions.Validation
import com.kou.uniclub.Model.Auth.LoginResponse
import com.kou.uniclub.Model.Auth.SignUpResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import kotlinx.android.synthetic.main.activity_pro_sign_up.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class ProSignUp : AppCompatActivity(), Validation {
    /******* SOCIAL AUTH *******/
    private val GOOGLE_SIGN = 4
    /******* Social MANAGERS *******/
    private lateinit var callbackManager: CallbackManager
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    /******* User attributes *******/

    private lateinit var fName: String
    private lateinit var lName: String
    private lateinit var mail: String
    private lateinit var password: String
    private lateinit var adress: String
    private lateinit var passwordC: String
    private var image: MultipartBody.Part? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pro_sign_up)
        btnFb.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        //TODO("instead of adress change it to company")
        adress = "deprectaed"

        /******* Facebook *****/
        callbackManager = CallbackManager.Factory.create()
        btnFb.setOnClickListener {
            facebook()
        }
        /******* google *****/

        GooUi()
        /******* uniclub signup *****/

        formFill()
        btnSignup.setOnClickListener {
            uniSignUP(fName, lName,null, mail, password, passwordC, adress, image)
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

    private fun GooUi() {
        val gooBtn = findViewById<SignInButton>(R.id.btnGoogle)
        gooBtn.setOnClickListener {
            google()

        }

        for (i in 0 until gooBtn.childCount) {
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

    private fun formFill() {


        edUsername.afterTextChanged {
            edUsername.error = if (it.isValidName()) null
            else "Enter a valid name and surname"
        }
        edEmail.afterTextChanged {
            edEmail.error = if (it.isValidEmail()) null
            else "Enter a valid email"
        }
        edPassword.afterTextChanged {
            edPassword.error = if (it.isValidPassword()) null
            else "Password is 6 digits long and includes at least one numeric digit."
        }
        edPasswordC.afterTextChanged {
            edPasswordC.error = if (it == edPassword.text.toString()) null
            else "Passwords don't match"
        }


        /******* Form Validation *******/
        btnSignup.isEnabled = false
        val validator = object : TextWatcher {


            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val u = edUsername.text.toString().isValidName()
                val e = edEmail.text.toString().isValidEmail()
                val p = edPassword.text.toString().isValidPassword()
                val pc = edPassword.text.toString() == edPasswordC.text.toString()
                btnSignup.isEnabled = u && e && p && pc

                if (btnSignup.isEnabled) {
                    val name = edUsername.text.toString()

                    fName = name.substring(0, name.indexOf(" ") + 1)
                    lName = name.substring(name.lastIndexOf(" ") + 1, name.length)
                    mail = edEmail.text.toString()
                    password = edPassword.text.toString()
                    passwordC = edPasswordC.text.toString()


                }


            }
        }
        edUsername.addTextChangedListener(validator)
        edPassword.addTextChangedListener(validator)
        edUsername.addTextChangedListener(validator)
        edPasswordC.addTextChangedListener(validator)


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
                        fName = fn
                        lName = ln
                        mail = email
                        password = "123social"
                        passwordC = "123social"

                        uniSignUP(fn, ln, "null", email, "123facebook", "123facebook", "unknown", null)
                        //TODO("response caching")
                        PrefsManager.setPicture(this@ProSignUp, pic)


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

    private fun google() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()//request email id
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN)//pass the declared request code here
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {

        val account = task.getResult(ApiException::class.java)!!
        fName = account.familyName.toString()
        lName = account.displayName.toString()
        mail = account.email.toString()
        password = "123social"
        passwordC = "123social"
        uniSignUP(
            fName, lName, null, mail, passwordC, passwordC, adress, MultipartBody.Part.createFormData(
                "attachment",
                "",
                RequestBody.create(MediaType.parse("text/plain"), "")
            )
        )
        //TODO("response caching")
        PrefsManager.setPicture(this@ProSignUp, account.photoUrl.toString())
    }

    private fun uniSignUP(
        fname: String,
        lname: String,
        birth: String?,
        mail: String,
        pass: String,
        passc: String,
        adress: String,
        image: MultipartBody.Part?
    ) {
        val service = UniclubApi.create()


        service.signUP(
            fName,
            lName,
            null,
            mail,
            password,
            password,
            adress,
            MultipartBody.Part.createFormData(
                "attachment",
                "",
                RequestBody.create(MediaType.parse("text/plain"), "")
            )
        ).enqueue(object : Callback<SignUpResponse> {
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                if (t is IOException)
                    Toast.makeText(this@ProSignUp, "Network faillure", Toast.LENGTH_SHORT).show()


            }

            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    if (response.code() == 201)
                        uniSignIn(service)

                } else if (response.code() == 404)
                    Toast.makeText(
                        this@ProSignUp,
                        "Email already exists or missing field",
                        Toast.LENGTH_SHORT
                    ).show()

            }


        })
    }


    private fun uniSignIn(service: UniclubApi) {
        service.signIN(mail, password).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    PrefsManager.seToken(this@ProSignUp, response.body()!!.accessToken)
                    startActivity(Intent(this@ProSignUp, Home::class.java))
                }
            }

        })
    }

}
