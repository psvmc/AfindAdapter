package com.sming.LoveMing.base

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.KeyEvent
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.school.sming.util.ActivityManager
import com.school.sming.util.IsInternet
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


abstract class BaseActivity : AppCompatActivity(), LifecycleProvider<ActivityEvent> {
    private val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()
    var myLoading: QMUITipDialog? = null
    var statusH: Int = 0
    protected abstract fun getContentView(): Int
    protected abstract fun initView()
    protected abstract fun initData()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this.requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        QMUIStatusBarHelper.translucent(this)
        changeStatusColor(true) //修改状态栏 颜色 默认黑  传参false时 变更为白色
        statusH = QMUIStatusBarHelper.getStatusbarHeight(this)
        setContentView(getContentView())
        ActivityManager.getInstance().addActivity(this)
        lifecycleSubject.onNext(ActivityEvent.CREATE)

        initView()
        initData()


    }

    fun checkNetWork() {
        if (!isNetWorkConnent()) {
            val dialog = AlertDialog.Builder(this)
                .setTitle("网络检查")
                .setMessage("网络尚未连接，要连接网络吗？")
                .setPositiveButton("是"
                ) { dialog, _ ->
                    dialog?.dismiss()
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }.create()

            dialog.setCanceledOnTouchOutside(false);
            dialog.show()

        }
    }

    private fun isNetWorkConnent(): Boolean {

        val manager = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.getActiveNetworkInfo()
        if (info != null) {
            return info.isAvailable();
        }
        return false
    }


    override fun getResources(): Resources {
        val res = super.getResources()
        if (res != null) {
            val config = res.configuration
            if (config != null && config.fontScale !== 1.0f) {
                config.fontScale = 1.0f
                res.updateConfiguration(config, res.displayMetrics)
            }
        }
        return res
    }

    @NonNull
    @CheckResult
    override fun lifecycle(): Observable<ActivityEvent> { //<ActivityEvent>
        return lifecycleSubject.hide()
    }

    @NonNull
    @CheckResult
    override fun <T> bindUntilEvent(@NonNull event: ActivityEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
    }

    @NonNull
    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(ActivityEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(ActivityEvent.RESUME)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        lifecycleSubject.onNext(ActivityEvent.STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleSubject.onNext(ActivityEvent.DESTROY)
        ActivityManager.getInstance().removeActivity(this)
    }
    // My New Add
    /**
     * @param icon  图片
     * @param title 标题
     * @param iscancelable 是否可点击取消  该对象为 myLoading
     */
    fun showMyLoadD(icon: Int, title: String, iscancelable: Boolean) {
        if (title == "" || TextUtils.isEmpty(title)) {
            myLoading = QMUITipDialog.Builder(this).setIconType(icon).create(iscancelable)
        } else {
            myLoading =
                QMUITipDialog.Builder(this).setIconType(icon).setTipWord(title).create(iscancelable)
        }
        myLoading?.show()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation !== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!IsInternet.isNetworkAvalible(this@BaseActivity)) {
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    //修改状态栏颜色
    private fun changeStatusColor(value: Boolean = false) {
        if (value) {
            QMUIStatusBarHelper.setStatusBarLightMode(this) //黑色
        } else {
            QMUIStatusBarHelper.setStatusBarDarkMode(this)   //白色
        }
    }

    var tipDialog: QMUITipDialog? = null
    fun showProgress() {
        dismissProgress()
        tipDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("加载中...")
            .create()
        tipDialog?.show()

    }

    //自定义文字
    fun showProgress(content: String) {
        tipDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord(content)
            .create()
        tipDialog?.show()
    }

    fun dismissProgress() {
        if (tipDialog != null) {
            tipDialog?.dismiss()
            tipDialog = null
        }
    }


    fun showConfimDialog(title: String, msg: String, obj: () -> Unit) {
        AlertDialog
            .Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setNegativeButton(
                "取消"
            ) { dialog, which -> dialog?.dismiss() }
            .setPositiveButton(
                "确定"
            ) { dialog, which ->
                dialog?.dismiss()
                obj.invoke()
            }
            .create()
            .show()
    }


}