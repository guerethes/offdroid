package br.com.guerethes.orm.utils;

import br.com.guerethes.orm.engine.i.PersistDB;
import br.com.guerethes.orm.reflection.FieldReflection;

public class EqualsUtils {

	public static boolean testeEquals(PersistDB obj, Object other, String... atributos) {
		int totalAtributos = atributos.length;
		int totalEquals = 0;
		for (String field : atributos) {
			if ( FieldReflection.getValue(obj, field).equals(FieldReflection.getValue(other, field)) )
				totalEquals++;
		}
		return totalAtributos == totalEquals;
	}
}