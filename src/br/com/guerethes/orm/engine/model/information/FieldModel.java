/**
 * 
 */
package br.com.guerethes.orm.engine.model.information;

import java.util.List;

import br.com.guerethes.orm.engine.model.information.i.IObjectModel;
import br.com.guerethes.orm.reflection.FieldReflection;

public class FieldModel extends ObjectModel {

	public Class<?> targetClass;

	/**
	 * 
	 * @param objectModel
	 */
	private FieldModel(Object objectModel) {
		super(objectModel);
	}

	/**
	 * 
	 * @param targetClass
	 * @param objectModel
	 */
	public FieldModel(Class<?> targetClass, Object objectModel) {
		super(objectModel);
		this.targetClass = targetClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.h4android.orm.engine.model.information.i.IObjectModel
	 * #getName()
	 */
	@Override
	public String getName() {
		return FieldReflection.getColumnName(targetClass, getField());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.h4android.orm.engine.model.information.i.IObjectModel
	 * #getClass2()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Class<T> getClass2() {
		return (Class<T>) getField().getType();
	}

	@Override
	@Deprecated
	public <T> T get() {
		return super.get();
	}

	public java.lang.reflect.Field getField() {
		return (java.lang.reflect.Field) get();
	}

	@Deprecated
	@Override
	public List<IObjectModel> getFields() {
		return null;
	}

}
