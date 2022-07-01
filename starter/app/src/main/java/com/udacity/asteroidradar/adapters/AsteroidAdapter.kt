package com.udacity.asteroidradar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.adapters.AsteroidAdapter.AsteroidListItemViewHolder
import com.udacity.asteroidradar.databinding.AsteroidItemViewBinding
import com.udacity.asteroidradar.data.models.Asteroid

class AsteroidAdapter(val clickListener: AsteroidClickListener): RecyclerView.Adapter<AsteroidListItemViewHolder>() {
    var data = listOf<Asteroid>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    class AsteroidListItemViewHolder(private val binding: AsteroidItemViewBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Asteroid, clickListener: AsteroidClickListener){
            binding.asteroid = item
            binding.clickListener = clickListener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidListItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = AsteroidItemViewBinding.inflate(layoutInflater,  parent, false)
        return AsteroidListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: AsteroidListItemViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, clickListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class AsteroidClickListener(val clickListener:(asteroid: Asteroid) -> Unit){
        fun OnClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}