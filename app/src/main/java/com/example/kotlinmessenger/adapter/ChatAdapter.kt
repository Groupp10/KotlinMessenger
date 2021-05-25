package com.example.kotlinmessenger.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.activity.Mess_MainActivity
import com.example.kotlinmessenger.model.Chat
import com.example.kotlinmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(private val context: Context, private val chatList:ArrayList<Chat>) :
        RecyclerView.Adapter<ChatAdapter.viewHolder>() {

        private val MESSAGE_TYPE_LEFT=0
        private val MESSAGE_TYPE_RIGHT=1

       var firebaseUser: FirebaseUser? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        if (viewType == MESSAGE_TYPE_RIGHT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_right, parent, false)
            return viewHolder(view)
        }else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false)
            return viewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ChatAdapter.viewHolder, position: Int) {
        val chat =chatList[position]
        //khong hieu 2
        holder.txtUserName.text=chat.message
   //     Glide.with(context).load(user.profileImage).placeholder(R.drawable.fffff).into(holder.imageUser)

    }

    override fun getItemCount(): Int {
        return chatList.size
    }
    class viewHolder(view: View):RecyclerView.ViewHolder(view){
        // khoong hieu cho findview
        val txtUserName: TextView =view.findViewById(R.id.tvMessage)

    //    val txtTemp: TextView =view.findViewById(R.id.temp)
      //  val imageUser: CircleImageView =view.findViewById(R.id.userImage)
       // val layoutUser: LinearLayout =view.findViewById(R.id.layoutUser)
    }

    override fun getItemViewType(position: Int): Int {
      firebaseUser=FirebaseAuth.getInstance().currentUser
      if(chatList[position].senderId==firebaseUser!!.uid){
          return MESSAGE_TYPE_RIGHT
      }else{
           return MESSAGE_TYPE_LEFT
      }
    }
}