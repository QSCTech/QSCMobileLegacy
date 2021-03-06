package com.myqsc.mobile2.xiaoche;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.myqsc.mobile2.xiaoche.uti.XiaocheAdapter;
import com.myqsc.mobile2.xiaoche.uti.XiaocheDataHelper;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class XiaocheActivity extends SwipeBackActivity{
    private String FROM[] = {
            "紫金港", "玉泉", "西溪", "之江", "华家池"
    };
    private String TO[] = {
            "玉泉", "紫金港", "西溪", "之江", "华家池"
    };

	ListView listView = null;
	Context mContext = null;
    XiaocheDataHelper helper = null;
    int from = 0, to = 0;

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiaoche);
		mContext = this;
		AwesomeFontHelper.setFontFace((TextView) findViewById(R.id.activity_xiaoche_exchange), this);
		
		listView = (ListView) findViewById(R.id.activity_xiaoche_listview);
		
		helper = new XiaocheDataHelper(mContext);

        Spinner fromSpinner = (Spinner) findViewById(R.id.activity_xiaoche_spinner_left);
        Spinner toSpinner = (Spinner) findViewById(R.id.activity_xiaoche_spinner_right);
        ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                FROM);
        ArrayAdapter<String> toAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                TO);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);
        toSpinner.setAdapter(toAdapter);
        fromSpinner.setOnItemSelectedListener(onItemSelectedListener);
        toSpinner.setOnItemSelectedListener(onItemSelectedListener);

        setData();
	}

    final Spinner.OnItemSelectedListener onItemSelectedListener = new Spinner.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getId() == R.id.activity_xiaoche_spinner_left) {
                from = i;
            } else {
                to = i;
            }
            setData();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private void setData() {
        XiaocheAdapter adapter = new XiaocheAdapter(helper.getBus(FROM[from], TO[to]),
                mContext, listView);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            scrollToFinishActivity();
            return true;
        }
        return false;
    }
}
