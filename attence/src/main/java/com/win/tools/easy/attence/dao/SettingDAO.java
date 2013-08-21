package com.win.tools.easy.attence.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 考勤管理数据库操作类
 * 
 * @author 袁晓冬
 * 
 */
public class SettingDAO {
	private static final String[] CREATETABLESCRIPT = {
			"DROP TABLE T_ATTENCE",
			"DROP TABLE T_WORKDAY",
			"DROP TABLE T_USER",
			"DROP TABLE T_LEAVE",
			"CREATE TABLE T_ATTENCE (ID VARCHAR(40) NOT NULL,NAME VARCHAR(40),DAY DATE,START VARCHAR(10),END VARCHAR(10),REMARK VARCHAR(100),constraint PK_T_ATTENCE primary key (ID))",
			"CREATE TABLE T_WORKDAY (ID VARCHAR(40) NOT NULL,WDATE DATE,ISWORK VARCHAR(1),REMARK VARCHAR(100),constraint PK_T_WORKDAY primary key (ID))",
			"CREATE TABLE T_USER (ID VARCHAR(40) NOT NULL,NAME VARCHAR(40) NOT NULL UNIQUE,STATUS VARCHAR(1),EMAIL VARCHAR(200),REMARK VARCHAR(100),constraint PK_T_USER primary key (ID))",
			"CREATE TABLE T_LEAVE (ID VARCHAR(40) NOT NULL,NAME VARCHAR(40),LEAVE_TYPE VARCHAR(1),LEAVE_DATE DATE,START_TIME VARCHAR(10),END_TIME VARCHAR(10),REMARK VARCHAR(100),constraint PK_T_LEAVE primary key (ID))" };

	/**
	 * 初始化数据库表
	 */
	public void initDatabase() {
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
}
