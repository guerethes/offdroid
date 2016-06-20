package br.com.guerethes.mqtt;

import java.lang.reflect.Constructor;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import br.com.guerethes.shared.utils.PreferenceTools;
import br.com.guerethes.synchronization.networkUtils.NetWorkUtils;

public class MQTTClient {

	private static MqttAsyncClient mqttClient = null; 
	private static MqttCallback callback = null;
	
	public static void start(Context context) throws Exception {
		if ( MQTTUtils.isReady(context) && (mqttClient == null || !mqttClient.isConnected() ) ) {
			Log.i("MQTT OffDroid", "start");
			NetWorkUtils.setContext(context);
			String host = PreferenceTools.getPreferencias("key_host", context);
			String login = PreferenceTools.getPreferencias("key_login", context);
			String pass = PreferenceTools.getPreferencias("key_pass", context);
			String clientId = PreferenceTools.getPreferencias("key_clientId", context);
			String qosTemp = PreferenceTools.getPreferencias("key_qos", context);
			String[] topicos = (String[]) PreferenceTools.getPreferenciasArray("key_topics", context);
			String callbackClass = PreferenceTools.getPreferencias("key_callback", context);
			
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(callbackClass);
			@SuppressWarnings({ "rawtypes", "unchecked" })
			Constructor ctor = clazz.getConstructor(ContextWrapper.class);
			callback = (MqttCallback) ctor.newInstance(context);
			
			try {
				mqttClient = new MqttAsyncClient(host, clientId, new MemoryPersistence());
				MqttConnectOptions connOpts = new MqttConnectOptions();
				connOpts.setCleanSession(false);
				connOpts.setKeepAliveInterval(5);
				
				//Inserindo login e senha do MQTT
				if ( (login != null && !login.isEmpty() ) && (pass != null && !pass.isEmpty() ) ) {
					connOpts.setUserName(login);
					connOpts.setPassword(pass.toCharArray());
				}
				
				mqttClient.setCallback(callback);
				mqttClient.connect(connOpts);
				while (!mqttClient.isConnected()) {}
				int qos = Integer.parseInt(qosTemp);
				
				for (int i = 0; i < topicos.length; i++) {
					mqttClient.subscribe(topicos[i], qos);
					Log.i("Subscribe Topic", topicos[i]);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void disconnect() throws MqttException {
		if ( mqttClient != null && mqttClient.isConnected() ) {
			mqttClient.disconnect();
			Log.i("MQTTClient", "Disconectando");
		} else {
			Log.i("MQTTClient", "já disconectado");			
		}
	}
	
}