package com.kou.uniclub.Activities.Authentification


import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kou.uniclub.Extensions.Validation
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.MultipartBody
import java.io.File
import java.util.*
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.kou.uniclub.Model.Auth.SignUpResponse
import com.kou.uniclub.Model.User.User
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.IOException
import java.text.SimpleDateFormat


class SignUP : AppCompatActivity(), Validation {

    //TODO("when the image is empty the app crashes")
//TODO("prevention contre l'erreur lors de signup form validation (+) >> if email already exists!!")
    private var cities = arrayOf("Ariana", "Tunis", "Bizerte")
    private var genders = arrayOf("Male", "Female")

    private var city: String? = null
    private var gender: String? = null

    /******* REQUESTS FOR RESULTS*******/
    private val SELECT_FILE = 1
    private val IMAGE_CAPTURE = 2
    private val PERMIS_REQUEST = 3

    /******* SOCIAL AUTH *******/
    private val GOOGLE_SIGN = 4

    /******* IMAGE UPLOAD*******/
    lateinit var chosenFile: File
    lateinit var chosenUri: Uri
    lateinit var body: MultipartBody.Part
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

    private var mBirthday:String?=null
    private var FName:String?=null
    private var LName:String?=null
    private var Gender:String?=null
    private var Email:String?=null
    private var password:String?=null
    private var Adress:String?=null
    private var passwordC:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.kou.uniclub.R.layout.activity_sign_up)



        /******************* camera  *****************/
        imProfile.setOnClickListener {
            if (checkPermis())
                selectImage()
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

        val dialogView = LayoutInflater.from(this@SignUP).inflate(R.layout.time_picker, null)
        val builder= AlertDialog.Builder(this@SignUP)
        val timePicker=dialogView.findViewById<MaterialCalendarView>(R.id.timePicker)
        builder.setView(dialogView)
        builder.setPositiveButton("confirm") { dialog, which ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            mBirthday=dateFormat.format(timePicker.selectedDate.date)
            edBirth.hint=mBirthday
            edBirth.setHintTextColor(ContextCompat.getColor(this@SignUP,R.color.black))
            dialog?.dismiss()
        }

        builder.setNegativeButton("Cancel"
        ) { dialog, which -> dialog?.dismiss()
        }
        val dialog=builder.create()

        edBirth.setOnClickListener {
            dialog.show()

        }

        /******************* Spinner Values *****************/
        spRegion.adapter = ArrayAdapter(this@SignUP, android.R.layout.simple_spinner_dropdown_item, cities)
        spRegion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                city = cities[position]
            }

        }

        spGender.adapter = ArrayAdapter(this@SignUP, android.R.layout.simple_spinner_dropdown_item, genders)
        spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                gender=genders[position]            }

        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE && resultCode == RESULT_OK) {


            imProfile.setImageURI(Uri.parse(mCurrentPhotoPath))
            chosenFile = File(mCurrentPhotoPath)
            //multipart stuff
            val requestFile = RequestBody.create(MediaType.parse("image/*"), chosenFile)
            body = MultipartBody.Part.createFormData("image", chosenFile.name, requestFile)


        } else if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            imProfile.setImageURI(data!!.data)
            chosenUri = data.data!!
            val filePath = arrayOf(MediaStore.Images.Media.DATA)
            val c = contentResolver.query(chosenUri, filePath, null, null, null)
            c!!.moveToFirst()
            val columnIndex = c.getColumnIndex(filePath[0])
            val filePathStr = c.getString(columnIndex)
            c.close()
            chosenFile = File(filePathStr!!)
            val requestFile = RequestBody.create(MediaType.parse("image/*"), chosenFile)
            body = MultipartBody.Part.createFormData("image", chosenFile.name, requestFile)

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

                //TODO("Unlock Sign UP button")
            } else
                Toast.makeText(this, "All permissions are required", Toast.LENGTH_SHORT).show()


        }


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

    fun TakePicture() {
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
                    startActivityForResult(takePictureIntent,IMAGE_CAPTURE)
                }
            }
        }
    }

    fun selectImage() {

        val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Image")

        builder.setItems(items) { dialogInterface, i ->
            when {
                items[i] == "Camera" -> TakePicture()
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

private fun signUP() {
    val service = UniclubApi.create()

    /*val user = User(
        sp_region.selectedItem.toString(),
        ed_birth.text.toString(),
        ed_email.text.toString(),
        fn,
        ed_gender.text.toString(),
        ln,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

        service.signUP(user.firstName,user.lastName,date,user.email,ed_password.text.toString(),ed_passConfirm.text.toString(),sp_region.selectedItem.toString(),body).enqueue(object:
            Callback<SignUpResponse>{
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                if(t is IOException)
                    Toast.makeText(this@SignUpResponse,t.message,Toast.LENGTH_SHORT)  .show()


            }

            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
            if(response.isSuccessful)
                Toast.makeText(this@SignUpResponse,response.body()!!.message,Toast.LENGTH_SHORT)  .show()
            }

        })*/

}

    fun formFill() {
        edUsername.afterTextChanged {
            if(!it.isValidName())
                edUsername.error="invalid username"
            else{null }



        }
        edEmail.afterTextChanged {
            edEmail.error = if (it.isValidEmail()) null
            else "invalid email"
        }
        edPassword.afterTextChanged {
            edPassword.error = if (it.isValidPassword()) null
            else "enter at least 6 digits that include a lower case an uppercase and a special character"

        }

    }

    fun formValidation() {
        btnSignup.isEnabled = false
        val formValidation = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val u = edUsername.text.toString().isValidName()
                val e = edEmail.text.toString().isValidEmail()
                val p = edPassword.text.toString().isValidPassword()

                val username = edUsername.text.toString()
                val fn = username.substring(0, username.lastIndexOf(" "))
                val ln = username.substring(username.lastIndexOf(" "),username.length)
                Log.d("nameO",fn +"*" +ln)

                btnSignup.isEnabled = u && e && p
            }

        }
        edUsername.addTextChangedListener(formValidation)
        edBirth.addTextChangedListener(formValidation)
        edPassword.addTextChangedListener(formValidation)
        edUsername.addTextChangedListener(formValidation)
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

    //Facebook
    private fun facebook() {
        btnFb.setReadPermissions(Arrays.asList("email", "public_profile"))
        btnFb.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    val request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken()
                    ) { me, _ ->
                        val email = me.get("email").toString() //?: ""
                        val fn = me.get("first_name").toString()
                        val ln = me.get("last_name").toString()
                        val pic = me.getJSONObject("picture").getJSONObject("data").get("url").toString()
                        Log.d("mfacebook", "$email $fn $ln $pic")

                        //TODO("caching UserResponseO >Saving to the remote server")


                    }

                    val parameters = Bundle()
                    parameters.putString("fields", "id,first_name,last_name,picture.type(large), email")
                    request.parameters = parameters
                    request.executeAsync()


                } else Log.d("mtoken", "is null")


            }

            override fun onCancel() {
                Log.e("FBLOGIN_FAILD", "Cancel")
            }

            override fun onError(error: FacebookException) {
                Log.e("FBLOGIN_FAILD", "ERROR", error)
            }
        })


    }

    //Google
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
        //TODO("caching UserResponseO >Saving to the remote server")
        Log.d("mGoogle", account.displayName)


    }





}
