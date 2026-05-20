package com.mopr.fruits_app.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.appbar.MaterialToolbar
import androidx.lifecycle.lifecycleScope
import com.mopr.fruits_app.R
import com.mopr.fruits_app.database.FirestoreManager
import com.mopr.fruits_app.models.CartItem
import com.mopr.fruits_app.models.Fruit
import kotlinx.coroutines.launch

class FruitDetailActivity : BaseActivity() {
    private lateinit var firestoreManager: FirestoreManager
    private var currentFruit: Fruit? = null
    private var isFavorite = false
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fruit_detail)

        firestoreManager = FirestoreManager()
        userId = firestoreManager.getCurrentUserId()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        currentFruit = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("FRUIT_OBJ", Fruit::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("FRUIT_OBJ")
        }

        currentFruit?.let { fruit ->
            findViewById<TextView>(R.id.fruitDetailName).text = fruit.name
            findViewById<TextView>(R.id.fruitDetailPrice).text = getString(R.string.price_label, fruit.price)
            findViewById<ImageView>(R.id.fruitDetailImage).setImageResource(fruit.imageRes)
            findViewById<TextView>(R.id.fruitDetailDesc).text = fruit.description
            findViewById<TextView>(R.id.fruitScientificName).text = fruit.scientificName
            findViewById<TextView>(R.id.fruitHealthBenefits).text = fruit.healthBenefits

            supportActionBar?.title = fruit.name
            
            checkFavoriteStatus(fruit.id)

            // Handle admin visibility
            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val isAdmin = sharedPref.getBoolean("IS_ADMIN", false)
            val adminControls = findViewById<LinearLayout>(R.id.adminControls)
            adminControls.visibility = if (isAdmin) View.VISIBLE else View.GONE

            findViewById<Button>(R.id.btnBuyNow).setOnClickListener {
                addToCart(fruit)
            }

            findViewById<Button>(R.id.btnEditFruit).setOnClickListener {
                val intent = Intent(this, AdminFruitActivity::class.java).apply {
                    putExtra("FRUIT_OBJ", fruit)
                }
                startActivity(intent)
            }

            findViewById<Button>(R.id.btnDeleteFruit).setOnClickListener {
                deleteFruit(fruit.id)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.fruit_detail_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val favItem = menu?.findItem(R.id.menu_favorite)
        favItem?.setIcon(R.drawable.ic_favorite_full)
        favItem?.icon?.setTint(if (isFavorite) getColor(R.color.pink_favorite) else getColor(R.color.white))
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_favorite -> {
                toggleFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkFavoriteStatus(fruitId: String) {
        if (userId == null) return
        lifecycleScope.launch {
            isFavorite = firestoreManager.isFavorite(userId!!, fruitId)
            invalidateOptionsMenu()
        }
    }

    private fun toggleFavorite() {
        val fruit = currentFruit ?: return
        val uid = userId ?: return
        lifecycleScope.launch {
            if (firestoreManager.toggleFavorite(uid, fruit.id)) {
                isFavorite = !isFavorite
                invalidateOptionsMenu()
                val msg = if (isFavorite) "Added to favorites" else "Removed from favorites"
                Toast.makeText(this@FruitDetailActivity, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addToCart(fruit: Fruit) {
        val uid = userId ?: return
        val cartItem = CartItem(
            fruitId = fruit.id,
            fruitName = fruit.name,
            quantity = 1,
            price = fruit.price,
            imageRes = fruit.imageRes
        )
        lifecycleScope.launch {
            if (firestoreManager.addToCart(uid, cartItem)) {
                Toast.makeText(this@FruitDetailActivity, "${fruit.name} added to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteFruit(fruitId: String) {
        lifecycleScope.launch {
            if (firestoreManager.deleteFruit(fruitId)) {
                Toast.makeText(this@FruitDetailActivity, "Fruit deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}