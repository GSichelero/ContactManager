package com.example.gsichelero.contactmanager.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gsichelero.contactmanager.data.db.dao.ContactDAO
import com.example.gsichelero.contactmanager.data.db.entity.ContactEntity

@Database(entities = [ContactEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val contactDAO: ContactDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance: AppDatabase? = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "app_database"
                    ).build()
                }

                return instance
            }
        }
    }
}