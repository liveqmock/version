package com.win.tools.easy.chat.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.win.tools.easy.chat.entity.User;

/**
 * 用户管理DAO
 * 
 * @author 袁晓冬
 * 
 */
public class UserDAO {
	/**
	 * 根据用户名查询用户
	 * 
	 * @param userName
	 * @return
	 */
	public User getUserByName(String userName) {
		if (null == userName || "".equals(userName)) {
			return null;
		}
		String sql = "SELECT * FROM T_CHAT_USER WHERE NAME=?";
		Connection connection = null;
		ResultSet rs = null;
		User user = null;
		try {
			connection = DBManager.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, userName);
			rs = stmt.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setId(rs.getString("ID"));
				user.setName(rs.getString("name"));
				user.setNickName(rs.getString("nick_name"));
				user.setPassword(rs.getString("password"));
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
		return user;
	}
}
