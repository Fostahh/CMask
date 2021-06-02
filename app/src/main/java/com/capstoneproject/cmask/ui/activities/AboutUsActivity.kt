package com.capstoneproject.cmask.ui.activities

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.capstoneproject.cmask.R
import com.capstoneproject.cmask.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)



        Glide.with(this).load(R.drawable.me).into(binding.imageViewAbout)
        Glide.with(this).load(R.drawable.haura).into(binding.imageViewAbout2)
        Glide.with(this).load(R.drawable.bang_pandu).into(binding.imageViewAbout4)
        Glide.with(this).load(R.drawable.kak_inggrid).into(binding.imageViewAbout3)
        Glide.with(this).load(R.drawable.bang_kevin).into(binding.imageViewAbout5)
        Glide.with(this).load(R.drawable.bang_faisal).into(binding.imageViewAbout6)
    }
}