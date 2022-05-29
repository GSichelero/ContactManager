package com.example.gsichelero.contactmanager.ui.contactlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.gsichelero.contactmanager.R
import com.example.gsichelero.contactmanager.data.db.AppDatabase
import com.example.gsichelero.contactmanager.extension.navigateWithAnimations
import com.example.gsichelero.contactmanager.repository.DatabaseDataSource
import com.example.gsichelero.contactmanager.repository.ContactRepository
import kotlinx.android.synthetic.main.contact_list_fragment.*

class ContactListFragment : Fragment(R.layout.contact_list_fragment) {

    private val viewModel: ContactListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val contactDao = AppDatabase.getInstance(requireContext()).contactDAO
                val repository: ContactRepository = DatabaseDataSource(contactDao)
                return ContactListViewModel(repository) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelEvents()
        configureViewListeners()
    }

    private fun observeViewModelEvents() {
        viewModel.allContactsEvent.observe(viewLifecycleOwner) { allContacts ->
            setHasOptionsMenu(allContacts.size > 1)

            val contactListAdapter = ContactListAdapter(allContacts) { contact ->
                val directions = ContactListFragmentDirections
                    .actionContactListFragmentToContactFragment(contact)

                findNavController().navigateWithAnimations(directions)
            }

            with(recycler_contacts) {
                setHasFixedSize(true)
                adapter = contactListAdapter
            }
        }

        viewModel.deleteAllContactsEvent.observe(viewLifecycleOwner) {
            viewModel.getContacts()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getContacts()
    }

    private fun configureViewListeners() {
        fabAddContact.setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_contactListFragment_to_contactFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.contact_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.delete_contacts) {
            viewModel.deleteAllContacts()
            true
        } else super.onOptionsItemSelected(item)
    }
}