package com.win.tools.easy.chat.client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import com.win.tools.easy.chat.client.ClientFactory;
import com.win.tools.easy.chat.entity.User;

public class AddressPane extends JPanel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1923731023759328267L;
	private JList userList = null;
	private DefaultListModel userListModel = null;

	public AddressPane() {
		userListModel = ClientFactory.getUserListModel();
		this.setLayout(new BorderLayout());

		userList = new JList(userListModel) {

			/**
			 * serialVersionUID
			 */
			private static final long serialVersionUID = 2712723452667446115L;

			private ListCellRenderer cellRenderer = new DefaultListCellRenderer() {
				/**
				 * serialVersionUID
				 */
				private static final long serialVersionUID = -1285626776493472963L;

				@Override
				public Component getListCellRendererComponent(JList list,
						Object value, int index, boolean isSelected,
						boolean cellHasFocus) {
					if (value instanceof User) {
						User u = (User) value;
						return super.getListCellRendererComponent(list,
								u.getNickName(), index, isSelected, cellHasFocus);
					}
					return super.getListCellRendererComponent(list, value,
							index, isSelected, cellHasFocus);
				}
			};

			@Override
			public ListCellRenderer getCellRenderer() {
				return cellRenderer;
			}

		};
		userList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1) {
					User user = (User) userList.getSelectedValue();
					ClientFactory.openChatWindow(user);
				}
				super.mouseClicked(e);
			}
		});
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.add(userList, BorderLayout.CENTER);
	}
}
