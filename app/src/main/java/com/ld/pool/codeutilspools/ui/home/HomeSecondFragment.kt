package com.ld.pool.codeutilspools.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ld.pool.codeutilspools.R
import com.ld.pool.common.utils.SystemHelper.appIsExist
import com.ld.pool.common.utils.SystemHelper.setTopApp


class HomeSecondFragment : Fragment() {

    private val args: HomeSecondFragmentArgs by this.navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_second, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.textview_home_second).text =
            getString(R.string.hello_home_second, args.myArg)

        view.findViewById<Button>(R.id.button_home_second).setOnClickListener {
            findNavController().navigate(R.id.action_HomeSecondFragment_to_HomeFragment)
        }

        initView(view)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView(view: View) {
        view.findViewById<Button>(R.id.btnStart).setOnClickListener {
            try {
                Log.i(
                    "Wmx Logs::",
                    "开始按钮被点击了 id = " + view.id.toString() +
                            "线程 = " + Thread.currentThread().name
                )

                /**
                 * 启动手机上 微博 APP (包名 com.sina.weibo)
                 * 休眠 10 秒
                 */
                startLocalApp("com.shanghai3h.xunjian")
                Thread.sleep(10000)
                /**最后将被挤压到后台的本应用重新置顶到最前端
                 * 当自己的应用在后台时，将它切换到前台来 */
                setTopApp(activity!!)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 启动本地安装好的第三方 APP
     * 注意：此种当时启动第三方 APP 时，如果第三方 APP 当时没有运行，则会启动它
     * 如果被启动的 APP 本身已经在运行，则直接将它从后台切换到最前端
     *
     * @param packageNameTarget :App 包名、如
     * 微博 com.sina.weibo、
     * 飞猪 com.taobao.trip、
     * QQ com.tencent.mobileqq、
     * 腾讯新闻 com.tencent.news
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun startLocalApp(packageNameTarget: String) {
        Log.i("Wmx logs::", "-----------------------开始启动第三方 APP=$packageNameTarget")
        if (appIsExist(activity!!, packageNameTarget)) {
            val packageManager: PackageManager? = activity?.packageManager
            val intent = packageManager?.getLaunchIntentForPackage(packageNameTarget)
            intent!!.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.flags =
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK
            /**android.intent.action.MAIN：打开另一程序
             */
            intent.action = "android.intent.action.MAIN"
            /**
             * FLAG_ACTIVITY_SINGLE_TOP:
             * 如果当前栈顶的activity就是要启动的activity,则不会再启动一个新的activity
             */
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        } else {
            Toast.makeText(activity?.applicationContext, "被启动的 APP 未安装", Toast.LENGTH_SHORT).show()
        }
    }

}
