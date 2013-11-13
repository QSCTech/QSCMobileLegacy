package com.myqsc.mobile2.uti;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

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
    public void noticeObserver() {
        for(DataObserver observer : observers)
            observer.update(this);
    }
}
