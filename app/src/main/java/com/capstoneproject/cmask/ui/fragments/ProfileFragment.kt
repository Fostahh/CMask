package com.capstoneproject.cmask.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstoneproject.cmask.databinding.FragmentProfileBinding
import com.capstoneproject.cmask.ui.activities.DetailProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference

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
            databaseReference =
                FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
            databaseReference.get().addOnSuccessListener {
                binding.textViewUserName.text = it.child("username").value.toString()
            }

            storageReference = FirebaseStorage.getInstance().reference.child("users/" + mAuth.currentUser?.uid + "/profile.jpg")
            setImageProfile()

            binding.floatingActionButtonUserEdit.setOnClickListener {
                startActivity(Intent(context, DetailProfileActivity::class.java))
            }
        }


    }

    private fun setImageProfile() {
        val userAvatar = storageReference
        userAvatar.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(binding.circleImageViewUserAvatar)
        }
    }

    override fun onResume() {
        super.onResume()
        mAuth = FirebaseAuth.getInstance()
        mAuth.currentUser?.let { firebaseUser ->
            databaseReference =
                FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
            databaseReference.get().addOnSuccessListener {
                binding.textViewUserName.text = it.child("username").value.toString()
            }
        }
    }
}