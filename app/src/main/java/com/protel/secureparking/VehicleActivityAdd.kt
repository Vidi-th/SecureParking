package com.protel.secureparking

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.protel.secureparking.databinding.ActivitySignupBinding
import com.protel.secureparking.databinding.ActivityVehicleAddBinding
import java.util.*

class VehicleActivityAdd : AppCompatActivity() {
    private lateinit var binding: ActivityVehicleAddBinding
    private lateinit var auth: FirebaseAuth

    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleAddBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Data")

        binding.submitData.setOnClickListener {
            val name = binding.namaPengguna.text.toString().trim()
            val plat = binding.platNomor.text.toString().trim()

            if (name.isEmpty()){
                binding.namaPengguna.error = "Please Enter Your Name..."
                binding.namaPengguna.requestFocus()
                return@setOnClickListener
            }else if(plat.isEmpty()){
                binding.platNomor.error = "Please Enter Your Plat Number..."
                binding.platNomor.requestFocus()
                return@setOnClickListener
            }
            uploadImageToFirebaseStorage(name, plat)
//            registerData(name, plat)

        }
        binding.fotoWajah.setOnClickListener {
            Log.d("VehicleActivityAdd","try to show face photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        binding.fotoStnk.setOnClickListener{
            Log.d("VehicleActivityAdd","try to show STNK selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
    }

    var selectedPhotoUri: Uri? = null
    var selectedPhotoSTNK: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            //proceed and check what the selected image was...
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
        }
        else if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoSTNK = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoSTNK)
        }
    }

    private fun registerData(name: String, plat: String, Photourl: String, Stnkurl: String) {
        //ini buat link ke firebase nya
        val ref = FirebaseDatabase.getInstance().getReference("data")
        //ini buat dapetin id nya
        val dataId = ref.push().key
        //ini buat database nya namanya apa dan isinya apa
        val data = DataSecure(dataId, name, plat, Photourl, Stnkurl)
        //ini buat nunjukin teks data berhasil ditambah kalo sukses nge save
        if (dataId != null) {
            ref.child(dataId).setValue(data).addOnCompleteListener{
                Toast.makeText(applicationContext,"Upload Data Success!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, VehicleActivity::class.java)
                startActivity(intent)

            }
        }
        else{
            Toast.makeText(this, "Upload Data failed, please try again!", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadImageToFirebaseStorage(name: String, plat: String) {
        if(selectedPhotoUri == null) return
        if(selectedPhotoSTNK == null) return
        val ref = FirebaseStorage.getInstance().getReference("/images/Photo/$name")
        val ref1 = FirebaseStorage.getInstance().getReference("/images/STNK/$name")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("VehicleActivityAdd","Successfully upload image")
                ref.downloadUrl.addOnSuccessListener {
                    var Photourl = it.toString()
                    ref1.putFile(selectedPhotoSTNK!!)
                        .addOnSuccessListener {
                            Log.d("VehicleActivityAdd","Successfully upload STNK")
                            ref1.downloadUrl.addOnSuccessListener {
                                registerData(name,plat,Photourl,it.toString())
                            }
                        }
                }
            }

    }

}