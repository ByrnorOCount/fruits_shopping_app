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
import com.mopr.fruits_app.models.User
import kotlinx.coroutines.launch

class SignupActivity : BaseActivity() {
    private lateinit var firestoreManager: FirestoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        firestoreManager = FirestoreManager()

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLoginLink = findViewById<TextView>(R.id.tvLoginLink)
        val editUsername = findViewById<EditText>(R.id.editUsername)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPassword = findViewById<EditText>(R.id.editPassword)

        btnRegister.setOnClickListener {
            val username = editUsername.text.toString()
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(username = username, email = email, password = password)
                lifecycleScope.launch {
                    val success = firestoreManager.registerUser(user)
                    if (success) {
                        // Set admin status in SharedPreferences
                        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        sharedPref.edit { putBoolean("IS_ADMIN", user.isAdmin) }

                        Toast.makeText(this@SignupActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignupActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@SignupActivity, "Registration Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        tvLoginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
