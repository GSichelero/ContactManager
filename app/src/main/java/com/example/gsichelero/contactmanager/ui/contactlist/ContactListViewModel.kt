package com.example.gsichelero.contactmanager.ui.contactlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gsichelero.contactmanager.data.db.entity.ContactEntity
import com.example.gsichelero.contactmanager.repository.ContactRepository
import kotlinx.coroutines.launch

class ContactListViewModel(
    private val repository: ContactRepository
) : ViewModel() {

    private val _allContactsEvent = MutableLiveData<List<ContactEntity>>()
    val allContactsEvent: LiveData<List<ContactEntity>>
        get() = _allContactsEvent

    private val _deleteAllContactsEvent = MutableLiveData<Unit>()
    val deleteAllContactsEvent: LiveData<Unit>
        get() = _deleteAllContactsEvent

    fun getContacts() = viewModelScope.launch {
        _allContactsEvent.postValue(repository.getAllContacts())
    }

    fun deleteAllContacts() = viewModelScope.launch {
        try {
            repository.deleteAllContacts()
            _deleteAllContactsEvent.postValue(Unit)
        } catch (ex: Exception) {
            Log.e(TAG, ex.toString())
        }
    }

    companion object {
        private val TAG = ContactListViewModel::class.java.simpleName
    }
}