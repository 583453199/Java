package com.zjj.util.common;

/**
 * 分页
 * 
 */
public class Page {
	
	/** 当前页 */
	private int currentPage;
	/** 页面显示的记录数 */
	private int pageSize;
	/** 总页数 */
	private int pageCount;
	/** 查询记录的总条数 */
	private int totalNum;
	/** 开始显示的页数 */
	private int start;

	private static final int PAGE_SIZE_LIMIT = 10; // 页面大小阀值
	private boolean limitPageSize = true; // 限制页面大小，默认限制

	public Page() {
		this.pageSize = 10;
		this.currentPage = 1;
	}

	/**
	 * 设置是否限制每页记录数：true 限制，false 不限制，此方法一定要在setPageSize()之前调用才能起作用。
	 * 
	 * @param limitPageSize
	 */
	public void setLimitPageSize(boolean limitPageSize) {
		this.limitPageSize = limitPageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 防止用户恶意指定很大的 pageSize，设置一个阀值
	 * 
	 * @param pageSize
	 *            每页条数
	 */
	public void setPageSize(int pageSize) {
		if (limitPageSize && pageSize > PAGE_SIZE_LIMIT) {
			pageSize = PAGE_SIZE_LIMIT;
		}
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
		this.pageCount = (totalNum % this.pageSize == 0) ? (totalNum / this.pageSize) : (totalNum / this.pageSize + 1);
	}

	public int getOffset() {
		return (this.currentPage - 1) * this.pageSize;
	}

	public Page clone() {
		Page page = new Page();
		page.setLimitPageSize(false);
		page.setCurrentPage(this.currentPage);
		page.setPageSize(this.pageSize);
		page.setPageCount(this.pageCount);
		page.setTotalNum(this.totalNum);
		page.setStart(this.start);
		return page;
	}

}
