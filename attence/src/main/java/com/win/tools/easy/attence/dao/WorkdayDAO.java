package com.win.tools.easy.attence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.win.tools.easy.attence.entity.WorkdayEntity;

/**
 * 考勤管理数据库操作类
 * 
 * @author 袁晓冬
 * 
 */
public class WorkdayDAO {
	/**
	 * 批量插入考勤记录
	 * 
	 * @param entities
	 */
	public void insertRecords(List<WorkdayEntity> entities) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Connection connection = null;
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("INSERT INTO T_WORKDAY(ID,WDATE,ISWORK,REMARK)VALUES(?,?,?,?)");
			for (WorkdayEntity entity : entities) {
				int index = 1;
				stmt.setString(index++, UUID.randomUUID().toString());
				Date date = null;
				try {
					date = df.parse(entity.getWdate());
				} catch (ParseException e) {
					e.printStackTrace();
					date = new Date();
				}
				stmt.setDate(index++, new java.sql.Date(date.getTime()));
				stmt.setString(index++, entity.getIswork());
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

	public void updateRecords(List<WorkdayEntity> entities) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Connection connection = null;
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("UPDATE T_WORKDAY SET WDATE=?,ISWORK=?,REMARK=? WHERE ID=?");
			for (WorkdayEntity entity : entities) {
				int index = 1;
				Date date = null;
				try {
					date = df.parse(entity.getWdate());
				} catch (ParseException e) {
					e.printStackTrace();
					date = new Date();
				}
				stmt.setDate(index++, new java.sql.Date(date.getTime()));
				stmt.setString(index++, entity.getIswork());
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
	public void deleteRecords(List<WorkdayEntity> entities) {
		Connection connection = null;
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("DELETE FROM T_WORKDAY WHERE ID=?");
			for (WorkdayEntity entity : entities) {
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
	public List<WorkdayEntity> getRecords(Date start, Date end) {
		StringBuffer sqlBuffer = new StringBuffer(128);
		sqlBuffer.append("SELECT * FROM T_WORKDAY WHERE 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if (null != start) {
			sqlBuffer.append(" AND WDATE>=?");
			params.add(start);
		}
		if (null != end) {
			sqlBuffer.append(" AND WDATE<=?");
			params.add(end);
		}
		sqlBuffer.append("  ORDER BY WDATE ASC ");
		Connection connection = null;
		ResultSet rs = null;
		List<WorkdayEntity> entities = new ArrayList<WorkdayEntity>();
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
				WorkdayEntity entity = new WorkdayEntity();
				entity.setWdate(df.format(rs.getDate("WDATE")));
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

	/**
	 * 初始化开始日期和结束日期之间的工作日
	 * 
	 * @param start
	 *            开始日期
	 * @param end
	 *            结束日期
	 */
	public void doWorkInit(Date start, Date end) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		Connection conn = null;
		try {
			conn = DBManager.getConnection();
			PreparedStatement stmt = conn
					.prepareStatement("INSERT INTO T_WORKDAY(ID,WDATE,ISWORK,REMARK)VALUES(?,?,?,?)");
			while (cal.getTimeInMillis() <= end.getTime()) {
				int index = 1;
				stmt.setString(index++, UUID.randomUUID().toString());
				stmt.setDate(index++, new java.sql.Date(cal.getTimeInMillis()));
				String iswork = "1";
				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
						|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					iswork = "0";
				}
				stmt.setString(index++, iswork);
				stmt.setString(index++, "");
				stmt.addBatch();
				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
			stmt.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
