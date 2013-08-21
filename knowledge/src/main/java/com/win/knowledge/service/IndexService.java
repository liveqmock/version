package com.win.knowledge.service;

/**
 * 索引服务接口
 * 
 * @author 袁晓冬
 * 
 */
public interface IndexService {
	/**
	 * 默认索引目录
	 */
	String INDEX_PATH = "index";

	/**
	 * 对path所指定的文件或者目录下文件进行索引
	 * 
	 * @param path
	 */
	void indexFile(String path);
}
