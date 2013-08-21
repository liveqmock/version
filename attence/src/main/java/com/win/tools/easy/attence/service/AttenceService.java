package com.win.tools.easy.attence.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.StringUtils;

import com.win.tools.easy.attence.common.AttenceConfig;
import com.win.tools.easy.attence.common.ExcelOperator;
import com.win.tools.easy.attence.dao.AttenceDAO;
import com.win.tools.easy.attence.dao.LeaveDAO;
import com.win.tools.easy.attence.dao.UserDAO;
import com.win.tools.easy.attence.dao.WorkdayDAO;
import com.win.tools.easy.attence.entity.AttenceEntity;
import com.win.tools.easy.attence.entity.LeaveEntity;
import com.win.tools.easy.attence.entity.UserEntity;
import com.win.tools.easy.attence.entity.WorkdayEntity;
import com.win.tools.easy.common.PersitentManager;
import com.win.tools.easy.mail.MailSender;
import com.win.tools.easy.mail.MailSenderInfo;

/**
 * 考勤服务
 * 
 * @author 袁晓冬
 * 
 */
public class AttenceService {
	private AttenceDAO dao;
	private UserDAO userDao;
	private WorkdayDAO workdayDAO;
	private LeaveDAO leaveDAO;

	public AttenceService() {
		dao = new AttenceDAO();
		userDao = new UserDAO();
		workdayDAO = new WorkdayDAO();
		leaveDAO = new LeaveDAO();
	}

	/**
	 * 邮件确认
	 * 
	 * @param start
	 * @param end
	 * @param userName
	 */
	public void mailConfirm(Date start, Date end, String userName, String pre) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Map<UserEntity, List<AttenceEntity>> mailData = dao.getMailGroupData(
				start, end, userName);
		for (Entry<UserEntity, List<AttenceEntity>> entry : mailData.entrySet()) {
			UserEntity user = entry.getKey();
			String email = user.getEmail();
			// 没有email的跳过
			if (!StringUtils.hasLength(email)) {
				continue;
			}
			List<AttenceEntity> data = entry.getValue();
			String content = toHtml(user, data, pre);
			PersitentManager manager = PersitentManager.load();
			MailSenderInfo mailInfo = manager.get(MailSenderInfo.class);
			mailInfo.setToAddress(user.getEmail());
			mailInfo.setSubject("考勤确认" + df.format(start) + "-"
					+ df.format(end));
			mailInfo.setContent(content);
			MailSender.sendHtmlMail(mailInfo);
		}
	}

	private String toHtml(UserEntity user, List<AttenceEntity> data, String pre) {
		InputStream template = null;
		StringBuffer html = new StringBuffer();
		try {
			template = ClassLoader
					.getSystemResourceAsStream("mailTemplate.html");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					template, "UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				html.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != template) {
				try {
					template.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String templateStr = html.toString();
		if (templateStr.indexOf("${content}") == -1) {
			return templateStr;
		}
		html.setLength(0);
		if (StringUtils.hasLength(pre)) {
			html.append("<pre>");
			html.append(pre);
			html.append("</pre>");
		}
		html.append("<table border=2 cellpadding=0 cellspacing=0 width=500 style='border-collapse: collapse; table-layout: fixed; text-align: center; vertical-align: middle;'>");
		html.append("<tr bgcolor=\"yellow\">");
		html.append("<td width=100 >姓名</td>");
		html.append("<td width=100 >日期</td>");
		html.append("<td width=100>上班时间</td>");
		html.append("<td width=100>下班时间</td>");
		html.append("<td width=100>备注</td>");
		html.append("</tr>");
		for (AttenceEntity entity : data) {
			html.append(getRowHtml(entity));
		}
		html.append("</table>");
		templateStr = templateStr.replace("${content}", html.toString());
		// html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		// html.append("<html>");
		// html.append("<head>");
		// html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		// html.append("</head>");
		// html.append("<body>");
		// if (StringUtils.hasLength(pre)) {
		// html.append("<pre>");
		// html.append(pre);
		// html.append("</pre>");
		// }
		// html.append("<table border=2 cellpadding=0 cellspacing=0 width=500 style='border-collapse: collapse; table-layout: fixed; text-align: center; vertical-align: middle;'>");
		// html.append("<tr bgcolor=\"yellow\">");
		// html.append("<td width=100 >姓名</td>");
		// html.append("<td width=100 >日期</td>");
		// html.append("<td width=100>上班时间</td>");
		// html.append("<td width=100>下班时间</td>");
		// html.append("<td width=100>备注</td>");
		// html.append("</tr>");
		// for (AttenceEntity entity : data) {
		// html.append(getRowHtml(entity));
		// }
		// html.append("</table>");
		// html.append("<p><font color=\"red\">请勿回复此邮件，如有问题与考勤负责人联系！</font></p>");
		// html.append("</body>");
		// html.append("</html>");
		return templateStr;
	}

	/**
	 * 获取一行记录
	 * 
	 * @param entity
	 * @return
	 */
	private String getRowHtml(AttenceEntity entity) {
		StringBuffer row = new StringBuffer();
		if (entity.isJJRWork()) {
			row.append("<tr bgcolor=\"yellow\">");
		} else {
			row.append("<tr>");
		}
		row.append("<td>");
		row.append(entity.getUserName());
		row.append("</td>");
		row.append("<td>");
		row.append(entity.getDay());
		row.append("</td>");
		row.append("<td>");
		row.append(entity.getStartTime());
		row.append("</td>");
		row.append("<td>");
		row.append(entity.getEndTime());
		row.append("</td>");
		row.append("<td>");
		row.append(entity.getRemark());
		row.append("</td>");
		row.append("</tr>");
		return row.toString();
	}

	/**
	 * 调整下班时间
	 * 
	 * @param start
	 * @param end
	 * @param userName
	 */
	public void setEndTime(Date start, Date end, String userName) {
		dao.setEndTime(start, end, userName);
	}

	/**
	 * 获取未保存的用户名称
	 * 
	 * @param attenceEntities
	 * @return
	 */
	public List<String> getUnSavedUser(List<AttenceEntity> attenceEntities) {
		Set<String> names = new HashSet<String>(attenceEntities.size());
		for (int i = 0; i < attenceEntities.size(); i++) {
			names.add(attenceEntities.get(i).getUserName());
		}
		String[] nameArr = new String[names.size()];
		names.toArray(nameArr);
		List<UserEntity> userEntities = userDao.getUserByNames(nameArr);
		List<String> unSavedNames = new ArrayList<String>();
		if (userEntities.size() < nameArr.length) {
			outer: for (String name : nameArr) {
				for (UserEntity entity : userEntities) {
					if (entity.getName().equals(name)) {
						continue outer;
					}
				}
				unSavedNames.add(name);
			}
		}
		return unSavedNames;
	}

	/**
	 * 查询记录
	 * 
	 * @return
	 */
	public List<AttenceEntity> getRecords(Date start, Date end, String userName) {
		List<AttenceEntity> attenceEntities = dao.getRecords(start, end,
				userName);
		List<LeaveEntity> leaveEntities = leaveDAO.getRecords(start, end,
				userName);
		AttenceConfig config = PersitentManager.load().get(AttenceConfig.class);
		// 上班开始时间
		String startTime = config.getStartTime();
		final int startH = Integer.parseInt(startTime.substring(0, 2));
		final int startM = Integer.parseInt(startTime.substring(2, 4));
		// 上班结束时间
		String endTime = config.getEndTime();
		final int endH = Integer.parseInt(endTime.substring(0, 2));
		final int endM = Integer.parseInt(endTime.substring(2, 4));
		for (AttenceEntity entity : attenceEntities) {
			String entityStartTime = entity.getStartTime();
			String entityEndTime = entity.getEndTime();
			// 上班时
			int entityStartH = Integer.MAX_VALUE;
			// 上班分
			int entityStartM = Integer.MAX_VALUE;
			if (null != entityStartTime && entityStartTime.length() == 4) {
				entityStartH = Integer
						.parseInt(entityStartTime.substring(0, 2));
				entityStartM = Integer
						.parseInt(entityStartTime.substring(2, 4));
			}
			// 下班时
			int entityEndH = Integer.MIN_VALUE;
			// 下班分
			int entityEndM = Integer.MIN_VALUE;
			if (null != entityEndTime && entityEndTime.length() == 4) {
				entityEndH = Integer.parseInt(entityEndTime.substring(0, 2));
				entityEndM = Integer.parseInt(entityEndTime.substring(2, 4));
			}
			// 查询请假记录
			for (LeaveEntity leave : leaveEntities) {
				// 找到本人本日的一条请假记录，判断请假开始和结束时间是否比打开时间范围大，并整合取最大范围
				if (leave.getName().equals(entity.getUserName())
						&& leave.getLeaveDate().equals(entity.getDay())) {
					String leaveStart = leave.getStartTime();
					String leaveEnd = leave.getEndTime();
					int leaveStartH = Integer.parseInt(leaveStart.substring(0,
							2));
					int leaveStartM = Integer.parseInt(leaveStart.substring(2,
							4));
					int leaveEndH = Integer.parseInt(leaveEnd.substring(0, 2));
					int leaveEndM = Integer.parseInt(leaveEnd.substring(2, 4));
					// 整合开始时间
					if (leaveStartH < entityStartH
							|| (leaveStartH == entityStartH && leaveStartM < entityStartM)) {
						entityStartH = leaveStartH;
						entityStartM = leaveStartM;
					}
					// 整合结束时间
					if (leaveEndH > entityEndH
							|| (leaveEndH == entityEndH && leaveEndM > entityEndM)) {
						entityEndH = leaveEndH;
						entityEndM = leaveEndM;
					}
				}
			}

			// 没有开始或结束时间，做旷工处理
			if (entityStartH == Integer.MAX_VALUE
					|| entityEndH == Integer.MIN_VALUE) {
				entity.setStatus(AttenceEntity.STATUS_KG);
				continue;
			}
			if (entity.getIswork().equals("0")) {
				entity.setStatus(AttenceEntity.STATUS_JJR);
				continue;
			}
			// 判断是否迟到
			if (entityStartH > startH
					|| (entityStartH == startH && entityStartM > startM)) {
				entity.setStatus(AttenceEntity.STATUS_CD);
				continue;
			}
			// 判断早退
			if (entityEndH < endH || (entityEndH == endH && entityEndM < endM)) {
				entity.setStatus(AttenceEntity.STATUS_ZT);
				continue;
			}
			entity.setStatus(AttenceEntity.STATUS_ZC);
		}
		return attenceEntities;
	}

	/**
	 * 批量插入考勤记录
	 * 
	 * @param attenceEntities
	 */
	public void insertRecords(List<AttenceEntity> attenceEntities) {
		dao.insertRecords(attenceEntities);
	}

	/**
	 * 计算考勤
	 * 
	 * @param entities
	 */
	public void calcAttence(List<AttenceEntity> entities, Date start, Date end) {
		Map<String, AttenceEntity> attenceMap = new HashMap<String, AttenceEntity>(
				entities.size());
		// entity所包涵的用户名
		for (AttenceEntity entity : entities) {
			String name = entity.getUserName();
			attenceMap.put(name + "_" + entity.getDay(), entity);
		}
		// 获取所有用户
		List<UserEntity> userEntities = userDao.getRecords(null, "1");
		// 获取start到end之间的工作日
		List<WorkdayEntity> workdayEntities = workdayDAO.getRecords(start, end);
		// 双层循环设置entity
		for (UserEntity user : userEntities) {
			for (WorkdayEntity workday : workdayEntities) {
				AttenceEntity attence = attenceMap.get(user.getName() + "_"
						+ workday.getWdate());
				// 工作日
				if ("1".equals(workday.getIswork())) {
					// 工作日无考勤记录，为旷工
					if (null == attence) {
						attence = new AttenceEntity();
						attence.setUserName(user.getName());
						attence.setDay(workday.getWdate());
						attence.setRemark("旷工");
						entities.add(attence);
						continue;
					}
				}
			}
		}
		Collections.sort(entities, new Comparator<AttenceEntity>() {
			@Override
			public int compare(AttenceEntity o1, AttenceEntity o2) {
				return o1.getUserName().compareTo(o2.getUserName());
			}
		});
	}

	/**
	 * 删除考勤记录
	 * 
	 * @param entities
	 */
	public void deleteRecords(List<AttenceEntity> entities) {
		dao.deleteRecords(entities);
	}

	/**
	 * 考勤自动转调休记录
	 * 
	 * @param entities
	 */
	public void leaveRecords(List<AttenceEntity> entities, String leaveType) {
		AttenceConfig config = PersitentManager.load().get(AttenceConfig.class);
		// 上班开始时间
		String startTime = config.getStartTime();
		// 上班结束时间
		String endTime = config.getEndTime();
		// 调休开始时间
		String leaveStartTime = config.getLeaveStartTime();
		// 调休结束时间
		String leaveEndTime = config.getLeaveEndTime();
		List<LeaveEntity> leaves = new ArrayList<LeaveEntity>();
		for (AttenceEntity entity : entities) {
			String start = entity.getStartTime();
			String end = entity.getEndTime();
			// 上下班时间存在
			if (StringUtils.hasLength(start) && StringUtils.hasLength(end)) {
				if (Integer.parseInt(start) > Integer.parseInt(startTime)
						&& Integer.parseInt(start) > Integer
								.parseInt(leaveStartTime)) {
					LeaveEntity leave = new LeaveEntity();
					leave.setName(entity.getUserName());
					leave.setLeaveDate(entity.getDay());
					leave.setLeaveType(leaveType);
					leave.setStartTime(leaveStartTime);
					leave.setEndTime(start);
					leaves.add(leave);
				}
				if (Integer.parseInt(end) < Integer.parseInt(leaveEndTime)
						&& Integer.parseInt(end) < Integer.parseInt(endTime)) {
					LeaveEntity leave = new LeaveEntity();
					leave.setName(entity.getUserName());
					leave.setLeaveDate(entity.getDay());
					leave.setLeaveType(leaveType);
					leave.setStartTime(end);
					leave.setEndTime(leaveEndTime);
					leaves.add(leave);
				}
			}
		}
		if (leaves.size() > 0) {
			leaveDAO.insertRecords(leaves);
		}
	}

	/**
	 * 更新考勤记录
	 * 
	 * @param entities
	 */
	public void updateRecords(List<AttenceEntity> entities) {
		dao.updateRecords(entities);
	}

	/**
	 * 从Excel导入记录
	 * 
	 * @param file
	 * @return
	 */
	public List<AttenceEntity> getAttenceEntitiesFromFile(File file) {
		InputStream is = null;
		List<AttenceEntity> entities = null;
		try {
			is = new FileInputStream(file);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			entities = new ArrayList<AttenceEntity>(hssfSheet.getLastRowNum());
			for (int i = 1; i <= hssfSheet.getLastRowNum(); i++) {
				HSSFRow row = hssfSheet.getRow(i);
				if (row == null) {
					continue;
				}
				AttenceEntity entity = new AttenceEntity();
				entity.setUserName(ExcelOperator.getValue(row.getCell(2)));
				String day = ExcelOperator.getValue(row.getCell(4));
				if (null != day && day.length() > 4) {
					day = day.replace("-", "");
				}
				entity.setDay(day);
				String startTime = ExcelOperator.getValue(row.getCell(5));
				if (null != startTime && startTime.length() > 4) {
					startTime = startTime.replace(":", "");
					startTime = startTime.substring(0, 4);
				}
				entity.setStartTime(startTime);
				int start = 6;
				String endTime = "";
				String temp = ExcelOperator.getValue(row.getCell(start));
				while (!"".equals(temp) && temp.length() > 4) {
					endTime = temp;
					temp = ExcelOperator.getValue(row.getCell(++start));
				}
				if (null != endTime && endTime.length() > 4) {
					endTime = endTime.replace(":", "");
					endTime = endTime.substring(0, 4);
				}
				entity.setEndTime(endTime);
				entities.add(entity);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return entities;
	}
}
