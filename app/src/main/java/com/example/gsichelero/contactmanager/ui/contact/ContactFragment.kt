package com.example.gsichelero.contactmanager.ui.contact

import android.os.Bundle
import android.view.View
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gsichelero.contactmanager.R
import com.example.gsichelero.contactmanager.data.db.AppDatabase
import com.example.gsichelero.contactmanager.data.db.dao.ContactDAO
import com.example.gsichelero.contactmanager.extension.hideKeyboard
import com.example.gsichelero.contactmanager.repository.ContactRepository
import com.example.gsichelero.contactmanager.repository.DatabaseDataSource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.contact_fragment.*

class ContactFragment : Fragment(R.layout.contact_fragment) {

    private val viewModel: ContactViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val contactDAO: ContactDAO =
                    AppDatabase.getInstance(requireContext()).contactDAO

                val repository: ContactRepository = DatabaseDataSource(contactDAO)
                return ContactViewModel(repository) as T
            }
        }
    }

    private val args: ContactFragmentArgs by navArgs()

    fun setSpinText(spin: Spinner, text: String) {
        for (i in 0 until spin.getAdapter().getCount()) {
            if (spin.getAdapter().getItem(i).toString().contains(text)) {
                spin.setSelection(i)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.contact?.let { contact ->
            button_contact.text = getString(R.string.contact_button_update)
            input_name.setText(contact.name)
            input_phone.setText(contact.phone)
            setSpinText(input_cel_type, contact.cel_type)

            button_delete.visibility = View.VISIBLE
        }

        observeEvents()
        setListeners()
    }

    private fun observeEvents() {
        viewModel.contactStateEventData.observe(viewLifecycleOwner) { contactState ->
            when (contactState) {
                is ContactViewModel.ContactState.Inserted,
                is ContactViewModel.ContactState.Updated,
                is ContactViewModel.ContactState.Deleted -> {
                    clearFields()
                    hideKeyboard()
                    requireView().requestFocus()

                    findNavController().popBackStack()
                }
            }
        }

        viewModel.messageEventData.observe(viewLifecycleOwner) { stringResId ->
            Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun clearFields() {
        input_name.text?.clear()
        input_phone.text?.clear()
        input_cel_type.getSelectedItem()
    }

    private fun hideKeyboard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity) {
            parentActivity.hideKeyboard()
        }
    }

    private fun setListeners() {
        button_contact.setOnClickListener {
            val name = input_name.text.toString()
            val phone = input_phone.text.toString()
            val cel_type = input_cel_type.getSelectedItem().toString()

            viewModel.addOrUpdateContact(name, phone, cel_type, args.contact?.id ?: 0)
        }

        button_delete.setOnClickListener {
            viewModel.removeContact(args.contact?.id ?: 0)
        }
    }
}