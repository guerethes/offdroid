package br.com.guerethes.synchronization.networkUtils;

import android.app.Activity;
import android.content.Context;

public class NetWorkUtils extends Activity {
	
	public static Context context;
	
	private static boolean onLine;

	public static Context getContext() {
		return context;
	}
	
	public static void setContext(Context context) {
		NetWorkUtils.context = context;
	}

	public static boolean isOnline() {
		return onLine;
	}

	public static void setOnline(boolean ativo) {
		onLine = ativo;
	}
	
}