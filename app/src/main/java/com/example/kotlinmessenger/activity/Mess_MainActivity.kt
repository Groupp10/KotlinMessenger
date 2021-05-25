package com.example.kotlinmessenger.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.adapter.ChatAdapter
import com.example.kotlinmessenger.adapter.UserAdapter
import com.example.kotlinmessenger.model.Chat
import com.example.kotlinmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class Mess_MainActivity : AppCompatActivity() {
    var firebaseUser:FirebaseUser?=null
    var reference:DatabaseReference?=null
    var chatList=ArrayList<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mess__main)

        val chatRecyclerView: RecyclerView =findViewById(R.id.chatRecyclerView)
        chatRecyclerView.layoutManager= LinearLayoutManager(this,LinearLayout.VERTICAL,false)

                var intent=getIntent()
                var userId=intent.getStringExtra("userId")

        val imgBack: ImageView =findViewById(R.id.imgBack)
        imgBack.setOnClickListener{
            onBackPressed()
        }

                firebaseUser=FirebaseAuth.getInstance().currentUser
                reference=FirebaseDatabase.getInstance().getReference("Users").child(userId!!)



                reference!!.addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {

                        val user=snapshot.getValue(User::class.java)
                        val tvUserName:TextView=findViewById(R.id.tvUserName)

                         tvUserName.text=user!!.userName
                        val imgprofile: CircleImageView =findViewById(R.id.imgprofile)
                        if(user.profileImage == ""){
                            imgprofile.setImageResource(R.drawable.ic_baseline_person_24)
                        }else{
                            Glide.with(this@Mess_MainActivity).load(user.profileImage).into(imgprofile)
                        }
                    }
                })

        findViewById<ImageButton>(R.id.btnSendMessage).setOnClickListener {

            var message: String = findViewById<EditText>(R.id.etMessage).text.toString()

            if (message.isEmpty()) {
            Toast.makeText(applicationContext, "message is ",Toast.LENGTH_SHORT).show()
                findViewById<EditText>(R.id.etMessage).setText("")
        }else{
            sendMessage(firebaseUser!!.uid,userId,message)
          findViewById<EditText>(R.id.etMessage).setText("")
            }
        }
        readMessage(firebaseUser!!.uid,userId)
            }

    private fun sendMessage(senderId:String,receiverId:String,message:String){
        var reference:DatabaseReference?=FirebaseDatabase.getInstance().getReference()

        var hashMap:HashMap<String,String> = HashMap()
        hashMap.put("senderId",senderId)
        hashMap.put("receiverId",receiverId)
        hashMap.put("message",message)
        reference!!.child("Chat").push().setValue(hashMap)
   }
    fun readMessage(senderId: String,receiverId: String){
        var firebase:FirebaseUser=FirebaseAuth.getInstance().currentUser!!
        var databaseReference:DatabaseReference=FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
              chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)
//#đây là cái để hiển thị tất cả nó ra trừ tài khoản của mình nếu muốn đổi thành tài khoản của mình bỏ dấu ! trước user

                    //    if(chat!!.senderId.equals(sendMessage(senderId = String()))){
                    //}

                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                            chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)) {
                        chatList.add(chat)
                    }
                }
                val chatAdapter = ChatAdapter(this@Mess_MainActivity, chatList)
                findViewById<RecyclerView>(R.id.chatRecyclerView).adapter = chatAdapter
            }
            })


        }
        }
