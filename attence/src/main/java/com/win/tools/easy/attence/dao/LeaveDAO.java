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
import java.util.List;
import java.util.UUID;

import com.win.tools.easy.attence.entity.LeaveEntity;

/**
 * 考勤管理数据库操作类
 * 
 * @author 袁晓冬
 * 
 */
public class LeaveDAO {
	/**
	 * 批量插入记录
	 * 
	 * @param entities
	 */
	public void insertRecords(List<LeaveEntity> entities) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Connection connection = null;
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("INSERT INTO T_LEAVE(ID,NAME,LEAVE_TYPE,LEAVE_DATE,START_TIME,END_TIME,REMARK)VALUES(?,?,?,?,?,?,?)");
			for (LeaveEntity entity : entities) {
				int index = 1;
				// ID
				stmt.setString(index++, UUID.randomUUID().toString());
				// NAME
				stmt.setString(index++, entity.getName());
				// LEAVE_TYPE
				stmt.setString(index++, entity.getLeaveType());
				Date date = null;
				try {
					date = df.parse(entity.getLeaveDate());
				} catch (ParseException e) {
					e.printStackTrace();
					date = new Date();
				}
				// LEAVE_DATE
				stmt.setDate(index++, new java.sql.Date(date.getTime()));
				// START_TIME
				stmt.setString(index++, entity.getStartTime());
				// END_TIME
				stmt.setString(index++, entity.getEndTime());
				// REMARK
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

	public void updateRecords(List<LeaveEntity> entities) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Connection connection = null;
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("UPDATE T_LEAVE SET NAME=?,LEAVE_TYPE=?,LEAVE_DATE=?,START_TIME=?,END_TIME=?,REMARK=? WHERE ID=?");
			for (LeaveEntity entity : entities) {
				int index = 1;
				// NAME
				stmt.setString(index++, entity.getName());
				// LEAVE_TYPE
				stmt.setString(index++, entity.getLeaveType());
				Date date = null;
				try {
					date = df.parse(entity.getLeaveDate());
				} catch (ParseException e) {
					e.printStackTrace();
					date = new Date();
				}
				// LEAVE_DATE
				stmt.setDate(index++, new java.sql.Date(date.getTime()));
				// START_TIME
				stmt.setString(index++, entity.getStartTime());
				// END_TIME
				stmt.setString(index++, entity.getEndTime());
				// REMARK
				stmt.setString(index++, entity.getRemark());
				// ID
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
	public void deleteRecords(List<LeaveEntity> entities) {
		Connection connection = null;
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("DELETE FROM T_LEAVE WHERE ID=?");
			for (LeaveEntity entity : entities) {
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
	 * 查询记录
	 * 
	 * @return
	 */
	public List<LeaveEntity> getRecords(Date start, Date end, String userName) {
		StringBuffer sqlBuffer = new StringBuffer(128);
		sqlBuffer.append("SELECT * FROM T_LEAVE WHERE 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if (null != start) {
			sqlBuffer.append(" AND LEAVE_DATE >= ? ");
			params.add(start);
		}
		if (null != end) {
			sqlBuffer.append(" AND LEAVE_DATE <= ? ");
			params.add(end);
		}
		if (null != userName && userName.length() > 0) {
			sqlBuffer.append(" AND NAME LIKE ? ");
			params.add("%" + userName + "%");
		}
		sqlBuffer.append("  ORDER BY NAME ASC");
		Connection connection = null;
		ResultSet rs = null;
		List<LeaveEntity> entities = new ArrayList<LeaveEntity>();
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
				LeaveEntity entity = new LeaveEntity();
				entity.setId(rs.getString("ID"));
				entity.setName(rs.getString("NAME"));
				entity.setLeaveType(rs.getString("LEAVE_TYPE"));
				entity.setLeaveDate(df.format(rs.getDate("LEAVE_DATE")));
				entity.setStartTime(rs.getString("START_TIME"));
				entity.setEndTime(rs.getString("END_TIME"));
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
		return entities;
	}
}
