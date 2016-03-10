package br.com.guerethes.mqtt;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import android.content.ContextWrapper;

public class PushCallback implements MqttCallback {

	public ContextWrapper context;
    
    public PushCallback(ContextWrapper context) {
        this.context = context;
    }
	
	@Override
	public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {

	}
	
	@Override
	public void connectionLost(Throwable arg0) {
		
	}

	@Override
	public void deliveryComplete(MqttDeliveryToken arg0) {
		
	}

}