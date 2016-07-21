package com.zjj.util.export;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.zjj.util.common.Page;

@SuppressWarnings("rawtypes")
public class ExportServiceCallable implements Callable {

	private Page page;
	private int i;
	private String exportTitleName;
	private String xlsFullPath;
	private Map<String, Object> paramMap;
	private Object targetDBObj;
	private String resultListName;
	private String exportMethodName;
	private Class targetExportClass;
	private Method method;
	private List<Map<String, Object>> resultList;
	private String extra;

	public ExportServiceCallable(Page page, int i, String exportTitleName, String xlsFullPath,
			Map<String, Object> paramMap, Object targetDBObj, String resultListName, String exportMethodName,
			Class targetExportClass, Method method, List<Map<String, Object>> resultList, String extra) {
		this.page = page.clone();
		this.i = i;
		this.exportTitleName = exportTitleName;
		this.xlsFullPath = xlsFullPath;
		this.paramMap = paramMap;
		this.targetDBObj = targetDBObj;
		this.resultListName = resultListName;
		this.exportMethodName = exportMethodName;
		this.targetExportClass = targetExportClass;
		this.method = method;
		this.resultList = resultList;
		this.extra = extra;
	}

	@SuppressWarnings("unchecked")
	public Object call() throws Exception {
		String fileName = xlsFullPath + "/" + exportTitleName + i + ".xls";
		if (i == 1) {
			ExportTool.invokeActionExportMethod(targetExportClass, exportMethodName, fileName, resultList, extra);
			return null;
		}
		page.setCurrentPage(i);

		// 调用
		Object nextResultObj = method.invoke(targetDBObj, paramMap, page);

		Map<String, Object> nextResultMap = ExportTool.getDBResultMap(nextResultObj);

		List<Map<String, Object>> nextList = (List<Map<String, Object>>) nextResultMap.get(resultListName);

		ExportTool.invokeActionExportMethod(targetExportClass, exportMethodName, fileName, nextList, extra);
		return null;
	}

}
