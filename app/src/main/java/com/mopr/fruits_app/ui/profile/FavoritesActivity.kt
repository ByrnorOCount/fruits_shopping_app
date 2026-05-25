package com.mopr.fruits_app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.appbar.MaterialToolbar
import androidx.lifecycle.lifecycleScope
import android.widget.GridView
import com.mopr.fruits_app.R
import com.mopr.fruits_app.ui.fruit.FruitAdapter
import com.mopr.fruits_app.data.remote.FirestoreManager
import com.mopr.fruits_app.ui.fruit.FruitDetailActivity
import com.mopr.fruits_app.util.BaseActivity
import kotlinx.coroutines.launch

class FavoritesActivity : BaseActivity() {
    private lateinit var firestoreManager: FirestoreManager
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home) // Reusing home layout with GridView

        firestoreManager = FirestoreManager()
        userId = firestoreManager.getCurrentUserId()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Favorites"
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Hide FAB in favorites
        findViewById<View>(R.id.fabAddFruit).visibility = View.GONE

        val gridView = findViewById<GridView>(R.id.homeGridView)

        lifecycleScope.launch {
            val allFruits = firestoreManager.getAllFruits()
            val favoriteIds = firestoreManager.getFavoriteFruitIds(userId!!)
            val favoriteFruits = allFruits.filter { favoriteIds.contains(it.id) }

            val adapter = FruitAdapter(this@FavoritesActivity, favoriteFruits)
            gridView.adapter = adapter

            gridView.setOnItemClickListener { _, _, position, _ ->
                val intent = Intent(this@FavoritesActivity, FruitDetailActivity::class.java).apply {
                    putExtra("FRUIT_OBJ", favoriteFruits[position])
                }
                startActivity(intent)
            }
        }
    }
}