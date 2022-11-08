package com.ads.one.profilelistactivity.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.ads.one.profilelistactivity.AddEditProfileActivity
import com.ads.one.profilelistactivity.R
import com.ads.one.profilelistactivity.localDatabase.User

class UserAdapter(private val context: Context, private val userList: List<User>) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profilePhoto)
        val fullName: TextView = itemView.findViewById(R.id.full_name)
        val editButton: Button = itemView.findViewById(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_profile, parent, false);
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (userList[position].imgUrl == "") {
            holder.profileImage.setImageResource(R.drawable.profile_icon)
        } else {
            holder.profileImage.setImageURI(userList[position].imgUrl.toString().toUri())
        }

        holder.fullName.text = userList[position].firstName + " " + userList[position].lastName
        holder.editButton.setOnClickListener {
            val intent = Intent(context, AddEditProfileActivity::class.java)
            //Sending these data using intent to the AddEditProfileActivity
            intent.putExtra("id", userList[position].id.toString())
            intent.putExtra("firstName", userList[position].firstName.toString())
            intent.putExtra("lastName", userList[position].lastName.toString())
            intent.putExtra("image", userList[position].imgUrl.toString())
            intent.putExtra("request", "Edit")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}