package com.ld.pool.codeutilspools.ui.bottomaction.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ld.pool.codeutilspools.R
import com.ld.pool.codeutilspools.adapter.DetailRecyclerViewAdapter
import com.ld.pool.common.adapter.dividerItemDecoration.GridDividerItemDecoration
import com.ld.pool.common.adapter.headerItemDecoration.ItemHeaderDecoration
import com.ld.pool.codeutilspools.ui.bottomaction.ui.home.viewModel.FloatBarViewModel
import com.ld.pool.data.bean.DetailBean


class FloatingBarFragment : Fragment() {

    private lateinit var floatBarViewModel: FloatBarViewModel
    private var mRecyclerView: RecyclerView? = null;
    private lateinit var mAdapter: DetailRecyclerViewAdapter;
    private lateinit var mList: MutableList<DetailBean>;
    private lateinit var mLayoutManager: GridLayoutManager;


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        floatBarViewModel =
            ViewModelProviders.of(this).get(FloatBarViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_floatingbar, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        floatBarViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        initView(root)
        initData()
        return root
    }

    private fun initView(root: View?) {
        mList = mutableListOf()
        mRecyclerView = root?.findViewById(R.id.detail_recycler)
        mAdapter = DetailRecyclerViewAdapter(mList)
        mLayoutManager = GridLayoutManager(activity, 3)
        mLayoutManager.spanSizeLookup = MySpanSizeLookup(mList)
        mRecyclerView!!.addItemDecoration(
            GridDividerItemDecoration(
                activity!!,
                mList
            )
        )
        mRecyclerView!!.addItemDecoration(
            ItemHeaderDecoration(
                activity!!,
                mList
            )
        )
        mRecyclerView?.layoutManager = mLayoutManager
        mRecyclerView?.adapter = mAdapter
    }


    private fun initData() {
        for (i in 0..15) {
            var bean = DetailBean()
            bean.itemType = DetailBean.ITEM_TYPE_TITLE
            bean.isTitle = true
            bean.tag = "$i"
            bean.name = "分类$i"
            mList.add(bean)
            for (j in 0..10) {
                bean = DetailBean()
                bean.itemType = DetailBean.ITEM_TYPE_TXET
                bean.isTitle = false
                bean.tag = "$i"
                bean.name = "$j"
                mList.add(bean)
            }
        }

        mAdapter.notifyDataSetChanged()
    }

    class MySpanSizeLookup(data: MutableList<DetailBean>) : GridLayoutManager.SpanSizeLookup() {
        private var mList: MutableList<DetailBean>? = null;

        init {
            mList = data
        }

        override fun getSpanSize(position: Int): Int {
            return when (mList?.get(position)?.itemType) {
                DetailBean.ITEM_TYPE_TITLE -> 3
                else -> 1
            }
        }

    }
}