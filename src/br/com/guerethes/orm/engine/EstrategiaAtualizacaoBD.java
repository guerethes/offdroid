package br.com.guerethes.orm.engine;

public enum EstrategiaAtualizacaoBD {

	UPDATE_TABLE,
	DROP_CREATE_ALL_TABLE;
	
	public boolean isUpdate(){
		return (this != null && this == UPDATE_TABLE);
	}

	public boolean isCreate(){
		return (this != null && this == DROP_CREATE_ALL_TABLE);
	}
	
}