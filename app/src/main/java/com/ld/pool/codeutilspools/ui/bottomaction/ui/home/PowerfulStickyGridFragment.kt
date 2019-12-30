package com.ld.pool.codeutilspools.ui.bottomaction.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.ld.pool.codeutilspools.R
import com.ld.pool.codeutilspools.ui.bottomaction.ui.home.viewModel.PowerfulStickyGridViewModel
import com.ld.pool.common.adapter.headerItemDecoration.PowerfulStickyDecoration
import com.ld.pool.common.listener.OnGroupClickListener
import com.ld.pool.common.listener.PowerGroupListener
import com.ld.pool.data.datautils.CityUtil
import com.ld.pool.data.entity.CityEntity


class PowerfulStickyGridFragment : Fragment() {

    private lateinit var powerfulStickyGridViewModel: PowerfulStickyGridViewModel

    var mRv: RecyclerView? = null

    var mAdapter: RecyclerView.Adapter<*>? = null
    var dataList: MutableList<CityEntity> = mutableListOf()
    var decoration: PowerfulStickyDecoration? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        powerfulStickyGridViewModel =
            ViewModelProviders.of(this).get(PowerfulStickyGridViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sticky, container, false)

        initView(root)
        initData()
        return root
    }

    private fun initView(root: View?) {
        mRv = root?.findViewById(R.id.rv)
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
        val manager = GridLayoutManager(activity, 3)
        decoration!!.resetSpan(mRv, manager)
        mRv!!.layoutManager = manager
        mRv!!.addItemDecoration(decoration!!)
        mAdapter = object : RecyclerView.Adapter<Holder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): Holder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_recycler_view, parent, false)
                return Holder(view)
            }

            override fun onBindViewHolder(viewHolder: Holder, position: Int) {
                val holder: Holder? = viewHolder
                holder?.mTextView?.text = dataList[position].name
                holder?.itemView?.setOnClickListener { showToast("Item click $position") }
            }

            override fun getItemCount(): Int {
                return dataList.size
            }
        }
        mRv!!.adapter = mAdapter
    }

    private fun initData() {

    }

    internal class Holder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var mTextView: TextView = itemView.findViewById(R.id.tv)

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