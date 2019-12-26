package com.ld.pool.codeutilspools.ui.bottomaction.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ld.pool.codeutilspools.R
import com.ld.pool.codeutilspools.adapter.TextViewAdapter
import com.ld.pool.codeutilspools.ui.bottomaction.ui.home.viewModel.StickyRecyclerViewViewModel
import com.ld.pool.data.bean.CommentBean
import com.ld.pool.data.bean.PostBean
import com.ld.pool.data.bean.TextViewBean
import java.util.*
import kotlin.collections.ArrayList


class StickyRecyclerViewFragment : Fragment() {

    private var adapter: TextViewAdapter? = null
    private var rv: RecyclerView? = null
    private var mSuspensionBar: TextView? = null
    private var mCurrentPosition = 0
    private var data: MutableList<TextViewBean>? = null

    companion object {
        fun newInstance() = StickyRecyclerViewFragment()
    }

    private lateinit var viewModel: StickyRecyclerViewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sticky_recycler_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StickyRecyclerViewViewModel::class.java)
        // TODO: Use the ViewModel

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setListener()
        initData()
    }

    private fun initView(view: View) {
        data = mutableListOf()
        rv = view.findViewById(R.id.rv)
        mSuspensionBar = view.findViewById(R.id.tv)
        // mSuspensionBar.setAlpha(0.5f);
        // mSuspensionBar.setAlpha(0.5f);
        adapter = TextViewAdapter(R.layout.item_text_layout, data!!)
        rv?.adapter = adapter
    }

    private fun setListener() {
        rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var linearLayoutManager =
                rv!!.layoutManager as LinearLayoutManager?
            var mSuspensionHeight = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mSuspensionHeight = mSuspensionBar!!.height

                val firstVisPos: Int? = linearLayoutManager?.findFirstVisibleItemPosition()

                val firstVisibleItem: Any = adapter?.getItem(firstVisPos!!)!!
                val nextItem: Any = adapter?.getItem(firstVisPos?.plus(1)!!)!!
                val nextView: View? =
                    linearLayoutManager?.findViewByPosition(firstVisPos?.plus(1)!!)


                if (dy > 0) {
                    if (nextItem is PostBean) {
                        if (nextView!!.top <= mSuspensionHeight) { //被顶掉的效果
                            mSuspensionBar!!.y = (-(mSuspensionHeight - nextView!!.top)).toFloat()
                        } else {
                            mSuspensionBar!!.y = 0f
                        }
                    }
                    //判断是否需要更新悬浮条
                    if (mCurrentPosition != firstVisPos && firstVisibleItem is PostBean) {
                        mCurrentPosition = firstVisPos!!
                        //更新悬浮条
                        updateSuspensionBar()
                        mSuspensionBar!!.y = 0f
                    }
                } else {
                    // 1、nextItem -> Post and firstVisibleItem -> Comment       mCurrentPosition = ((Comment) firstVisibleItem).getParentPostPosition()
                    // 2、nextItem -> Post and firstVisibleItem -> Post          mCurrentPosition = firstVisPos
                    // 3、nextItem -> Comment and firstVisibleItem -> Comment    mSuspensionBar 不动
                    // 4、nextItem -> Comment and firstVisibleItem -> Post       mSuspensionBar 不动
                    if (nextItem is PostBean) {
                        mCurrentPosition =
                            if (firstVisibleItem is PostBean) firstVisPos!! else (firstVisibleItem as CommentBean).parentPostPosition
                        updateSuspensionBar()
                        if (nextView!!.top <= mSuspensionHeight) { //被顶掉的效果
                            mSuspensionBar!!.y = (-(mSuspensionHeight - nextView.top)).toFloat()
                        } else {
                            mSuspensionBar!!.y = 0f
                        }
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }
        })
    }

    private fun updateSuspensionBar() {
        val s = (adapter?.getItem(mCurrentPosition) as TextViewBean).text
        mSuspensionBar!!.text = s
    }

    private fun initData() {
        //模拟数据
        //模拟数据
        val list: MutableList<PostBean> = ArrayList()
        var index = 0
        var parentPostPos: Int
        val random = Random()
        for (i in 0..9) {
            val post = PostBean("pos = $index")
            parentPostPos = index
            list.add(post)
            index++
            val k: Int = random.nextInt(5)
            post.comments = ArrayList()
            for (j in 0 until k) {
                val comment = CommentBean(
                    "pos = $index",
                    parentPostPos
                )
                (post.comments as MutableList<CommentBean>).add(comment)
                index++
            }
        }

        for (post in list) {
            data?.add(post)
            post.comments?.let { data?.addAll(it) }
        }
        adapter?.notifyDataSetChanged()
        //更新悬浮条
        updateSuspensionBar()
    }
}
