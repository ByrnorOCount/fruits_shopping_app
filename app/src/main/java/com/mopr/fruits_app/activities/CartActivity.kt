package com.mopr.fruits_app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.appbar.MaterialToolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mopr.fruits_app.R
import com.mopr.fruits_app.adapters.CartAdapter
import com.mopr.fruits_app.database.FirestoreManager
import kotlinx.coroutines.launch

class CartActivity : BaseActivity() {
    private lateinit var firestoreManager: FirestoreManager
    private lateinit var cartAdapter: CartAdapter
    private lateinit var tvTotal: TextView
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        firestoreManager = FirestoreManager()
        userId = firestoreManager.getCurrentUserId()

        if (userId == null) {
            finish()
            return
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        tvTotal = findViewById(R.id.tvTotalPrice)
        val rvCart = findViewById<RecyclerView>(R.id.rvCartItems)
        val btnCheckout = findViewById<Button>(R.id.btnCheckout)

        cartAdapter = CartAdapter(emptyList(), { fruitId, newQty ->
            updateQuantity(fruitId, newQty)
        }, { fruitId ->
            removeItem(fruitId)
        })

        rvCart.layoutManager = LinearLayoutManager(this)
        rvCart.adapter = cartAdapter

        btnCheckout.setOnClickListener {
            if (cartAdapter.itemCount > 0) {
                startActivity(Intent(this, CheckoutActivity::class.java))
            } else {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
            }
        }

        loadCartItems()
    }

    private fun loadCartItems() {
        lifecycleScope.launch {
            val items = firestoreManager.getCartItems(userId!!)
            cartAdapter.updateItems(items)
            updateTotalPrice(items)
        }
    }

    private fun updateQuantity(fruitId: String, quantity: Int) {
        lifecycleScope.launch {
            if (firestoreManager.updateCartItemQuantity(userId!!, fruitId, quantity)) {
                loadCartItems()
            }
        }
    }

    private fun removeItem(fruitId: String) {
        lifecycleScope.launch {
            if (firestoreManager.removeFromCart(userId!!, fruitId)) {
                loadCartItems()
            }
        }
    }

    private fun updateTotalPrice(items: List<com.mopr.fruits_app.models.CartItem>) {
        val total = items.sumOf { it.price * it.quantity }
        tvTotal.text = String.format("$%.2f", total)
    }
}