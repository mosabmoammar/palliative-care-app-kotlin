package com.example.palliativecare.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.palliativecare.R
import com.example.palliativecare.adapter.UserAdapter
import com.example.palliativecare.databinding.FragmentChatBinding
import com.example.palliativecare.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.database.*
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ChatFragment : Fragment() {

    var userList = ArrayList<User>()
    private lateinit var binding: FragmentChatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun getUsersList() {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        var userid = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")


        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users")


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUser = snapshot.getValue(User::class.java)
                if (currentUser!!.profileImage == "") {
                    binding.doctorChatImgProfile.setImageResource(R.drawable.ic_launcher_background)
                } else {
                    Picasso.get().load(currentUser.profileImage).into(binding.doctorChatImgProfile)
                }

                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(User::class.java)

                    if (!user!!.userId.equals(firebase.uid)) {

                        userList.add(user)
                    }
                }

                val userAdapter = UserAdapter(requireActivity(), userList)

                binding.doctorChatRecyclerView.adapter = userAdapter
            }

        })
    }
}