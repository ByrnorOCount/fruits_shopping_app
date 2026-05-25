package com.mopr.fruits_app.ui.fruit

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mopr.fruits_app.R
import com.mopr.fruits_app.data.model.Fruit

class RelatedFruitAdapter(private val fruits: List<Fruit>) : RecyclerView.Adapter<RelatedFruitAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.gridImage)
        val tvName: TextView = view.findViewById(R.id.gridText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        // Adjust width for horizontal scroll
        view.layoutParams.width = 300 
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruits[position]
        holder.tvName.text = fruit.name
        holder.ivImage.setImageResource(fruit.imageRes)
        
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FruitDetailActivity::class.java).apply {
                putExtra("FRUIT_OBJ", fruit)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = fruits.size
}