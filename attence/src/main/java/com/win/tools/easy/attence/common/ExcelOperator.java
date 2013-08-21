package com.win.tools.easy.attence.common;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * EXCEL操作类
 * 
 * @author 袁晓冬
 * 
 */
public class ExcelOperator {
	/**
	 * Excel获取单元格内容
	 * 
	 * @param cell
	 * @return
	 */
	public static String getValue(HSSFCell cell) {
		if (null == cell) {
			return "";
		}
		if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			return String.valueOf(cell.getNumericCellValue());

		} else {
			// 返回字符串类型的值
			return String.valueOf(cell.getStringCellValue());

		}
	}

	/**
	 * Excel导出内容共同样式
	 * 
	 * @param wb
	 * @param cellStyle
	 */
	public static void setDataCellStyle(HSSFWorkbook wb, HSSFCellStyle cellStyle) {
		HSSFFont font = wb.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight((short) 400);
		cellStyle.setFont(font);
		cellStyle.setAlignment((short) 1);
		cellStyle.setVerticalAlignment((short) 0);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setLeftBorderColor((short) 8);
		cellStyle.setBorderTop((short) 1);
		cellStyle.setTopBorderColor((short) 8);
		cellStyle.setBorderRight((short) 1);
		cellStyle.setRightBorderColor((short) 8);
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBottomBorderColor((short) 8);
		cellStyle.setWrapText(true);
	}

	/**
	 * Excel底色黄色内容共同样式
	 * 
	 * @param wb
	 * @param cellStyle
	 */
	public static void setDataCellYellowStyle(HSSFWorkbook wb,
			HSSFCellStyle cellStyle) {
		HSSFFont font = wb.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight((short) 400);
		cellStyle.setFont(font);
		cellStyle.setAlignment((short) 1);
		cellStyle.setVerticalAlignment((short) 0);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setLeftBorderColor((short) 8);
		cellStyle.setBorderTop((short) 1);
		cellStyle.setTopBorderColor((short) 8);
		cellStyle.setBorderRight((short) 1);
		cellStyle.setRightBorderColor((short) 8);
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBottomBorderColor((short) 8);
		cellStyle.setWrapText(true);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(new HSSFColor.YELLOW().getIndex());
		cellStyle.setFillBackgroundColor(new HSSFColor.YELLOW().getIndex());
	}

	/***
	 * 共同导出头样式
	 * 
	 * @param wb
	 * @param cellStyle
	 */
	public static void setHeaderStyle(HSSFWorkbook wb, HSSFCellStyle cellStyle) {
		HSSFFont font = wb.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight((short) 700);
		cellStyle.setFont(font);
		cellStyle.setAlignment((short) 2);
		cellStyle.setVerticalAlignment((short) 1);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setLeftBorderColor((short) 8);
		cellStyle.setBorderTop((short) 1);
		cellStyle.setTopBorderColor((short) 8);
		cellStyle.setBorderRight((short) 1);
		cellStyle.setRightBorderColor((short) 8);
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBottomBorderColor((short) 8);
		cellStyle.setWrapText(true);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(new HSSFColor.YELLOW().getIndex());
		cellStyle.setFillBackgroundColor(new HSSFColor.YELLOW().getIndex());
	}

}
