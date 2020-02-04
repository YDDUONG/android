package com.example.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.pager_photo_view.view.*

class PagerPhotoAdpter: ListAdapter<Photo, PagerPhotoViewHolder>(DiffCallBack) {
    object DiffCallBack:DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.photoId == newItem.photoId
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerPhotoViewHolder {
        //选择加载的View
        LayoutInflater.from(parent.context).inflate(R.layout.pager_photo_view, parent, false).apply {
            return PagerPhotoViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: PagerPhotoViewHolder, position: Int) {
        //加载图片
        Glide.with(holder.itemView)
            .load(getItem(position).fullUrl)
            .placeholder(R.drawable.ic_photo_black_24dp)
            .into(holder.itemView.pagerPhoto)
    }
}

class PagerPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)