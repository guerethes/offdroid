package br.com.guerethes.orm.engine;

public enum EstrategiaURL {

	PATH,
	QUERY;
	
	public boolean isPath(){
		return (this != null && this == PATH);
	}

	public boolean isQuery(){
		return (this != null && this == QUERY);
	}
	
}