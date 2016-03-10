package br.com.guerethes.notification.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

@SuppressLint("NewApi")
public class NotificationUtils extends Activity {
	
	public static void notify(Context context, Intent intent, int icon, String title, String body){
		notify(context, intent, icon, title, body, 0xffffffff);
	}
		
	public static void notify(Context context, Intent intent, int icon, String title, String body, int cor){
		Notification.Builder builder = new Notification.Builder(context);
		PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent, 0);
		builder.setContentIntent(pendingIntent1)
            .setSmallIcon(icon)
            .setTicker("My Ticker")
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS)
            .setLights(cor, 300, 100)
            .setContentTitle(title)
            .setContentText(body);
        
		@SuppressWarnings("deprecation")
		Notification notification = builder.getNotification();
		notification.ledARGB = cor;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.ledOnMS = 1000;
		notification.ledOffMS = 3000;
        
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
	}

	public static void toatsShort(Context c, String msg){
		Toast.makeText(c.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void toatsLong(Context c, String msg){
		Toast.makeText(c.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

}