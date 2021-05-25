package com.example.kotlinmessenger.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.kotlinmessenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val btnSignUp:Button=findViewById(R.id.btnSignUp)
        val etName:EditText=findViewById(R.id.etName)
        val etEmail:EditText=findViewById(R.id.etEmail)
        val etpassword:EditText=findViewById(R.id.etPassword)
        val etConfirmpassword:EditText=findViewById(R.id.etConfirmPassword)
        val btnLogin:Button=findViewById(R.id.btnLogins)

        auth=FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener{
            val userName=etName.text.toString()
            val email=etEmail.text.toString()
            val password=etpassword.text.toString()
            val confirmPassword=etConfirmpassword.text.toString()

            if(TextUtils.isEmpty(userName)){
                Toast.makeText(applicationContext,"nhập tên người dùng là cần thiết ",Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"email là cần thiết ",Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext,"password là cần thiết ",Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(confirmPassword)){
                Toast.makeText(applicationContext,"confirmPassword là cần thiết ",Toast.LENGTH_SHORT).show()
            }

            if(password != confirmPassword){
               Toast.makeText(applicationContext,"nhập lại mật khẩu không đúng mời bạn nhập lại",Toast.LENGTH_SHORT).show()
            }
            registerUser(userName,email,password)
        }
    btnLogin.setOnClickListener{
        val intent=Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    }


    private fun registerUser(username:String,email:String,password:String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    val user:FirebaseUser?=auth.currentUser
                    val userId:String=user!!.uid

                    databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap.put("userId",userId)
                    hashMap.put("userName",username)
                    hashMap.put("profileImage","")

                    databaseReference.setValue(hashMap).addOnCompleteListener(this){
                        if(it.isSuccessful){


                            findViewById<EditText>(R.id.etName).setText("")
                            findViewById<EditText>(R.id.etPassword).setText("")
                            findViewById<EditText>(R.id.etConfirmPassword).setText("")
                            findViewById<EditText>(R.id.etEmail).setText("")

                            val intent=Intent(this, ProfileActivity::class.java)
                            startActivity(intent)
                             finish()

                        }
                    }


                }
            }

    }
}