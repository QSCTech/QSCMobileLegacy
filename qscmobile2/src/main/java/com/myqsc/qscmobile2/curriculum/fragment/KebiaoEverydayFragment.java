package com.myqsc.qscmobile2.curriculum.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoClassData;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoEverydayAdapter;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.uti.Utility;

import java.util.Calendar;
import java.util.List;

/**
 * Created by richard on 13-8-29.
 */
public class KebiaoEverydayFragment extends Fragment {
    ListView kebiaoListView = null;
    KebiaoDataHelper helper = null;
    Calendar calendar = Calendar.getInstance();
    TextView dateTextView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kebiao_everyday, null);
        AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.fragment_kebiao_everyday_icon_left)
                , getActivity());
        AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.fragment_kebiao_everyday_icon_right)
                , getActivity());

        kebiaoListView = (ListView) view.findViewById(R.id.fragment_kebiao_everyday_listview);
        helper = new KebiaoDataHelper(getActivity());

        view.findViewById(R.id.fragment_kebiao_everyday_icon_left)
                .setOnClickListener(onClickListener);
        view.findViewById(R.id.fragment_kebiao_everyday_icon_right)
                .setOnClickListener(onClickListener);
        dateTextView = (TextView) view.findViewById(R.id.fragment_kebiao_everyday_title);

        setListData();
        return view;
    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.fragment_kebiao_everyday_icon_left){
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
        dateTextView.setText(com.myqsc.qscmobile2.curriculum.uti.Utility.processTimeTitle(calendar));
        Utility.setListViewHeightBasedOnChildren(kebiaoListView);
    }
}
