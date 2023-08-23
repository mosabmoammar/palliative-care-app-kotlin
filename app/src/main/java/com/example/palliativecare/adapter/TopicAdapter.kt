package com.example.palliativecare.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.palliativecare.R
import com.example.palliativecare.model.Topic
import com.example.palliativecare.view.activity.EditTopicActivity
import com.example.palliativecare.view.activity.TopicActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class TopicAdapter(val context: Context, val data: ArrayList<Topic>) :
    RecyclerView.Adapter<TopicAdapter.MyViewHolder>() {

    val db = Firebase.firestore

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.item_topic_img_logo)
        val title = itemView.findViewById<TextView>(R.id.item_topic_tv_name)
        val description = itemView.findViewById<TextView>(R.id.item_topic_tv_textual)
        val btnDisplay = itemView.findViewById<Button>(R.id.item_topic_btn_display)
        val btnEdit = itemView.findViewById<Button>(R.id.item_topic_btn_edit)
        val btnHide = itemView.findViewById<Button>(R.id.item_topic_btn_hide)
        val btnDelete = itemView.findViewById<Button>(R.id.item_topic_btn_delete)

        var imgLogo = itemView.findViewById<ImageView>(R.id.topic_img_logo)
        val tvTitle = itemView.findViewById<TextView>(R.id.topic_tv_name)
        val videoView = itemView.findViewById<VideoView>(R.id.topic_video_view)
        val tvDesc = itemView.findViewById<TextView>(R.id.topic_tv_textual)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.home_item_topic, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val topic = data[position]

        Picasso.get().load(topic.img).into(holder.img)

        holder.title.text = topic.title
        holder.description.text = topic.desc

        holder.btnDisplay.setOnClickListener {
            val db = Firebase.firestore
            db.collection("topics")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val title = document.get("title").toString()
                        if (title == topic.title) {
                            val id = document.id
                            db.collection("topics").document(id)
                                .get()
                                .addOnSuccessListener {
                                    Picasso.get().load(document.data["imageURL"].toString())
                                        .into(holder.imgLogo)
                                    holder.tvTitle.text = document.data["title"].toString()
                                    TopicActivity().videoURL = document.data["videoURL"].toString()
                                    holder.tvDesc.text = document.data["description"].toString()
                                    val intent = Intent(context, TopicActivity::class.java)
                                    context.startActivity(intent)
                                    Log.d("SSSSSSS", "Success loadded")
                                }.addOnFailureListener {
                                    Log.d("FFFFFFF", "$it")
                                }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("ContentValues", "Error getting documents.", exception)
                }
        }

        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, EditTopicActivity::class.java)
            context.startActivity(intent)
        }

        holder.btnHide.setOnClickListener {

        }

        holder.btnDelete.setOnClickListener {
            db.collection("topics")
                .document(topic.id.toString()).delete()

            notifyDataSetChanged()
        }
    }

    fun getidUpdate(topicTitle: String, desc: String, hidden: Boolean) {
        val db = Firebase.firestore
        db.collection("topics")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val title = document.get("title").toString()
                    val desc = document.get("description").toString()
                    if (title == topicTitle) {
                        val id = document.id
                        db.collection("topics").document(id).update("hidden", hidden)
                        val intent = Intent(context, EditTopicActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        context.startActivity(intent)
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.w("ContentValues", "Error getting documents.", exception)
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getidDelete(titleTopic: String, position: Int) {
        val db = Firebase.firestore
        db.collection("topics")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val title = document.get("title").toString()
                    if (title == titleTopic) {
                        val id = document.id
                        db.collection("topics").document(id).delete()
                    }
                }
                data.removeAt(position)
                notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("ContentValues", "Error getting documents.", exception)
            }
    }

}
