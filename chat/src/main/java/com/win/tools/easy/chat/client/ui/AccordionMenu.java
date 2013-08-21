package com.win.tools.easy.chat.client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;

public class AccordionMenu extends JComponent {

	private static final long serialVersionUID = 1L;

	protected Map<String, AccordItem> groupMap = new HashMap<String, AccordItem>();

	public AccordionMenu() {
		
		super.setLayout(new AccordionLayout());
	}

	final public void setLayout(LayoutManager mgr) {

	}

	public void addGroup(String groupName) {
		AccordItem item = new AccordItem(groupName);
		groupMap.put(groupName, item);
		add(item);
	}

	public void removeGroup(String groupName) {
		AccordItem item = groupMap.get(groupName);
		if (item != null) {
			remove(item);
		}
	}

	public void addCell(String groupName, Component comp) {
		AccordItem item = groupMap.get(groupName);
		if (item != null) {
			item.addCell(comp);
		}
	}

	class AccordItem extends JComponent {

		private static final long serialVersionUID = 1L;

		private int rowHeight = 25;
		private int row = 1;
		private JButton titleButton;
		private ItemContentPanel itemContent;
		private boolean select = false;

		public AccordItem(String title) {
			titleButton = new JButton(title);
			Font a = new Font(null,Font.BOLD,12);
			titleButton.setFont(a);
			itemContent = new ItemContentPanel();

			setLayout(new BorderLayout());
			
			add(titleButton, BorderLayout.NORTH);
			add(itemContent, BorderLayout.CENTER);
			setSelect(false);
			titleButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setSelect(!select);
					validate();
				}
			});
		}

		public boolean isSelect() {
			return select;
		}

		public void setSelect(boolean select) {
			this.select = select;
			if (select) {
				add(itemContent, BorderLayout.CENTER);
				this.setPreferredSize(new Dimension(getWidth(), rowHeight + row
						* rowHeight));
				itemContent.setPreferredSize(new Dimension(getWidth(), 0));
				itemContent.fireChildCountChange();
			} else {
				remove(itemContent);
				this.setPreferredSize(new Dimension(getWidth(), rowHeight));
				itemContent.setPreferredSize(new Dimension(getWidth(), 0));
				itemContent.fireChildCountChange();
			}
			if (getParent() != null) {
				getParent().doLayout();
			}
		}

		public int getRowHeight() {
			return rowHeight;
		}

		public int getRow() {
			return row;
		}

		public void addCell(Component comp) {
			itemContent.addCell(comp);
		}

		public void removeCell(Component comp) {
			itemContent.removeCell(comp);
		}

		class ItemContentPanel extends JComponent {

			private static final long serialVersionUID = 1L;
			GridLayout layout;
			int col = 2;
			int size = 0;

			public ItemContentPanel() {
				layout = new GridLayout(1, 2);
				layout.setVgap(0);
				layout.setHgap(0);
				setLayout(layout);
				fireChildCountChange();
			}

			public Component addCell(Component comp) {
				size++;
				if (size > 1) {
					row++;
				}
				fireChildCountChange();
				comp.setFont(new Font(null,Font.LAYOUT_LEFT_TO_RIGHT,11));
				comp = super.add(comp);
				updateUI();
				return comp;
			}

			public void removeCell(Component comp) {
				size--;
				if (size != 0) {
					row--;
				}
				fireChildCountChange();
				super.remove(comp);
				updateUI();
			}

			private void fireChildCountChange() {
				layout.setRows(row);
				layout.setColumns(col);
				setPreferredSize(new Dimension(getWidth(), row * rowHeight));
			}

		}
	}

	class AccordionLayout implements LayoutManager {
		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void layoutContainer(Container parent) {
			synchronized (parent.getTreeLock()) {
				int w = parent.getWidth();
				int y = 0;
				int count = parent.getComponentCount();
				AccordItem item;
				for (int i = 0; i < count; i++) {
					item = (AccordItem) parent.getComponent(i);
					int row = 1;
					if (item.isSelect()) {
						row += item.getRow();
						item.setBounds(0, y, w, row * item.getRowHeight());
						y += row * item.getRowHeight() + 1;
					} else {
						item.setBounds(0, y, w, item.getRowHeight());
						y += item.getRowHeight() + 1;
					}
				}
			}
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			synchronized (parent.getTreeLock()) {
				int w = parent.getWidth();
				int y = 0;
				int count = parent.getComponentCount();
				for (int i = 0; i < count; i++) {
					y += parent.getHeight() + 1;
				}
				return new Dimension(w - 10, y + 5);
			}
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			synchronized (parent.getTreeLock()) {
				int w = parent.getWidth();
				int y = 0;
				int count = parent.getComponentCount();
				for (int i = 0; i < count; i++) {
					y += parent.getHeight() + 1;
				}
				return new Dimension(w - 10, y + 5);
			}

		}

		@Override
		public void removeLayoutComponent(Component comp) {

		}

	}
}
