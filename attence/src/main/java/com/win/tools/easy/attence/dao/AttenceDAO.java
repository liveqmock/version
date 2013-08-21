package com.win.tools.easy.attence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.win.tools.easy.attence.entity.AttenceEntity;
import com.win.tools.easy.attence.entity.UserEntity;

/**
 * 考勤管理数据库操作类
 * 
 * @author 袁晓冬
 * 
 */
public class AttenceDAO {

	/**
	 * 邮件确认
	 * 
	 * @param start
	 * @param end
	 * @param userName
	 */
	public Map<UserEntity, List<AttenceEntity>> getMailGroupData(Date start, Date end,
			String userName) {
		// 查询人员列表
		StringBuffer sqlBuffer = new StringBuffer(128);
		sqlBuffer
				.append("SELECT DISTINCT B.* FROM T_USER B JOIN T_ATTENCE A ON A.NAME= B.NAME WHERE B.EMAIL IS NOT NULL AND B.EMAIL <> '' ");
		List<Object> params = new ArrayList<Object>();
		if (null != start) {
			sqlBuffer.append(" AND A.DAY>=?");
			params.add(start);
		}
		if (null != end) {
			sqlBuffer.append(" AND A.DAY<=?");
			params.add(end);
		}
		if (null != userName && userName.length() > 0) {
			sqlBuffer.append(" AND B.NAME LIKE ? ");
			params.add("%" + userName + "%");
		}
		sqlBuffer.append("  ORDER BY B.NAME ASC");
		Connection connection = null;
		ResultSet rs = null;
		List<UserEntity> entities = new ArrayList<UserEntity>();
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sqlBuffer
					.toString());
			for (int i = 1; i <= params.size(); i++) {
				stmt.setObject(i, params.get(i - 1));
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				UserEntity entity = new UserEntity();
				entity.setId(rs.getString("ID"));
				entity.setName(rs.getString("NAME"));
				entity.setStatus(rs.getString("STATUS"));
				entity.setEmail(rs.getString("EMAIL"));
				entity.setRemark(rs.getString("REMARK"));
				entities.add(entity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		// 遍历以上人员列表进行邮件确认
		if (entities.size() == 0) {
			return null;
		}
		Map<UserEntity, List<AttenceEntity>> retData = new HashMap<UserEntity, List<AttenceEntity>>();
		for (UserEntity entity : entities) {
			List<AttenceEntity> attenceEntities = getRecords(start, end, entity.getName());
			retData.put(entity, attenceEntities);
		}
		return retData;
	}

	/**
	 * 调整下班时间
	 * 
	 * @param start
	 * @param end
	 * @param userName
	 */
	public void setEndTime(Date start, Date end, String userName) {
		StringBuffer sqlBuffer = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sqlBuffer.append("UPDATE T_ATTENCE SET END=? WHERE ");
		params.add("1800");
		// 正常上班
		sqlBuffer
				.append("CAST(CASEWHEN(START='', '10000', START) AS INTEGER)<=830 ");
		// 正常下班
		sqlBuffer
				.append("AND CAST(CASEWHEN(END='', '0', END) AS INTEGER)>=1730 ");
		// 没有加班
		sqlBuffer
				.append("AND CAST(CASEWHEN(END='', '0', END)  AS INTEGER)<=1900 ");

		if (null != start) {
			sqlBuffer.append(" AND DAY>=?");
			params.add(start);
		}
		if (null != end) {
			sqlBuffer.append(" AND DAY<=?");
			params.add(end);
		}
		if (null != userName && userName.length() > 0) {
			sqlBuffer.append(" AND NAME LIKE ? ");
			params.add("%" + userName + "%");
		}
		Connection connection = null;
		ResultSet rs = null;
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sqlBuffer
					.toString());
			for (int i = 1; i <= params.size(); i++) {
				stmt.setObject(i, params.get(i - 1));
			}
			stmt.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void updateRecords(List<AttenceEntity> entities) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Connection connection = null;
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("UPDATE T_ATTENCE SET NAME=?,DAY=?,START=?,END=?,REMARK=? WHERE ID=?");
			for (AttenceEntity entity : entities) {
				int index = 1;
				stmt.setString(index++, entity.getUserName());
				Date date = null;
				try {
					date = df.parse(entity.getDay());
				} catch (ParseException e) {
					e.printStackTrace();
					date = new Date();
				}
				stmt.setDate(index++, new java.sql.Date(date.getTime()));
				stmt.setString(index++, entity.getStartTime());
				stmt.setString(index++, entity.getEndTime());
				stmt.setString(index++, entity.getRemark());
				stmt.setString(index++, entity.getId());
				stmt.addBatch();
			}
			stmt.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 根据ID删除记录
	 * 
	 * @param ids
	 */
	public void deleteRecords(List<AttenceEntity> entities) {
		Connection connection = null;
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("DELETE FROM T_ATTENCE WHERE ID=?");
			for (AttenceEntity entity : entities) {
				stmt.setString(1, entity.getId());
				stmt.addBatch();
			}
			stmt.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 批量插入考勤记录
	 * 
	 * @param attenceEntities
	 */
	public void insertRecords(List<AttenceEntity> attenceEntities) {
		Connection connection = null;
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("INSERT INTO T_ATTENCE(ID,NAME,DAY,START,END,REMARK)VALUES(?,?,?,?,?,?)");
			for (AttenceEntity entity : attenceEntities) {
				int index = 1;
				stmt.setString(index++, UUID.randomUUID().toString());
				stmt.setString(index++, entity.getUserName());
				Date date = null;
				try {
					date = df.parse(entity.getDay());
				} catch (ParseException e) {
					e.printStackTrace();
					date = new Date();
				}
				stmt.setDate(index++, new java.sql.Date(date.getTime()));
				stmt.setString(index++, entity.getStartTime());
				stmt.setString(index++, entity.getEndTime());
				stmt.setString(index++, entity.getRemark());
				stmt.addBatch();
			}
			stmt.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 查询记录
	 * 
	 * @return
	 */
	public List<AttenceEntity> getRecords(Date start, Date end, String userName) {
		StringBuffer sqlBuffer = new StringBuffer(128);
		sqlBuffer
				.append("SELECT A.*,B.ISWORK FROM T_ATTENCE A JOIN T_WORKDAY B ON A.DAY = B.WDATE WHERE 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if (null != start) {
			sqlBuffer.append(" AND A.DAY>=?");
			params.add(start);
		}
		if (null != end) {
			sqlBuffer.append(" AND A.DAY<=?");
			params.add(end);
		}
		if (null != userName && userName.length() > 0) {
			sqlBuffer.append(" AND A.NAME LIKE ? ");
			params.add("%" + userName + "%");
		}
		sqlBuffer.append("  ORDER BY A.NAME ASC,A.DAY ASC");
		Connection connection = null;
		ResultSet rs = null;
		List<AttenceEntity> entities = new ArrayList<AttenceEntity>();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sqlBuffer
					.toString());
			for (int i = 1; i <= params.size(); i++) {
				stmt.setObject(i, params.get(i - 1));
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				AttenceEntity entity = new AttenceEntity();
				entity.setUserName(rs.getString("NAME"));
				entity.setDay(df.format(rs.getDate("DAY")));
				entity.setStartTime(rs.getString("START"));
				entity.setEndTime(rs.getString("END"));
				entity.setRemark(rs.getString("REMARK"));
				entity.setId(rs.getString("ID"));
				entity.setIswork(rs.getString("ISWORK"));
				entities.add(entity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return entities;
	}
}
