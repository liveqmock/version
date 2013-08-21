package com.win.util.component;

import java.io.Serializable;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

public class DefaultWinComboBoxModel extends AbstractListModel implements
		MutableComboBoxModel, Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 214380784827340534L;
	/** 下拉列表条目集合 */
	Vector<Item> objects;
	/** the selected item */
	Item selectedObject;

	/**
	 * Constructs an empty DefaultWinComboBoxModel object.
	 */
	public DefaultWinComboBoxModel() {
		objects = new Vector<Item>();
	}

	public DefaultWinComboBoxModel(String[] values, String[] texts) {
		if (values == null || texts == null || values.length != texts.length) {
			objects = new Vector<Item>();
			return;
		}

	}

	/**
	 * Constructs a DefaultWinComboBoxModel object initialized with an array of
	 * item objects.
	 * 
	 * @param items
	 *            an array of Object objects
	 */
	public DefaultWinComboBoxModel(final Item items[]) {
		objects = new Vector<Item>();
		objects.ensureCapacity(items.length);

		int i, c;
		for (i = 0, c = items.length; i < c; i++)
			objects.addElement(items[i]);

		if (getSize() > 0) {
			selectedObject = getElementAt(0);
		}
	}

	/**
	 * Constructs a DefaultComboBoxModel object initialized with a {@see Item}
	 * vector.
	 * 
	 * @param v
	 *            a Vector object ...
	 */
	public DefaultWinComboBoxModel(Vector<Item> v) {
		objects = v;

		if (getSize() > 0) {
			selectedObject = getElementAt(0);
		}
	}

	@Override
	public void setSelectedItem(Object anItem) {
		if ((selectedObject != null && !selectedObject.equals(anItem))
				|| selectedObject == null && anItem != null) {
			selectedObject = (Item) anItem;
			fireContentsChanged(this, -1, -1);
		}

	}

	@Override
	public Object getSelectedItem() {
		return selectedObject;
	}

	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public Item getElementAt(int index) {
		if (index >= 0 && index < objects.size())
			return objects.elementAt(index);
		else
			return null;
	}

	@Override
	public void addElement(Object obj) {
		Item newItem = null;
		if (obj instanceof Item) {
			newItem = (Item) obj;
		}
		objects.addElement(newItem);
		fireIntervalAdded(this, objects.size() - 1, objects.size() - 1);
		if (objects.size() == 1 && selectedObject == null && newItem != null) {
			setSelectedItem(newItem);
		}
	}

	@Override
	public void removeElement(Object obj) {
		int index = objects.indexOf(obj);
		if (index != -1) {
			removeElementAt(index);
		}
	}

	@Override
	public void insertElementAt(Object obj, int index) {
		Item newItem = null;
		if (obj instanceof Item) {
			newItem = (Item) obj;
		}
		objects.insertElementAt(newItem, index);
		fireIntervalAdded(this, index, index);
	}

	@Override
	public void removeElementAt(int index) {
		if (getElementAt(index) == selectedObject) {
			if (index == 0) {
				setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
			} else {
				setSelectedItem(getElementAt(index - 1));
			}
		}

		objects.removeElementAt(index);
		fireIntervalRemoved(this, index, index);
	}

	/**
	 * 下拉框条目
	 * 
	 * @author 袁晓冬
	 * 
	 */
	public static class Item implements Serializable {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = -600721374217464354L;
		/** 值 */
		private String value;
		/** 显示 */
		private String text;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}

}
