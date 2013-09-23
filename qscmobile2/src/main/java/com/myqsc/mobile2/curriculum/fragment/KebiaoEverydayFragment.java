package com.myqsc.mobile2.curriculum.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.mobile2.curriculum.uti.KebiaoEverydayAdapter;
import com.myqsc.mobile2.curriculum.uti.KebiaoUtility;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;

import java.util.Calendar;

/**
 * Created by richard on 13-8-29.
 */
public class KebiaoEverydayFragment extends Fragment {
    ListView kebiaoListView = null;
    KebiaoDataHelper helper = null;
    Calendar calendar = Calendar.getInstance();
    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_kebiao_everyday, null);

        kebiaoListView = (ListView) view.findViewById(R.id.fragment_kebiao_everyday_listview);
        helper = new KebiaoDataHelper(getActivity());

        Utility.initCheckBar(view, getActivity(), onClickListener);

        setListData();
        return view;
    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == Utility.ICON_LEFT){
                //左
                calendar.add(Calendar.DATE, -1);
            } else{
                //右
                calendar.add(Calendar.DATE, 1);
            }
            LogHelper.d(calendar.get(Calendar.DAY_OF_WEEK) + " " + calendar.get(Calendar.DATE));
            setListData();
        }
    };

    private void setListData() {
        KebiaoEverydayAdapter adapter = new KebiaoEverydayAdapter(
                helper.getDay(calendar),
                getActivity()
        );
        kebiaoListView.setAdapter(adapter);
        Utility.setCheckBarTitle(KebiaoUtility.processTimeTitle(calendar), view);
        Utility.setListViewHeightBasedOnChildren(kebiaoListView);
    }
}
