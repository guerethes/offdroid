package br.com.guerethes.mqtt;

import br.com.guerethes.shared.utils.PreferenceTools;
import br.com.guerethes.synchronization.networkUtils.NetWorkUtils;

public class MQTTUtils {
	
	public static final String SERVICE_CLASS = "br.com.guerethes.mqtt.MQTTService";

	public void addTopic(String topic) {
		String[] topics = (String[]) PreferenceTools.getPreferenciasArray("key_topics", NetWorkUtils.context);
		String[] topicTemp = new String[topics.length+1];
		int i = 0;
		for ( ; i < topics.length; i++)
			topicTemp[i] = topics[i];
		topicTemp[i] = topic;
		topics = topicTemp;
		PreferenceTools.setPreferencias(topicTemp, "key_topics", true, NetWorkUtils.context);
	}
	
	public static void setInstance(String host, String[] topics, String callback) {
		PreferenceTools.setPreferencias(host, "key_host", true, NetWorkUtils.context);
		PreferenceTools.setPreferencias(callback, "key_callback", true, NetWorkUtils.context);
		PreferenceTools.setPreferencias(topics , "key_topics", true, NetWorkUtils.context);	
			
	}

	public static boolean isReady() {
		return ( !PreferenceTools.getPreferencias("key_host", NetWorkUtils.context).isEmpty() &&
			!PreferenceTools.getPreferencias("key_callback", NetWorkUtils.context).isEmpty() &&
			!PreferenceTools.getPreferencias("key_topics", NetWorkUtils.context).isEmpty() );
	}

	public static String getHost() {
		return PreferenceTools.getPreferencias("key_host", NetWorkUtils.context);
	}
	
}