package com.myqsc.qscmobile2.uti;

public final class LogHelper {
	final static String TAG = "Mobile";
	
	public static void i(String data){
		android.util.Log.i(TAG, buildMessage(data));
	}
	
	public static void d(String data){
		android.util.Log.d(TAG, buildMessage(data));
	}
	
	protected static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];

        return new StringBuilder()
                .append(caller.getClassName())
                .append(".")
                .append(caller.getMethodName())
                .append("(): ")
                .append(msg).toString();
    }
}
