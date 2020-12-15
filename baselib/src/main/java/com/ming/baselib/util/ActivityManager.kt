package com.school.sming.util

import android.app.Activity
import java.util.*
/**
 * @作者: zhan_0313@163.com
 * @创建日期: 2020/5/22 15:27
 * @文件作用: Activity管理
 * class 前面跟object 就是一个单例！
 */
class ActivityManager {
    private val activityList = LinkedList<Activity>()
    companion object{
        private var instance: ActivityManager? = null
        // 单例模式中获取唯一的MyApplication实例
        fun getInstance(): ActivityManager {
            if (null == instance) {
                instance = ActivityManager()
            }
            return instance as ActivityManager
        }
    }
    // 添加Activity到容器中
    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }
    // 删除Activity到容器中  New Add
    fun removeActivity(activity: Activity) {
        activityList.remove(activity)
        activity.finish()
    }
    fun close() {
        for (activity in activityList) {
            activity.finish()
        }
    }

    // 遍历所有Activity并finish
    fun exit() {
        for (activity in activityList) {
            activity.finish()
        }
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}