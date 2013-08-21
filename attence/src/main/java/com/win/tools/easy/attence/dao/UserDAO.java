package com.win.tools.easy.attence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.win.tools.easy.attence.entity.UserEntity;

/**
 * 考勤管理数据库操作类
 * 
 * @author 袁晓冬
 * 
 */
public class UserDAO {

	/**
	 * 快速新增用户
	 * 
	 * @param names
	 *            用户名
	 */
	public boolean fastSaveUser(List<String> names) {
		Connection connection = null;
		boolean success = false;
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("INSERT INTO T_USER(ID,NAME,STATUS,EMAIL,REMARK)VALUES(?,?,?,?,?)");
			for (String name : names) {
				int index = 1;
				stmt.setString(index++, UUID.randomUUID().toString());
				stmt.setString(index++, name);
				stmt.setString(index++, "1");
				stmt.setString(index++, "");
				stmt.setString(index++, "自动添加");
				stmt.addBatch();
			}
			stmt.executeBatch();
			connection.commit();
			success = true;
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
		return success;
	}

	public List<UserEntity> getUserByNames(String[] names) {
		StringBuffer sqlBuffer = new StringBuffer(128);
		sqlBuffer.append("SELECT * FROM T_USER WHERE NAME IN(");
		for (int i = 0; i < names.length; i++) {
			sqlBuffer.append("?");
			sqlBuffer.append(",");
		}
		sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
		sqlBuffer.append(") ORDER BY NAME ASC");
		Connection connection = null;
		ResultSet rs = null;
		List<UserEntity> entities = new ArrayList<UserEntity>();
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sqlBuffer
					.toString());
			for (int i = 0; i < names.length; i++) {
				stmt.setString(i + 1, names[i]);
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
		return entities;
	}

	/**
	 * 批量插入考勤记录
	 * 
	 * @param entities
	 */
	public void insertRecords(List<UserEntity> entities) {
		Connection connection = null;
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("INSERT INTO T_USER(ID,NAME,STATUS,EMAIL,REMARK)VALUES(?,?,?,?,?)");
			for (UserEntity entity : entities) {
				int index = 1;
				stmt.setString(index++, UUID.randomUUID().toString());
				stmt.setString(index++, entity.getName());
				stmt.setString(index++, entity.getStatus());
				stmt.setString(index++, entity.getEmail());
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
	 * 批量插入人员记录
	 * 
	 * @param entities
	 */
	public void updateRecords(List<UserEntity> entities) {
		Connection connection = null;
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("UPDATE T_USER SET NAME=?,STATUS=?,EMAIL=?,REMARK=? WHERE ID=?");
			for (UserEntity entity : entities) {
				int index = 1;
				stmt.setString(index++, entity.getName());
				stmt.setString(index++, entity.getStatus());
				stmt.setString(index++, entity.getEmail());
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
	public void deleteRecords(List<UserEntity> entities) {
		Connection connection = null;
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("DELETE FROM T_USER WHERE ID=?");
			for (UserEntity entity : entities) {
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
	public List<UserEntity> getRecords(String userName, String status) {
		StringBuffer sqlBuffer = new StringBuffer(128);
		sqlBuffer.append("SELECT * FROM T_USER WHERE 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if (null != userName && userName.length() > 0) {
			sqlBuffer.append(" AND NAME LIKE ? ");
			params.add("%" + userName + "%");
		}
		if (null != status && status.length() > 0) {
			sqlBuffer.append(" AND STATUS=? ");
			params.add(status);
		}
		sqlBuffer.append("  ORDER BY NAME ASC");
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
		return entities;
	}
}
