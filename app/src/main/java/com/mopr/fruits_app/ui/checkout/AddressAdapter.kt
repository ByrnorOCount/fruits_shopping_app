package com.mopr.fruits_app.ui.checkout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mopr.fruits_app.R
import com.mopr.fruits_app.data.model.Address

class AddressAdapter(
    private val addresses: List<Address>,
    private val onAddressSelected: (Address) -> Unit
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvAddressName)
        val tvDetails: TextView = view.findViewById(R.id.tvAddressDetails)
        val tvDefault: TextView = view.findViewById(R.id.tvDefaultLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addresses[position]
        holder.tvName.text = address.name
        holder.tvDetails.text = "${address.street}, ${address.city}, ${address.zipCode}"
        holder.tvDefault.visibility = if (address.isDefault) View.VISIBLE else View.GONE
        
        holder.itemView.setOnClickListener {
            onAddressSelected(address)
        }
    }

    override fun getItemCount() = addresses.size
}