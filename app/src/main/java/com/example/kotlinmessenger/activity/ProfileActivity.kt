package com.example.kotlinmessenger.activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.model.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var firebaseUser:FirebaseUser
    private lateinit var firebaseReference:DatabaseReference

    private var filePath:Uri?=null

    private val PICK_THAGE_REQUEST:Int=2020

    private lateinit var storage:FirebaseStorage
    private lateinit var storageRef:StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseUser=FirebaseAuth.getInstance().currentUser!!
        firebaseReference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        storage=FirebaseStorage.getInstance()
        storageRef=storage.reference

        firebaseReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
             Toast.makeText(applicationContext,error.message,Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
               val user=snapshot.getValue(User::class.java)
                val etuserName:EditText=findViewById(R.id.etuserName)
                val imgprofile:CircleImageView=findViewById(R.id.imgprofile)
                etuserName.setText(user!!.userName)

                if(user.profileImage == ""){
                    imgprofile.setImageResource(R.drawable.ic_baseline_person_24)
                }else{
                    Glide.with(this@ProfileActivity).load(user.profileImage).into(imgprofile)
                }
            }
        })

        val imgBack: ImageView =findViewById(R.id.imgBack)
        imgBack.setOnClickListener{
            onBackPressed()
        }

      findViewById<CircleImageView>(R.id.userImage).setOnClickListener{
          chooseImage()
      }
        findViewById<Button>(R.id.btnSave).setOnClickListener{
            uploadImage()
            findViewById<Button>(R.id.btnSave).visibility=View.VISIBLE
        }
//        val imgprofile: CircleImageView =findViewById(R.id.imgprofile)
//        imgprofile.setOnClickListener{
//            val intent = Intent(this, UserActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }
private fun chooseImage(){
    val intent:Intent=Intent()
    intent.type="image/"
    intent.action=Intent.ACTION_GET_CONTENT
    startActivityForResult(Intent.createChooser(intent,"select Image"),PICK_THAGE_REQUEST)
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_THAGE_REQUEST && resultCode !=null){
            filePath=data!!.data

            try{
                var bitmap:Bitmap=MediaStore.Images.Media.getBitmap(contentResolver,filePath)
        findViewById<CircleImageView>(R.id.userImage).setImageBitmap(bitmap)
                findViewById<Button>(R.id.btnSave).visibility=View.VISIBLE
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }
    private fun uploadImage(){
        if(filePath !=null){


            var ref:StorageReference=storageRef.child("image/"+UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener {

                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap.put("userName",findViewById<EditText>(R.id.etuserName).text.toString())
                    hashMap.put("profileImage",filePath.toString())
                    firebaseReference.updateChildren(hashMap as Map<String,Any>)

                    findViewById<ProgressBar>(R.id.progressBar).visibility=View.GONE
                        Toast.makeText(applicationContext,"Uploaded",Toast.LENGTH_SHORT).show()
                        findViewById<Button>(R.id.btnSave).visibility=View.GONE
                    }

                .addOnFailureListener{

                        findViewById<Button>(R.id.btnSave).visibility=View.GONE
                        Toast.makeText(applicationContext,"Failed"+it.message,Toast.LENGTH_SHORT).show()

                }


        }
    }
}