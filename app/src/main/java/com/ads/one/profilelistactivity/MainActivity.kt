package com.ads.one.profilelistactivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.ads.one.profilelistactivity.Adapters.UserAdapter
import com.ads.one.profilelistactivity.databinding.ActivityMainBinding
import com.ads.one.profilelistactivity.localDatabase.UserDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addProfile.setOnClickListener {
            val intent = Intent(this, AddEditProfileActivity::class.java)
            intent.putExtra("request", "Add")
            startActivity(intent)
        }
        getUserData()
    }

    private fun getUserData() {
        val db: UserDatabase =
            Room.databaseBuilder(applicationContext, UserDatabase::class.java, "user_db")
                .allowMainThreadQueries().build()
        val userDao = db.userDao()
        binding.profileRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.profileRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        val profiles = userDao.getAllProfile()
        val adapter = UserAdapter(this, profiles)
        binding.profileRecyclerView.adapter = adapter
    }
}