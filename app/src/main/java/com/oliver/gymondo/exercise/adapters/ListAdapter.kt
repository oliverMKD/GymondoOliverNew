package com.oliver.gymondo.exercise.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.oliver.gymondo.database.models.ExerciseImage
import com.oliver.gymondo.databinding.ImagesItemBinding

class ListAdapter : androidx.recyclerview.widget.ListAdapter<
        ExerciseImage, ListAdapter.ViewHolder>(CarDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.apply {
            bind(exercise)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ImagesItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class ViewHolder(private val binding: ImagesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ExerciseImage) {
            binding.apply {
                image = item
                executePendingBindings()
            }
        }
    }
}

private class CarDiffCallback : DiffUtil.ItemCallback<ExerciseImage>() {

    override fun areItemsTheSame(oldItem: ExerciseImage, newItem: ExerciseImage): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ExerciseImage, newItem: ExerciseImage): Boolean {
        return oldItem == newItem
    }
}