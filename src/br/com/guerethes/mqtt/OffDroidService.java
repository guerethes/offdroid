package br.com.guerethes.mqtt;

import br.com.guerethes.synchronization.networkUtils.NetWorkUtils;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class OffDroidService extends Service {

    private BroadcastReceiver offDroidReceiver;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }    

    @Override
    public void onCreate() {
    	Log.i("OffDroidService", "onCreate()");
        super.onCreate();
        final IntentFilter mIntentFilter = new IntentFilter();
	    mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
	    mIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        this.offDroidReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ( NetWorkUtils.isOnline(context) ) {
					startMQTT(context);
				}
            }
        };
        
        this.registerReceiver(this.offDroidReceiver, mIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.offDroidReceiver);
    }
    
    private void startMQTT(final Context context) {
		try {
			MQTTClient.start(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}