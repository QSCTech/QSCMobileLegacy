package com.myqsc.mobile2.uti;

import android.os.Handler;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 13-11-10.
 */
public abstract class MyFragment extends Fragment implements DataObservable {
    List<DataObserver> observers = new ArrayList<DataObserver>();

    @Override
    public void addObserver(DataObserver o) {
        observers.add(o);
    }

    @Override
    public void noticeObserver(final int code) {
        for(final DataObserver observer : observers) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogHelper.e("noticed");
                    observer.update(MyFragment.this, code);
                }
            }, 100);
        }
    }
}
