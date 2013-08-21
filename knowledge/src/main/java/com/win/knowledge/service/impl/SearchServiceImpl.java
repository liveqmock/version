package com.win.knowledge.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.win.knowledge.service.IndexService;
import com.win.knowledge.service.SearchService;

/**
 * 检索服务
 * 
 * @author 袁晓冬
 * 
 */
public class SearchServiceImpl implements SearchService {
	private static final Logger LOGGER = Logger
			.getLogger(SearchServiceImpl.class);

	public static void main(String[] args) {

	}

	@Override
	public List<Document> search(String queryStr) {
		try {
			queryStr = queryStr.trim();
			IndexReader reader = DirectoryReader.open(FSDirectory
					.open(new File(IndexService.INDEX_PATH)));
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_43);
			String field = "contents";
			QueryParser parser = new QueryParser(Version.LUCENE_43, field,
					analyzer);
			Query query = parser.parse(queryStr);
			LOGGER.debug("Searching for: " + query.toString(field));
			List<Document> docList = doSearch(searcher, query);
			for (Document doc : docList) {
				String path = doc.get("path");
				System.err.println("path:" + path);
			}
		} catch (IOException e) {
			LOGGER.debug(e);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Document> doSearch(IndexSearcher searcher, Query query)
			throws IOException {
		final int initCount = 50;
		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 50);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		LOGGER.debug(numTotalHits + " total matching documents");

		int end = Math.min(numTotalHits, initCount);
		List<Document> docList = new ArrayList<Document>();

		if (end > hits.length) {
			hits = searcher.search(query, numTotalHits).scoreDocs;
		}
		for (ScoreDoc scoreDoc : hits) {
			docList.add(searcher.doc(scoreDoc.doc));
		}

		return docList;
	}
}
