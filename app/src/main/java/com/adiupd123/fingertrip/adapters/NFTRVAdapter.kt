package com.adiupd123.fingertrip.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adiupd123.fingertrip.databinding.NftItemLayoutBinding
import com.adiupd123.fingertrip.models.OpenSeaAsset
import com.bumptech.glide.Glide

class NFTRVAdapter(private val onNFTItemClicked: (OpenSeaAsset) -> Unit) :
androidx.recyclerview.widget.ListAdapter<OpenSeaAsset, NFTRVAdapter.NFTViewHolder>(
ComparatorDiffUtil()
) {

    inner class NFTViewHolder(private val binding: NftItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(nftItem: OpenSeaAsset) {
            Glide.with(binding.nftImageView)
                .load(nftItem.imageUrl)
                .into(binding.nftImageView)
            binding.root.setOnClickListener {
                onNFTItemClicked(nftItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NFTViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NftItemLayoutBinding.inflate(inflater, parent, false)
        return NFTViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OpenSeaAsset, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<OpenSeaAsset>() {
        override fun areItemsTheSame(
            oldItem: OpenSeaAsset,
            newItem: OpenSeaAsset
        ): Boolean {
            return oldItem.tokenId == newItem.tokenId
        }

        override fun areContentsTheSame(
            oldItem: OpenSeaAsset,
            newItem: OpenSeaAsset
        ): Boolean {
            return oldItem == newItem
        }

    }
}