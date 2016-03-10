/**
 * 
 */
package br.com.guerethes.orm.engine.model.information;

import br.com.guerethes.orm.engine.model.information.i.IObjectModel;

/**
 * @author timoshenko
 * @param <T>
 * 
 */
public abstract class ObjectModel implements IObjectModel {

	private Object objectModel;

	public ObjectModel(Object objectModel) {
		super();
		this.objectModel = objectModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.h4android.orm.engine.model.information.i.IObjectModel
	 * #get()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T get() {
		return (T) objectModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.h4android.orm.engine.model.information.i.IObjectModel
	 * #isSimple()
	 */
	@Override
	public Boolean isSimple() {
		return true;
	}

}
