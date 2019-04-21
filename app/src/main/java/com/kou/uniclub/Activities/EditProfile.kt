package com.kou.uniclub.Activities

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_edit_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditProfile : AppCompatActivity() {
    //REQUESTS FOR RESULTS
    private val SELECT_FILE=1
    private val REQUEST_IMAGE_CAPTURE = 2
    private  val PERMIS_REQUEST=2001
    //IMAGE UPLOAD
    lateinit var chosenFile: File
    lateinit var chosenUri: Uri
    lateinit var  body: MultipartBody.Part
    private var mCurrentPhotoPath: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        edEmail.hint = "test"
        changePhoto.setOnClickListener {
            selectImage()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {


            imProfile.setImageURI(Uri.parse(mCurrentPhotoPath))
            chosenFile=File(mCurrentPhotoPath)
            //multipart stuff
            val requestFile = RequestBody.create(MediaType.parse("image/*"),chosenFile)
            body = MultipartBody.Part.createFormData("image", chosenFile.name, requestFile)


        } else if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
           imProfile.setImageURI(data!!.data)
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

    fun selectImage() {

        val items = arrayOf<CharSequence>("Camera", "Gallery","Import from Google","Import from facebook", "Delete profile picture")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Set a profile picture")

        builder.setItems(items) { dialogInterface, i ->
            when {
                items[i] == "Camera" -> TakePicture()
                items[i] == "Gallery" -> {

                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intent.type = "image/*"
                    startActivityForResult(intent, SELECT_FILE)

                }
                //TODO("develop these and use prefs manager")
                items[i] == "Import from Google" -> null
                items[i] == "Import from facebook" -> null
                items[i] == "Delete profile picture" -> null


            }
        }
        builder.show()

    }

}
