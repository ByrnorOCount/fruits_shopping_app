package com.mopr.fruits_app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.appbar.MaterialToolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mopr.fruits_app.R
import com.mopr.fruits_app.data.remote.FirestoreManager
import com.mopr.fruits_app.data.model.Order
import com.mopr.fruits_app.util.BaseActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderHistoryActivity : BaseActivity() {
    private lateinit var firestoreManager: FirestoreManager
    private var userId: String? = null
    private lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        firestoreManager = FirestoreManager()
        userId = firestoreManager.getCurrentUserId()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.order_history)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        rv = findViewById(R.id.rvItems)
        rv.layoutManager = LinearLayoutManager(this)

        loadOrderHistory()
    }

    private fun loadOrderHistory() {
        val uid = userId ?: return
        lifecycleScope.launch {
            val orders = firestoreManager.getOrderHistory(uid)
            rv.adapter = OrderAdapter(orders) { orderId ->
                cancelOrder(orderId)
            }
        }
    }

    private fun cancelOrder(orderId: String) {
        lifecycleScope.launch {
            if (firestoreManager.cancelOrder(orderId)) {
                Toast.makeText(this@OrderHistoryActivity, "Order Cancelled", Toast.LENGTH_SHORT).show()
                loadOrderHistory()
            }
        }
    }

    class OrderAdapter(
        private val orders: List<Order>,
        private val onCancelClick: (String) -> Unit
    ) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

        class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvId: TextView = view.findViewById(R.id.tvOrderId)
            val tvDate: TextView = view.findViewById(R.id.tvOrderDate)
            val tvItems: TextView = view.findViewById(R.id.tvOrderItems)
            val tvTotal: TextView = view.findViewById(R.id.tvOrderTotal)
            val tvStatus: TextView = view.findViewById(R.id.tvOrderStatus)
            val btnCancel: Button = view.findViewById(R.id.btnCancelOrder)
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
            holder.tvStatus.text = context.getString(R.string.status_label, order.status)

            holder.btnCancel.visibility = if (order.status == "Completed" || order.status == "Pending") View.VISIBLE else View.GONE
            
            holder.btnCancel.setOnClickListener {
                onCancelClick(order.id)
            }
        }

        override fun getItemCount(): Int = orders.size
    }
}