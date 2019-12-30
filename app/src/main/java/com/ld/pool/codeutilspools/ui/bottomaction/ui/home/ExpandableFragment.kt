package com.ld.pool.codeutilspools.ui.bottomaction.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.ld.pool.codeutilspools.R
import com.ld.pool.codeutilspools.adapter.SimpleAdapter
import com.ld.pool.codeutilspools.ui.bottomaction.ui.home.viewModel.ExpandableViewModel
import com.ld.pool.common.adapter.headerItemDecoration.PowerfulStickyDecoration
import com.ld.pool.common.listener.OnGroupClickListener
import com.ld.pool.common.listener.PowerGroupListener
import com.ld.pool.data.datautils.CityUtil
import com.ld.pool.data.entity.CityEntity


class ExpandableFragment : Fragment() {

    private lateinit var expandableViewModel: ExpandableViewModel

    var mRv: RecyclerView? = null

    var mAdapter: RecyclerView.Adapter<*>? = null
    var dataList: MutableList<CityEntity> = mutableListOf()
    var decoration: PowerfulStickyDecoration? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        expandableViewModel =
            ViewModelProviders.of(this).get(ExpandableViewModel::class.java)
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

        val manager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRv!!.layoutManager = manager
        //------------- PowerfulStickyDecoration 使用部分  ----------------
        //------------- PowerfulStickyDecoration 使用部分  ----------------
        decoration = PowerfulStickyDecoration.Builder
            .init(object : PowerGroupListener {
                override fun getGroupName(position: Int): String? { //获取组名，用于判断是否是同一组
                    return if (dataList.size > position) {
                        dataList[position].province
                    } else null
                }

                override fun getGroupView(position: Int): View? { //获取自定定义的组View
                    return if (dataList.size > position) {
                        val view: View =
                            layoutInflater.inflate(R.layout.layout_city_group, null, false)
                        (view.findViewById<View>(R.id.tv) as TextView).text =
                            dataList[position].province
                        val imageView: ImageView =
                            view.findViewById<View>(R.id.iv) as ImageView
                        imageView.setImageResource(dataList[position].icon)
                        view
                    } else {
                        null
                    }
                }
            })
            .setCacheEnable(true)
            .setGroupHeight(SizeUtils.dp2px(40F))
            .setOnClickListener(object : OnGroupClickListener {
                override fun onClick(position: Int, id: Int) {
                    if (dataList.size > position) { //修改数据
                        changeExpandedState(position)
                        val city: CityEntity = dataList[position]
                        //修改悬浮窗
                        val view: View =
                            layoutInflater.inflate(R.layout.layout_city_group, null, false)
                        (view.findViewById<View>(R.id.tv) as TextView).text =
                            dataList[position].province
                        val imageView: ImageView =
                            view.findViewById<View>(R.id.iv) as ImageView
                        imageView.setImageResource(dataList[position].icon)
                        val ivExpanded: ImageView =
                            view.findViewById<View>(R.id.iv_expanded) as ImageView
                        val rotation = if (city.isExpanded) 0 else 180
                        ivExpanded.rotation = rotation.toFloat()
                        //修改数据后，刷新指定的悬浮窗
                        decoration!!.notifyRedraw(mRv!!, view, position)
                        mAdapter!!.notifyDataSetChanged()
                    }
                }
            })
            .build()
        //----------------                 -------------
        //下面是平时的RecyclerView操作
        //----------------                 -------------
//下面是平时的RecyclerView操作
        mRv!!.addItemDecoration(decoration!!)
        mAdapter = SimpleAdapter(activity, dataList)
        mRv!!.adapter = mAdapter
    }

    private fun initData() {

    }

    /**
     * 修改数据
     *
     * @param position
     */
    private fun changeExpandedState(position: Int) {
        var position = position
        if (dataList.size > position) {
            val city: CityEntity = dataList[position]
            city.isExpanded = !city.isExpanded
            position++
            if (dataList.size > position) { //下个是当前分组
                val city2: CityEntity = dataList[position]
                if (TextUtils.equals(city.province, city2.province)) {
                    changeExpandedState(position)
                }
            }
        }
    }

    fun onRefresh(v: View?) {}

}