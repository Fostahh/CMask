package com.capstoneproject.cmask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.cmask.R
import com.capstoneproject.cmask.databinding.ItemHistoryBinding
import com.capstoneproject.cmask.model.History
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class HistoryRecyclerViewAdapter : RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>() {

    private var listHistory = ArrayList<History>()

    fun setHistories(histores: List<History>?) {
        if (histores == null) return
        this.listHistory.clear()
        this.listHistory.addAll(histores)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.apply {
                textViewHistoryAccuracyValue.text = "${history.nilaiAkurat}"
                textViewHistoryTitle.text = history.feature
                val timestamp = binding.root.resources.getString(R.string.time_stamp, history.date, history.time)
                textViewHistoryTimeStamp.text = timestamp
                history.photo?.let {
                    Picasso.get().load(it).into(binding.imageView)
                } ?: run {
                    val dateSplit = history.date?.split("/")?.joinToString("")
                    val timeSplit = history.time?.split(":")?.joinToString("")
                    val storageReference = FirebaseStorage.getInstance().reference.child("users/" + FirebaseAuth.getInstance().currentUser?.uid + "/history/${dateSplit}_${timeSplit}.jpg")
                    storageReference.downloadUrl.addOnSuccessListener {
                        Picasso.get().load(it).into(binding.imageView)
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = listHistory[position]
        holder.bind(history)
    }

    override fun getItemCount(): Int = listHistory.size
}