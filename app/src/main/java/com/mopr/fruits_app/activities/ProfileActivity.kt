package com.mopr.fruits_app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.appbar.MaterialToolbar
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.mopr.fruits_app.R
import com.mopr.fruits_app.database.FirestoreManager
import kotlinx.coroutines.launch

class ProfileActivity : BaseActivity() {
    private lateinit var firestoreManager: FirestoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firestoreManager = FirestoreManager()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        loadUserProfile()

        findViewById<Button>(R.id.btnViewCart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewOrders).setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewFavorites).setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        findViewById<Button>(R.id.btnLogoutProfile).setOnClickListener {
            firestoreManager.logout()
            // Clear admin status
            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            sharedPref.edit { remove("IS_ADMIN") }

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun loadUserProfile() {
        val userId = firestoreManager.getCurrentUserId() ?: return
        lifecycleScope.launch {
            val user = firestoreManager.getUserData(userId)
            if (user != null) {
                findViewById<TextView>(R.id.tvProfileName).text = user.username
                findViewById<TextView>(R.id.tvProfileEmail).text = "Email: ${user.email}"
                
                val orders = firestoreManager.getOrderHistory(userId)
                findViewById<TextView>(R.id.tvOrderCount).text = getString(R.string.recent_orders_label, orders.size)

                calculateFavoriteFruit(orders)
            }
        }
    }

    private fun calculateFavoriteFruit(orders: List<com.mopr.fruits_app.models.Order>) {
        if (orders.isEmpty()) {
            findViewById<TextView>(R.id.tvFavoriteFruit).text = getString(R.string.favorite_fruit_label, "None")
            return
        }

        val fruitCounts = mutableMapOf<String, Int>()
        val fruitLastOrdered = mutableMapOf<String, Long>()

        for (order in orders) {
            for (item in order.items) {
                fruitCounts[item.fruitName] = fruitCounts.getOrDefault(item.fruitName, 0) + item.quantity
                
                val lastTime = fruitLastOrdered.getOrDefault(item.fruitName, 0L)
                if (order.timestamp > lastTime) {
                    fruitLastOrdered[item.fruitName] = order.timestamp
                }
            }
        }

        val favoriteFruit = fruitCounts.maxWithOrNull(compareBy({ it.value }, { fruitLastOrdered[it.key] }))?.key ?: "None"
        findViewById<TextView>(R.id.tvFavoriteFruit).text = getString(R.string.favorite_fruit_label, favoriteFruit)
    }
}
