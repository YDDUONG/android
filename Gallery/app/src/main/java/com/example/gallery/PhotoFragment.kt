package com.example.gallery


import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.android.synthetic.main.gallery_cell.*
import kotlinx.android.synthetic.main.gallery_cell.view.*

/**
 * A simple [Fragment] subclass.
 */
class PhotoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //占位符
        shimmerPhoto.apply {
            setShimmerColor(0x55FFFFFF) //初始颜色
            setShimmerAngle(3) //闪动角度
            startShimmerAnimation() //开始闪动
        }

        //加载图片
        Glide.with(requireContext())
            .load(arguments?.getParcelable<Photo>("PHOTO")?.fullUrl)
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
                    return false.also { shimmerPhoto.stopShimmerAnimation() }
                }

            })
            .into(photoView)
    }


}
