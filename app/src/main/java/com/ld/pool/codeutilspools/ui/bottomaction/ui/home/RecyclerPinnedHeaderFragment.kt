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
import androidx.recyclerview.widget.GridLayoutManager
import com.ld.pool.codeutilspools.R
import com.ld.pool.codeutilspools.adapter.GridRecyclerAdapter
import com.ld.pool.codeutilspools.ui.bottomaction.ui.home.viewModel.RecyclerPinnedHeaderViewModel
import com.ld.pool.common.adapter.headerItemDecoration.PinnedHeaderItemDecoration
import com.ld.pool.common.view.PinnedHeaderRecyclerView
import com.ld.pool.common.view.PinnedHeaderRecyclerView.OnPinnedHeaderClickListener


class RecyclerPinnedHeaderFragment : Fragment() {

    companion object {
        fun newInstance() = RecyclerPinnedHeaderFragment()
    }

    private lateinit var viewModel: RecyclerPinnedHeaderViewModel
    private var mRecyclerView: PinnedHeaderRecyclerView? = null
    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycler_pinned_header, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RecyclerPinnedHeaderViewModel::class.java)
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
        mRecyclerView = view.findViewById(R.id.recycler_grid)
        val manager = GridLayoutManager(mContext, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position % 5 == 0) {
                    manager.spanCount
                } else {
                    1
                }
            }
        }
        mRecyclerView!!.layoutManager = manager
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
        val adapter = GridRecyclerAdapter(obtainData())
        mRecyclerView!!.adapter = adapter
        mRecyclerView!!.addItemDecoration(PinnedHeaderItemDecoration())
    }

    private fun obtainData(): List<String>? {
        val list: MutableList<String> = ArrayList()
        list.add("2016-07-20")
        list.add(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514456311&di=2a8ccd6c814c5851fb8763418dd60455&src=http://wenwen.soso.com/p/20130907/20130907174128-2028703867.jpg"
        )
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1960816299,803825902&fm=27&gp=0.jpg")
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3790810349,4012335838&fm=27&gp=0.jpg")
        list.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3781123768,373023027&fm=27&gp=0.jpg")
        list.add("2016-07-21")
        list.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4052445718,1344904722&fm=27&gp=0.jpg")
        list.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1730874133,3861130981&fm=27&gp=0.jpg")
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4117837678,3129898700&fm=27&gp=0.jpg")
        list.add(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514457163&di=b5c6e3c2242452070e8a40a47fca117f&src=http://p4.gexing.com/G1/M00/A2/1B/rBACFFKAfKzzywqAAAAZExvP8P4963_200x200_3.jpg?recache=20131108"
        )
        list.add("2016-07-22")
        list.add(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514456311&di=2a8ccd6c814c5851fb8763418dd60455&src=http://wenwen.soso.com/p/20130907/20130907174128-2028703867.jpg"
        )
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1960816299,803825902&fm=27&gp=0.jpg")
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3790810349,4012335838&fm=27&gp=0.jpg")
        list.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3781123768,373023027&fm=27&gp=0.jpg")
        list.add("2016-07-23")
        list.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4052445718,1344904722&fm=27&gp=0.jpg")
        list.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1730874133,3861130981&fm=27&gp=0.jpg")
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4117837678,3129898700&fm=27&gp=0.jpg")
        list.add(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514457163&di=b5c6e3c2242452070e8a40a47fca117f&src=http://p4.gexing.com/G1/M00/A2/1B/rBACFFKAfKzzywqAAAAZExvP8P4963_200x200_3.jpg?recache=20131108"
        )
        list.add("2016-07-24")
        list.add(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514456311&di=2a8ccd6c814c5851fb8763418dd60455&src=http://wenwen.soso.com/p/20130907/20130907174128-2028703867.jpg"
        )
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1960816299,803825902&fm=27&gp=0.jpg")
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3790810349,4012335838&fm=27&gp=0.jpg")
        list.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3781123768,373023027&fm=27&gp=0.jpg")
        list.add("2016-07-25")
        list.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4052445718,1344904722&fm=27&gp=0.jpg")
        list.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1730874133,3861130981&fm=27&gp=0.jpg")
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4117837678,3129898700&fm=27&gp=0.jpg")
        list.add(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514457163&di=b5c6e3c2242452070e8a40a47fca117f&src=http://p4.gexing.com/G1/M00/A2/1B/rBACFFKAfKzzywqAAAAZExvP8P4963_200x200_3.jpg?recache=20131108"
        )
        list.add("2016-07-26")
        list.add(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514456311&di=2a8ccd6c814c5851fb8763418dd60455&src=http://wenwen.soso.com/p/20130907/20130907174128-2028703867.jpg"
        )
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1960816299,803825902&fm=27&gp=0.jpg")
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3790810349,4012335838&fm=27&gp=0.jpg")
        list.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3781123768,373023027&fm=27&gp=0.jpg")
        list.add("2016-07-27")
        list.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4052445718,1344904722&fm=27&gp=0.jpg")
        list.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1730874133,3861130981&fm=27&gp=0.jpg")
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4117837678,3129898700&fm=27&gp=0.jpg")
        list.add(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514457163&di=b5c6e3c2242452070e8a40a47fca117f&src=http://p4.gexing.com/G1/M00/A2/1B/rBACFFKAfKzzywqAAAAZExvP8P4963_200x200_3.jpg?recache=20131108"
        )
        list.add("2016-07-28")
        list.add(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514456311&di=2a8ccd6c814c5851fb8763418dd60455&src=http://wenwen.soso.com/p/20130907/20130907174128-2028703867.jpg"
        )
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1960816299,803825902&fm=27&gp=0.jpg")
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3790810349,4012335838&fm=27&gp=0.jpg")
        list.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3781123768,373023027&fm=27&gp=0.jpg")
        list.add("2016-07-29")
        list.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4052445718,1344904722&fm=27&gp=0.jpg")
        list.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1730874133,3861130981&fm=27&gp=0.jpg")
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4117837678,3129898700&fm=27&gp=0.jpg")
        list.add(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514457163&di=b5c6e3c2242452070e8a40a47fca117f&src=http://p4.gexing.com/G1/M00/A2/1B/rBACFFKAfKzzywqAAAAZExvP8P4963_200x200_3.jpg?recache=20131108"
        )
        list.add("2016-07-30")
        list.add(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514456311&di=2a8ccd6c814c5851fb8763418dd60455&src=http://wenwen.soso.com/p/20130907/20130907174128-2028703867.jpg"
        )
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1960816299,803825902&fm=27&gp=0.jpg")
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3790810349,4012335838&fm=27&gp=0.jpg")
        list.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3781123768,373023027&fm=27&gp=0.jpg")
        list.add("2016-07-21")
        list.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4052445718,1344904722&fm=27&gp=0.jpg")
        list.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1730874133,3861130981&fm=27&gp=0.jpg")
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4117837678,3129898700&fm=27&gp=0.jpg")
        list.add(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514457163&di=b5c6e3c2242452070e8a40a47fca117f&src=http://p4.gexing.com/G1/M00/A2/1B/rBACFFKAfKzzywqAAAAZExvP8P4963_200x200_3.jpg?recache=20131108"
        )
        return list
    }

}
