package com.sming.LoveMing.base;

import com.uber.autodispose.AutoDisposeConverter;

public interface BaseView {

    void goodBye();

    void showToast(String str);

    /**
     * 显示加载中
     */
    void showLoading();
    /**
     * 自定义加载提示内容
     */
    void showLoading(String content);

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 数据获取失败
     *
     * @param throwable
     */
    void onError(Throwable throwable);

    void endRefreshAndLoadMore();

    /**
     * 绑定Android生命周期 防止RxJava内存泄漏
     *
     * @param <T>
     * @return
     */
    <T> AutoDisposeConverter<T> bindAutoDispose();
}
