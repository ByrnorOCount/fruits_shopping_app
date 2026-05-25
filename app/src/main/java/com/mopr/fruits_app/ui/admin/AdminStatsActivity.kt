package com.mopr.fruits_app.ui.admin

import android.os.Bundle
import android.widget.Toast
import com.mopr.fruits_app.util.BaseActivity
import com.mopr.fruits_app.R

class AdminStatsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_stats)
        Toast.makeText(this, "Admin Statistics Coming Soon", Toast.LENGTH_SHORT).show()
    }
}
