package com.zjj.util.common;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 利用dom4j进行XML编程
 * 
 */
public class SysPathReadTool {
	private static Log LOG = LogFactory.getLog(SysPathReadTool.class);

	/**
	 * 遍历整个XML文件，获取所有节点的值与其属性的值，并放入HashMap中
	 * 
	 * @param filename  String 待遍历的XML文件（相对路径或者绝对路径）
	 * @param map HashMap 存放遍历结果，格式：<name-path>
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, String> ReadXML(String filename) {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader saxReader = new SAXReader();
		try {
			// 获取根目录
			String path = SysPathReadTool.class.getResource("/").getPath() + filename;
			System.out.println("===========" + path + "==================");
			Document document = saxReader.read(new File(path));
			// 根节点
			Element root = document.getRootElement();

			// 遍历根结点（paths）的所有孩子节点（肯定是path节点）
			for (Iterator iter = root.elementIterator(); iter.hasNext();) {
				Element element = (Element) iter.next();
				String name = "";
				String path_ = "";
				// 遍历path结点的所有孩子节点（即name，path，folder），并进行处理
				for (Iterator iterInner = element.elementIterator(); iterInner.hasNext();) {

					Element elementInner = (Element) iterInner.next();
					if (elementInner.getName().equals("name")) {
						name = elementInner.getText();
					}
					if (elementInner.getName().equals("path")) {
						path_ = elementInner.getText();
					}
					map.put(name, path_);
				}
			}

		} catch (DocumentException e) {
			LOG.error("利用dom4j进行XML编程出错", e);
		}
		return map;
	}
}