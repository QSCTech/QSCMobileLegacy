package com.myqsc.mobile2.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.myqsc.mobile2.login.uti.PersonalDataHelper;
import com.myqsc.mobile2.network.DataUpdater;
import com.myqsc.mobile2.network.DataUpdaterRunnable;
import com.myqsc.mobile2.support.database.structure.UserIDStructure;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by richard on 13-9-26.
 */
public class UpdateAllService extends IntentService {
    Context context = null;

    final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.obj != null) {
                String key = message.getData().getString("key");
                String data = (String) message.obj;

                LogHelper.d(key + " Service update complete");

                try {
                    JSONObject jsonObject = new JSONObject(data);

                    String msg = jsonObject.getString("msg");
                    //意味着有错误
                    if (context != null)
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    //没有msg
                    if (context != null)
                        context.getSharedPreferences(Utility.PREFERENCE, 0)
                                .edit()
                                .putString(key, data)
                                .commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }
    });

    public UpdateAllService() {
        super("UpdateAllService");
        context = this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Context context = this;

        ExecutorService service = Executors.newFixedThreadPool(2);

        final PersonalDataHelper helper = new PersonalDataHelper(context);
        final UserIDStructure structure = helper.getCurrentUser();
        if (structure == null)
            return;
        List<String> subs = new ArrayList<String>();

        if (intent.getStringArrayExtra("update") == null) {
            //全部更新
            for (String name : DataUpdater.name.keySet()) {
                subs.add(name);
            }
        } else {
            //部分更新
            String[] names = intent.getStringArrayExtra("update");
            for (String name : names) {
                subs.add(name);
            }
        }

        for (String name : subs) {
            service.submit(new DataUpdaterRunnable(name, handler, this));
        }
    }
}
