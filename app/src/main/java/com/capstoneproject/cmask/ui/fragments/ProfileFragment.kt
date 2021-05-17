package com.capstoneproject.cmask.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstoneproject.cmask.databinding.FragmentProfileBinding
import com.capstoneproject.cmask.ui.activities.DetailProfileActivity

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.circleImageViewUserProfilePicture.setOnClickListener {
            startActivity(Intent(context, DetailProfileActivity::class.java))
        }
    }
}