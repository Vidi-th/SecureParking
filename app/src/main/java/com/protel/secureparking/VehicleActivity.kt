package com.protel.secureparking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.protel.secureparking.databinding.ActivityVehicleBinding
import android.content.Intent
import android.provider.ContactsContract
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class VehicleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVehicleBinding
    private lateinit var dbref : DatabaseReference
    private lateinit var dataRecyclerview : RecyclerView
    private lateinit var dataArrayList : ArrayList<DataRead>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.back.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        binding.tambahData.setOnClickListener {
            val intent = Intent(this,VehicleActivityAdd::class.java)
            startActivity(intent)
        }

        dataRecyclerview = findViewById(R.id.rview)
        dataRecyclerview.layoutManager = LinearLayoutManager(this)
        dataRecyclerview.setHasFixedSize(true)

        dataArrayList = arrayListOf<DataRead>()
        getData()
    }

    private fun getData() {
        dbref = FirebaseDatabase.getInstance().getReference("data")
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(DataRead::class.java)
                        dataArrayList.add(user!!)
                    }
                    dataRecyclerview.adapter = MyAdapter(dataArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}