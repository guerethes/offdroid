package br.com.guerethes.synchronization.networkUtils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class NetWorkUtils extends Activity {
	
	public static Context context;
	
	private static boolean onLine;

	public static boolean isOnline(Context context) {
		return ( isConnectedWifi(context) || isConnected() );
	}
	
	private static boolean isConnected() {
		try {
			Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com.br");
			return p1.waitFor() == 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean isConnectedWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
	}
	
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