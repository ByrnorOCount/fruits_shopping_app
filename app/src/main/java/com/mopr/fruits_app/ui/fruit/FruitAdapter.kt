package com.mopr.fruits_app.ui.fruit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.mopr.fruits_app.R
import com.mopr.fruits_app.data.model.Fruit
import java.util.Locale

class FruitAdapter (
    private val context: Context,
    private val fruitList: List<Fruit>
) : BaseAdapter() {
    override fun getCount(): Int = fruitList.size

    override fun getItem(position: Int): Any = fruitList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Reuse the view if possible
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)

        val imageView = view.findViewById<ImageView>(R.id.gridImage)
        val textView = view.findViewById<TextView>(R.id.gridText)
        val priceView = view.findViewById<TextView>(R.id.gridPrice)

        val fruit = fruitList[position]
        imageView.setImageResource(fruit.imageRes)
        textView.text = fruit.name
        priceView.text = String.format(Locale.getDefault(), "$%.2f", fruit.price)

        return view
    }
}