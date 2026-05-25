package com.mopr.fruits_app.ui.checkout

import android.os.Bundle
import android.widget.Toast
import com.mopr.fruits_app.util.BaseActivity
import com.mopr.fruits_app.R

class AddressActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        Toast.makeText(this, "Address Management Coming Soon", Toast.LENGTH_SHORT).show()
    }
}
