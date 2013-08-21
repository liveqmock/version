package com.win.tools.easy.chat.common;


/**
 * �������������
 * 
 * @author Ԭ����
 * 
 */
public class SimpleFilterChain implements FilterChain {
	/** ��ǰ�ڵ����洢�Ĺ����� */
	private Filter filter = null;
	/** ָ����һ�������� */
	private SimpleFilterChain next = null;
	/** ָ����β */
	public final static SimpleFilterChain END;
	/** ָ����ͷ */
	public final static SimpleFilterChain HEAD;

	/**
	 * �������Ƿ�Ϊ��
	 * 
	 * @return
	 */
	public static boolean isEmperty() {
		return HEAD.next == END;
	}

	/**
	 * ��ʼ��
	 */
	static {
		END = new SimpleFilterChain();
		HEAD = new SimpleFilterChain();
		HEAD.next = END;
	}

	private SimpleFilterChain() {
	}

	/**
	 * ��ӹ���
	 * 
	 * @param filter
	 */
	public synchronized static void addFilter(Filter filter) {
		if (null == filter) {
			return;
		}
		SimpleFilterChain toAdd = HEAD;
		while (toAdd.next != END) {
			toAdd = toAdd.next;
		}
		// �����½ڵ�
		SimpleFilterChain newChain = new SimpleFilterChain();
		newChain.setFilter(filter);
		// �½ڵ�ָ����β
		newChain.next = END;
		toAdd.next = newChain;
	}

	@Override
	public void doFilter(Request request, Response response) {
		// ������β�����
		if (this == END) {
			return;
		}
		// ������ͷ
		if (this == HEAD) {
			this.next.doFilter(request, response);
			return;
		}
		SimpleFilterChain chain = this.next;
		// ��������������Ϊ��
		if (null == getFilter()) {
			return;
		}
		getFilter().doFilter(request, response, chain);
	}

	private Filter getFilter() {
		return filter;
	}

	private void setFilter(Filter filter) {
		this.filter = filter;
	}
}
