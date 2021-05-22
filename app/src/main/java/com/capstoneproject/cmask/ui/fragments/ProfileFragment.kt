package com.capstoneproject.cmask.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstoneproject.cmask.R
import com.capstoneproject.cmask.databinding.FragmentProfileBinding
import com.capstoneproject.cmask.ui.activities.DetailProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mAuth.currentUser?.let { firebaseUser ->
            databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
            databaseReference.get().addOnSuccessListener {
                binding.textViewUserName.text = it.child("username").value.toString()
            }
        }

        binding.floatingActionButtonUserEdit.setOnClickListener {
            startActivity(Intent(context, DetailProfileActivity::class.java))
        }
    }
}