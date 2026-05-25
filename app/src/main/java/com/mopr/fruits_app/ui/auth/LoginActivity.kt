package com.mopr.fruits_app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.mopr.fruits_app.R
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.mopr.fruits_app.util.BaseActivity
import com.mopr.fruits_app.ui.home.HomeActivity
import com.mopr.fruits_app.data.remote.FirestoreManager
import com.mopr.fruits_app.util.SeedData
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {
    private lateinit var firestoreManager: FirestoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firestoreManager = FirestoreManager()

        // Auto-seed check on startup
        lifecycleScope.launch {
            seedAllData(false)
        }

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvSignupLink = findViewById<TextView>(R.id.tvSignupLink)
        val btnReset = findViewById<Button>(R.id.btnResetDatabase)
        val editUsername = findViewById<EditText>(R.id.editUsername)
        val editPassword = findViewById<EditText>(R.id.editPassword)

        btnLogin.setOnClickListener {
            val identifier = editUsername.text.toString()
            val password = editPassword.text.toString()

            lifecycleScope.launch {
                val user = firestoreManager.loginUser(identifier, password)
                if (user != null) {
                    val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    sharedPref.edit { putBoolean("IS_ADMIN", user.isAdmin) }

                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnReset.setOnClickListener {
            lifecycleScope.launch {
                Toast.makeText(this@LoginActivity, "Wiping and Reseeding...", Toast.LENGTH_SHORT).show()
                seedAllData(true)
                Toast.makeText(this@LoginActivity, "Database Reset Complete", Toast.LENGTH_LONG).show()
            }
        }

        tvSignupLink.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun seedAllData(forceWipe: Boolean) {
        if (forceWipe) {
            firestoreManager.wipeEverything()
        }
        val idMap = firestoreManager.seedUsers(SeedData.users)
        firestoreManager.seedFruits(SeedData.fruits)
        firestoreManager.seedPromotions(SeedData.promotions)
        firestoreManager.seedComments(SeedData.comments, idMap)
        firestoreManager.seedOrders(SeedData.orders, idMap)
    }
}
