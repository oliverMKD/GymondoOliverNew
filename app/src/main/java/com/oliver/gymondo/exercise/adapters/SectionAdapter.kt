package com.oliver.gymondo.exercise.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.oliver.gymondo.database.models.ModelExercise
import com.oliver.gymondo.databinding.RecyclerItemBinding

class SectionAdapter(val context: Context, private val interaction: Interaction? = null) :
    PagedListAdapter<ModelExercise, SectionAdapter.DetailsViewHolder>(DeviceDiffCallback()) {

    private class DeviceDiffCallback : DiffUtil.ItemCallback<ModelExercise>() {

        override fun areItemsTheSame(oldItem: ModelExercise, newItem: ModelExercise): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ModelExercise, newItem: ModelExercise): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        return DetailsViewHolder(
            RecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), interaction
        )
    }


    inner class DetailsViewHolder(
        private val binding: RecyclerItemBinding,
        private val interaction: Interaction?
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val clicked = getItem(adapterPosition)
            interaction?.itemClicked(clicked!!)
        }

        fun bind(item: ModelExercise) {
            binding.apply {
                itemExercise = item
                executePendingBindings()
            }
        }
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.apply {
            bind(exercise!!)
        }
    }


    interface Interaction {
        fun itemClicked(modelExercise: ModelExercise)
    }
}


