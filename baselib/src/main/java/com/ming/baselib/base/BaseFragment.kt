package com.sming.LoveMing.base

import android.content.Context
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog


abstract class BaseFragment : Fragment() {
    var mContext: AppCompatActivity? = null

    //    var StatusbarHeight:Int? = 0
    protected var mBaseView: View? = null
    var statusH: Int = 0 //状态栏高度
    protected abstract fun getLayoutId(): Int
    protected abstract fun initViews(savedInstanceState: Bundle)
    protected abstract fun initData()

    //new Add
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context as AppCompatActivity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusH = QMUIStatusBarHelper.getStatusbarHeight(mContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mBaseView == null) {
            mBaseView = inflater.inflate(getLayoutId(), container, false)
        }
        return mBaseView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            initViews(savedInstanceState)
        } else {
            initViews(savedInstanceState = Bundle())
        }
        initData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    var tipDialog: QMUITipDialog? = null
    fun showProgress(iconType: Int = QMUITipDialog.Builder.ICON_TYPE_LOADING, tit: String = "正在加载") {
        tipDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        tipDialog = QMUITipDialog.Builder(activity)
            .setIconType(iconType)
            .setTipWord(tit)
            .create()
        tipDialog?.show()
    }

    fun dismissProgress() {
        tipDialog?.let {
            it.dismiss()
            tipDialog = null
        }
    }




}