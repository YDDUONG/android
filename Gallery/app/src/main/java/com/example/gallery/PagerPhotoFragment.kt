package com.example.gallery


import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_pager_photo.*
import kotlinx.android.synthetic.main.pager_photo_view.view.*
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 */
class PagerPhotoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_photo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val photoList = arguments?.getParcelableArrayList<Photo>("PHOTO_LIST") //获取传过来的数据
        //加载资源
        PagerPhotoAdpter().apply {
            viewPager2.adapter = this
            submitList(photoList)
        }

        //图片选中事件
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                photoTag.text = "${position + 1}/${photoList?.size}"
            }
        })

        //当前图片
        viewPager2.setCurrentItem(arguments?.getInt("PHOTO_POSITION") ?: 0, false)

        //保存图片事件, 动态申请权限
        saveButton.setOnClickListener {
            if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) { //没有权限，弹出一个请求权限对话框
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            } else { //已有权限，处理事件
                viewLifecycleOwner.lifecycleScope.launch {
                    //lifecycleScope使协程的生命周期随着activity的生命周期变化
                    savePhoto()
                }
            }
        }
    }

    //请求权限对话框处理
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //处理返回结果
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //权限通过，执行事件
                    viewLifecycleOwner.lifecycleScope.launch {
                        //lifecycleScope使协程的生命周期随着activity的生命周期变化
                        savePhoto()
                    }
                } else {
                    //用户不允许使用该权限，失败提示
                    Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //协程处理保存图片
    private suspend fun savePhoto() { //suspend:在另外的线程允许挂起
        withContext(Dispatchers.IO) {
            //定义线程的范围
            //获取图像的位图资源
            val holder: PagerPhotoViewHolder =
                (viewPager2[0] as RecyclerView).findViewHolderForAdapterPosition(viewPager2.currentItem) as PagerPhotoViewHolder
            val bitmap: Bitmap = holder.itemView.pagerPhoto.drawable.toBitmap() //可以传入图片的大小，默认是显示的图片

            //设置保存地址
            val saveUri: Uri = requireContext().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            ) ?: kotlin.run {
                Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show()
                return@withContext
            }

            delay(5000) //延时测试，5秒之后保存文件

            //保存图片
            requireContext().contentResolver.openOutputStream(saveUri).use {
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)) { //90%的压缩率，it是输出流
                    //在主线程提示用户
                    MainScope().launch {
                        Toast.makeText(
                            requireContext(),
                            "存储成功",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    MainScope().launch {
                        Toast.makeText(
                            requireContext(),
                            "存储失败",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
    }


}
