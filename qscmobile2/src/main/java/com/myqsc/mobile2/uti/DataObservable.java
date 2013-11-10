package com.myqsc.mobile2.uti;

import java.util.Observer;

/**
 * Created by richard on 13-11-10.
 */
public interface DataObservable {
    /**
     * 添加一个观察者
     * @param o
     */
    public void addObserver(DataObserver o);

    /**
     * 通知所有的观察者
     */
    public void noticeObserver();
}
