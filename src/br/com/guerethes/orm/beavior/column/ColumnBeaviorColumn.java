package br.com.guerethes.orm.beavior.column;

import java.lang.reflect.Field;

import br.com.guerethes.orm.annotation.ddl.Column;
import br.com.guerethes.orm.beavior.column.i.IColumnBeavior;
import br.com.guerethes.orm.enumeration.validation.TypeColumn;

public class ColumnBeaviorColumn implements IColumnBeavior {

	public String getNameColumn(Field field) {
		Column c = field.getAnnotation(Column.class);
		return c.name();
	}

	public String getDDLColumn(Field field) {
		String ddl;
		Column column = field.getAnnotation(Column.class);
		ddl = column.name() + " " + column.typeColumn();
		if (column.typeColumn() == TypeColumn.VARCHAR) {
			ddl += "(" + column.length() + ")";
		}
		if (!column.allowNulls()) {
			ddl += " NOT NULL";
		}
		return ddl;
	}

}
