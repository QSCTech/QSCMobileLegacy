package com.myqsc.mobile2.curriculum.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.curriculum.CurriculumActivity;
import com.myqsc.mobile2.curriculum.uti.KebiaoClassData;
import com.myqsc.mobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.mobile2.curriculum.uti.KebiaoUtility;
import com.myqsc.mobile2.fragment.FragmentUtility;
import com.myqsc.mobile2.network.DataUpdater;
import com.myqsc.mobile2.network.UpdateHelper;
import com.myqsc.mobile2.uti.BroadcastHelper;
import com.myqsc.mobile2.uti.LogHelper;

import java.util.Calendar;
import java.util.List;

/**
 * Created by richard on 13-9-5.
 */
public class KebiaoCardEmptyFragment extends Fragment {
    Handler handler = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_fragment_kebiao_empty, null);

        handler = new Handler();
        final KebiaoDataHelper kebiaoDataHelper = new KebiaoDataHelper(getActivity());

        handler.post(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                Calendar calendar = Calendar.getInstance();
                List<KebiaoClassData> list = kebiaoDataHelper.getDay(calendar);
                if (KebiaoUtility.getDiffTime(calendar, list) != null) {
                    Intent intent = new Intent(BroadcastHelper.BROADCAST_CARD_REDRAW);
                    intent.putExtra("card", DataUpdater.JW_KEBIAO);
                    if (getActivity() != null)
                        getActivity().sendBroadcast(intent);
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CurriculumActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_push_in, 0);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(null);
    }
}
