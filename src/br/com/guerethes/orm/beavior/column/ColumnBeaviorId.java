package br.com.guerethes.orm.beavior.column;

import java.lang.reflect.Field;

import br.com.guerethes.orm.beavior.column.i.IColumnBeavior;

public class ColumnBeaviorId implements IColumnBeavior {

	public String getNameColumn(Field field) {
		return "_ID";
	}

	public String getDDLColumn(Field field) {
		return "_ID INTEGER PRIMARY KEY AUTOINCREMENT";
	}

}