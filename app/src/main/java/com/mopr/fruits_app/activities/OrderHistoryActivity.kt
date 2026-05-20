package com.mopr.fruits_app.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.appbar.MaterialToolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mopr.fruits_app.R
import com.mopr.fruits_app.database.FirestoreManager
import com.mopr.fruits_app.models.Order
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderHistoryActivity : BaseActivity() {
    private lateinit var firestoreManager: FirestoreManager
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        firestoreManager = FirestoreManager()
        userId = firestoreManager.getCurrentUserId()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Order History"
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val rv = findViewById<RecyclerView>(R.id.rvItems)
        rv.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            val orders = firestoreManager.getOrderHistory(userId!!)
            rv.adapter = OrderAdapter(orders)
        }
    }

    class OrderAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
        class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvId: TextView = view.findViewById(R.id.tvOrderId)
            val tvDate: TextView = view.findViewById(R.id.tvOrderDate)
            val tvItems: TextView = view.findViewById(R.id.tvOrderItems)
            val tvTotal: TextView = view.findViewById(R.id.tvOrderTotal)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
            return OrderViewHolder(view)
        }

        override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
            val order = orders[position]
            val context = holder.itemView.context
            
            holder.tvId.text = context.getString(R.string.order_id_label, order.id)
            
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            holder.tvDate.text = context.getString(R.string.order_date_label, sdf.format(Date(order.timestamp)))
            
            val itemSummary = order.items.joinToString(", ") { "${it.fruitName} (x${it.quantity})" }
            holder.tvItems.text = context.getString(R.string.order_items_label, itemSummary)
            
            holder.tvTotal.text = context.getString(R.string.order_total_label, order.totalPrice)
        }

        override fun getItemCount(): Int = orders.size
    }
}