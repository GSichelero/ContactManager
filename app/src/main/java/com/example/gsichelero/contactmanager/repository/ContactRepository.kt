package com.example.gsichelero.contactmanager.repository

import com.example.gsichelero.contactmanager.data.db.entity.ContactEntity

interface ContactRepository {
    suspend fun insertContact(name: String, phone: String, cel_type: String): Long

    suspend fun updateContact(id: Long, name: String, phone: String, cel_type: String)

    suspend fun deleteContact(id: Long)

    suspend fun deleteAllContacts()

    suspend fun getAllContacts(): List<ContactEntity>
}