package edu.softserveinc.healthbody.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.softserveinc.healthbody.constants.Constants.GroupUserViewCard;
import edu.softserveinc.healthbody.constants.ErrorConstants;
import edu.softserveinc.healthbody.constants.DaoStatementsConstant.GroupDBQueries;
import edu.softserveinc.healthbody.entity.GroupUserView;
import edu.softserveinc.healthbody.exceptions.DataBaseReadingException;
import edu.softserveinc.healthbody.exceptions.JDBCDriverException;
import edu.softserveinc.healthbody.exceptions.QueryNotFoundException;

public class GroupUserViewDao extends AbstractDao<GroupUserView> {

	private static volatile GroupUserViewDao instance;

	private GroupUserViewDao() {
		init();
	}

	public static GroupUserViewDao getInstance() {
		if (instance == null) {
			synchronized (GroupDao.class) {
				if (instance == null) {
					instance = new GroupUserViewDao();
				}
			}
		}
		return instance;
	}
	
	
	@Override
	protected void init() {
		for (GroupDBQueries groupDBQueries : GroupDBQueries.values()) {
			sqlQueries.put(groupDBQueries.getDaoQuery(), groupDBQueries);
		}
	}

	@Override
	public GroupUserView createInstance(final String[] args) {
		return new GroupUserView(args[GroupUserViewCard.ID] == null ? UUID.randomUUID().toString() : args[GroupUserViewCard.ID], 
				args[GroupUserViewCard.NAME] == null ? new String() : args[GroupUserViewCard.NAME],
				Integer.parseInt(args[GroupUserViewCard.COUNT] == null ? "0" : args[GroupUserViewCard.COUNT]), 
				args[GroupUserViewCard.DESCRIPTION] == null ? new String() : args[GroupUserViewCard.DESCRIPTION],
				args[GroupUserViewCard.SCOREGROUP] == null ? new String() : args[GroupUserViewCard.SCOREGROUP],
				args[GroupUserViewCard.STATUS] == null ? new String() : args[GroupUserViewCard.STATUS],		
				args[GroupUserViewCard.USERS] == null ? new String() : args[GroupUserViewCard.USERS],
				args[GroupUserViewCard.FIRSTNAME] == null ? new String() : args[GroupUserViewCard.FIRSTNAME],
				args[GroupUserViewCard.LASTNAME] == null ? new String() : args[GroupUserViewCard.LASTNAME]);
		
	}
	
	public List<GroupUserView> getAllGroupsParticiapnts(final Connection connection, final int partNumber, final int partSize) 
			throws QueryNotFoundException, JDBCDriverException,	DataBaseReadingException {
		List<GroupUserView> result = new ArrayList<>();
		String query = sqlQueries.get(DaoQueries.GET_ALL_GROUPS_PARTICIPANTS).toString();
		if (query == null) {
			throw new QueryNotFoundException(String.format(ErrorConstants.QUERY_NOT_FOUND, GroupDBQueries.GET_ALL_GROUPS_PARTICIPANTS.name()));
		}
		if ((partNumber >= 0) && (partSize > 0)) {
			query = query.substring(0, query.lastIndexOf(";")) + SQL_LIMIT;
		}
		try (PreparedStatement pst = createPreparedStatement(connection, query, partNumber, partSize);
			ResultSet resultSet = pst.executeQuery()) {
			String[] queryResult = new String[resultSet.getMetaData().getColumnCount()];
			while (resultSet.next()) {
				result.add(createInstance(getQueryResultArr(queryResult, resultSet)));
			}
		} catch (SQLException e) {
			throw new DataBaseReadingException(ErrorConstants.DATABASE_READING_ERROR, e);
		}
		return result;
	}

	private PreparedStatement createPreparedStatement(final Connection connection, final String query, final int partNumber, final int partSize)
			throws SQLException, JDBCDriverException {
		PreparedStatement pst = connection.prepareStatement(query);
		if ((partNumber >= 0) && (partSize > 0)) {
			pst.setInt(1, (partNumber - 1) * partSize);
			pst.setInt(2, partSize);
		}
		return pst;
}
}
