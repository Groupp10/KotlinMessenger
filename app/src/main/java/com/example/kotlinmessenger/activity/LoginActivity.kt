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

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        firebaseUser=auth.currentUser!!
//        if(firebaseUser!=null){
//            val intent = Intent(this, UserActivity::class.java)
//            startActivity(intent)
//            finish()
//        }


        val btnLogin: Button = findViewById(R.id.btnLogin)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etpassword: EditText = findViewById(R.id.etPassword)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etpassword.text.toString()

            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                Toast.makeText(
                    applicationContext,
                    "Xin lỗi bạn đã nhập sai email hoặc password,mời bạn nhập lại",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            etEmail.setText("")
                            etpassword.setText("")
                            val intent = Intent(this, UserActivity::class.java)
                            startActivity(intent)
                            finish()


                        } else {
                            Toast.makeText(
                                applicationContext,
                                "email hoặc password không hợp lệ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }
        }
        btnSignUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}