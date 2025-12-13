package com.shubhamjitseekho.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.shubhamjitseekho.R
import com.shubhamjitseekho.databinding.ItemAnimeCardBinding
import com.shubhamjitseekho.domain.model.Anime

class AnimeListAdapter(
    private val onItemClick: (Anime) -> Unit
) : ListAdapter<Anime, AnimeListAdapter.AnimeViewHolder>(AnimeDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnimeViewHolder(binding, onItemClick)
    }
    
    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class AnimeViewHolder(
        private val binding: ItemAnimeCardBinding,
        private val onItemClick: (Anime) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(anime: Anime) {
            binding.apply {
                tvAnimeTitle.text = anime.title
                tvAnimeScore.text = anime.formattedScore
                tvAnimeEpisodes.text = anime.formattedEpisodes
                
                Glide.with(itemView.context)
                    .load(anime.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_play)
                    .error(R.drawable.ic_play)
                    .into(binding.ivAnimeImage)
                
                root.setOnClickListener {
                    onItemClick(anime)
                }
            }
        }
    }
    
    private class AnimeDiffCallback : DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem == newItem
        }
    }
}