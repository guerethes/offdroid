/**
 * 
 */
package br.com.guerethes.orm.engine.criterya;

import java.util.List;
import java.util.Map;

import br.com.guerethes.orm.engine.criterya.pattern.ElementsQueryModel1;
import br.com.guerethes.orm.engine.criterya.pattern.IElementsQuery;
import br.com.guerethes.orm.reflection.FieldReflection;
import br.com.guerethes.orm.util.StringUtil;

/**
 * @author timoshenko
 * 
 */
public class Lk extends ElementsQueryModel1 {

	private Lk(String nameObject) {
		super(nameObject);
	}

	public static Lk create(String field, String value) {

		Lk lk = new Lk(field);
		lk.value = value;
		lk.SIGNAL_OPERATOR = "LIKE";
		return lk;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.h4android.orm.engine.criterya.pattern.IElementsQuery#
	 * add(br.com.softctrl.h4android.orm.engine.criterya.pattern.IElementsQuery)
	 */
	@Override
	public IElementsQuery add(IElementsQuery iElementsQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.h4android.orm.engine.criterya.pattern.ElementsQueryModel1
	 * #toSql()
	 */
	@Override
	public String toSql() {
		String sColumn = "";
		if ( getName().lastIndexOf(".") == -1 ) {
			sColumn = FieldReflection.getColumnName(getClassEntity(), getName());
		} else {
			List<Object> values = Query.getJoin().get(getName().substring(0, getName().lastIndexOf(".")));
			sColumn = values.get(0) + "." + FieldReflection.getColumnName( (Class<?>) values.get(1), getName());
		}

		String sValue = StringUtil.objectToString("%" + value + "%");
		return " AND ( " + sColumn + " " + SIGNAL_OPERATOR + " " + sValue + " )";
	}

	@Override
	public IElementsQuery addOrder(IElementsQuery iElementsQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IElementsQuery addLimit(IElementsQuery iElementsQuery) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Map<String, String> toSqlMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
