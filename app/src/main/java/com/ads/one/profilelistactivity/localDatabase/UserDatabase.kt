package com.ads.one.profilelistactivity.localDatabase

import androidx.room.Database
import androidx.room.RoomDatabase

//creation of profile database
@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}