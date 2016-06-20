package br.com.guerethes.synchronization.networkUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import br.com.guerethes.mqtt.MQTTUtils;
import br.com.guerethes.mqtt.OffDroidService;
import br.com.guerethes.sync.utils.SyncUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(final Context context, final Intent intent) {
		NetWorkUtils.setContext(context);
		if( NetWorkUtils.isOnline(context) ) {
			NetWorkUtils.setOnline(true);
			SyncUtils.sincronizar();
			if( MQTTUtils.isReady(context) )
                context.startService(new Intent(context, OffDroidService.class));
		} else {
			NetWorkUtils.setOnline(false);
		}
	}

}