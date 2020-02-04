package com.example.gallery

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.gallery_cell.view.*

//recycleView适配器
class GalleryAdapter: ListAdapter<Photo, MyViewHolder>(diffCallBack) {
    //静态常量 比较器
    object  diffCallBack: DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            //判断是否是同一个对象
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            //内容是否相同
            return oldItem.photoId == newItem.photoId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //加载View
        val holder = MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_cell, parent, false))
        //点击holder的事件，进入第二个图片
        holder.itemView.setOnClickListener {
            Bundle().apply {
                putParcelableArrayList("PHOTO_LIST", ArrayList(currentList))
                putInt("PHOTO_POSITION", holder.adapterPosition)
                holder.itemView.findNavController().navigate(R.id.action_galleryFragment_to_pagerPhotoFragment, this)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val photo = getItem(position)
        //加载图片
        with(holder.itemView) {
            shimmerGalleryLayout.apply {
                setShimmerColor(0x55FFFFFF) //初始颜色
                setShimmerAngle(3) //闪动角度
                startShimmerAnimation() //开始闪动
            }
            textViewUser.text = photo.photoUser
            textViewLikes.text = photo.photoLikes.toString()
            textViewFavorites.text = photo.photoFavorites.toString()
            imageView.layoutParams.height = photo.photoHeight //设置占位符初始高度
        }
//        holder.itemView.shimmerGalleryLayout.apply {
//            setShimmerColor(0x55FFFFFF) //初始颜色
//            setShimmerAngle(3) //闪动角度
//            startShimmerAnimation() //开始闪动
//        }
//        holder.itemView.imageView.layoutParams.height = getItem(position).photoHeight //设置占位符初始高度
        Glide.with(holder.itemView)
            .load(getItem(position).previewUrl)
            .placeholder(R.drawable.ic_photo_black_24dp)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    //失败
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false.also { holder.itemView.shimmerGalleryLayout?.stopShimmerAnimation() }
                }

            })
            .into(holder.itemView.imageView)
    }


}

//MyViewHolder
class  MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)