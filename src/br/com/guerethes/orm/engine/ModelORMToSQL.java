package br.com.guerethes.orm.engine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.guerethes.orm.engine.i.IModelORMDDL;
import br.com.guerethes.orm.exception.NotEntityException;
import br.com.guerethes.orm.reflection.EntityReflection;
import br.com.guerethes.orm.reflection.FieldReflection;
import br.com.guerethes.orm.util.StringUtil;

public class ModelORMToSQL implements IModelORMDDL {

	private Class<?> targetClass;

	private String tableName;
	private List<java.lang.String> columns;

	public ModelORMToSQL(Class<?> targetClass) throws NotEntityException {
		super();
		if (EntityReflection.isEntity(targetClass)) {
			this.targetClass = targetClass;
			this.tableName = EntityReflection.getTableName(this.targetClass);
		} else {
			throw new NotEntityException("A classe " + targetClass.toString()
					+ " não corresponde a uma entidade mapeada válida!");
		}

	}

	@Override
	public String createSQL() {

		List<Field> fields = EntityReflection.getEntityFields(targetClass);
		this.columns = new ArrayList<java.lang.String>();
		for (Field field : fields) {
			String column = FieldReflection.getColumnNameDDL(this.targetClass, field);
			if (!column.equals("")) {
				this.columns.add(column);
			}
		}
		return StringUtil.createSQL(this.tableName, this.columns);
	}

	@Override
	public String createSQL(String table) {
		if ( table.equals(this.tableName) ) {
			List<Field> fields = EntityReflection.getEntityFields(targetClass);
			this.columns = new ArrayList<java.lang.String>();
			for (Field field : fields) {
				String column = FieldReflection.getColumnNameDDL(this.targetClass, field);
				if (!column.equals("")) {
					this.columns.add(column);
				}
			}
			return StringUtil.createSQL(this.tableName, this.columns);
		}
		return null;
	}
	
	@Override
	public String dropSQL() {
		return StringUtil.dropSQL(this.tableName);
	}
}
