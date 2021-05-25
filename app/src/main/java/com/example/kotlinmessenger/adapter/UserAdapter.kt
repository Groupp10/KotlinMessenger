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
import com.example.kotlinmessenger.activity.Mess_MainActivity
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.model.User
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private val context: Context,private val userList:ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.viewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
    val view=LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
  return viewHolder(view)
       }


    override fun onBindViewHolder(holder: viewHolder, position: Int) {
      val user =userList[position]
        //khong hieu 2
        holder.txtUserName.text=user.userName
        Glide.with(context).load(user.profileImage).placeholder(R.drawable.fffff).into(holder.imageUser)

        holder.layoutUser.setOnClickListener{
            val intent=Intent(context, Mess_MainActivity::class.java)
            intent.putExtra("userId",user.userId)
            context.startActivity(intent)
        }

   ///2
    }

    override fun getItemCount(): Int {
       return userList.size
    }

    class viewHolder(view: View):RecyclerView.ViewHolder(view){
        // khoong hieu cho findview
    val txtUserName:TextView=view.findViewById(R.id.userName)
  // val txtTemp:TextView=view.findViewById(R.id.temp)
        val imageUser:CircleImageView=view.findViewById(R.id.userImage)
        val layoutUser:LinearLayout=view.findViewById(R.id.layoutUser)
    }
}