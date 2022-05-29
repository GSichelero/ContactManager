package com.example.gsichelero.contactmanager.ui.contactlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gsichelero.contactmanager.R
import com.example.gsichelero.contactmanager.data.db.entity.ContactEntity
import kotlinx.android.synthetic.main.contact_item.view.*

class ContactListAdapter(
    private val contacts: List<ContactEntity>,
    private val onContactClickListener: (contact: ContactEntity) -> Unit
) : RecyclerView.Adapter<ContactListAdapter.ContactListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)

        return ContactListViewHolder(view, onContactClickListener)
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.bindView(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size

    class ContactListViewHolder(
        itemView: View,
        private val onContactClickListener: (contact: ContactEntity) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val textViewContactName: TextView = itemView.text_contact_name
        private val textViewContactPhone: TextView = itemView.text_contact_phone
        private val textViewContactCelType: TextView = itemView.text_contact_cel_type

        fun bindView(contact: ContactEntity) {
            textViewContactName.text = contact.name
            textViewContactPhone.text = contact.phone
            textViewContactCelType.text = contact.cel_type

            itemView.setOnClickListener {
                onContactClickListener.invoke(contact)
            }
        }
    }
}