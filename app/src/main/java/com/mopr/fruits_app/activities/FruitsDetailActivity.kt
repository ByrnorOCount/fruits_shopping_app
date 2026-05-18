package com.mopr.fruits_app.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.mopr.fruits_app.R
import com.mopr.fruits_app.models.Fruit

class FruitDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fruit_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val fruit = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("FRUIT_OBJ", Fruit::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("FRUIT_OBJ")
        }

        fruit?.let {
            findViewById<TextView>(R.id.fruitDetailName).text = it.name
            findViewById<ImageView>(R.id.fruitDetailImage).setImageResource(it.imageRes)
            findViewById<TextView>(R.id.fruitDetailDesc).text = it.description
            findViewById<TextView>(R.id.fruitScientificName).text = it.scientificName
            findViewById<TextView>(R.id.fruitHealthBenefits).text = it.healthBenefits

            supportActionBar?.title = it.name
            
            // Handle admin visibility
            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val isAdmin = sharedPref.getBoolean("IS_ADMIN", false)
            val adminControls = findViewById<LinearLayout>(R.id.adminControls)
            adminControls.visibility = if (isAdmin) View.VISIBLE else View.GONE
        }
    }
}