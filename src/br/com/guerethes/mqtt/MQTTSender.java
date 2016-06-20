package br.com.guerethes.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.content.Context;
import android.provider.Settings.Secure;
import br.com.guerethes.synchronization.networkUtils.NetWorkUtils;

public class MQTTSender {
	static MqttClient client;

	public static void publicar(String msg, TypeQoS qos, String topic, Context context) {
		try {
			String clientId = Secure.getString(NetWorkUtils.context.getContentResolver(), Secure.ANDROID_ID);
			MqttConnectOptions mqttOptions = new MqttConnectOptions();
			mqttOptions.setCleanSession(false);
			client = new MqttClient(MQTTUtils.getHost(context), clientId, new MemoryPersistence());
			MqttTopic mqttTopic = client.getTopic(topic);
			client.connect(mqttOptions);
			mqttTopic.publish(msg.getBytes(), qos.getValue(), false);
			client.unsubscribe(topic);
			client.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}