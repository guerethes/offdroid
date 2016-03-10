/**
 * 
 */
package br.com.guerethes.orm.engine.model.information;

import java.util.List;

import br.com.guerethes.orm.engine.model.information.i.IObjectModel;
import br.com.guerethes.orm.reflection.EntityReflection;

/**
 * @author timoshenko
 * 
 */
public class TableModel extends ObjectModel {

	private java.util.List<IObjectModel> fields;

	public TableModel(Object objectModel) {
		super(objectModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.h4android.orm.engine.model.information.ObjectModel#isSimple
	 * ()
	 */
	@Override
	public Boolean isSimple() {
		return false;
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
		return EntityReflection.getTableName(get().getClass());
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
		return (Class<T>) get().getClass();
	}

	@Override
	public List<IObjectModel> getFields() {
		return fields;
	}

}
