package com.example.gsichelero.contactmanager.data.db.dao

import androidx.room.*
import com.example.gsichelero.contactmanager.data.db.entity.ContactEntity

@Dao
interface ContactDAO {

    @Insert
    suspend fun insert(contact: ContactEntity): Long

    @Update
    suspend fun update(contact: ContactEntity)

    @Query("DELETE FROM contact WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM contact")
    suspend fun deleteAll()

    @Query("SELECT * FROM contact ORDER BY name")
    suspend fun getAll(): List<ContactEntity>
}