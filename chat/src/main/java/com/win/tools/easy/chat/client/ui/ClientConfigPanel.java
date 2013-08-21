package com.win.tools.easy.chat.client.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

public class ClientConfigPanel extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3346022815707073113L;
	private static ClientConfigPanel frame;
	private JPanel contentPane;
	private AccordionMenu accordionMenu;
	private JPanel panel_2;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new ClientConfigPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static ClientConfigPanel getFrame() {
		return frame;
	}

	public static void setFrame(ClientConfigPanel frame) {
		ClientConfigPanel.frame = frame;
	}

	/**
	 * Create the frame.
	 */
	public ClientConfigPanel() {
		setTitle("XXX设置");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 605, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPanel, BorderLayout.CENTER);

		splitPanel.setDividerLocation(40);
		final JSplitPane splitPanel1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPanel.add(splitPanel1);

		accordionMenu = new AccordionMenu();
		accordionMenu.addGroup("个人设置");
		accordionMenu.addGroup("系统设置");
		accordionMenu.addGroup("应用设置");
		accordionMenu.setMinimumSize(accordionMenu.getPreferredSize());
		splitPanel1.setLeftComponent(accordionMenu);
		splitPanel1.setRightComponent(new JPanel());
		splitPanel1.setDividerLocation(150);
		
		

		panel_2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton cbtn = new JButton("确定");
		panel_2.add(cbtn);
		cbtn = new JButton("取消");
		cbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ClientConfigPanel.getFrame().dispose();
			}
		});
		panel_2.add(cbtn);
		cbtn = new JButton("应用");
		panel_2.add(cbtn);
		contentPane.add(panel_2, BorderLayout.SOUTH);
		
		
		JButton btn = new JButton("基本资料");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				splitPanel1.setRightComponent(new ClientSerConfigPanel());
//				panel_1.removeAll();
//				panel_1.add(new ClientSerConfigPanel(),LEFT_ALIGNMENT);
//				panel_1.updateUI();
			}
		});
		accordionMenu.addCell("个人设置", btn);
		
		btn = new JButton("联系方式");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("个人设置", btn);
		
		btn = new JButton("详细资料");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("个人设置", btn);
		
		
		btn = new JButton("热键设置");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("个人设置", btn);
		
		
		btn = new JButton("回复设置");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("个人设置", btn);
		
		btn = new JButton("面板设置");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("个人设置", btn);

		
		
		btn = new JButton("基本设置");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("系统设置", btn);
		
		btn = new JButton("声音设置");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("系统设置", btn);
		
		
		btn = new JButton("传输文件");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("系统设置", btn);
		
		
		btn = new JButton("办公集成");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("系统设置", btn);
		
		btn = new JButton("代理设置");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("系统设置", btn);
		
		btn = new JButton("服务器设置");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				splitPanel1.setRightComponent(new ClientSerConfigPanel());
			}
		});
		accordionMenu.addCell("系统设置", btn);
		
		
		btn = new JButton("邮件设置");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("应用设置", btn);
		
		
		
//		splitPanel
	}
}
