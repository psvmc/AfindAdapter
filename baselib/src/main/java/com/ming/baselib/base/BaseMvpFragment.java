package com.sming.LoveMing.base;

import android.os.Bundle;

import androidx.lifecycle.Lifecycle;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.sming.LoveMing.util.ToastUtil;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import org.jetbrains.annotations.NotNull;

public abstract class BaseMvpFragment<T extends BasePresenter> extends BaseFragment implements BaseView {

    protected T mPresenter;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroyView();
    }

    /**
     * 绑定生命周期 防止MVP内存泄漏
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }

    @Override
    public void goodBye() {
        getActivity().finish();
    }

    @Override
    public void showToast(String str) {
        ToastUtil.showShort(BaseApplication.getInstance(), str);
    }

    @Override
    public void showLoading() {
        showProgress(QMUITipDialog.Builder.ICON_TYPE_LOADING,"正在加载");
    }
    @Override
    public void showLoading(String content) {
        showProgress(QMUITipDialog.Builder.ICON_TYPE_LOADING,content);
    }


    @Override
    public void hideLoading() {
        dismissProgress();
    }

    @Override
    public void onError(Throwable throwable) {
        ToastUtil.showShort(BaseApplication.getInstance(), "页面加载异常，请重试！");
    }

    @Override
    protected void initViews(@NotNull Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void endRefreshAndLoadMore() {

    }

}
