package com.win.tools.easy.chat.common;


/**
 * 单向链表过滤链
 * 
 * @author 袁晓冬
 * 
 */
public class SimpleFilterChain implements FilterChain {
	/** 当前节点所存储的过滤器 */
	private Filter filter = null;
	/** 指向下一个过滤器 */
	private SimpleFilterChain next = null;
	/** 指向链尾 */
	public final static SimpleFilterChain END;
	/** 指向链头 */
	public final static SimpleFilterChain HEAD;

	/**
	 * 过滤器是否为空
	 * 
	 * @return
	 */
	public static boolean isEmperty() {
		return HEAD.next == END;
	}

	/**
	 * 初始化
	 */
	static {
		END = new SimpleFilterChain();
		HEAD = new SimpleFilterChain();
		HEAD.next = END;
	}

	private SimpleFilterChain() {
	}

	/**
	 * 添加过滤
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
		// 构造新节点
		SimpleFilterChain newChain = new SimpleFilterChain();
		newChain.setFilter(filter);
		// 新节点指向链尾
		newChain.next = END;
		toAdd.next = newChain;
	}

	@Override
	public void doFilter(Request request, Response response) {
		// 遇到链尾则结束
		if (this == END) {
			return;
		}
		// 跳过链头
		if (this == HEAD) {
			this.next.doFilter(request, response);
			return;
		}
		SimpleFilterChain chain = this.next;
		// 正常过滤器不能为空
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
