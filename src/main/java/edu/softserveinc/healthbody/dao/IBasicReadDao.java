package edu.softserveinc.healthbody.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import edu.softserveinc.healthbody.exceptions.CloseStatementException;
import edu.softserveinc.healthbody.exceptions.DataBaseReadingException;
import edu.softserveinc.healthbody.exceptions.EmptyResultSetException;
import edu.softserveinc.healthbody.exceptions.JDBCDriverException;
import edu.softserveinc.healthbody.exceptions.QueryNotFoundException;

public interface IBasicReadDao<TEntity> {
	

	TEntity getById(Connection connection, String id) 
			throws QueryNotFoundException, JDBCDriverException, DataBaseReadingException, CloseStatementException;

	List<TEntity> getAll(Connection connection) throws JDBCDriverException, DataBaseReadingException;
	

	List<TEntity> getFilterRange(Connection connection, int partNumber, int partSize, Map<String, String> filters) 
			throws QueryNotFoundException, JDBCDriverException, DataBaseReadingException, EmptyResultSetException, CloseStatementException;

}
