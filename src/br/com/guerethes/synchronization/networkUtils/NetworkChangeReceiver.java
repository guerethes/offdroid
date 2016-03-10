package br.com.guerethes.synchronization.networkUtils;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import br.com.guerethes.mqtt.MQTTService;
import br.com.guerethes.mqtt.MQTTUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(final Context context, final Intent intent) {
		if(isOnline(context)){
			NetWorkUtils.setOnline(true);
			startMQTT(context);
		} else {
			if ( MQTTUtils.isReady() )
		        context.stopService(new Intent(context, MQTTService.class));
			NetWorkUtils.setOnline(false);
		}
	}

	private boolean serviceIsRunning() {
		if ( MQTTUtils.isReady() ) {
			ActivityManager manager = (ActivityManager) NetWorkUtils.context.getSystemService(Context.ACTIVITY_SERVICE);
			for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
				if ( MQTTUtils.SERVICE_CLASS.equals(service.service.getClassName()) ) {
					return true;
				}
			}
		}
		return false;
	}
	
//	private void startMQTT(final Context context) {
//		if ( MQTTUtils.getInstance() != null && !serviceIsRunning()) {
//		    context.startService(new Intent(context, MQTTService.class));
//		}
//	}
	
	private void startMQTT(final Context context) {
		if ( MQTTUtils.isReady() && !serviceIsRunning()) {
			@SuppressWarnings("unused")
			Intent mqttService = new Intent(context, MQTTService.class);
			context.startService(new Intent(context, MQTTService.class));
		}
	}
	
	
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
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
	}

}