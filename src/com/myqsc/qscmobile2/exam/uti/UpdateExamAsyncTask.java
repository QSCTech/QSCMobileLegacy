package com.myqsc.qscmobile2.exam.uti;

import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;

import android.os.AsyncTask;
import android.os.Message;

public abstract class UpdateExamAsyncTask extends AsyncTask<Void, Message, Message> implements HandleAsyncTaskMessage {

	@Override
	protected Message doInBackground(Void... params) {
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
