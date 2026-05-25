package com.mopr.fruits_app.ui.checkout

import android.os.Bundle
import android.widget.Toast
import com.mopr.fruits_app.util.BaseActivity
import com.mopr.fruits_app.R

class PaymentActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        Toast.makeText(this, "Payment Integration Coming Soon", Toast.LENGTH_SHORT).show()
    }
}
