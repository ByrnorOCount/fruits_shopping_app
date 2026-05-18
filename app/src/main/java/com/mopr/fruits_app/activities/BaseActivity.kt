package com.mopr.fruits_app.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    protected val tag = "LifecycleDemo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "${this.javaClass.simpleName} - onCreate called")
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "${this.javaClass.simpleName} - onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "${this.javaClass.simpleName} - onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "${this.javaClass.simpleName} - onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag, "${this.javaClass.simpleName} - onStop called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(tag, "${this.javaClass.simpleName} - onRestart called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "${this.javaClass.simpleName} - onDestroy called")
    }
}
