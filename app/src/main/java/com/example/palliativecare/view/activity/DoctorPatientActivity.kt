package com.example.palliativecare.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.palliativecare.R
import com.example.palliativecare.databinding.ActivityDoctorPatientBinding

class DoctorPatientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorPatientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_patient)
        binding = ActivityDoctorPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.doctorPatientBtnDoctor.setOnClickListener {
            val intent = Intent(this,DoctorAccountActivity::class.java)
            startActivity(intent)
        }

        binding.doctorPatientBtnPatient.setOnClickListener {
            val intent = Intent(this,PatientAccountActivity::class.java)
            startActivity(intent)
        }
    }
}