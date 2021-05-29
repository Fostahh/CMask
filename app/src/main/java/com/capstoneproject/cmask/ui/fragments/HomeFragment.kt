package com.capstoneproject.cmask.ui.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.capstoneproject.cmask.R
import com.capstoneproject.cmask.databinding.FragmentHomeBinding
import com.capstoneproject.cmask.ui.activities.AboutUsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

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
            databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
            databaseReference.get().addOnSuccessListener {
                val textUsername = resources.getString(R.string.hi_name, it.child("username").value)
                binding.textViewHomeUser.text = textUsername
            }
        }

        binding.webView.loadUrl("http://35.219.90.227/backend/index.php/chart")

        binding.imageButtonAboutUs.setOnClickListener {
            startActivity(Intent(context, AboutUsActivity::class.java))
        }
    }
}