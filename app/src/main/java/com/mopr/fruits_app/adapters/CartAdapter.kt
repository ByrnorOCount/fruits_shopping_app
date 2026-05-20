package com.mopr.fruits_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mopr.fruits_app.R
import com.mopr.fruits_app.models.CartItem

class CartAdapter(
    private var items: List<CartItem>,
    private val onQuantityChanged: (String, Int) -> Unit,
    private val onRemoveItem: (String) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.ivCartItem)
        val tvName: TextView = view.findViewById(R.id.tvCartItemName)
        val tvPrice: TextView = view.findViewById(R.id.tvCartItemPrice)
        val tvQuantity: TextView = view.findViewById(R.id.tvCartQuantity)
        val btnPlus: ImageButton = view.findViewById(R.id.btnPlus)
        val btnMinus: ImageButton = view.findViewById(R.id.btnMinus)
        val btnRemove: ImageButton = view.findViewById(R.id.btnRemoveItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.ivImage.setImageResource(item.imageRes)
        holder.tvName.text = item.fruitName
        holder.tvPrice.text = String.format("$%.2f", item.price * item.quantity)
        holder.tvQuantity.text = item.quantity.toString()

        holder.btnPlus.setOnClickListener {
            onQuantityChanged(item.fruitId, item.quantity + 1)
        }

        holder.btnMinus.setOnClickListener {
            if (item.quantity > 1) {
                onQuantityChanged(item.fruitId, item.quantity - 1)
            }
        }

        holder.btnRemove.setOnClickListener {
            onRemoveItem(item.fruitId)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}