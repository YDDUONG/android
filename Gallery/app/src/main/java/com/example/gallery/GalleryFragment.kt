package com.example.gallery


import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_gallery.*

/**
 * A simple [Fragment] subclass.
 */
class GalleryFragment : Fragment() {
    private lateinit var  galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    //主体
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true) //显示菜单

        val galleryAdapter = GalleryAdapter() //声明适配器

        recycleView.apply {
            adapter = galleryAdapter //为recycleView添加适配器
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) //添加布局
        }

        //ViewModel
        galleryViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(GalleryViewModel::class.java)
        galleryViewModel.photoListLive.observe(this, Observer {
            galleryAdapter.submitList(it)
            swipeGalleryLayout.isRefreshing = false //关闭刷新
        })

        galleryViewModel.photoListLive.value?:galleryViewModel.fetchData()

        //下拉
        swipeGalleryLayout.setOnRefreshListener {
            galleryViewModel.fetchData()
        }
    }

    //添加菜单
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    //菜单功能
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.swipeIndicator -> {
                swipeGalleryLayout.isRefreshing = true
                Handler().postDelayed(Runnable {galleryViewModel.fetchData()}, 1000) //添加延时
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
