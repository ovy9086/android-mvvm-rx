package org.olu.mvvm.repository.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import org.olu.mvvm.repository.data.User


@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}