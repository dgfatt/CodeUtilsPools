package com.ld.pool.codeutilspools.ui.bottomaction.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
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
import com.ld.pool.codeutilspools.ui.bottomaction.ui.home.viewModel.PowerfulStickyViewModel
import com.ld.pool.common.adapter.headerItemDecoration.PowerfulStickyDecoration
import com.ld.pool.common.listener.OnGroupClickListener
import com.ld.pool.common.listener.PowerGroupListener
import com.ld.pool.data.datautils.CityUtil
import com.ld.pool.data.entity.CityEntity


class PowerfulStickyFragment : Fragment() {

    private lateinit var powerfulStickyViewModel: PowerfulStickyViewModel

    var mRecyclerView: RecyclerView? = null

    var mAdapter: RecyclerView.Adapter<*>? = null
    var dataList: MutableList<CityEntity> = mutableListOf()
    var decoration: PowerfulStickyDecoration? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        powerfulStickyViewModel =
            ViewModelProviders.of(this).get(PowerfulStickyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sticky, container, false)

        initView(root)
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

        //------------- PowerfulStickyDecoration 使用部分  ----------------
        //------------- PowerfulStickyDecoration 使用部分  ----------------
        val listener: PowerGroupListener = object : PowerGroupListener {
            override fun getGroupName(position: Int): String? { //获取组名，用于判断是否是同一组
                return if (dataList.size > position) {
                    dataList[position].province
                } else null
            }

            override fun getGroupView(position: Int): View? { //获取自定定义的组View
                return if (dataList.size > position) {
                    val view: View =
                        layoutInflater.inflate(R.layout.item_group, null, false)
                    (view.findViewById<View>(R.id.tv) as TextView).text =
                        dataList[position].province
                    view
                } else {
                    null
                }
            }
        }
        decoration = PowerfulStickyDecoration.Builder
            .init(listener)
            .setGroupHeight(SizeUtils.dp2px(40F))
            .setGroupBackground(Color.parseColor("#48BDFF"))
            .setDivideColor(Color.parseColor("#27ad9a"))
            .setDivideHeight(SizeUtils.dp2px(1F))
            .setCacheEnable(true)
            .setHeaderCount(1)
            .setOnClickListener(object : OnGroupClickListener {
                override fun onClick(position: Int, id: Int) { //Group点击事件
                    val content =
                        "onGroupClick --> " + dataList[position].province + "   id --> " + id
                    showToast(content)
                }
            })
            .build()
        //-------------                  ----------------
        //下面是平时的RecyclerView操作

        //-------------                  ----------------
//下面是平时的RecyclerView操作
        mAdapter = QuickAdapter(dataList)
        (mAdapter as QuickAdapter).setOnItemClickListener { adapter, view, position ->
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
        (mAdapter as QuickAdapter).addHeaderView(header)

        mRecyclerView!!.layoutManager = manager
        mRecyclerView!!.addItemDecoration(decoration!!)
        mRecyclerView!!.adapter = mAdapter
    }

    private fun initData() {

    }

    internal class QuickAdapter(dataList: MutableList<CityEntity>) :
        BaseQuickAdapter<CityEntity, BaseViewHolder>(R.layout.item_recycler_view, dataList) {
        override fun convert(@Nullable holder: BaseViewHolder, item: CityEntity) {
            holder.setText(R.id.tv, item.name)
        }
    }


    fun onRefresh(v: View?) {
        dataList.clear()
        dataList.addAll(CityUtil.randomCityEntityList)
        mAdapter!!.notifyDataSetChanged()
        decoration!!.clearCache()
    }

    private fun showToast(content: String) {
        Toast.makeText(activity, content, Toast.LENGTH_LONG).show()
    }

    private fun l(str: String) {
        Log.i("TAG", str)
    }

}