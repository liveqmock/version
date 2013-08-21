package com.win.tools.wm;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class TestExportWord {
	private static Document getDocument() {
		// 用dom4j读写xml文件（模版文件）
		SAXReader reader = new SAXReader();
		InputStream is = TestExportWord.class.getResourceAsStream("zs.xml");
		// 模版文件为 ： conf\\testReportTemplate.xml
		Document document = null;
		try {
			document = reader.read(is);
			Element root = document.getRootElement();

			// 读取相关节点信息（以下的这几个标签在xml模版文件中都是w:body，w:sect等这样的形式，但是读取的时候不能加w：，并且在增加内容的时候一定要加上w:，比如下面我们经常用到的增加p、r、t，就是要用w:p
			// w:r w:t。

			Element wbody = root.element("body");

			// 读取相关section的信息，也就是模版的具体内容，将要往里面填充数据内容的区域
			List<Element> sections = wbody.selectNodes("//w:t[@dataid]");
			for (Element e : sections) {
				e.setText("12");
			}
			System.err.println(sections.size());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(getDocument()
				.asXML().getBytes("utf-8"));
		POIFSFileSystem fs = new POIFSFileSystem();
		DirectoryEntry directory = fs.getRoot();
		DocumentEntry de = directory.createDocument("WordDocument", bais);

		// 文件输出流
		OutputStream fos = new FileOutputStream("test.doc");
		// 文件输出相关信息
		fs.writeFilesystem(fos);
		bais.close();
		fos.flush();
		fos.close();

	}

}
