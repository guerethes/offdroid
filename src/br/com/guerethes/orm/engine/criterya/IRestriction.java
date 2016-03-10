package br.com.guerethes.orm.engine.criterya;

public interface IRestriction {

	public Eq eq(String field, Object value);

	public Gt gt(String field, Object value);
	
	public Ge ge(String field, Object value);

	public In in(String field, Object[] values);

	public Lt lt(String field, Object value);

	public Le le(String field, Object value);
	
	public Ne ne(String field, Object value);

	public Ni ni(String field, Object[] values);

	public Null isNull(String field);

	public Lk lk(String field, String value);
	
}