package br.com.guerethes.orm.engine.i;

import java.io.Serializable;

public interface PersistDB extends Serializable {
	
	public Integer getId();
	public void setId(Integer id);
	public boolean equals(Object o);
	public int hashCode();
}