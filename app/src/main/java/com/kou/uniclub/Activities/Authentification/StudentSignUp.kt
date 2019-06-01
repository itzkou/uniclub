package com.kou.uniclub.Activities.Authentification


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.widget.DatePicker
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
import kotlinx.android.synthetic.main.activity_stud_signup.*
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

class StudentSignUp : AppCompatActivity(), Validation {

    //TODO(" when email exists ")


    /******* REQUESTS FOR RESULTS*******/
    private val SELECT_FILE = 1
    private val IMAGE_CAPTURE = 2
    private val PERMIS_REQUEST = 3

    /******* SOCIAL AUTH *******/
    private val GOOGLE_SIGN = 4

    /******* IMAGE UPLOAD*******/
    private var chosenFile: File? = null
    private var chosenUri: Uri? = null
    private var image = MultipartBody.Part.createFormData(
        "attachment",
        "",
        RequestBody.create(MediaType.parse("text/plain"), "")
    )
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
        setContentView(com.kou.uniclub.R.layout.activity_stud_signup)
        //TODO("instead of adress change it to university")
        adress = "deprectaed"
        btnFb.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)


        /******************* camera  *****************/
        imSettings.setOnClickListener {
            if (checkPermis())
                selectImage()
        }


        val dialogView = LayoutInflater.from(this@StudentSignUp).inflate(R.layout.builder_time_picker, null)
        val builder = AlertDialog.Builder(this@StudentSignUp)
        val timePicker = dialogView.findViewById<DatePicker>(R.id.timePicker)
        builder.setView(dialogView)
        builder.setPositiveButton("confirm") { dialog, which ->

            //birthday =timePicker.getDate()
            val format = SimpleDateFormat(
                "EE MMM dd HH:mm:ss z yyyy",
                Locale.ENGLISH
            )
            val date = format.parse(timePicker.getDate().toString())
            val day = DateFormat.format("dd", date) as String
            val month = DateFormat.format("MMM", date) as String
            val monthy = DateFormat.format("MM", date) as String
            val year = DateFormat.format("yyyy", date) as String
            birthday = "$year-$monthy-$day"


            edBirth.hint = "$day  $month  $year"
            edBirth.setHintTextColor(ContextCompat.getColor(this@StudentSignUp, R.color.black))
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


        /******************* with facebook *****************/
        callbackManager = CallbackManager.Factory.create()
        btnFb.setOnClickListener {
            facebook()
        }
        /******************* with google *****************/
        GooUi()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE && resultCode == RESULT_OK) {

            imSettings.setImageURI(Uri.parse(mCurrentPhotoPath))
            chosenFile = File(mCurrentPhotoPath)
            //multipart stuff
            val requestFile = RequestBody.create(MediaType.parse("image/*"), chosenFile)
            image = MultipartBody.Part.createFormData("image", chosenFile!!.name, requestFile)


        } else if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {

            imSettings.setImageURI(data!!.data)
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
            birthday,
            mail,
            password,
            password,
            adress,
            image
        ).enqueue(object : Callback<SignUpResponse> {
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                if (t is IOException)
                    Toast.makeText(this@StudentSignUp, "Network faillure", Toast.LENGTH_SHORT).show()


            }

            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    if (response.code() == 201)
                        uniSignIn(service)

                } else if (response.code() == 404)
                    Toast.makeText(
                        this@StudentSignUp,
                        "Email already exists or missing field",
                        Toast.LENGTH_SHORT
                    ).show()

            }


        })

    }

    private fun uniSignIn(service: UniclubApi) {
        service.signIN(mail, password).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                if (t is IOException)
                    Log.d("lol", "lol")
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    PrefsManager.seToken(this@StudentSignUp, response.body()!!.accessToken)
                    startActivity(Intent(this@StudentSignUp, Home::class.java))
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
        /******* Form Validation *******/
        btnSignup.isEnabled = false

        var okUsName = false
        var okEmail = false
        var okPass = false
        var okPassc = false



        edUsername.afterTextChanged {
            if (it.isValidName()) {

                ilUsername.error = null
                ilUsername.isErrorEnabled = false
                okUsName = true
                fName = it.substring(0, it.indexOf(" ") + 1)
                lName = it.substring(it.lastIndexOf(" ") + 1, it.length)

            } else {
                ilUsername.isErrorEnabled = true
                ilUsername.error = "Enter a valid name and surname"
                okUsName = false
            }
            btnSignup.isEnabled = okUsName && okEmail && okPass && okPassc
        }
        edEmail.afterTextChanged {
            if (it.isValidEmail()) {
                ilEmail.error = null
                ilEmail.isErrorEnabled = false
                okEmail = true
                mail = it

            } else {
                ilEmail.isErrorEnabled = true
                ilEmail.error = "Enter a valid email"
                okEmail = false

            }
            btnSignup.isEnabled = okUsName && okEmail && okPass && okPassc
        }
        edPassword.afterTextChanged {
            if (it.isValidPassword()) {

                ilPass.error = null
                ilPass.isErrorEnabled = false
                okPass = true
                password = it
            } else {
                ilPass.isErrorEnabled = true
                ilPass.error = "Password is a 6-digit long and must include numbers"
                okPass = false
            }
            btnSignup.isEnabled = okUsName && okEmail && okPass && okPassc
        }
        edPasswordC.afterTextChanged {
            if (it == edPassword.text.toString()) {
                ilPassC.error = null
                ilPassC.isErrorEnabled = false
                okPassc = true
                passwordC = it
            } else {
                ilPassC.isErrorEnabled = true
                ilPassC.error = "Passwords don't match"
                okPassc = false
            }
            btnSignup.isEnabled = okUsName && okEmail && okPass && okPassc
        }



        btnSignup.setOnClickListener {

            uniSignUP(fName, lName, birthday, mail, password, passwordC, adress, image)

        }


    }

    private fun checkPermis(): Boolean {
        val listPermis = ArrayList<String>()

        for (i in appPermissions) {
            if (ContextCompat.checkSelfPermission(this@StudentSignUp, i) != PackageManager.PERMISSION_GRANTED) {
                listPermis.add(i)

            }
        }

        if (listPermis.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this@StudentSignUp,
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
                        fName = fn
                        lName = ln
                        mail = email
                        password = "123social"
                        passwordC = "123social"

                        uniSignUP(fn, ln, "null", email, "123facebook", "123facebook", "unknown", image)
                        //TODO("response caching")
                        PrefsManager.setPicture(this@StudentSignUp, pic)


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
        uniSignUP(fName, lName, birthday, mail, passwordC, passwordC, adress, image)
        //TODO("response caching")
        PrefsManager.setPicture(this@StudentSignUp, account.photoUrl.toString())
    }

    fun DatePicker.getDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return calendar.time
    }


}
