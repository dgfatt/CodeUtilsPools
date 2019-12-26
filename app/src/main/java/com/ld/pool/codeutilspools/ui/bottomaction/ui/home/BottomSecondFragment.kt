package com.ld.pool.codeutilspools.ui.bottomaction.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ld.pool.codeutilspools.R
import com.ld.pool.codeutilspools.adapter.PerformerListAdapter
import com.ld.pool.common.adapter.dividerItemDecoration.ListDividerItemDecoration
import com.ld.pool.common.adapter.headerItemDecoration.StickyItemDecoration
import com.ld.pool.data.bean.PerformerBean


class BottomSecondFragment : Fragment() {

    private val args: BottomSecondFragmentArgs by navArgs()

    private var mRecyclerView: RecyclerView? = null

    private var performers: MutableList<PerformerBean>? = null

    private var adapter: PerformerListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_second_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.textview_home_second).text =
            getString(R.string.hello_home_second, args.myArg)

        view.findViewById<Button>(R.id.button_home_second).setOnClickListener {
            // 跳转到一个新的BottomHomeFragment页面
            // findNavController().navigate(R.id.action_BottomHomeSecondFragment_to_BottomHomeFragment)
            // 返回到上一个页面
            Navigation.findNavController(view).navigateUp();
        }

        initView(view)
        getData()
    }

    private fun initView(view: View) {
        performers = mutableListOf();
        mRecyclerView = view.findViewById(R.id.rv)
        mRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        mRecyclerView!!.addItemDecoration(StickyItemDecoration())
        mRecyclerView!!.addItemDecoration(ListDividerItemDecoration())
        adapter = PerformerListAdapter(performers)
        mRecyclerView!!.adapter = adapter
    }

    private fun getData() {

//        Performer performer = new Performer("香港明星");
//        performers.add(performer);
//
//        Performer ldh = new Performer("刘德华", 10);
//        performers.add(ldh);
//        Performer zxy = new Performer("张学友", 10);
//        performers.add(zxy);
//        Performer zrf = new Performer("周润发", 10);
//        performers.add(zrf);
//        Performer lcw = new Performer("梁朝伟", 10);
//        performers.add(lcw);
//        Performer wyj = new Performer("吴毅将", 10);
//        performers.add(wyj);
//        Performer lm = new Performer("黎明", 10);
//        performers.add(lm);
//        Performer cgx = new Performer("陈冠希", 10);
//        performers.add(cgx);
//        Performer gfc = new Performer("郭富城", 10);
//        performers.add(gfc);
//        Performer xtf = new Performer("谢霆锋", 10);
//        performers.add(xtf);
//
//        Performer performerTw = new Performer("台湾明星：指的是中国台湾的一些有名气的电影，电视演员和歌手，他们具有很高的人气，成名时间早，成名时间久");
//        performers.add(performerTw);
//
//        Performer rxq = new Performer("任贤齐", 10);
//        performers.add(rxq);
//        Performer mtw = new Performer("孟庭苇", 10);
//        performers.add(mtw);
//        Performer ldy = new Performer("罗大佑", 10);
//        performers.add(ldy);
//        Performer lzs = new Performer("李宗盛", 10);
//        performers.add(lzs);
//        Performer xc = new Performer("小虫", 10);
//        performers.add(xc);
//        Performer zhj = new Performer("周华健", 10);
//        performers.add(zhj);
//        Performer zhl = new Performer("周杰伦", 10);
//        performers.add(zhl);
//
//        Performer performerNl = new Performer("内陆明星");
//        performers.add(performerNl);
//
//        Performer lh = new Performer("鹿晗", 10);
//        performers.add(lh);
//        Performer wzw = new Performer("王志文", 10);
//        performers.add(wzw);
//        Performer yq = new Performer("羽泉", 10);
//        performers.add(yq);
//        Performer lxl = new Performer("李小璐", 10);
//        performers.add(lxl);
//        Performer hh = new Performer("韩红", 10);
//        performers.add(hh);
//        Performer ny = new Performer("那英", 10);
//        performers.add(ny);
//        Performer lhh = new Performer("刘欢", 10);
//        performers.add(lhh);
//        Performer yk = new Performer("杨坤", 10);
//        performers.add(yk);
//        Performer zj = new Performer("周杰", 10);
//        performers.add(zj);
//
//        Performer performerOm = new Performer("美国明星");
//        performers.add(performerOm);
//        Performer mm = new Performer("梅梅", 10);
//        performers.add(mm);
//        Performer ade = new Performer("Gaga", 10);
//        performers.add(ade);
//        Performer hff = new Performer("黑寡妇", 10);
//        performers.add(hff);
//        Performer xlz = new Performer("小李子", 10);
//        performers.add(xlz);
//
//        Performer performerNba = new Performer("NBA明星");
//        performers.add(performerNba);
//        Performer xhd = new Performer("小皇帝", 10);
//        performers.add(xhd);
//        Performer kb = new Performer("科比", 10);
//        performers.add(kb);
//        Performer ym = new Performer("姚明", 10);
//        performers.add(ym);
//        Performer md = new Performer("麦迪", 10);
//        performers.add(md);
//        Performer dlt = new Performer("杜兰特", 10);
//        performers.add(dlt);
//        Performer kl = new Performer("库里", 10);
//        performers.add(kl);
//        Performer ouw = new Performer("欧文", 10);
//        performers.add(ouw);
//        Performer qd = new Performer("乔丹", 10);
//        performers.add(qd);
//        Performer alzw = new Performer("奥拉朱旺", 10);
//        performers.add(alzw);
//        Performer pp = new Performer("皮蓬", 10);
//        performers.add(pp);
//        Performer ldm = new Performer("罗德曼", 10);
//        performers.add(ldm);
//        Performer ke = new Performer("科尔", 10);
//        performers.add(ke);
//        Performer pesi = new Performer("皮尔斯", 10);
//        performers.add(pesi);
//        Performer jnt = new Performer("加内特", 10);
//        performers.add(jnt);
//        Performer lal = new Performer("雷阿伦", 10);
//        performers.add(lal);
//        Performer zmg = new Performer("字母哥", 10);
//        performers.add(zmg);
//        Performer adn = new Performer("安东尼", 10);
//        performers.add(adn);
//
//        Performer performerDy = new Performer("导演");
//        performers.add(performerDy);
//        Performer jzk = new Performer("贾樟柯", 10);
//        performers.add(jzk);
//        Performer ly = new Performer("李杨", 10);
//        performers.add(ly);
//        Performer fxg = new Performer("冯小刚", 10);
//        performers.add(fxg);
//        Performer lyy = new Performer("娄烨", 10);
//        performers.add(lyy);
//        Performer zym = new Performer("张艺谋", 10);
//        performers.add(zym);


        //        Performer performer = new Performer("香港明星");
//        performers.add(performer);
//
//        Performer ldh = new Performer("刘德华", 10);
//        performers.add(ldh);
//        Performer zxy = new Performer("张学友", 10);
//        performers.add(zxy);
//        Performer zrf = new Performer("周润发", 10);
//        performers.add(zrf);
//        Performer lcw = new Performer("梁朝伟", 10);
//        performers.add(lcw);
//        Performer wyj = new Performer("吴毅将", 10);
//        performers.add(wyj);
//        Performer lm = new Performer("黎明", 10);
//        performers.add(lm);
//        Performer cgx = new Performer("陈冠希", 10);
//        performers.add(cgx);
//        Performer gfc = new Performer("郭富城", 10);
//        performers.add(gfc);
//        Performer xtf = new Performer("谢霆锋", 10);
//        performers.add(xtf);
//
//        Performer performerTw = new Performer("台湾明星：指的是中国台湾的一些有名气的电影，电视演员和歌手，他们具有很高的人气，成名时间早，成名时间久");
//        performers.add(performerTw);
//
//        Performer rxq = new Performer("任贤齐", 10);
//        performers.add(rxq);
//        Performer mtw = new Performer("孟庭苇", 10);
//        performers.add(mtw);
//        Performer ldy = new Performer("罗大佑", 10);
//        performers.add(ldy);
//        Performer lzs = new Performer("李宗盛", 10);
//        performers.add(lzs);
//        Performer xc = new Performer("小虫", 10);
//        performers.add(xc);
//        Performer zhj = new Performer("周华健", 10);
//        performers.add(zhj);
//        Performer zhl = new Performer("周杰伦", 10);
//        performers.add(zhl);
//
//        Performer performerNl = new Performer("内陆明星");
//        performers.add(performerNl);
//
//        Performer lh = new Performer("鹿晗", 10);
//        performers.add(lh);
//        Performer wzw = new Performer("王志文", 10);
//        performers.add(wzw);
//        Performer yq = new Performer("羽泉", 10);
//        performers.add(yq);
//        Performer lxl = new Performer("李小璐", 10);
//        performers.add(lxl);
//        Performer hh = new Performer("韩红", 10);
//        performers.add(hh);
//        Performer ny = new Performer("那英", 10);
//        performers.add(ny);
//        Performer lhh = new Performer("刘欢", 10);
//        performers.add(lhh);
//        Performer yk = new Performer("杨坤", 10);
//        performers.add(yk);
//        Performer zj = new Performer("周杰", 10);
//        performers.add(zj);
//
//        Performer performerOm = new Performer("美国明星");
//        performers.add(performerOm);
//        Performer mm = new Performer("梅梅", 10);
//        performers.add(mm);
//        Performer ade = new Performer("Gaga", 10);
//        performers.add(ade);
//        Performer hff = new Performer("黑寡妇", 10);
//        performers.add(hff);
//        Performer xlz = new Performer("小李子", 10);
//        performers.add(xlz);
//
//        Performer performerNba = new Performer("NBA明星");
//        performers.add(performerNba);
//        Performer xhd = new Performer("小皇帝", 10);
//        performers.add(xhd);
//        Performer kb = new Performer("科比", 10);
//        performers.add(kb);
//        Performer ym = new Performer("姚明", 10);
//        performers.add(ym);
//        Performer md = new Performer("麦迪", 10);
//        performers.add(md);
//        Performer dlt = new Performer("杜兰特", 10);
//        performers.add(dlt);
//        Performer kl = new Performer("库里", 10);
//        performers.add(kl);
//        Performer ouw = new Performer("欧文", 10);
//        performers.add(ouw);
//        Performer qd = new Performer("乔丹", 10);
//        performers.add(qd);
//        Performer alzw = new Performer("奥拉朱旺", 10);
//        performers.add(alzw);
//        Performer pp = new Performer("皮蓬", 10);
//        performers.add(pp);
//        Performer ldm = new Performer("罗德曼", 10);
//        performers.add(ldm);
//        Performer ke = new Performer("科尔", 10);
//        performers.add(ke);
//        Performer pesi = new Performer("皮尔斯", 10);
//        performers.add(pesi);
//        Performer jnt = new Performer("加内特", 10);
//        performers.add(jnt);
//        Performer lal = new Performer("雷阿伦", 10);
//        performers.add(lal);
//        Performer zmg = new Performer("字母哥", 10);
//        performers.add(zmg);
//        Performer adn = new Performer("安东尼", 10);
//        performers.add(adn);
//
//        Performer performerDy = new Performer("导演");
//        performers.add(performerDy);
//        Performer jzk = new Performer("贾樟柯", 10);
//        performers.add(jzk);
//        Performer ly = new Performer("李杨", 10);
//        performers.add(ly);
//        Performer fxg = new Performer("冯小刚", 10);
//        performers.add(fxg);
//        Performer lyy = new Performer("娄烨", 10);
//        performers.add(lyy);
//        Performer zym = new Performer("张艺谋", 10);
//        performers.add(zym);
        val performer = PerformerBean("香港明星")
        performers?.add(performer)

        val ldh = PerformerBean("刘德华", 10)
        performers?.add(ldh)
        val zxy = PerformerBean("张学友", 10)
        performers?.add(zxy)
        val zrf = PerformerBean("周润发", 10)
        performers?.add(zrf)
        val lcw = PerformerBean("梁朝伟", 10)
        performers?.add(lcw)
        val wyj = PerformerBean("吴毅将", 10)
        performers?.add(wyj)
        val lm = PerformerBean("黎明", 10)
        performers?.add(lm)
        val cgx = PerformerBean("陈冠希", 10)
        performers?.add(cgx)
        val gfc = PerformerBean("郭富城", 10)
        performers?.add(gfc)
        val xtf = PerformerBean("谢霆锋", 10)
        performers?.add(xtf)

        val performerTw =
            PerformerBean("台湾明星：指的是中国台湾的一些有名气的电影，电视演员和歌手，他们具有很高的人气，成名时间早，成名时间久")
        performers?.add(performerTw)

        val rxq = PerformerBean("任贤齐", 10)
        performers?.add(rxq)
        val mtw = PerformerBean("孟庭苇", 10)
        performers?.add(mtw)

        val performerTw2 =
            PerformerBean("台湾新生代明星：指的是中国台湾的一些有名气的电影，电视演员和歌手，他们具有很高的人气，成名时间早，成名时间久")
        performers?.add(performerTw2)

        val rxq2 = PerformerBean("罗志祥", 10)
        performers?.add(rxq2)

        val performerTw3 =
            PerformerBean("台湾明星：指的是中国台湾的一些有名气的电影，电视演员和歌手，他们具有很高的人气，成名时间早，成名时间久")
        performers?.add(performerTw3)

        val rxq3 = PerformerBean("李宗盛", 10)
        performers?.add(rxq3)

        val performerNl = PerformerBean("内陆明星")
        performers?.add(performerNl)

        val lh = PerformerBean("鹿晗", 10)
        performers?.add(lh)
        val wzw = PerformerBean("王志文", 10)
        performers?.add(wzw)
        val yq = PerformerBean("羽泉", 10)
        performers?.add(yq)
        val lxl = PerformerBean("李小璐", 10)
        performers?.add(lxl)
        val hh = PerformerBean("韩红", 10)
        performers?.add(hh)
        val ny = PerformerBean("那英", 10)
        performers?.add(ny)
        val lhh = PerformerBean("刘欢", 10)
        performers?.add(lhh)
        val yk = PerformerBean("杨坤", 10)
        performers?.add(yk)
        val zj = PerformerBean("周杰", 10)
        performers?.add(zj)

        val performerOm = PerformerBean("美国明星")
        performers?.add(performerOm)
        val mm = PerformerBean("梅梅", 10)
        performers?.add(mm)
        val ade = PerformerBean("Gaga", 10)
        performers?.add(ade)
        val hff = PerformerBean("黑寡妇", 10)
        performers?.add(hff)
        val xlz = PerformerBean("小李子", 10)
        performers?.add(xlz)

        val performerNba = PerformerBean("NBA明星")
        performers?.add(performerNba)
        val xhd = PerformerBean("小皇帝", 10)
        performers?.add(xhd)
        val kb = PerformerBean("科比", 10)
        performers?.add(kb)
        val ym = PerformerBean("姚明", 10)
        performers?.add(ym)
        val md = PerformerBean("麦迪", 10)
        performers?.add(md)
        val dlt = PerformerBean("杜兰特", 10)
        performers?.add(dlt)
        val kl = PerformerBean("库里", 10)
        performers?.add(kl)
        val ouw = PerformerBean("欧文", 10)
        performers?.add(ouw)
        val qd = PerformerBean("乔丹", 10)
        performers?.add(qd)
        val alzw = PerformerBean("奥拉朱旺", 10)
        performers?.add(alzw)
        val pp = PerformerBean("皮蓬", 10)
        performers?.add(pp)
        val ldm = PerformerBean("罗德曼", 10)
        performers?.add(ldm)
        val ke = PerformerBean("科尔", 10)
        performers?.add(ke)
        val pesi = PerformerBean("皮尔斯", 10)
        performers?.add(pesi)
        val jnt = PerformerBean("加内特", 10)
        performers?.add(jnt)
        val lal = PerformerBean("雷阿伦", 10)
        performers?.add(lal)
        val zmg = PerformerBean("字母哥", 10)
        performers?.add(zmg)
        val adn = PerformerBean("安东尼", 10)
        performers?.add(adn)

        val performerDy = PerformerBean("导演")
        performers?.add(performerDy)
        val jzk = PerformerBean("贾樟柯", 10)
        performers?.add(jzk)
        val ly = PerformerBean("李杨", 10)
        performers?.add(ly)
        val fxg = PerformerBean("冯小刚", 10)
        performers?.add(fxg)
        val lyy = PerformerBean("娄烨", 10)
        performers?.add(lyy)
        val zym = PerformerBean("张艺谋", 10)
        performers?.add(zym)

        adapter?.notifyDataSetChanged()
    }
}
