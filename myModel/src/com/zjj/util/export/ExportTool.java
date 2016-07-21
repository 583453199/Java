package com.zjj.util.export;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.util.CollectionUtils;
import com.zjj.util.common.Page;
import com.zjj.util.common.SysPathTool;

public class ExportTool {

	private static final Log LOG = LogFactory.getLog(ExportTool.class);
	private static final int EXPORT_CONNECTION_COUNT = Integer.parseInt(SysPathTool.getMap().get("EXPORT_CONNECTION_COUNT"));
	private static final int EXPORT_PAGE_SIZE = Integer.parseInt(SysPathTool.getMap().get("EXPORT_PAGE_SIZE"));

	/**
	 * 导出路径
	 * 
	 * @return
	 */
	private static String getExportExcelPath() {
		return ServletActionContext.getServletContext().getRealPath("exportExcel");
	}

	/**
	 * 创建文件目录 如果存在先删除
	 * 
	 * @param path
	 */
	private static void createMkdir(String path) {
		File file = new File(path);
		if (file.exists()) {
			try {
				FileUtils.deleteDirectory(file);
			} catch (IOException e) {
				LOG.error("删除文件夹失败 ", e);
			}
		}
		file.mkdirs();
	}

	/**
	 * 导出并分页组装数据
	 * 
	 * @param targetDBObj
	 * @param targetExportClass
	 * @param paramsMap
	 */
	@SuppressWarnings("unchecked")
	public static String exportPageByGroup(Object targetDBObj, Class<?> targetExportClass, Map<String, Object> paramsMap) {
		try {
			Map<String, Object> paramMap = (Map<String, Object>) paramsMap.get("PARAMS");
			String targetMethod = (String) paramsMap.get("METHOD_NAME");
			String resultListName = (String) paramsMap.get("RESULT_LIST_NAME");
			String exportMethodName = (String) paramsMap.get("EXPORT_METHOD_NAME");
			String loginUserId = (String) paramsMap.get("LOGIN_USER_ID");
			String exportTitleName = (String) paramsMap.get("EXPORT_TITLE_NAME");
			String extra = (String) paramsMap.get("EXTRA");
			String path = getExportExcelPath();
			String xlsFullPath = path + "/" + loginUserId;
			String fileName = xlsFullPath + "/" + exportTitleName + ".xls";
			File userDirs = new File(xlsFullPath);
			if (!userDirs.exists()) {
				userDirs.mkdirs();
			}

			// 设置分页
			int currentPage = 1;
			Page page = new Page();
			page.setLimitPageSize(false);
			page.setPageSize(EXPORT_PAGE_SIZE);
			page.setCurrentPage(currentPage);

			// 调用目标类
			Class<?> targetDBClass = targetDBObj.getClass();
			Method method = targetDBClass.getDeclaredMethod(targetMethod, Map.class, Page.class);
			Object resultObj = method.invoke(targetDBObj, paramMap, page);
			if (resultObj == null) {
				invokeActionExportMethod(targetExportClass, exportMethodName, fileName,
						new ArrayList<Map<String, Object>>(), extra);
				return fileName;
			}

			Map<String, Object> resultMap = getDBResultMap(resultObj);
			List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap.get(resultListName);
			if (CollectionUtils.isEmpty(resultList)) {
				invokeActionExportMethod(targetExportClass, exportMethodName, fileName, resultList, extra);
				return fileName;
			}

			// 统计总数 并计算需要分页次数 如果只有一页就直接返回参数
			String totalNumStr = (String) resultMap.get("TOTAL_NUM");
			totalNumStr = StringUtils.isEmpty(totalNumStr) ? "0" : totalNumStr;
			int totalNum = Integer.parseInt(totalNumStr);
			if (totalNum == 0) {
				invokeActionExportMethod(targetExportClass, exportMethodName, fileName, resultList, extra);
				return fileName;
			}
			int exportPageCount = totalNum % EXPORT_PAGE_SIZE == 0 ? totalNum / EXPORT_PAGE_SIZE : (totalNum
					/ EXPORT_PAGE_SIZE + 1);
			if (exportPageCount == 1) {
				invokeActionExportMethod(targetExportClass, exportMethodName, fileName, resultList, extra);
				return fileName;
			}

			// 创建 xls文件夹目录
			xlsFullPath += "/xls";
			ExportTool.createMkdir(xlsFullPath);

			ExecutorService pool = Executors.newFixedThreadPool(EXPORT_CONNECTION_COUNT);
			List<Future<?>> futureList = new LinkedList<Future<?>>();
			for (int i = 1; i <= exportPageCount; i++) {
				ExportServiceCallable callable = new ExportServiceCallable(page, i, exportTitleName, xlsFullPath,
						paramMap, targetDBObj, resultListName, exportMethodName, targetExportClass, method, resultList,
						extra);
				Future<?> future = pool.submit(callable);
				futureList.add(future);
			}

			for (Future<?> future : futureList) {
				try {
					future.get();
				} catch (InterruptedException e) {
					LOG.error("执行中断", e);
				} catch (ExecutionException e) {
					LOG.error("执行出错", e);
				}
			}
			pool.shutdown();

			String zipFilePath = path + "/" + loginUserId + "/" + exportTitleName + ".zip";
			ZipCompressor zc = new ZipCompressor(zipFilePath);
			zc.compress(xlsFullPath);

			File xlsFile = new File(xlsFullPath);
			if (xlsFile.exists()) {
				FileUtils.deleteDirectory(xlsFile);
			}
			return zipFilePath;
		} catch (Exception e) {
			LOG.error("导出并分页组装数据 异常", e);
		}
		return null;
	}

	/**
	 * 调用Action类的 导出方法
	 * 
	 * @param targetExportClass
	 * @param exportMethodName
	 * @param fileName
	 * @param resultList
	 * @param extra
	 * 最多支持一个 额外的参数 用于 导出使用
	 */
	@SuppressWarnings("rawtypes")
	public static void invokeActionExportMethod(Class<?> targetExportClass, String exportMethodName, String fileName,
			List<Map<String, Object>> resultList, String extra) {
		try {
			if (CollectionUtils.isEmpty(resultList)) {
				resultList = new ArrayList<Map<String, Object>>();
			}
			Class[] cla = new Class[] { String.class, ArrayList.class };
			if (StringUtils.isNotBlank(extra)) {
				cla = new Class[] { String.class, ArrayList.class, String.class };
			}
			Method exportMethod = targetExportClass.getDeclaredMethod(exportMethodName, cla);

			if (StringUtils.isBlank(extra)) {
				exportMethod.invoke(targetExportClass.newInstance(), fileName, resultList);
			} else {
				exportMethod.invoke(targetExportClass.newInstance(), fileName, resultList, extra);
			}
		} catch (Exception e) {
			LOG.error("反射调用Action导出方法 异常", e);
		}
	}

	/**
	 * 返回数据库查询Map结果集
	 * 
	 * @param resultObj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getDBResultMap(Object resultObj) {
		Map<String, Object> resultMap = null;
		if (resultObj == null) {
			return null;
		}
		if (resultObj instanceof Map) {
			resultMap = (Map<String, Object>) resultObj;
		} else if (resultObj instanceof List) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) resultObj;
			resultMap = list.get(0);
		}
		return resultMap;
	}
}
