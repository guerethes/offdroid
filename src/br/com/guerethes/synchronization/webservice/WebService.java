package br.com.guerethes.synchronization.webservice;

import java.util.List;

import br.com.guerethes.orm.engine.CriteryaSQLite;


public interface WebService {

	public <T> T post(T entity) throws Exception;
	public <T> T put(T entity) throws Exception;
	public void delete(Object entity, int id) throws Exception;
	public <T> List<T> get(CriteryaSQLite criteria) throws Exception;
	public <T> T login(CriteryaSQLite criteria) throws Exception;
}