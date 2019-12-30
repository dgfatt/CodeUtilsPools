package com.ld.pool.codeutilspools.ui.bottomaction.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ld.pool.codeutilspools.R
import com.ld.pool.codeutilspools.ui.bottomaction.ui.home.viewModel.StickyViewModel
import com.ld.pool.common.adapter.headerItemDecoration.StickyDecoration
import com.ld.pool.common.listener.GroupListener
import com.ld.pool.common.listener.OnGroupClickListener
import com.ld.pool.common.view.MyRecyclerView
import com.ld.pool.data.datautils.CityUtil
import com.ld.pool.data.entity.CityEntity


/**
 * 文字悬浮
 */
class StickyFragment : Fragment() {

    private lateinit var stickyViewModel: StickyViewModel
    var mRecyclerView: MyRecyclerView? = null

    private var mAdapter: QuickAdapter? = null
    var dataList: MutableList<CityEntity> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        stickyViewModel =
            ViewModelProviders.of(this).get(StickyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sticky, container, false)
        initView(root)
        setListener(root)
        initData()
        return root
    }

    @SuppressLint("WrongConstant")
    private fun initView(root: View?) {
        mRecyclerView = root?.findViewById(R.id.rv)
        //模拟数据
        //模拟数据
        dataList.addAll(CityUtil.cityEntityList)
        dataList.addAll(CityUtil.cityEntityList)

        //------------- StickyDecoration 使用部分  ----------------
        //------------- StickyDecoration 使用部分  ----------------
        val builder: StickyDecoration.Builder = StickyDecoration.Builder
            .init(object : GroupListener {
                override fun getGroupName(position: Int): String? { //组名回调
                    return if (dataList.size > position && position > -1) { //获取组名，用于判断是否是同一组
                        dataList[position].province
                    } else null
                }
            }) //背景色
            .setGroupBackground(Color.parseColor("#48BDFF")) //高度
            .setGroupHeight(SizeUtils.dp2px(35F)) //分割线颜色
            .setDivideColor(Color.parseColor("#EE96BC")) //分割线高度 (默认没有分割线)
            .setDivideHeight(SizeUtils.dp2px(2F)) //字体颜色 （默认）
            .setGroupTextColor(Color.BLACK) //字体大小
            .setGroupTextSize(SizeUtils.sp2px(15F)) // 边距   靠左时为左边距  靠右时为右边距
            .setTextSideMargin(SizeUtils.dp2px(10F)) // header数量（默认0）
            //.setHeaderCount(1)
            //Group点击事件
            .setOnClickListener(object : OnGroupClickListener {
                override fun onClick(
                    position: Int,
                    id: Int
                ) { //点击事件，返回当前分组下的第一个item的position
                    val content =
                        "onGroupClick --> " + position + " " + dataList[position].province
                    Toast.makeText(activity, content, Toast.LENGTH_SHORT).show()
                }
            })
        //------------- StickyDecoration 使用部分  ----------------
        //下面是平时的RecyclerView操作

        //------------- StickyDecoration 使用部分  ----------------
        //下面是平时的RecyclerView操作
        mAdapter = QuickAdapter(dataList)
        mAdapter!!.setOnItemClickListener { _, _, position ->
            val city: CityEntity = dataList[position]
            Toast.makeText(
                activity,
                "item click " + position + " : " + city.province + " - " + city.name,
                1000
            ).show()
        }

        val manager: RecyclerView.LayoutManager
        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        // header
        // header
        val header = TextView(activity)
        header.text = "header"
        header.gravity = Gravity.CENTER
        val params: MarginLayoutParams =
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200)
        header.layoutParams = params
        //((QuickAdapter) mAdapter).addHeaderView(header);

        //((QuickAdapter) mAdapter).addHeaderView(header);
        val decoration: StickyDecoration = builder.build()
        mRecyclerView!!.layoutManager = manager
        mRecyclerView!!.addItemDecoration(decoration)
        mRecyclerView!!.adapter = mAdapter
    }

    private fun setListener(root: View?) {
        root?.findViewById<Button>(R.id.onRefresh)?.setOnClickListener {
            onRefresh()
        }
    }

    private fun initData() {

    }

    // ---------  忽略下面的代码  --------------

    // ---------  忽略下面的代码  --------------
    val position = 3

    fun onAdd() {
        val previousSize = dataList.size
        val list: List<CityEntity> = CityUtil.cityEntityList
        dataList.addAll(list)
        mAdapter!!.notifyItemRangeInserted(previousSize, list.size)
        mAdapter!!.notifyItemRangeChanged(previousSize, list.size)
    }

    fun onDelete() {
        dataList.removeAt(position)
        mAdapter!!.notifyItemRemoved(position)
        mAdapter!!.notifyItemRangeChanged(position, dataList.size - 3)
    }

    fun onDeleteLast() {
        val endPosition = dataList.size - 1
        dataList.removeAt(endPosition)
        mAdapter!!.notifyItemRemoved(endPosition)
        mAdapter!!.notifyItemChanged(endPosition)
    }

    fun onRefresh() {
        dataList.clear()
        dataList.addAll(CityUtil.randomCityEntityList)
        mAdapter?.notifyDataSetChanged()
    }

    fun onClean() {
        dataList.clear()
        mAdapter!!.notifyDataSetChanged()
    }

    class QuickAdapter(dataList: MutableList<CityEntity>) :
        BaseQuickAdapter<CityEntity, BaseViewHolder>(R.layout.item_recycler_view, dataList) {
        override fun convert(@Nullable holder: BaseViewHolder, item: CityEntity) {
            holder.setText(R.id.tv, item.name)
        }
    }

}