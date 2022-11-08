package com.ads.one.profilelistactivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.room.Room
import com.ads.one.profilelistactivity.databinding.ActivityAddEditProfileBinding
import com.ads.one.profilelistactivity.localDatabase.User
import com.ads.one.profilelistactivity.localDatabase.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class AddEditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditProfileBinding
    private lateinit var imgUri: Uri
    private var request: String = ""
    private var imageUrl: String = ""
    private var first: String = ""
    private var last: String = ""
    private var id: String = ""
    private var imgUpload: Boolean = false
    private var imgRemoved: Boolean = false
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            imgUpload = true
            binding.profileImage.setImageURI(imgUri)
            binding.profileImage.setImageURI(imgUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //view binding
        binding = ActivityAddEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //request checks that the user wants to add a profile or edit a profile
        request = intent.getStringExtra("request").toString()
        val currentTime = System.currentTimeMillis().toString()
        imgUri = createImageUri(currentTime)!!
        if (request == "Edit") {
            imageUrl = intent.getStringExtra("image").toString()
            first = intent.getStringExtra("firstName").toString()
            last = intent.getStringExtra("lastName").toString()
            id = intent.getStringExtra("id").toString()
            if (imageUrl == "") {
                binding.profileImage.setImageResource(R.drawable.add_photo)
            } else {
                binding.profileImage.setImageURI(imageUrl.toUri())
                binding.removeProfileImage.visibility = View.VISIBLE
            }
            binding.etFirstName.setText(first)
            binding.etLastName.setText(last)
            binding.btnAddProfile.text = "Edit Profile"
            binding.removeProfileImage.setOnClickListener {
                binding.profileImage.setImageResource(R.drawable.add_photo)
                imgRemoved = true
            }
            binding.btnAddProfile.setOnClickListener {
                if (binding.etFirstName.text!!.trim().toString().isEmpty()) {
                    binding.etFirstName.error = "Please Enter your first name"
                    binding.etFirstName.requestFocus()
                } else if (binding.etLastName.text!!.trim().toString().isEmpty()) {
                    binding.etLastName.error = "Please Enter your last name"
                    binding.etLastName.requestFocus()
                } else {
                    //checks if profile image is removed or not
                    if (imgRemoved) {
                        editYourProfile(
                            id.toInt(),
                            binding.etFirstName.text.toString(),
                            binding.etLastName.text.toString(),
                            ""
                        )
                    } else {
                        //checks whether a new image is uploaded or not
                        if (imgUpload) {
                            editYourProfile(
                                id.toInt(),
                                binding.etFirstName.text.toString(),
                                binding.etLastName.text.toString(),
                                imgUri.toString()
                            )
                        } else {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        } else if (request == "Add") {
            binding.removeProfileImage.visibility = View.GONE
            binding.btnAddProfile.setOnClickListener {
                if (binding.etFirstName.text!!.trim().toString().isEmpty()) {
                    binding.etFirstName.error = "Please Enter your first name"
                    binding.etFirstName.requestFocus()
                } else if (binding.etLastName.text!!.trim().toString().isEmpty()) {
                    binding.etLastName.error = "Please Enter your last name"
                    binding.etLastName.requestFocus()
                } else if (!imgUpload) {
                    Toast.makeText(this, "Please upload profile picture", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    addYourProfile()
                }
            }
        }
        binding.profileImage.setOnClickListener {
            contract.launch(imgUri)
        }
    }

    private fun editYourProfile(id: Int, fName: String, lName: String, image: String) {
        //launching a coroutine in background to edit the profile
        GlobalScope.launch(Dispatchers.IO) {
            val db: UserDatabase =
                Room.databaseBuilder(applicationContext, UserDatabase::class.java, "user_db")
                    .build()
            val userDao = db.userDao()
            userDao.updateProfile(id, fName, lName, image)
        }.invokeOnCompletion {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun addYourProfile() {
        //launching a coroutine in background to add the profile
        GlobalScope.launch {
            val db: UserDatabase =
                Room.databaseBuilder(applicationContext, UserDatabase::class.java, "user_db")
                    .build()
            val userDao = db.userDao()
            val fName = binding.etFirstName.text.toString()
            val lName = binding.etLastName.text.toString()
            userDao.insertProfile(User(null, fName, lName, imgUri.toString()))
        }.invokeOnCompletion {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    //create image in the private storage and provides its uri
    private fun createImageUri(currentTime: String): Uri? {
        val img = File(applicationContext.filesDir, "$currentTime.png")
        return FileProvider.getUriForFile(
            applicationContext,
            "com.ads.one.profilelistactivity.fileProvider",
            img
        )
    }
}