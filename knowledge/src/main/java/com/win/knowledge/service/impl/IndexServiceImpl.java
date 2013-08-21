package com.win.knowledge.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.win.knowledge.service.IndexService;

/**
 * 索引服务实现
 * 
 * @author 袁晓冬
 * 
 */
public class IndexServiceImpl implements IndexService {
	private static final Logger LOGGER = Logger
			.getLogger(IndexServiceImpl.class);

	/** 默认索引目录 */
	public static void main(String[] args) {
		IndexServiceImpl service = new IndexServiceImpl();
		service.indexFile("D:\\luceneTest");
		SearchServiceImpl service2 = new SearchServiceImpl();
		service2.search("Save");
	}

	@Override
	public void indexFile(String path) {
		indexFile(path, INDEX_PATH);
	}

	/**
	 * 索引path下所有文件，并保存索引到目录indexPath下
	 * 
	 * @param path
	 * @param indexPath
	 */
	public void indexFile(String path, String indexPath) {
		final File docDir = new File(path);
		if (!docDir.exists() || !docDir.canRead()) {
			LOGGER.info("Document directory '"
					+ docDir.getAbsolutePath()
					+ "' does not exist or is not readable, please check the path");
			return;
		}
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_43);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43,
					analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			IndexWriter writer = new IndexWriter(dir, iwc);
			indexDocs(writer, docDir);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info(e);
		}

	}

	/**
	 * 索引文件
	 * 
	 * @param writer
	 * @param file
	 * @throws IOException
	 */
	void indexDocs(IndexWriter writer, File file) throws IOException {
		if (file.canRead()) {
			if (file.isDirectory()) {
				String[] files = file.list();
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						indexDocs(writer, new File(file, files[i]));
					}
				}
			} else {
				FileInputStream fis;
				try {
					fis = new FileInputStream(file);
				} catch (FileNotFoundException fnfe) {
					return;
				}
				try {
					Document doc = new Document();
					Field pathField = new StringField("path", file.getPath(),
							Field.Store.YES);
					doc.add(pathField);
					doc.add(new LongField("modified", file.lastModified(),
							Field.Store.NO));
					doc.add(new TextField("contents", new BufferedReader(
							new InputStreamReader(fis, "UTF-8"))));

					if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
						LOGGER.debug("adding " + file);
						writer.addDocument(doc);
					} else {
						LOGGER.debug("updating " + file);
						writer.updateDocument(new Term("path", file.getPath()),
								doc);
					}
				} finally {
					fis.close();
				}
			}
		}

	}
}
