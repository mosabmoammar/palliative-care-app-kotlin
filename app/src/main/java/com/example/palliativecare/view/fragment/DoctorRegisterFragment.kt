package com.example.palliativecare.view.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.example.palliativecare.databinding.FragmentDoctorRegisterBinding
import com.example.palliativecare.view.activity.DoctorBottomNavigationBarActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.math.BigInteger
import java.security.MessageDigest

class DoctorRegisterFragment : Fragment() {

    private lateinit var binding: FragmentDoctorRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDoctorRegisterBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding.doctorRegisterTvBirthDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { dateDialog, year, month, day ->
                    binding.doctorRegisterTvBirthDate.text = "$day/${month + 1}/$year"
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        binding.doctorRegisterBtnRegister.setOnClickListener {
            if (binding.doctorRegisterEtFirstName.text.toString().trim().isNotEmpty() &&
                binding.doctorRegisterEtMiddleName.text.toString().trim().isNotEmpty() &&
                binding.doctorRegisterEtFamilyName.text.toString().trim().isNotEmpty() &&
                binding.doctorRegisterTvBirthDate.text.toString().trim().isNotEmpty() &&
                binding.doctorRegisterEtAddress.text.toString().trim().isNotEmpty() &&
                binding.doctorRegisterEtEmail.text.toString().trim().isNotEmpty() &&
                binding.doctorRegisterEtPhone.text.toString().trim().isNotEmpty() &&
                binding.doctorRegisterEtPassword.text.toString().trim().isNotEmpty() &&
                binding.doctorRegisterEtConfirmPassword.text.toString().trim().isNotEmpty()
            ) {
                if (binding.doctorRegisterEtPassword.text.toString() ==
                    binding.doctorRegisterEtConfirmPassword.text.toString()
                ) {
                    createUser(
                        binding.doctorRegisterEtEmail.text.toString(),
                        binding.doctorRegisterEtPassword.text.toString()
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
//                    val editor = sharedPreferences.edit()
//                    editor.putString("doctor", "patient")
//                    editor.apply()

                    val intent = Intent(requireActivity(), DoctorBottomNavigationBarActivity::class.java)
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
            "first_name" to binding.doctorRegisterEtFirstName.text.toString(),
            "middle_name" to binding.doctorRegisterEtMiddleName.text.toString(),
            "family_name" to binding.doctorRegisterEtFamilyName.text.toString(),
            "date_of_birth" to binding.doctorRegisterTvBirthDate.text.toString(),
            "address" to binding.doctorRegisterEtAddress.text.toString(),
            "email" to binding.doctorRegisterEtEmail.text.toString(),
            "phone" to binding.doctorRegisterEtPhone.text.toString(),
//            "password" to md5(binding.doctorRegisterEtPassword.text.toString())
        )

        db.collection("doctors")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(
                    requireActivity(),
                    "Register in firestore Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnSuccessListener { e ->
                Toast.makeText(
                    requireActivity(),
                    "Register in firestore Fail $e",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("Error adding document", e.toString())
            }
    }


    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

}