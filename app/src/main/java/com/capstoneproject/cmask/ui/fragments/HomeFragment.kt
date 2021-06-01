package com.capstoneproject.cmask.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.cmask.databinding.FragmentHomeBinding
import com.capstoneproject.cmask.model.History
import com.capstoneproject.cmask.ui.activities.AboutUsActivity
import com.capstoneproject.cmask.ui.activities.HistoryActivity
import com.capstoneproject.cmask.ui.adapter.HomeRecyclerViewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReference2: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private val histories = ArrayList<History>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mAuth.currentUser?.let { firebaseUser ->
            databaseReference =
                FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
            databaseReference.get().addOnSuccessListener {
                binding.textViewHomeUser.text = "Hi, ${it.child("username").value}"
            }
        }

        binding.imageButtonAboutUs.setOnClickListener {
            startActivity(Intent(context, AboutUsActivity::class.java))
        }

        binding.webView.loadUrl("http://35.219.90.227/backend/index.php/chart")

        mAuth.currentUser?.let { currentUser ->
            databaseReference2 = FirebaseDatabase.getInstance().reference.child("Users").child(currentUser.uid)
                    .child("History")
            databaseReference2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val history = data.getValue(History::class.java)
                        history?.let {
                            histories.add(it)
                        }
                    }

                    if (histories.size > 0) {
                        binding.apply {
                            textViewSeeDetail.visibility = View.VISIBLE
                            homeRecyclerView.visibility = View.VISIBLE
                            homeRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            homeRecyclerView.setHasFixedSize(true)

                            val adapter = HomeRecyclerViewAdapter()
                            adapter.setHistories(histories)
                            homeRecyclerView.adapter = adapter
                        }
                    } else {
                        binding.linearLayoutAnnoncement.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })
        }

        binding.textViewSeeDetail.setOnClickListener {
            startActivity(Intent(activity, HistoryActivity::class.java))
        }
    }
}