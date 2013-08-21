package com.win.tools.easy.chat.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.win.tools.easy.chat.common.PersitentManager;
import com.win.tools.easy.chat.server.ServiceConfig;

/**
 * 数据库管理类
 * 
 * @author 袁晓冬
 * 
 */
public class DBManager {
	/***
	 * 获取数据库连接，使用完成后需要自行关闭连接及结果集
	 * 
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		ServiceConfig config = PersitentManager.load().get(ServiceConfig.class);
		return getConnection(config);
	}

	public static Connection getConnection(ServiceConfig config)
			throws SQLException {
		return DriverManager.getConnection(config.getConnectionURL(),
				config.getConnectionUser(), config.getConnectionPassword());
	}

	private static final String[] CREATETABLESCRIPT = {
			"DROP TABLE T_CHAT_USER",
			"CREATE TABLE T_CHAT_USER (ID VARCHAR(40) NOT NULL,NAME VARCHAR(40) NOT NULL UNIQUE,STATUS VARCHAR(1),NICK_NAME VARCHAR(100),EMAIL VARCHAR(200),REMARK VARCHAR(100),PASSWORD VARCHAR(100),constraint PK_T_CHAT_USER primary key (ID))" };

	/**
	 * 初始化数据库
	 */
	public static void initDatabase() {
		Connection connection = null;
		try {
			connection = DBManager.getConnection();
			Statement stmt = connection.createStatement();
			for (String s : CREATETABLESCRIPT) {
				try {
					stmt.execute(s);
					connection.commit();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		initDatabase();

	}
}
