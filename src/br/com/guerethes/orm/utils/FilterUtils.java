package br.com.guerethes.orm.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.guerethes.orm.reflection.FieldReflection;

public class FilterUtils {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<?> filterBy(List<?> listObj, String[] fields, Object[] values) {
        List result = new ArrayList();
        for (Object object : listObj) {
            if (testFilter(object, fields, values))
                result.add(object);
        }
        return result;
    }
 
    private static boolean testFilter(Object object, String[] fields, Object[] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Date && FieldReflection.getValue(object, fields[i]) instanceof Date) {
                if ( ((Date)values[i]).getTime() != ((Date)FieldReflection.getValue(object, fields[i])).getTime() )
                    return false;
            } else if (!values[i].equals(FieldReflection.getValue(object, fields[i]))) {
                return false;
            }
        }
        return true;
    }
	
}