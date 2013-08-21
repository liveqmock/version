package com.win.knowledge.service;

import java.util.List;

import org.apache.lucene.document.Document;

/**
 * 检索服务
 * 
 * @author 袁晓冬
 * 
 */
public interface SearchService {
	/**
	 * 根据默认索引检索queryStr字符串
	 * 
	 * @param queryStr
	 * @return
	 */
	List<Document> search(String queryStr);
}
