package com.example.gallery


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_pager_photo.*

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
        viewPager2.setCurrentItem(arguments?.getInt("PHOTO_POSITION")?:0, false)

    }


}
