package com.example.palliativecare.view.activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.palliativecare.R
import com.example.palliativecare.databinding.ActivityAddTopicBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*

class AddTopicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTopicBinding
    private lateinit var storage: FirebaseStorage
    val reqCode: Int = 100
    private lateinit var imagePath: Uri
    private lateinit var progressDialog: ProgressDialog
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_topic)
        binding = ActivityAddTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = Firebase.storage
        val storageRef = storage.reference

        binding.addTopicImgLogo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, reqCode)
        }

        binding.addTopicBtnAddTopic.setOnClickListener {
            progressDialog = ProgressDialog(this)
            progressDialog.setCancelable(false)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.setTitle("جار رفع الملف")
            progressDialog.show()

            storageRef.child("Images/" + UUID.randomUUID().toString())
                .putFile(imagePath)
                .addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener {
                        addTopicToFirestore(it.toString())
                        Log.d("Success Download URL SSSS", it.toString())
                    }
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Upload Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext,DoctorBottomNavigationBarActivity::class.java)
                    finish()
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Please Upload Image", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == reqCode) {
            imagePath = data!!.data!!
            val bitMap = MediaStore.Images.Media.getBitmap(contentResolver, imagePath)
            binding.addTopicImgLogo.setImageBitmap(bitMap)
        }
    }

    private fun addTopicToFirestore(url: String) {
        val topic = hashMapOf(
            "imageURL" to url,
            "title" to binding.addTopicTitle.text.toString(),
            "description" to binding.addTopicDescription.text.toString(),
            "videoURL" to binding.addTopicEtVideoURL.text.toString(),
            "hidden" to false
        )

        db.collection("topics")
            .add(topic)
            .addOnSuccessListener {
                Toast.makeText(applicationContext,"DocumentSnapshot added with ID: ${it.id}",Toast.LENGTH_SHORT).show()
                Log.d("Add topic SSSS","DocumentSnapshot added with ID: ${it.id}")
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Error adding document: $it",Toast.LENGTH_SHORT).show()
                Log.d("Add topic FFFF","Error adding document: $it")
            }

    }
}