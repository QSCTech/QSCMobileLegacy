package com.myqsc.qscmobile2.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.fragment.cardlist.FunctionStructure;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.uti.Utility;
import com.myqsc.qscmobile2.xiaoli.fragment.XiaoliCardFragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

@SuppressLint("ValidFragment")
public class CardFragment extends Fragment {
	View view = null;
	LinearLayout baseLayout = null;
	List<String> list = null;
    FragmentManager fragmentManager = null;

    final static int FRAGMENT_MAGIC_NUM = 0XDD00;

	public CardFragment() {
		this.list = new ArrayList<String>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter intentFilter = new IntentFilter(
				BroadcastHelper.BROADCAST_FUNCTIONLIST_CHANGED);
		getActivity().registerReceiver(new receiver(), intentFilter);
		this.list = getListFromPreference();
		if (this.list == null)
			this.list = new ArrayList<String>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = (ScrollView) inflater.inflate(R.layout.fragment_card, null);
		baseLayout = (LinearLayout) view
				.findViewById(R.id.fragment_card_layout);
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentInflate(baseLayout, LayoutInflater.from(getActivity()), list);
		return view;
	}

	private class receiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			list = getListFromPreference();
			if (list == null)
				list = new ArrayList<String>();
			baseLayout.removeAllViews();
			final LayoutInflater inflater = LayoutInflater.from(getActivity());
			fragmentInflate(baseLayout, inflater, list);
		}
	}

	private List<String> getListFromPreference() {
		if (getActivity() == null)
			return null;
		String encode = getActivity().getSharedPreferences(Utility.PREFERENCE,
				0).getString(FunctionStructure.PREFERENCE, null);
		if (encode == null)
			return null;
		
		List<String> list = new ArrayList<String>();
		try {
			JSONArray jsonArray = new JSONArray(encode);
			for(int i = 0; i != jsonArray.length(); ++i)
				list.add(jsonArray.optString(i));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



    private void fragmentInflate(LinearLayout linearLayout,
			LayoutInflater inflater, List<String> cardList) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment fragment[] = new Fragment[cardList.size()];

		for (int i = 0; i != cardList.size(); ++i) {
            String name = cardList.get(i);
            LogHelper.d(name);

			fragment[i] = fragmentManager.findFragmentByTag(name);
			if (fragment[i] != null) {
                transaction.remove(fragment[i]);
            }
			fragment[i] = FragmentUtility.getCardFragmentByName(name);
		}

        linearLayout.removeAllViews();

        LinearLayout tempLayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_card_background, null);
        tempLayout.findViewById(R.id.fragment_card).setId(1000);
        baseLayout.addView(tempLayout);
        transaction.add(1000, new XiaoliCardFragment());

        for(int i = 0; i != cardList.size(); ++i) {
            LinearLayout layout = (LinearLayout) inflater.inflate(
                    R.layout.fragment_card_background, null);
            layout.findViewById(R.id.fragment_card).setId(i + FRAGMENT_MAGIC_NUM);
            baseLayout.addView(layout);
            transaction.replace(i + FRAGMENT_MAGIC_NUM, fragment[i], cardList.get(i));
        }
		transaction.commit();
	}
}
