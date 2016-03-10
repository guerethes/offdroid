/**
 * 
 */
package br.com.guerethes.orm.engine.model.information.i;

public interface IObjectModel {

	public Boolean isSimple();
	
	public java.util.List<IObjectModel> getFields();

	public String getName();

	public <T> Class<T> getClass2();

	public <T> T get();

}