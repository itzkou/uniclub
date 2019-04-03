package com.kou.uniclub.Authentication

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.kou.uniclub.Extensions.Validation
import com.kou.uniclub.Home
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
import java.net.ConnectException
import java.text.SimpleDateFormat
import java.util.*

class SignUP : AppCompatActivity(),Validation {

//TODO("prevention contre l'erreur lors de signup form validation (+) >> if email already exists!!")
     private var cities = arrayOf("","Ariana", "Tunis", "Bizerte")
    private var city:String?=null

    //REQUESTS FOR RESULTS
    private val SELECT_FILE=1
    private val REQUEST_IMAGE_CAPTURE = 2
    //IMAGE UPLOAD
    lateinit var chosenFile: File
    lateinit var chosenUri: Uri
    lateinit var  body: MultipartBody.Part
    private var mCurrentPhotoPath: String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //redirect
        back.setOnClickListener {
            startActivity(Intent(this@SignUP,Auth::class.java))
        }

        //photos
        icon_photo.setOnClickListener {
            selectImage()
        }
    }

    override fun onStart() {
        super.onStart()

        //retrofit
        btn_signup.setOnClickListener {
            val service=UniclubApi.create()
            val u=User(ed_email.text.toString(),ed_password.text.toString(),ed_username.text.toString(),1,1,"")
            service.signUP(u.name,u.email,u.password,1,1,body).enqueue(object:Callback<Token>{
                override fun onFailure(call: Call<Token>, t: Throwable) {
                        if(t  is IOException)
                            Toast.makeText(this@SignUP,"Network faillure ALLunivs",Toast.LENGTH_SHORT).show()
                    else   Toast.makeText(this@SignUP,"Conversion error",Toast.LENGTH_SHORT).show()

                                  }

                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    if (response.isSuccessful){
                        Toast.makeText(this@SignUP,"Account Created",Toast.LENGTH_SHORT).show()
                        PrefsManager.seToken(this@SignUP,response.body()!!.token)
                        Log.d("myToken",PrefsManager.geToken(this@SignUP))

                        startActivity(Intent(this@SignUP,Home::class.java))
                        finish()

                    }                }

            })

        }



        ed_username.afterTextChanged() {
            ed_username.error =if( it.isValidName() && it.length>=4) null
               else  "username is a 4 digit long"



        }
        ed_email.afterTextChanged {
            ed_email.error=if (it.length >= 6 &&it.isValidEmail())  null
            else "invalid email "
        }

        ed_mobile.afterTextChanged {
            ed_mobile.error=if(it.length==8&&it.isValidPhone()) null
            else "enter an 8-digit phone number"
        }

        ed_password.afterTextChanged {
            ed_password.error=if(it.length>=6&&it.isValidPassword()) null
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

    //Alert dialog for Pictures
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
}
