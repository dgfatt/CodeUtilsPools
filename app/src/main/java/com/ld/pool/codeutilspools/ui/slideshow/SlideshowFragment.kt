package com.ld.pool.codeutilspools.ui.slideshow

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
import com.ld.pool.codeutilspools.adapter.StepProgressAdapter

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private var data: MutableList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProviders.of(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        initView(root)
        return root
    }

    private fun initView(root: View) {
        data = mutableListOf(
            "包裹正在等待揽件", "[深圳市] 快件已揽件！", "[深圳市] 快件已到达 南山蛇口！",
            "[合肥市] 合肥转运中心已发出", "[上海市] 普陀区桃浦镇转运中心，马上为您派件！",
            "[上海市] 普陀区桃浦镇派件员为您派件！", "[上海市] 快件已签收，感谢您使用顺丰快递！，期待再次为您服务！"
        )
        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
        recyclerView.adapter = StepProgressAdapter(R.layout.item_supplier_express_track, data)
    }
}
