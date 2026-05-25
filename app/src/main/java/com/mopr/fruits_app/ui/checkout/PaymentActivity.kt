package com.mopr.fruits_app.ui.checkout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.android.material.appbar.MaterialToolbar
import com.mopr.fruits_app.R
import com.mopr.fruits_app.util.BaseActivity

class PaymentActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val rgPayment = findViewById<RadioGroup>(R.id.rgPaymentMethods)
        val btnConfirm = findViewById<Button>(R.id.btnConfirmPayment)

        btnConfirm.setOnClickListener {
            val selectedId = rgPayment.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
            } else {
                val radioButton = findViewById<RadioButton>(selectedId)
                val method = radioButton.text.toString()
                
                val resultIntent = Intent()
                resultIntent.putExtra("SELECTED_PAYMENT", method)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}