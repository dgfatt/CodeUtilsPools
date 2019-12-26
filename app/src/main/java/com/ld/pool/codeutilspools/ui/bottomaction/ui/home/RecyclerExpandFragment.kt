package com.ld.pool.codeutilspools.ui.bottomaction.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ld.pool.codeutilspools.R
import com.ld.pool.codeutilspools.adapter.PatrolGroupAdapter
import com.ld.pool.codeutilspools.ui.bottomaction.ui.home.viewModel.RecyclerExpandViewModel
import com.ld.pool.common.adapter.headerItemDecoration.PinnedHeaderItemDecoration
import com.ld.pool.common.view.PinnedHeaderRecyclerView
import com.ld.pool.common.view.PinnedHeaderRecyclerView.OnPinnedHeaderClickListener
import com.ld.pool.data.entity.ExpandGroupItemEntity
import com.ld.pool.data.entity.PatrolItemEntity


class RecyclerExpandFragment : Fragment() {

    private lateinit var recyclerExpandViewModel: RecyclerExpandViewModel
    private var mContext: Context? = null
    private var mRecyclerView: PinnedHeaderRecyclerView? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mAdapter: PatrolGroupAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recyclerExpandViewModel =
            ViewModelProviders.of(this).get(RecyclerExpandViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recycler_expand, container, false)
        mContext = view?.context
        initView(root)
        setListener()
        initData()
        return root
    }

    private fun initView(view: View?) {
        mRecyclerView = view?.findViewById(R.id.recycler_order_list)
        mRecyclerView!!.layoutManager = LinearLayoutManager(mContext).also { mLayoutManager = it }
        mRecyclerView!!.addItemDecoration(PinnedHeaderItemDecoration())
    }

    private fun setListener() {
        /**
         * 当标题栏被悬浮的时候的点击功能
         */
        /**
         * 当标题栏被悬浮的时候的点击功能
         */
        mRecyclerView!!.setOnPinnedHeaderClickListener(object : OnPinnedHeaderClickListener {
            override fun onPinnedHeaderClick(adapterPosition: Int) {
                mAdapter!!.switchExpand(adapterPosition)
                //标题栏被点击之后，滑动到指定位置
                mLayoutManager!!.scrollToPositionWithOffset(adapterPosition, 0)
            }
        })
    }

    private fun initData() {
        mAdapter = PatrolGroupAdapter()
        mAdapter!!.data = obtainDataList()
        mRecyclerView!!.adapter = mAdapter
    }

    private fun obtainDataList(): List<ExpandGroupItemEntity<String, PatrolItemEntity>>? {
        val dataList: MutableList<ExpandGroupItemEntity<String, PatrolItemEntity>> =
            ArrayList()
        for (group in 0..9) {
            val groupItem: ExpandGroupItemEntity<String, PatrolItemEntity> = ExpandGroupItemEntity()
            groupItem.isExpand = true
            groupItem.setParent("分组 $group")
            val childList: MutableList<PatrolItemEntity> = ArrayList()
            for (child in 0 until group + 1) {
                val childItem = PatrolItemEntity()
                childItem.time = "2018-04-20 15:00"
                childItem.factoryName = (2000 + child).toString() + " 项目"
                childItem.user = "电工 $child"
                childItem.state = child % 5
                childList.add(childItem)
            }
            groupItem.childList = childList
            dataList.add(groupItem)
        }
        return dataList
    }

}