package com.ld.pool.codeutilspools.ui.bottomaction.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ld.pool.codeutilspools.R
import com.ld.pool.codeutilspools.adapter.LinearRecyclerAdapter
import com.ld.pool.codeutilspools.ui.bottomaction.ui.home.viewModel.RecyclerPinnedHeader2ViewModel
import com.ld.pool.common.adapter.headerItemDecoration.PinnedHeaderItemDecoration
import com.ld.pool.common.view.PinnedHeaderRecyclerView
import com.ld.pool.common.view.PinnedHeaderRecyclerView.OnPinnedHeaderClickListener


class RecyclerPinnedHeader2Fragment : Fragment() {

    companion object {
        fun newInstance() = RecyclerPinnedHeader2Fragment()
    }

    private lateinit var viewModel: RecyclerPinnedHeader2ViewModel
    private var mRecyclerView: PinnedHeaderRecyclerView? = null
    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycler_pinned_header2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RecyclerPinnedHeader2ViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context;
        initView(view);
        initEvent();
        initData();
    }

    private fun initView(view: View) {
        mRecyclerView = view.findViewById(R.id.recycler_linear)
        val layoutManager = LinearLayoutManager(mContext)
        mRecyclerView!!.layoutManager = layoutManager
    }

    private fun initEvent() {
        mRecyclerView!!.setOnPinnedHeaderClickListener(object : OnPinnedHeaderClickListener {
            override fun onPinnedHeaderClick(adapterPosition: Int) {
                Toast.makeText(mContext, "点击了悬浮标题 position = $adapterPosition", LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun initData() {
        val adapter = LinearRecyclerAdapter(obtainData())
        mRecyclerView!!.adapter = adapter
        mRecyclerView!!.addItemDecoration(PinnedHeaderItemDecoration())
    }

    private fun obtainData(): List<String>? {
        val list: MutableList<String> = ArrayList()
        list.add("2016-07-20")
        list.add("萍乡")
        list.add("高安")
        list.add("江西")
        list.add("南昌")
        list.add("2016-07-21")
        list.add("江西")
        list.add("南昌")
        list.add("江西")
        list.add("南昌")
        list.add("2016-07-22")
        list.add("中国")
        list.add("北京")
        list.add("江西")
        list.add("南昌")
        list.add("2016-07-23")
        list.add("辽宁")
        list.add("沈阳")
        list.add("江西")
        list.add("南昌")
        list.add("2016-07-24")
        list.add("辽宁")
        list.add("沈阳")
        list.add("江西")
        list.add("南昌")
        list.add("2016-07-25")
        list.add("辽宁")
        list.add("沈阳")
        list.add("江西")
        list.add("南昌")
        list.add("2016-07-26")
        list.add("辽宁")
        list.add("沈阳")
        list.add("江西")
        list.add("南昌")
        list.add("2016-07-27")
        list.add("辽宁")
        list.add("沈阳")
        list.add("江西")
        list.add("南昌")
        list.add("2016-07-28")
        list.add("辽宁")
        list.add("沈阳")
        list.add("江西")
        list.add("南昌")
        list.add("2016-07-29")
        list.add("辽宁")
        list.add("沈阳")
        list.add("江西")
        list.add("南昌")
        list.add("2016-07-30")
        list.add("辽宁")
        list.add("沈阳")
        list.add("江西")
        list.add("南昌")
        list.add("2016-07-21")
        list.add("辽宁")
        list.add("沈阳")
        list.add("江西")
        list.add("南昌")
        return list
    }

}
