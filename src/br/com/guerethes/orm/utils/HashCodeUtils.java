package br.com.guerethes.orm.utils;

import br.com.guerethes.orm.engine.i.PersistDB;
import br.com.guerethes.orm.reflection.FieldReflection;

public class HashCodeUtils {

	public static int testHashCode(PersistDB obj, String... atributos) {
		int result = 0;
		for (String field : atributos) {
			Object value = FieldReflection.getValue(obj, field);
			result += 31 * result + value.hashCode();
		}
		return result;
	}
	
}