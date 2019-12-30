package com.ld.pool.codeutilspools.ui.bottomaction.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.ld.pool.codeutilspools.R
import com.ld.pool.codeutilspools.ui.bottomaction.ui.home.viewModel.StickyGridViewModel
import com.ld.pool.common.adapter.headerItemDecoration.StickyDecoration
import com.ld.pool.common.listener.GroupListener
import com.ld.pool.common.listener.OnGroupClickListener
import com.ld.pool.common.view.MyRecyclerView
import com.ld.pool.data.datautils.CityUtil
import com.ld.pool.data.entity.CityEntity


class StickyGridFragment : Fragment() {

    private lateinit var stickyGridViewModel: StickyGridViewModel

    var mRecyclerView: MyRecyclerView? = null

    var mAdapter: RecyclerView.Adapter<*>? = null
    var dataList: MutableList<CityEntity> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        stickyGridViewModel =
            ViewModelProviders.of(this).get(StickyGridViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sticky, container, false)

        initView(root)
        setListener(root)
        initData()
        return root
    }

    private fun initView(root: View?) {
        mRecyclerView = root?.findViewById(R.id.rv)
        //模拟数据
        //模拟数据
        dataList.addAll(CityUtil.cityEntityList)
        dataList.addAll(CityUtil.cityEntityList)

        //------------- StickyDecoration 使用部分  ----------------
        //------------- StickyDecoration 使用部分  ----------------
        val decoration = StickyDecoration.Builder
            .init(object : GroupListener {
                override fun getGroupName(position: Int): String? { //组名回调
                    return if (dataList.size > position && position > -1) { //获取组名，用于判断是否是同一组
                        dataList[position].province
                    } else null
                }
            })
            .setGroupBackground(Color.parseColor("#48BDFF"))
            .setGroupHeight(SizeUtils.dp2px(35F))
            .setDivideColor(Color.parseColor("#EE96BC"))
            .setDivideHeight(SizeUtils.dp2px(2F))
            .setGroupTextColor(Color.BLACK)
            .setGroupTextSize(SizeUtils.sp2px(15F))
            .setTextSideMargin(SizeUtils.dp2px(10F))
            .setOnClickListener(object : OnGroupClickListener {
                override fun onClick(position: Int, id: Int) { //Group点击事件
                    val content =
                        "onGroupClick --> " + dataList[position].province
                    Toast.makeText(activity, content, Toast.LENGTH_SHORT).show()
                }
            })
            .build()
        //------------- StickyDecoration 使用部分  ----------------
        //下面是平时的RecyclerView操作

        //------------- StickyDecoration 使用部分  ----------------
//下面是平时的RecyclerView操作
        val manager: RecyclerView.LayoutManager
        manager = GridLayoutManager(activity, 3)
        decoration.resetSpan(mRecyclerView, manager)
        mRecyclerView!!.layoutManager = manager
        mRecyclerView!!.addItemDecoration(decoration)

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
                val holder: Holder? = viewHolder as Holder?
                holder?.mTextView?.text = dataList[position].name
                holder?.itemView?.setOnClickListener(View.OnClickListener {
                    Toast.makeText(
                        activity,
                        "item click $position",
                        Toast.LENGTH_LONG
                    ).show()
                })
            }

            override fun getItemCount(): Int {
                return dataList.size
            }

        }
        mRecyclerView!!.adapter = mAdapter
    }

    private fun setListener(root: View?) {
        root?.findViewById<Button>(R.id.onRefresh)?.setOnClickListener {
            onRefresh()
        }
    }

    private fun initData() {

    }

    internal class Holder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var mTextView: TextView = itemView.findViewById(R.id.tv)

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
        mAdapter!!.notifyDataSetChanged()
    }

    fun onClean() {
        dataList.clear()
        mAdapter!!.notifyDataSetChanged()
    }

}