package com.example.palliativecare.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.palliativecare.R
import com.example.palliativecare.adapter.ChatAdapter
import com.example.palliativecare.databinding.ActivityDoctorChattingBinding
import com.example.palliativecare.model.Chat
import com.example.palliativecare.model.NotificationData
import com.example.palliativecare.model.PushNotification
import com.example.palliativecare.model.User
import com.example.palliativecare.retrofit.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DoctorChattingActivity : AppCompatActivity() {

    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var chatList = ArrayList<Chat>()
    var topic = ""
    private lateinit var binding: ActivityDoctorChattingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_chatting)
        binding = ActivityDoctorChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.doctorChattingRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


        var intent = getIntent()
        var userId = intent.getStringExtra("userId")
        var userName = intent.getStringExtra("userName")


        binding.doctorChattingImgBack.setOnClickListener {
            onBackPressed()
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)




        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)
                binding.doctorChattingTvUserName.text = user!!.userName
                if (user.profileImage == "") {
                    binding.doctorChattingImgProfile.setImageResource(R.drawable.ic_launcher_background)
                } else {
                    Picasso.get().load(user.profileImage).into(binding.doctorChattingImgProfile)
                }
            }
        })

        binding.doctorChattingBtnSendMessage.setOnClickListener {
            var message: String = binding.doctorChattingEtMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                binding.doctorChattingEtMessage.setText("")
            } else {
                sendMessage(firebaseUser!!.uid, userId, message)
                binding.doctorChattingEtMessage.setText("")
                topic = "/topics/$userId"
                PushNotification(
                    NotificationData( userName!!,message),
                    topic).also {
                    sendNotification(it)
                }

            }
        }

        readMessage(firebaseUser!!.uid, userId)
    }

    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)

        reference!!.child("Chat").push().setValue(hashMap)

    }

    fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                    ) {
                        chatList.add(chat)
                    }
                }

                val chatAdapter = ChatAdapter(this@DoctorChattingActivity, chatList)

                binding.doctorChattingRecyclerView.adapter = chatAdapter
            }
        })
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("TAG", response.errorBody()!!.string())
            }
        } catch(e: Exception) {
            Log.e("TAG", e.toString())
        }
    }
}