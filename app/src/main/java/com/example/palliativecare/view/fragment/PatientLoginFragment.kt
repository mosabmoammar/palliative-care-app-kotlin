package com.example.palliativecare.view.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.palliativecare.databinding.FragmentPatientLoginBinding
import com.example.palliativecare.view.activity.DoctorBottomNavigationBarActivity
import com.example.palliativecare.view.activity.PatientBottomNavigationBarActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PatientLoginFragment : Fragment() {

    private lateinit var binding: FragmentPatientLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPatientLoginBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        binding.patientLoginBtnLogin.setOnClickListener {
            if (binding.patientLoginEtEmail.text.toString().trim().isNotEmpty() &&
                binding.patientLoginEtPassword.text.toString().trim().isNotEmpty()
            ) {
                signIn(
                    binding.patientLoginEtEmail.text.toString(),
                    binding.patientLoginEtPassword.text.toString()
                )
            } else {
                Toast.makeText(requireActivity(), "املأ جميع الحقول", Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(
                        requireActivity(),
                        "Login Success.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val intent = Intent(requireActivity(), PatientBottomNavigationBarActivity::class.java)
                    startActivity(intent)
                    Log.d("Login success", "Login success")

                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Login failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    Log.d("signIn failure", task.exception.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireActivity(),
                    "Login failure Listener.",
                    Toast.LENGTH_SHORT,
                ).show()
                Log.d("signIn failure Listener", it.toString())
            }
    }

}