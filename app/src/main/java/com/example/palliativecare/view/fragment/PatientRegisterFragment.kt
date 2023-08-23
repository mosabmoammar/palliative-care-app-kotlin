package com.example.palliativecare.view.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.example.palliativecare.databinding.FragmentPatientRegisterBinding
import com.example.palliativecare.view.activity.PatientBottomNavigationBarActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.math.BigInteger
import java.security.MessageDigest


class PatientRegisterFragment : Fragment() {

    private lateinit var binding: FragmentPatientRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPatientRegisterBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding.patientRegisterTvBirthDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { dateDialog, year, month, day ->
                    binding.patientRegisterTvBirthDate.text = "$day/${month + 1}/$year"
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        binding.patientRegisterBtnRegister.setOnClickListener {
            if (binding.patientRegisterEtFirstName.text.toString().trim().isNotEmpty() &&
                binding.patientRegisterEtMiddleName.text.toString().trim().isNotEmpty() &&
                binding.patientRegisterEtFamilyName.text.toString().trim().isNotEmpty() &&
                binding.patientRegisterTvBirthDate.text.toString().trim().isNotEmpty() &&
                binding.patientRegisterEtAddress.text.toString().trim().isNotEmpty() &&
                binding.patientRegisterEtEmail.text.toString().trim().isNotEmpty() &&
                binding.patientRegisterEtPhone.text.toString().trim().isNotEmpty() &&
                binding.patientRegisterEtPassword.text.toString().trim().isNotEmpty() &&
                binding.patientRegisterEtConfirmPassword.text.toString().trim().isNotEmpty()
            ) {
                if (binding.patientRegisterEtPassword.text.toString() ==
                    binding.patientRegisterEtConfirmPassword.text.toString()
                ) {
                    createUser(
                        binding.patientRegisterEtEmail.text.toString(),
                        binding.patientRegisterEtPassword.text.toString()
                    )
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "كلمة المرور غير متطابقة مع التأكيد",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireActivity(), "املأ جميع الحقول", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(
                        requireActivity(),
                        "${user?.email} >> Register create user Success",
                        Toast.LENGTH_SHORT
                    ).show()
                    addNewUserToFirestore()
                    val intent =
                        Intent(requireActivity(), PatientBottomNavigationBarActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Register create user Fail",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun addNewUserToFirestore() {
        val user = hashMapOf(
            "first_name" to binding.patientRegisterEtFirstName.text.toString(),
            "middle_name" to binding.patientRegisterEtMiddleName.text.toString(),
            "family_name" to binding.patientRegisterEtFamilyName.text.toString(),
            "date_of_birth" to binding.patientRegisterTvBirthDate.text.toString(),
            "address" to binding.patientRegisterEtAddress.text.toString(),
            "email" to binding.patientRegisterEtEmail.text.toString(),
            "phone" to binding.patientRegisterEtPhone.text.toString(),
//            "password" to md5(binding.doctorRegisterEtPassword.text.toString())
        )

        db.collection("patients")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(requireActivity(), "Register in firestore Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener { e ->
                Toast.makeText(requireActivity(), "Register in firestore Fail $e", Toast.LENGTH_SHORT).show()
                Log.d("Error adding document", e.toString())
            }
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

}