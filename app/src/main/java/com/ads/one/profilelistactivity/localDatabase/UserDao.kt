package com.ads.one.profilelistactivity.localDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

//Queries written to fetch,update and insert data in the table
@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllProfile(): List<User>

    @Insert
    fun insertProfile(user: User)

    @Query("UPDATE user SET first_name=:firstName,last_name=:lastName, img_url=:imgUrl WHERE id LIKE :id")
    fun updateProfile(id: Int, firstName: String, lastName: String, imgUrl: String):Void
}