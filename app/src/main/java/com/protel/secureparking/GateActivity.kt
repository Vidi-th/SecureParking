package com.protel.secureparking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.protel.secureparking.databinding.ActivityGateBinding

class GateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGateBinding
    private lateinit var dbref : DatabaseReference
    private lateinit var dataRecyclerview : RecyclerView
    private lateinit var dataArrayList : ArrayList<GateRead>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.back.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        dataRecyclerview = findViewById(R.id.review)
        dataRecyclerview.layoutManager = LinearLayoutManager(this)
        dataRecyclerview.setHasFixedSize(true)

        dataArrayList = arrayListOf<GateRead>()
        getData()
    }

    private fun getData() {
        dbref = FirebaseDatabase.getInstance().getReference("gate")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(GateRead::class.java)
                        dataArrayList.add(user!!)
                    }
                    dataRecyclerview.adapter = MyGateAdapter(dataArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}