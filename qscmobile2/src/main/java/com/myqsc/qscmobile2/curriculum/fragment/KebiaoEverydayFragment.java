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

import java.util.List;

/**
 * Created by richard on 13-8-29.
 */
public class KebiaoEverydayFragment extends Fragment {
    ListView kebiaoListView = null;
    KebiaoDataHelper helper = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kebiao_everyday, null);
        AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.fragment_kebiao_everyday_icon_left)
                , getActivity());
        AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.fragment_kebiao_everyday_icon_right)
                , getActivity());

        kebiaoListView = (ListView) view.findViewById(R.id.fragment_kebiao_everyday_listview);

        helper = new KebiaoDataHelper(getActivity());
        return view;
    }

    private void setListData(List<KebiaoClassData> data) {
        KebiaoEverydayAdapter adapter = new KebiaoEverydayAdapter(data, getActivity());
        kebiaoListView.setAdapter(adapter);
    }

    private HandleAsyncTaskMessage handleAsyncTaskMessage = new HandleAsyncTaskMessage() {
        @Override
        public void onHandleMessage(Message message) {
            if (message.what == 0) {
                Toast.makeText(getActivity(), (CharSequence) message.obj, Toast.LENGTH_LONG).show();
                return;
            }
            setListData((List<KebiaoClassData>) message.obj);
        }
    };


}
