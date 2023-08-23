package com.example.palliativecare.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.palliativecare.R
import com.example.palliativecare.databinding.ActivityDoctorAccountBinding
import com.example.palliativecare.view.fragment.DoctorLoginFragment
import com.example.palliativecare.view.fragment.DoctorRegisterFragment

class DoctorAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_account)
        binding = ActivityDoctorAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val doctorRegisterFragment = DoctorRegisterFragment()
        val doctorLoginFragment = DoctorLoginFragment()

        setCurrentFragment(doctorLoginFragment)

        binding.doctorAccountBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_register -> setCurrentFragment(doctorRegisterFragment)
                R.id.bottom_login -> setCurrentFragment(doctorLoginFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.doctor_account_frame_layout, fragment).commit()
    }
}