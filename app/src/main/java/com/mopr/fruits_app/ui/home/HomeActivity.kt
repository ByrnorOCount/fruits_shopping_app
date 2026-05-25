package com.mopr.fruits_app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridView
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mopr.fruits_app.R
import com.mopr.fruits_app.ui.admin.AdminFruitActivity
import com.mopr.fruits_app.ui.cart.CartActivity
import com.mopr.fruits_app.ui.profile.FavoritesActivity
import com.mopr.fruits_app.ui.fruit.FruitDetailActivity
import com.mopr.fruits_app.ui.profile.ProfileActivity
import com.mopr.fruits_app.ui.fruit.FruitAdapter
import com.mopr.fruits_app.data.remote.FirestoreManager
import com.mopr.fruits_app.ui.auth.LoginActivity
import com.mopr.fruits_app.util.BaseActivity
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {
    private lateinit var firestoreManager: FirestoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        firestoreManager = FirestoreManager()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val gridView = findViewById<GridView>(R.id.homeGridView)

        // Check if admin
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val isAdmin = sharedPref.getBoolean("IS_ADMIN", false)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAddFruit)
        fabAdd.visibility = if (isAdmin) View.VISIBLE else View.GONE

        fabAdd.setOnClickListener {
            startActivity(Intent(this, AdminFruitActivity::class.java))
        }

        // Load data source from Firestore
        lifecycleScope.launch {
            val fruitsList = firestoreManager.getAllFruits()

            // Create adapter and set it to the GridView
            val adapter = FruitAdapter(this@HomeActivity, fruitsList)
            gridView.adapter = adapter

            // Handle item clicks
            gridView.setOnItemClickListener { _, _, position, _ ->
                val intent = Intent(this@HomeActivity, FruitDetailActivity::class.java).apply {
                    putExtra("FRUIT_OBJ", fruitsList[position])
                }
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
                true
            }
            R.id.menu_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_favorites -> {
                startActivity(Intent(this, FavoritesActivity::class.java))
                true
            }
            R.id.menu_logout -> {
                firestoreManager.logout()
                // Clear admin status
                val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                sharedPref.edit { remove("IS_ADMIN") }

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.menu_settings -> {
                // Future Settings action
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}