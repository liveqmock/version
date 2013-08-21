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
		setTitle("XXX����");
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
		accordionMenu.addGroup("��������");
		accordionMenu.addGroup("ϵͳ����");
		accordionMenu.addGroup("Ӧ������");
		accordionMenu.setMinimumSize(accordionMenu.getPreferredSize());
		splitPanel1.setLeftComponent(accordionMenu);
		splitPanel1.setRightComponent(new JPanel());
		splitPanel1.setDividerLocation(150);
		
		

		panel_2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton cbtn = new JButton("ȷ��");
		panel_2.add(cbtn);
		cbtn = new JButton("ȡ��");
		cbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ClientConfigPanel.getFrame().dispose();
			}
		});
		panel_2.add(cbtn);
		cbtn = new JButton("Ӧ��");
		panel_2.add(cbtn);
		contentPane.add(panel_2, BorderLayout.SOUTH);
		
		
		JButton btn = new JButton("��������");
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
		accordionMenu.addCell("��������", btn);
		
		btn = new JButton("��ϵ��ʽ");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("��������", btn);
		
		btn = new JButton("��ϸ����");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("��������", btn);
		
		
		btn = new JButton("�ȼ�����");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("��������", btn);
		
		
		btn = new JButton("�ظ�����");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("��������", btn);
		
		btn = new JButton("�������");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("��������", btn);

		
		
		btn = new JButton("��������");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("ϵͳ����", btn);
		
		btn = new JButton("��������");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("ϵͳ����", btn);
		
		
		btn = new JButton("�����ļ�");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("ϵͳ����", btn);
		
		
		btn = new JButton("�칫����");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("ϵͳ����", btn);
		
		btn = new JButton("��������");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("ϵͳ����", btn);
		
		btn = new JButton("����������");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				splitPanel1.setRightComponent(new ClientSerConfigPanel());
			}
		});
		accordionMenu.addCell("ϵͳ����", btn);
		
		
		btn = new JButton("�ʼ�����");
		btn.setBorder(new EmptyBorder(0,0,0,0));
		btn.setContentAreaFilled(false);
		accordionMenu.addCell("Ӧ������", btn);
		
		
		
//		splitPanel
	}
}
