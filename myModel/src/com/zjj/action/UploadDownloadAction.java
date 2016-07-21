package com.zjj.action;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UploadDownloadAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private static final Log LOG = LogFactory.getLog(UploadDownloadAction.class);
	
	private String excelPath;
	
	private String downMessage;
	
	private String exportFlag;
	
	private String downFileName;
	
	private String down;
	
	private String path;
	
	/**
	 * 导出excle
	 * 
	 * @return
	 */
	public String downExcle() {
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
				.get(StrutsStatics.HTTP_RESPONSE);
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		response.addCookie(cookie);

		// 如果返回页面为空直接返回错误
		if (StringUtils.isBlank(path)) {
			return ERROR;
		}
		try {
			getInputStream();
		} catch (UnsupportedEncodingException e) {
			downMessage = "文件编码不支持！";
			LOG.error(downMessage, e);
			return path;
		} catch (FileNotFoundException e) {
			downMessage = "没有找到文件！";
			LOG.error(downMessage, e);
			return path;
		} catch (Exception e) {
			downMessage = "导出失败！";
			LOG.error(downMessage, e);
			return path;
		}
		downMessage = "ok";
		return SUCCESS;
	}
	
	public InputStream getInputStream() throws UnsupportedEncodingException, FileNotFoundException,Exception{
		// 导出exel表格
		if ("down".equals(exportFlag)) {
			return null;
		}
		String filePath = this.getExcelPath();
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		downFileName = new String(fileName.trim().getBytes("gb2312"), "ISO8859-1");
		File file = new File(filePath);
		return new FileInputStream(file);
	}

	public String getExcelPath() {
		return excelPath;
	}

	public void setExcelPath(String excelPath) {
		this.excelPath = excelPath;
	}

	public String getDownMessage() {
		return downMessage;
	}

	public void setDownMessage(String downMessage) {
		this.downMessage = downMessage;
	}

	public String getDown() {
		return down;
	}

	public void setDown(String down) {
		this.down = down;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getExportFlag() {
		return exportFlag;
	}

	public void setExportFlag(String exportFlag) {
		this.exportFlag = exportFlag;
	}

	public String getDownFileName() {
		return downFileName;
	}

	public void setDownFileName(String downFileName) {
		this.downFileName = downFileName;
	}
	
}
