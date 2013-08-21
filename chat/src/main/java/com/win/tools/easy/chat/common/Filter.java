package com.win.tools.easy.chat.common;

public interface Filter {

	public void init();
	public void doFilter (Request request, Response response, FilterChain chain );

}
