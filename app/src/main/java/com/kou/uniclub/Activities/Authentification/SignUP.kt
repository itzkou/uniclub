package com.kou.uniclub.Activities.Authentification

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginResult
import com.kou.uniclub.Extensions.Validation
import com.kou.uniclub.Activities.Home
import com.kou.uniclub.Model.Token
import com.kou.uniclub.Model.User
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
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

class SignUP : AppCompatActivity(),Validation {
//TODO("when the image is empty the app crashes")
//TODO("prevention contre l'erreur lors de signup form validation (+) >> if email already exists!!")
     private var cities = arrayOf("","Ariana", "Tunis", "Bizerte")
    private var city:String?=null

    //REQUESTS FOR RESULTS
    private val SELECT_FILE=1
    private val REQUEST_IMAGE_CAPTURE = 2
    private  val PERMIS_REQUEST=1997
    //IMAGE UPLOAD
    lateinit var chosenFile: File
    lateinit var chosenUri: Uri
    lateinit var  body: MultipartBody.Part
    private var mCurrentPhotoPath: String=""
    //Permissions array
    private val appPermissions= arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE)
    //facebook callback manager
    private lateinit var callbackManager: CallbackManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //redirect
        back.setOnClickListener {
            //startActivity(Intent(this@SignUP,Auth::class.java))
        }
        //photos
        icon_photo.setOnClickListener {
            if (checkPermis())
                selectImage()
        }

        //form Validation
        formValidation()

        //with facebook
        callbackManager = CallbackManager.Factory.create()
        btn_fb.setOnClickListener {
            facebook()
        }



    }

    override fun onStart() {
        super.onStart()
        formFill()
        btn_signup.setOnClickListener {
signUP()        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {


            icon_photo.setImageURI(Uri.parse(mCurrentPhotoPath))
            chosenFile=File(mCurrentPhotoPath)
            //multipart stuff
            val requestFile = RequestBody.create(MediaType.parse("image/*"),chosenFile)
            body = MultipartBody.Part.createFormData("image", chosenFile.name, requestFile)


        } else if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            icon_photo.setImageURI(data!!.data)
            chosenUri=data.data!!
            val filePath = arrayOf(MediaStore.Images.Media.DATA)
            val c = contentResolver.query(chosenUri, filePath, null, null, null)
            c!!.moveToFirst()
            val columnIndex = c.getColumnIndex(filePath[0])
            val filePathStr = c.getString(columnIndex)
            c.close()
            chosenFile=File(filePathStr!!)
            val requestFile = RequestBody.create(MediaType.parse("image/*"),chosenFile)
            body = MultipartBody.Part.createFormData("image", chosenFile.name, requestFile)

        }
        //facebook
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== PERMIS_REQUEST)
        { val permisResults=HashMap<String,Int>()
            var deniedCount=0


            // gather granted results
            for(i in 0 until grantResults.size)
            {
                if (grantResults[i]== PackageManager.PERMISSION_DENIED)
                {
                    permisResults[permissions[i]] = grantResults[i]
                    deniedCount++
                }


            }
            if(deniedCount==0) {
                Toast.makeText(this, "All permissions are granted", Toast.LENGTH_SHORT).show()
                selectImage()

                //TODO("Unlock Sign UP button")
            }
            else
                Toast.makeText(this, "All permissions are required", Toast.LENGTH_SHORT).show()







        }


    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String? =  SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
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

    fun TakePicture(){
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
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
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

    fun signUP(){
        //retrofit
            val service=UniclubApi.create()
            val u=User(ed_email.text.toString(),ed_password.text.toString(),ed_username.text.toString(),1,1,"")
            service.signUP(u.name,u.email,u.password,1,1,body).enqueue(object:Callback<Token>{
                override fun onFailure(call: Call<Token>, t: Throwable) {
                    if(t  is IOException)
                        Toast.makeText(this@SignUP,"Network faillure SignUP",Toast.LENGTH_SHORT).show()
                    else   Toast.makeText(this@SignUP,"Conversion error",Toast.LENGTH_SHORT).show()

                }

                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    if (response.isSuccessful){
                        Toast.makeText(this@SignUP,"Account Created",Toast.LENGTH_SHORT).show()
                        PrefsManager.seToken(this@SignUP,response.body()!!.token)
                        Log.d("stoken",PrefsManager.geToken(this@SignUP))

                        startActivity(Intent(this@SignUP, Home::class.java))
                        finish()

                    }                }

            })

        }

    fun formFill(){
        ed_username.afterTextChanged{
            ed_username.error=if( it.isValidName() )null
            else "invalid username"


        }
        ed_email.afterTextChanged {
            ed_email.error= if (it.isValidEmail()) null
            else "invalid email"
        }

        ed_mobile.afterTextChanged {
            ed_mobile.error=if(it.isValidPhone()) null
            else "enter an 8-digit phone number"
        }

        ed_password.afterTextChanged {
            ed_password.error=if(it.isValidPassword()) null
            else "enter at least 6 digits that include a lower case an uppercase and a special character"

        }
        //spinner values
        sp_region.adapter=ArrayAdapter(this@SignUP,android.R.layout.simple_spinner_dropdown_item,cities)
        sp_region.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                city=cities[position]
            }

        }
    }

    fun formValidation(){
        btn_signup.isEnabled=false
        val formValidation= object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val u = ed_username.text.toString().isValidName()
                val e = ed_email.text.toString().isValidEmail()
                val p = ed_password.text.toString().isValidPassword()
                val m = ed_mobile.text.toString().isValidPhone()

                btn_signup.isEnabled = u && e && p && m
            }

        }
        ed_username.addTextChangedListener(formValidation)
        ed_mobile.addTextChangedListener(formValidation)
        ed_password.addTextChangedListener(formValidation)
        ed_username.addTextChangedListener(formValidation)
    }


    private  fun checkPermis():Boolean{
        val listPermis= ArrayList<String>()

        for (i in appPermissions){
            if (ContextCompat.checkSelfPermission(this@SignUP,i)!= PackageManager.PERMISSION_GRANTED){
                listPermis.add(i)

            }
        }

        if (listPermis.isNotEmpty())
        {
            ActivityCompat.requestPermissions(this@SignUP,listPermis.toArray(arrayOfNulls(listPermis.size)), PERMIS_REQUEST)
            return false
        }

        return true
    }
    //Facebook
    private fun facebook(){
        btn_fb.setReadPermissions(Arrays.asList("email", "public_profile"))
        btn_fb.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    val request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken()
                    ) { me, _ ->
                        val email = me.get("email").toString() //?: ""
                        val fn =me.get("first_name").toString()
                        val ln =me.get("last_name").toString()
                        val pic = me.getJSONObject("picture").getJSONObject("data").get("url").toString()
                        Log.d("mfacebook","$email $fn $ln $pic")

                        //TODO("caching response >Saving to the remote server")




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

}
