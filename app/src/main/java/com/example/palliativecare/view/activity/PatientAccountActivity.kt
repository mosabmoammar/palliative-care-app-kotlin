package com.example.palliativecare.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.palliativecare.R
import com.example.palliativecare.databinding.ActivityPatientAccountBinding
import com.example.palliativecare.view.fragment.PatientLoginFragment
import com.example.palliativecare.view.fragment.PatientRegisterFragment

class PatientAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_account)
        binding = ActivityPatientAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val patientLoginFragment = PatientLoginFragment()
        val patientRegisterFragment = PatientRegisterFragment()

        setCurrentFragment(patientLoginFragment)

        binding.patientAccountBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_register -> setCurrentFragment(patientRegisterFragment)
                R.id.bottom_login -> setCurrentFragment(patientLoginFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.patient_account_frame_layout, fragment).commit()
    }
}