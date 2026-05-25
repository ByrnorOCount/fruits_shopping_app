package com.mopr.fruits_app.ui.admin

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.mopr.fruits_app.R
import com.mopr.fruits_app.data.remote.FirestoreManager
import com.mopr.fruits_app.util.BaseActivity
import kotlinx.coroutines.launch

class AdminStatsActivity : BaseActivity() {
    private lateinit var firestoreManager: FirestoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_stats)

        firestoreManager = FirestoreManager()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        loadStatistics()
    }

    private fun loadStatistics() {
        lifecycleScope.launch {
            val stats = firestoreManager.getAdminStats()
            
            findViewById<TextView>(R.id.tvTotalRevenue).text = "$${String.format("%.2f", stats.totalRevenue)}"
            findViewById<TextView>(R.id.tvTotalOrders).text = stats.totalOrders.toString()
            
            val topSellingText = if (stats.topSellingFruits.isEmpty()) {
                "No data available"
            } else {
                stats.topSellingFruits.joinToString("\n")
            }
            findViewById<TextView>(R.id.tvTopSelling).text = topSellingText
        }
    }
}