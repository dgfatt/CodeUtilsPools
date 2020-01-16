package com.ld.pool.common.utils

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi


/**
 * 系统帮助类
 */
object SystemHelper {
    /**
     * 判断本地是否已经安装好了指定的应用程序包
     *
     * @param packageNameTarget ：待判断的 App 包名，如 微博 com.sina.weibo
     * @return 已安装时返回 true,不存在时返回 false
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun appIsExist(context: Context, packageNameTarget: String): Boolean {
        if ("" != packageNameTarget.trim { it <= ' ' }) {
            val packageManager: PackageManager = context.packageManager
            val packageInfoList =
                packageManager.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES)
            for (packageInfo in packageInfoList) {
                val packageNameSource = packageInfo.packageName
                if (packageNameSource == packageNameTarget) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 将本应用置顶到最前端
     * 当本应用位于后台时，则将它切换到最前端
     *
     * @param context
     */
    fun setTopApp(context: Context) {
        if (!isRunningForeground(context)) {
            /**获取ActivityManager */
            val activityManager =
                context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            /**获得当前运行的task(任务) */
            val taskInfoList =
                activityManager.getRunningTasks(100)
            for (taskInfo in taskInfoList) {
                /**找到本应用的 task，并将它切换到前台 */
                if (taskInfo.topActivity!!.packageName == context.packageName) {
                    activityManager.moveTaskToFront(taskInfo.id, 0)
                    break
                }
            }
        }
    }

    /**
     * 判断本应用是否已经位于最前端
     *
     * @param context
     * @return 本应用已经位于最前端时，返回 true；否则返回 false
     */
    private fun isRunningForeground(context: Context): Boolean {
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcessInfoList =
            activityManager.runningAppProcesses
        /**枚举进程 */
        if (appProcessInfoList.size > 0) {
            for (appProcessInfo in appProcessInfoList) {
                if (appProcessInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (appProcessInfo.processName == context.applicationInfo.processName) {
                        return true
                    }
                }
            }
        }
        return false
    }

}