package com.capstoneproject.cmask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.cmask.databinding.ItemRowBinding
import com.capstoneproject.cmask.model.History
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class HomeRecyclerViewAdapter: RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    private var listHistory = ArrayList<History>()

    fun setHistories(histores: List<History>?) {
        if (histores == null) return
        this.listHistory.clear()
        this.listHistory.addAll(histores)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.apply {
                history.photoUrl?.let {
                    Picasso.get().load(it).into(binding.imageViewHistoryHome)
                } ?: run {
                    val dateSplit = history.date?.split("/")?.joinToString("")
                    val timeSplit = history.time?.split(":")?.joinToString("")
                    val storageReference = FirebaseStorage.getInstance().reference.child("users/" + FirebaseAuth.getInstance().currentUser?.uid + "/history/${dateSplit}_${timeSplit}.jpg")
                    storageReference.downloadUrl.addOnSuccessListener {
                        Picasso.get().load(it).into(binding.imageViewHistoryHome)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = listHistory[position]
        holder.bind(history)
    }

    override fun getItemCount(): Int = listHistory.size
}