package com.example.gsichelero.contactmanager.repository

import com.example.gsichelero.contactmanager.data.db.dao.ContactDAO
import com.example.gsichelero.contactmanager.data.db.entity.ContactEntity

class DatabaseDataSource(private val contactDAO: ContactDAO) : ContactRepository {

    override suspend fun insertContact(name: String, phone: String, cel_type: String): Long {
        val contact = ContactEntity(
            name = name,
            phone = phone,
            cel_type = cel_type
        )

        return contactDAO.insert(contact)
    }

    override suspend fun updateContact(id: Long, name: String, phone: String, cel_type: String) {
        val contact = ContactEntity(
            id = id,
            name = name,
            phone = phone,
            cel_type = cel_type
        )

        contactDAO.update(contact)
    }

    override suspend fun deleteContact(id: Long) {
        contactDAO.delete(id)
    }

    override suspend fun deleteAllContacts() {
        contactDAO.deleteAll()
    }

    override suspend fun getAllContacts(): List<ContactEntity> {
        return contactDAO.getAll()
    }
}