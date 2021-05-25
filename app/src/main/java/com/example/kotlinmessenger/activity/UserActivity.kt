package com.example.kotlinmessenger.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.adapter.UserAdapter
import com.example.kotlinmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class UserActivity : AppCompatActivity() {
    var userlist=ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val userRecyclerView:RecyclerView=findViewById(R.id.userRecyclerView)

         userRecyclerView.layoutManager=LinearLayoutManager(this,LinearLayout.VERTICAL,false)
       val imgBack:ImageView=findViewById(R.id.imgBack)
        imgBack.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
//        imgBack.setOnClickListener{
//           onBackPressed()
//        }
        val imgprofile:CircleImageView=findViewById(R.id.imgprofile)
        imgprofile.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

//        userlist.add(User("anh","https://sites.google.com/site/hinhanhdep24h/_/rsrc/1436687439788/home/hinh%20anh%20thien%20nhien%20dep%202015%20%281%29.jpeg"))
//        userlist.add(User("anh","https://www.nhatbanaz.com/wp-content/uploads/3322_pixta_30838512_L-310x205.jpg"))
//        userlist.add(User("anh","https://www.nhatbanaz.com/wp-content/uploads/3322_pixta_30838512_L-310x205.jpg"))

        getUsersList()

    }
    fun getUsersList(){
        var firebase:FirebaseUser=FirebaseAuth.getInstance().currentUser!!
        var databaseReference:DatabaseReference=FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object :ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
     Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userlist.clear()

          val currentUser=snapshot.getValue(User::class.java)

                val imgprofile:CircleImageView=findViewById(R.id.imgprofile)
                if(currentUser!!.profileImage == ""){
                    imgprofile.setImageResource(R.drawable.ic_baseline_person_24)
                }else{
                    Glide.with(this@UserActivity).load(currentUser.profileImage).into(imgprofile)
                }

                for (dataSnapShot:DataSnapshot in snapshot.children){
                    val user=dataSnapShot.getValue(User::class.java)
//#đây là cái để hiển thị tất cả nó ra trừ tài khoản của mình nếu muốn đổi thành tài khoản của mình bỏ dấu ! trước user


                    if(!user!!.userId.equals(firebase.uid)){
                        userlist.add(user)

//                        val tv_profile:TextView =findViewById(R.id.tv_profile)
//                        tv_profile.setText(user!!.userName)
                    }
                }
                val userAdapter=UserAdapter(this@UserActivity,userlist)
                findViewById<RecyclerView>(R.id.userRecyclerView).adapter=userAdapter

            }
        })
    }

}