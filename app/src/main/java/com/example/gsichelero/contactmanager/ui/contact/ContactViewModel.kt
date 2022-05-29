package com.example.gsichelero.contactmanager.ui.contact

import android.telephony.PhoneNumberUtils.isGlobalPhoneNumber
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gsichelero.contactmanager.R
import com.example.gsichelero.contactmanager.repository.ContactRepository
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    private val _contactStateEventData = MutableLiveData<ContactState>()
    val contactStateEventData: LiveData<ContactState>
        get() = _contactStateEventData

    private val _messageEventData = MutableLiveData<Int>()
    val messageEventData: LiveData<Int>
        get() = _messageEventData

    fun addOrUpdateContact(name: String, phone: String, cel_type: String, id: Long = 0) = viewModelScope.launch {
        if (id > 0) {
            updateContact(id, name, phone, cel_type)
        } else {
            insertContact(name, phone, cel_type)
        }
    }

    private fun updateContact(id: Long, name: String, phone: String, cel_type: String) = viewModelScope.launch {
        try {
            if (!name.isNullOrEmpty() && isGlobalPhoneNumber(phone) && listOf("Work", "Cellphone", "Home").contains(cel_type)) {
                repository.updateContact(id, name, phone, cel_type)

                _contactStateEventData.value = ContactState.Updated
                _messageEventData.value = R.string.contact_updated_successfully
            }
            else {
                _messageEventData.value = R.string.contact_validation_error
            }
        } catch (ex: Exception) {
            _messageEventData.value = R.string.contact_error_to_update
            Log.e(TAG, ex.toString())
        }
    }

    private fun insertContact(name: String, phone: String, cel_type: String) = viewModelScope.launch {
        try {
            if (!name.isNullOrEmpty() && isGlobalPhoneNumber(phone) && listOf("Work", "Cellphone", "Home").contains(cel_type)) {
                val id = repository.insertContact(name, phone, cel_type)
                if (id > 0) {
                    _contactStateEventData.value = ContactState.Inserted
                    _messageEventData.value = R.string.contact_inserted_successfully
                }
            }
            else {
                _messageEventData.value = R.string.contact_validation_error
            }
        } catch (ex: Exception) {
            _messageEventData.value = R.string.contact_error_to_insert
            Log.e(TAG, ex.toString())
        }
    }

    fun removeContact(id: Long) = viewModelScope.launch {
        try {
            if (id > 0) {
                repository.deleteContact(id)
                _contactStateEventData.value = ContactState.Deleted
                _messageEventData.value = R.string.contact_deleted_successfully
            }
        } catch (ex: Exception) {
            _messageEventData.value = R.string.contact_error_to_delete
            Log.e(TAG, ex.toString())
        }

    }

    sealed class ContactState {
        object Inserted : ContactState()
        object Updated : ContactState()
        object Deleted : ContactState()
    }

    companion object {
        private val TAG = ContactViewModel::class.java.simpleName
    }
}