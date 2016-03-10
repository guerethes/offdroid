package br.com.guerethes.mqtt;

public enum TypeQoS {

	AT_MOST_ONCE(0),
	AT_LEAST_ONCE(1),
	EXACTLY_ONCE(2);
	
	private java.lang.Integer value;

	private TypeQoS(java.lang.Integer value) {
		this.value = value;
	}
	
	public Integer getValue(){
		return this.value;
	}
	
}