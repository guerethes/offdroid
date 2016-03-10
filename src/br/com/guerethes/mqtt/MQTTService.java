package br.com.guerethes.mqtt;

import java.lang.reflect.Constructor;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

import br.com.guerethes.shared.utils.PreferenceTools;
import android.app.Service;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

public class MQTTService extends Service {

//	public static final String BROKER_URL = "tcp://10.3.225.89:1883";
	public static boolean ativo = true;
	public static int timeout = 10000;
	private MqttClient mqttClient;
	private MqttCallback callback;
	private MQTTUtils utils;
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Worker w = new Worker(startId);
		w.start();
		try {
			Log.e("CALL", PreferenceTools.getPreferencias("key_callback", getApplicationContext()));
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(PreferenceTools.getPreferencias("key_callback", getApplicationContext()));
			@SuppressWarnings({ "rawtypes", "unchecked" })
			Constructor ctor = clazz.getConstructor(ContextWrapper.class);
			callback = (MqttCallback) ctor.newInstance(getApplication());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (START_STICKY);
	}

	class Worker extends Thread {
		public int startId;
		public String clientId = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);

		public Worker(int startId) {
			this.startId = startId;
		}

		public void run() {
			Looper.prepare();
			ativo = true;
			while (ativo) {
				try {
					mqttClient = new MqttClient(PreferenceTools.getPreferencias("key_host", getApplicationContext()), clientId, new MemoryPersistence());
					if (utils == null) {
						String[] topicos;
					    topicos = (String[]) PreferenceTools.getPreferenciasArray("key_topics", getApplicationContext());
						registerCallBack(topicos, clientId, callback, mqttClient, timeout);
						Thread.sleep(timeout);
						mqttClient.disconnect();
					}
				} catch (MqttException e) {
					Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Looper.loop();
		}
	}

	public static void registerCallBack(String[] topicName, String clientId, MqttCallback callback, MqttClient client, int timeout) {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(false);
		options.setKeepAliveInterval(timeout);
		try {
			client.setCallback(callback);
			client.connect(options);
			client.subscribe(topicName);
			for (int i = 0; i < topicName.length; i++) {
				Log.v("Callback", "successfuly registered callback to topic " + topicName[i]);
			}
		} catch (MqttException me) {
			me.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		ativo = false;
	}

}