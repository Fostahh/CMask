package com.capstoneproject.cmask.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.cmask.databinding.ActivityHistoryBinding
import com.capstoneproject.cmask.model.History
import com.capstoneproject.cmask.ui.adapter.HistoryRecyclerViewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        mAuth.currentUser?.let { firebaseUser ->
            databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid).child("History")

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val histories = ArrayList<History>()
                    for (data in snapshot.children) {
                        val history = data.getValue(History::class.java)
                        Log.d("History Value", history.toString())
                        history?.let {
                            histories.add(it)
                        }
                    }

                    if (histories.size > 0) {
                        binding.apply {
                            historyRecyclerView.layoutManager = LinearLayoutManager(this@HistoryActivity)
                            historyRecyclerView.setHasFixedSize(true)

                            val adapter = HistoryRecyclerViewAdapter()
                            adapter.setHistories(histories)
                            historyRecyclerView.adapter = adapter
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
    }
}