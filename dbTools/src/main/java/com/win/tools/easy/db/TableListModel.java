package com.win.tools.easy.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractListModel;

/**
 * 数据库表列表
 * 
 * @author 袁晓冬
 * 
 */
public class TableListModel extends AbstractListModel {
	private static final long serialVersionUID = -5011050895194934522L;
	private final List<Object> delegate = new ArrayList<Object>();

	@Override
	public int getSize() {
		return delegate.size();
	}

	@Override
	public Object getElementAt(int index) {
		return delegate.get(index);
	}

	/**
	 * Adds the specified component to the end of this list.
	 * 
	 * @param obj
	 *            the component to be added
	 * @see Vector#addElement(Object)
	 */
	public void addElement(Object obj) {
		int index = delegate.size();
		delegate.add(obj);
		fireIntervalAdded(this, index, index);
	}

	/**
	 * Searches for the first occurrence of <code>elem</code>.
	 * 
	 * @param elem
	 *            an object
	 * @return the index of the first occurrence of the argument in this list;
	 *         returns <code>-1</code> if the object is not found
	 * @see Vector#indexOf(Object)
	 */
	public int indexOf(Object elem) {
		return delegate.indexOf(elem);
	}

	/**
	 * Removes the first (lowest-indexed) occurrence of the argument from this
	 * list.
	 * 
	 * @param obj
	 *            the component to be removed
	 * @return <code>true</code> if the argument was a component of this list;
	 *         <code>false</code> otherwise
	 * @see Vector#removeElement(Object)
	 */
	public boolean removeElement(Object obj) {
		int index = indexOf(obj);
		boolean rv = delegate.remove(obj);
		if (index >= 0) {
			fireIntervalRemoved(this, index, index);
		}
		return rv;
	}
}
