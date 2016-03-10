/**
 * 
 */
package br.com.guerethes.orm.engine.criterya;

import br.com.guerethes.orm.engine.criterya.pattern.ElementsQuery;
import br.com.guerethes.orm.engine.criterya.pattern.IElementsQuery;

/**
 * @author timoshenko
 *
 */
public abstract class Not extends ElementsQuery {

	private Not(String nameObject) {
		super(nameObject);
	}
	
	public static Not create(IElementsQuery elementsQuery) {
		new Exception("Not  implemented yet.");
		return null;
	}
	
}
