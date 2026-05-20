package com.mopr.fruits_app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.appbar.MaterialToolbar
import androidx.lifecycle.lifecycleScope
import com.mopr.fruits_app.R
import com.mopr.fruits_app.database.FirestoreManager
import com.mopr.fruits_app.models.CartItem
import com.mopr.fruits_app.models.Order
import kotlinx.coroutines.launch

class CheckoutActivity : BaseActivity() {
    private lateinit var firestoreManager: FirestoreManager
    private var cartItems: List<CartItem> = emptyList()
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        firestoreManager = FirestoreManager()
        userId = firestoreManager.getCurrentUserId()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val tvSummary = findViewById<TextView>(R.id.tvOrderSummary)
        val tvTotal = findViewById<TextView>(R.id.tvTotalCheckout)
        val btnPlaceOrder = findViewById<Button>(R.id.btnPlaceOrder)

        lifecycleScope.launch {
            cartItems = firestoreManager.getCartItems(userId!!)
            if (cartItems.isEmpty()) {
                finish()
                return@launch
            }

            val summary = StringBuilder()
            var total = 0.0
            for (item in cartItems) {
                summary.append("${item.fruitName} x${item.quantity} - $${String.format("%.2f", item.price * item.quantity)}\n")
                total += item.price * item.quantity
            }
            
            tvSummary.text = summary.toString()
            tvTotal.text = "Total: $${String.format("%.2f", total)}"
        }

        btnPlaceOrder.setOnClickListener {
            placeOrder()
        }
    }

    private fun placeOrder() {
        val uid = userId ?: return
        val total = cartItems.sumOf { it.price * it.quantity }
        val order = Order(
            userId = uid,
            items = cartItems,
            totalPrice = total,
            timestamp = System.currentTimeMillis()
        )

        lifecycleScope.launch {
            if (firestoreManager.placeOrder(order)) {
                Toast.makeText(this@CheckoutActivity, "Order placed successfully!", Toast.LENGTH_LONG).show()
                val intent = Intent(this@CheckoutActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(this@CheckoutActivity, "Error placing order", Toast.LENGTH_SHORT).show()
            }
        }
    }
}