package com.win.tools.easy.attence.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.win.tools.easy.attence.common.AttenceConfig;
import com.win.tools.easy.common.PersitentManager;

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
		AttenceConfig config = PersitentManager.load().get(AttenceConfig.class);
		return DriverManager.getConnection(config.getConnectionURL(),
				config.getConnectionUser(), config.getConnectionPassword());
	}
}
