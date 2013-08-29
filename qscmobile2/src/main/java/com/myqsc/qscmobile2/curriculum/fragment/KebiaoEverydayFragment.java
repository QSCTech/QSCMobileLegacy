package com.myqsc.qscmobile2.curriculum.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoClassData;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoEverydayAdapter;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;

import java.util.List;

/**
 * Created by richard on 13-8-29.
 */
public class KebiaoEverydayFragment extends Fragment {
    ListView kebiaoListView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kebiao_everyday, null);
        AwesomeFontHelper.setFontFace((TextView)view.findViewById(R.id.fragment_kebiao_everyday_icon_left)
                , getActivity());
        AwesomeFontHelper.setFontFace((TextView)view.findViewById(R.id.fragment_kebiao_everyday_icon_right)
                , getActivity());

        kebiaoListView = (ListView) view.findViewById(R.id.fragment_kebiao_everyday_listview);
        return view;
    }

    private void setListData(List<KebiaoClassData> data){
        KebiaoEverydayAdapter adapter = new KebiaoEverydayAdapter(data, getActivity());
        kebiaoListView.setAdapter(adapter);
    }


}
