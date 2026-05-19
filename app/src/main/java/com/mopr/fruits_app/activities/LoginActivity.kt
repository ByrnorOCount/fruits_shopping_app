package com.mopr.fruits_app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.mopr.fruits_app.R
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.mopr.fruits_app.database.FirestoreManager
import com.mopr.fruits_app.database.SeedData
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {
    private lateinit var firestoreManager: FirestoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firestoreManager = FirestoreManager()

        // One-time upload of SeedData to Firebase
        lifecycleScope.launch {
            firestoreManager.seedUsers(SeedData.users)
            firestoreManager.seedFruits(SeedData.fruits)
        }

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvSignupLink = findViewById<TextView>(R.id.tvSignupLink)
        val editUsername = findViewById<EditText>(R.id.editUsername)
        val editPassword = findViewById<EditText>(R.id.editPassword)

        btnLogin.setOnClickListener {
            val identifier = editUsername.text.toString()
            val password = editPassword.text.toString()

            lifecycleScope.launch {
                val user = firestoreManager.loginUser(identifier, password)
                if (user != null) {
                    // Store admin status in SharedPreferences for easy access
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

        tvSignupLink.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}
