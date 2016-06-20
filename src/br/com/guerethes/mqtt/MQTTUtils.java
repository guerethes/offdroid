package br.com.guerethes.mqtt;

import android.content.Context;
import br.com.guerethes.shared.utils.PreferenceTools;

public class MQTTUtils {
	
	public void addTopic(String topic, Context context) {
		String[] topics = (String[]) PreferenceTools.getPreferenciasArray("key_topics", context);
		String[] topicTemp = new String[topics.length+1];
		int i = 0;
		for ( ; i < topics.length; i++)
			topicTemp[i] = topics[i];
		topicTemp[i] = topic;
		topics = topicTemp;
		PreferenceTools.setPreferencias(topicTemp, "key_topics", true, context);
	}
	
	public static void setInstance(String host, String[] topics, String clientId, String callback, TypeQoS type, Context context) {
		PreferenceTools.setPreferencias(host, "key_host", true, context);
		PreferenceTools.setPreferencias(callback, "key_callback", true, context);
		PreferenceTools.setPreferencias(topics, "key_topics", true, context);
		PreferenceTools.setPreferencias(clientId, "key_clientId", true, context);
		PreferenceTools.setPreferencias(type.getValue(), "key_qos", true, context);
	}
	
	public static void setInstance(String host, String[] topics, String clientId, String callback, TypeQoS type, 
			String login, String pass, Context context) {
		setInstance(host, topics, clientId, callback, type, context);
		PreferenceTools.setPreferencias(login, "key_login", true, context);
		PreferenceTools.setPreferencias(pass, "key_pass", true, context);
	}

	public static boolean isReady(Context context) {
		return ( !PreferenceTools.getPreferencias("key_host", context).isEmpty() &&
			!PreferenceTools.getPreferencias("key_callback", context).isEmpty() &&
			!PreferenceTools.getPreferencias("key_topics", context).isEmpty() );
	}

	public static String getHost(Context context) {
		return PreferenceTools.getPreferencias("key_host", context);
	}
	
}