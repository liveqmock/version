package com.win.tools.easy.platform.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;


/**
 * EasyTools日志
 * 
 * @author 袁晓冬
 * 
 */
public class EasyLogAppender extends AppenderSkeleton {

	@Override
	protected void append(LoggingEvent event) {
		TextInternalFrame logWin = FrameFactory.getLogWindow();
		if (null != logWin) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd mm:ss");
			logWin.content.insert(
					dateFormat.format(new Date()) + " : " + event.getMessage()
							+ "\n", 0);
		}
	}

	@Override
	public void close() {

	}

	@Override
	public boolean requiresLayout() {
		return false;
	}
}
