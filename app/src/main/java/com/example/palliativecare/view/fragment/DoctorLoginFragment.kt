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
import com.example.palliativecare.databinding.FragmentDoctorLoginBinding
import com.example.palliativecare.view.activity.DoctorBottomNavigationBarActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DoctorLoginFragment : Fragment() {

    private lateinit var binding: FragmentDoctorLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val db = Firebase.firestore


//    override fun onStart() {
//        super.onStart()
//        val currentUser = auth.currentUser
//        if(currentUser != null){
////            updateUI(currentUser)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDoctorLoginBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        binding.doctorLoginBtnLogin.setOnClickListener {
            if (binding.doctorLoginEtEmail.text.toString().trim().isNotEmpty() &&
                    binding.doctorLoginEtPassword.text.toString().trim().isNotEmpty()){
                signIn(binding.doctorLoginEtEmail.text.toString(),binding.doctorLoginEtPassword.text.toString())
            }else{
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
//                    val editor = sharedPreferences.edit()
//                    editor.putString("doctor", "login")
//                    editor.apply()
////                    updateUI(user)
                    val intent = Intent(requireActivity(), DoctorBottomNavigationBarActivity::class.java)
                    startActivity(intent)
                    Log.d( "Login success","Login success")

                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Login failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    Log.d( "signIn failure", task.exception.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireActivity(),
                    "Login failure Listener.",
                    Toast.LENGTH_SHORT,
                ).show()
                Log.d( "signIn failure Listener", it.toString())
            }
    }

//    private fun updateUI(user: FirebaseUser?) {
//        val doctor = sharedPreferences.getString("doctor", "error")
//        if(doctor!!.equals("login")){
//            val editor = sharedPreferences.edit()
//            editor.putString("userEmail", user!!.email)
//            editor.putString("userId", user.uid)
//            editor.apply()
//            val i = Intent(requireActivity(), DoctorBottomNavigationBarActivity::class.java)
//            i.putExtra("email",user!!.email)
//            i.putExtra("id",user.uid)
//            startActivity(i)
//        }
//        if(doctor!!.equals("register")){
//            val i = Intent(requireActivity(), DoctorLoginFragment::class.java)
//            startActivity(i)
//        }
//    }
}