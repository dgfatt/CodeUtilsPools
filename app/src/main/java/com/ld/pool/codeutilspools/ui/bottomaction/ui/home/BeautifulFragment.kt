package com.ld.pool.codeutilspools.ui.bottomaction.ui.home

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
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
import com.ld.pool.codeutilspools.ui.bottomaction.ui.home.viewModel.BeautifulViewModel
import com.ld.pool.common.adapter.headerItemDecoration.PowerfulStickyDecoration
import com.ld.pool.common.listener.PowerGroupListener
import com.ld.pool.data.datautils.CityUtil
import com.ld.pool.data.entity.CityEntity


class BeautifulFragment : Fragment() {

    private lateinit var beautifulViewModel: BeautifulViewModel

    var mRv: RecyclerView? = null

    var mAdapter: RecyclerView.Adapter<*>? = null
    var dataList: MutableList<CityEntity> = mutableListOf()
    var decoration: PowerfulStickyDecoration? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        beautifulViewModel =
            ViewModelProviders.of(this).get(BeautifulViewModel::class.java)
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

        val manager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRv!!.layoutManager = manager
        //------------- PowerfulStickyDecoration 使用部分  ----------------
        //------------- PowerfulStickyDecoration 使用部分  ----------------
        decoration = PowerfulStickyDecoration.Builder
            .init(object : PowerGroupListener {
                override fun getGroupName(position: Int): String? {
                    var position = position
                    position++
                    //获取组名，用于判断是否是同一组
                    return if (dataList.size > position) {
                        dataList[position].province
                    } else null
                }

                override fun getGroupView(position: Int): View? {
                    var position = position
                    position++
                    //获取自定定义的组View
                    return if (dataList.size > position) {
                        val view: View =
                            layoutInflater.inflate(R.layout.layout_city_group, null, false)
                        (view.findViewById<View>(R.id.tv) as TextView).text =
                            dataList[position].province
                        val imageView: ImageView =
                            view.findViewById<View>(R.id.iv) as ImageView
                        imageView.setImageResource(dataList[position].icon)
                        //  模拟网络加载图片
                        //asyncLoadImage(view, decoration, position);
                        view
                    } else {
                        null
                    }
                }
            })
            .setCacheEnable(true)
            .setGroupHeight(SizeUtils.dp2px(80F)) //设置高度
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
     * 模拟网络加载图
     *
     * @param decoration
     * @param position
     */
    @SuppressLint("StaticFieldLeak")
    private fun asyncLoadImage(
        view: View,
        decoration: PowerfulStickyDecoration?,
        position: Int
    ) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg params: Void?): Void? {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                return null
            }

            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                (view.findViewById<View>(R.id.tv) as TextView).text = dataList[position].province
                val imageView: ImageView =
                    view.findViewById<View>(R.id.iv) as ImageView
                imageView.setImageResource(dataList[position].icon)
                decoration?.notifyRedraw(mRv!!, view, position)
            }
        }.execute()
    }

    fun onRefresh(v: View?) {
        decoration!!.clearCache()
        dataList.clear()
        val list: MutableList<CityEntity> = CityUtil.randomCityEntityList
        Log.i("tag", list.toString())
        dataList.addAll(list)
        mAdapter!!.notifyDataSetChanged()
    }

}