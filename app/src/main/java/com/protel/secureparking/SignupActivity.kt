package com.protel.secureparking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.protel.secureparking.MainActivity
import com.protel.secureparking.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //init firebase auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        register()
    }

    private fun register(){
        binding.Signup.setOnClickListener{
            val userEmail: String = binding.inputemail.text.toString().trim()
            val userName: String = binding.inputuname.text.toString().trim()
            val userPassword: String = binding.inputpassword.text.toString().trim()

            if(TextUtils.isEmpty(userName)){
                binding.inputuname.error = "Please enter username"
                return@setOnClickListener
            }else if (TextUtils.isEmpty(userEmail)){
                binding.inputemail.error = "Please enter Email"
                return@setOnClickListener
            }else if (TextUtils.isEmpty(userPassword)) {
                binding.inputpassword.error = "Please fill the Password"
                return@setOnClickListener
            }else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                binding.inputemail.error = "Please fill with a valid email"
                return@setOnClickListener
            }else if (userPassword.length < 6) {
                binding.inputpassword.error = "Password have to be 6 character at least"
                return@setOnClickListener
            }

            //ini buat link ke firebase nya
            val ref = FirebaseDatabase.getInstance().getReference("user")
            //ini buat dapetin id nya
            val userId = ref.push().key
            //ini buat database nya namanya apa dan isinya apa
            val user = UserSecure(userId,userEmail, userName, userPassword)
            //ini buat nunjukin teks data berhasil ditambah kalo sukses nge save
            if (userId != null) {
                ref.child(userId).setValue(user).addOnCompleteListener{
                    Toast.makeText(applicationContext,"Registration Success!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            else{
                Toast.makeText(this, "Registration failed, please try again!", Toast.LENGTH_LONG).show()
            }
        }
    }
}