package com.example.palliativecare.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.palliativecare.R
import com.example.palliativecare.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        auth = Firebase.auth
        db.collection("doctors")
            .whereEqualTo("email", auth.currentUser?.email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    binding.profileTvName.text =
                        "${document.data["first_name"]} ${document.data["middle_name"]} ${document.data["family_name"]}"
                }
                Toast.makeText(requireContext(), "Success on get document", Toast.LENGTH_SHORT)
                    .show()

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failure on get document", Toast.LENGTH_SHORT)
                    .show()
            }

        return binding.root
    }

}