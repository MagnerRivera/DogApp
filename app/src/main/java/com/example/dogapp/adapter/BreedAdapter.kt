package com.example.dogapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dogapp.R
import com.example.dogapp.databinding.ListBreedsBinding
import com.example.dogapp.retrofit.Breed
import com.squareup.picasso.Picasso

class BreedAdapter(
    private var breeds: List<Breed>,
    private val shareImage: (String, String) -> Unit
) : RecyclerView.Adapter<BreedAdapter.BreedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        val binding = ListBreedsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BreedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        holder.bind(breeds[position])
    }

    override fun getItemCount(): Int = breeds.size

    inner class BreedViewHolder(private val binding: ListBreedsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(breed: Breed) {
            binding.tvBreed.text = breed.name
            Picasso.get()
                .load(breed.imageUrl)
                .placeholder(R.drawable.ic_dog)
                .into(binding.ivBreed)

            binding.ivShare.setOnClickListener {
                shareImage(breed.name, breed.imageUrl)
            }
        }
    }

    fun updateBreeds(newBreeds: List<Breed>) {
        breeds = newBreeds
        notifyDataSetChanged()
    }
}