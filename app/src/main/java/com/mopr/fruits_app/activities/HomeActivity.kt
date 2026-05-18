package com.mopr.fruits_app.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridView
import androidx.appcompat.widget.Toolbar
import com.mopr.fruits_app.R
import com.mopr.fruits_app.adapters.GridAdapter
import com.mopr.fruits_app.database.DatabaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : BaseActivity() {
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = DatabaseHelper(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val gridView = findViewById<GridView>(R.id.homeGridView)

        // Check if admin
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val isAdmin = sharedPref.getBoolean("IS_ADMIN", false)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAddFruit)
        fabAdd.visibility = if (isAdmin) View.VISIBLE else View.GONE
        
        fabAdd.setOnClickListener {
            // Logic for adding a fruits (maybe a dialog or new activity)
        }

        // Data source from DB
        val fruitsList = db.getAllFruits()

        // Create adapter and set it to the GridView
        val adapter = GridAdapter(this, fruitsList)
        gridView.adapter = adapter
        
        // Handle item clicks
        gridView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, FruitDetailActivity::class.java).apply {
                putExtra("FRUITS_OBJ", fruitsList[position])
            }
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.menu_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
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