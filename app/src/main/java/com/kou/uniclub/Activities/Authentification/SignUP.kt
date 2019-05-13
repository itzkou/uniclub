package com.kou.uniclub.Activities.Authentification


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.*
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
import com.kou.uniclub.Model.Auth.SignUpResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SignUP : AppCompatActivity(), Validation {

    //TODO("Make all fields Nullable + Upload facebook & google pictures to server")


    private var cities = arrayOf("Ariana", "Tunis", "Bizerte")


    /******* REQUESTS FOR RESULTS*******/
    private val SELECT_FILE = 1
    private val IMAGE_CAPTURE = 2
    private val PERMIS_REQUEST = 3

    /******* SOCIAL AUTH *******/
    private val GOOGLE_SIGN = 4

    /******* IMAGE UPLOAD*******/
    private var chosenFile: File? = null
    private var chosenUri: Uri? = null
    private var image: MultipartBody.Part? = null
    private var mCurrentPhotoPath: String = ""

    /******* ARRAY PERMISS*******/
    private val appPermissions = arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    /******* Social MANAGERS *******/
    private lateinit var callbackManager: CallbackManager
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    /******* User attributes *******/

    private var birthday: String? = null
    private lateinit var fName: String
    private lateinit var lName: String
    private lateinit var mail: String
    private lateinit var password: String
    private lateinit var adress: String
    private lateinit var passwordC: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.kou.uniclub.R.layout.activity_sign_up)
        //TODO("add university fiels and company field for both pro and student")
        adress = "deprectaed"
        btnFb.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)


        /******************* camera  *****************/
        imProfile.setOnClickListener {
            if (checkPermis())
                selectImage()
        }


        val dialogView = LayoutInflater.from(this@SignUP).inflate(R.layout.builder_time_picker, null)
        val builder = AlertDialog.Builder(this@SignUP)
        val timePicker = dialogView.findViewById<DatePicker>(R.id.timePicker)
        builder.setView(dialogView)
        builder.setPositiveButton("confirm") { dialog, which ->

            birthday =
                timePicker.year.toString() + "-" + timePicker.month.toString() + "-" + timePicker.dayOfMonth.toString()
            edBirth.hint = birthday
            edBirth.setHintTextColor(ContextCompat.getColor(this@SignUP, R.color.black))
            dialog?.dismiss()
        }

        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which ->
            dialog?.dismiss()
        }
        val dialog = builder.create()

        edBirth.setOnClickListener {
            dialog.show()

        }



        formFill()

        btnSignup.setOnClickListener {
            uniSignUP(fName,lName,birthday,mail,password,passwordC,adress,image)
        }
        /******************* with facebook *****************/
        callbackManager = CallbackManager.Factory.create()
        btnFb.setOnClickListener {
            facebook()
        }
        /******************* with google *****************/
        btnGoogle.setOnClickListener {
            google()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE && resultCode == RESULT_OK) {

            PrefsManager.setPicture(this@SignUP, mCurrentPhotoPath)
            imProfile.setImageURI(Uri.parse(mCurrentPhotoPath))
            chosenFile = File(mCurrentPhotoPath)
            //multipart stuff
            val requestFile = RequestBody.create(MediaType.parse("image/*"), chosenFile)
            image = MultipartBody.Part.createFormData("image", chosenFile!!.name, requestFile)


        } else if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            PrefsManager.setPicture(this@SignUP, data!!.data!!.toString())

            imProfile.setImageURI(data.data)
            chosenUri = data.data!!
            val filePath = arrayOf(MediaStore.Images.Media.DATA)
            val c = contentResolver.query(chosenUri!!, filePath, null, null, null)
            c!!.moveToFirst()
            val columnIndex = c.getColumnIndex(filePath[0])
            val filePathStr = c.getString(columnIndex)
            c.close()
            chosenFile = File(filePathStr!!)
            val requestFile = RequestBody.create(MediaType.parse("image/*"), chosenFile)
            image = MultipartBody.Part.createFormData("image", chosenFile!!.name, requestFile)

        } else if (requestCode == GOOGLE_SIGN && resultCode == RESULT_OK) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
        //facebook
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMIS_REQUEST) {
            val permisResults = HashMap<String, Int>()
            var deniedCount = 0


            // gather granted results
            for (i in 0 until grantResults.size) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permisResults[permissions[i]] = grantResults[i]
                    deniedCount++
                }


            }
            if (deniedCount == 0) {
                Toast.makeText(this, "All permissions are granted", Toast.LENGTH_SHORT).show()
                selectImage()

            } else
                Toast.makeText(this, "All permissions are required", Toast.LENGTH_SHORT).show()


        }


    }

    private fun uniSignUP(fname:String,lname:String,birth:String?,mail:String,pass:String,passc:String,adress:String,image:MultipartBody.Part?) {
        val service = UniclubApi.create()
        if (image != null) {
            service.signUP(fname, lname, birth, mail, pass,passc, adress, image)
                .enqueue(object : Callback<SignUpResponse> {
                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {

                        if (t is IOException)
                            Toast.makeText(this@SignUP, "Network faillure", Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(this@SignUP, "conversion error", Toast.LENGTH_SHORT).show()

                    }

                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                        if (response.isSuccessful) {
                            if (response.code() == 201)
                                uniSignIn(service)

                        } else if (response.code() == 404)
                            Toast.makeText(
                                this@SignUP,
                                "Email already exists or missing field",
                                Toast.LENGTH_SHORT
                            ).show()

                    }

                })
        } else {
            service.signUP(
                fName,
                lName,
                birthday,
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
                        Toast.makeText(this@SignUP, "Network faillure", Toast.LENGTH_SHORT).show()


                }

                override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                    if (response.isSuccessful) {
                        if (response.code() == 201)
                            uniSignIn(service)

                    } else if (response.code() == 404)
                        Toast.makeText(
                            this@SignUP,
                            "Email already exists or missing field",
                            Toast.LENGTH_SHORT
                        ).show()

                }


            })
        }
    }

    private fun uniSignIn(service: UniclubApi) {
        service.signIN(mail, password).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                if (t is IOException)
                    Log.d("lol", "lol")
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    PrefsManager.seToken(this@SignUP, response.body()!!.accessToken)
                    startActivity(Intent(this@SignUP, Home::class.java))
                }
            }

        })
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String? = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath

        }
    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.post_sample.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun selectImage() {

        val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Image")

        builder.setItems(items) { dialogInterface, i ->
            when {
                items[i] == "Camera" -> takePicture()
                items[i] == "Gallery" -> {

                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intent.type = "image/*"
                    startActivityForResult(intent, SELECT_FILE)

                }
                items[i] == "Cancel" -> dialogInterface.dismiss()
            }
        }
        builder.show()

    }

    private fun formFill() {


        edUsername.afterTextChanged {
            edUsername.error = if (it.isValidName()) null
            else "Invalid name"
        }
        edEmail.afterTextChanged {
            edEmail.error = if (it.isValidEmail()) null
            else "Invalid email"
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
        edBirth.addTextChangedListener(validator)
        edPassword.addTextChangedListener(validator)
        edUsername.addTextChangedListener(validator)
        edPasswordC.addTextChangedListener(validator)


    }

    private fun checkPermis(): Boolean {
        val listPermis = ArrayList<String>()

        for (i in appPermissions) {
            if (ContextCompat.checkSelfPermission(this@SignUP, i) != PackageManager.PERMISSION_GRANTED) {
                listPermis.add(i)

            }
        }

        if (listPermis.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this@SignUP,
                listPermis.toArray(arrayOfNulls(listPermis.size)),
                PERMIS_REQUEST
            )
            return false
        }

        return true
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
                       fName=fn
                        lName=ln
                        mail=email
                        password="123facebook"
                        passwordC="123facebook"

                        uniSignUP(fn,ln,"null",email,"123facebook","123facebook","unknown",null)
                        //TODO("response caching")
                        PrefsManager.setPicture(this@SignUP, pic)


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
        fName=account.familyName.toString()
        lName=account.displayName.toString()
        mail=account.email.toString()
        password="123facebook"
        passwordC="123facebook"
        uniSignUP(fName,lName,birthday,mail,passwordC,passwordC,adress,image)
        //TODO("response caching")
        PrefsManager.setPicture(this@SignUP, account.photoUrl.toString())
    }


}
