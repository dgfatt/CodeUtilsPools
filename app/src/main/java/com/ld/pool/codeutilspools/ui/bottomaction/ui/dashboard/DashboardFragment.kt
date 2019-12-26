package com.ld.pool.codeutilspools.ui.bottomaction.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ld.pool.codeutilspools.R
import com.ld.pool.codeutilspools.adapter.FloatingTabAdapter
import com.ld.pool.data.bean.BannerBean
import com.ld.pool.data.bean.FloatingTabMultipleBean


class DashboardFragment : Fragment() {

    private var recyclerView: RecyclerView? = null

    private lateinit var dashboardViewModel: DashboardViewModel
    private var data: MutableList<FloatingTabMultipleBean>? = null
    private lateinit var images: MutableList<BannerBean>
    private lateinit var adapter: FloatingTabAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        initView(root)
        initData()
        return root
    }

    private fun initView(root: View?) {
        data = mutableListOf()
        recyclerView = root?.findViewById(R.id.recycler_view)
        recyclerView?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = FloatingTabAdapter(data)
        recyclerView?.adapter = adapter
    }

    private fun initData() {
        images = mutableListOf()
        val urls = resources.getStringArray(R.array.url)
        for (url in urls) {
            val bannerBean = BannerBean()
            bannerBean.url = url
            images.add(bannerBean)
        }
        val multipleBean = FloatingTabMultipleBean()
        multipleBean.itemType = FloatingTabMultipleBean.ITEM_TYPE_BANNER
        multipleBean.bannerListBean = images
        data?.add(multipleBean)

        adapter.notifyDataSetChanged()
    }

}
