package com.example.palliativecare.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.palliativecare.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val intent = Intent(this, DoctorPatientActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}