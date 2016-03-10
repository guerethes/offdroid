package br.com.guerethes.orm.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionUtils {

	public static <T> List<T> subtract(Collection<T> list1, Collection<T> list2){
		return subtract(new ArrayList<T>(list1), new ArrayList<T>(list2));
	}

	public static <T> List<T> subtract(List<T> list1,List<T> list2){
		List<T> resutList = new ArrayList<T>();
		Set<T> set2 = new HashSet<T>(list2);
		for (T t1 : list1) {
			if ( !set2.contains(t1) ) {
				resutList.add(t1);
			}
		}
		return resutList;
	}
	
}