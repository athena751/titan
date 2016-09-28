package com.hp.app.dao;

import java.util.ArrayList;
import java.util.List;

public class PageResults<T> {

	private List<T> results = new ArrayList<T>();
	private int totalCount = 0;
	private int pageSize = 15;
	// private int pageCount = 0;
	private int currentPage = 1;
	private String orderBy;
	private boolean isASC;

	public boolean isASC() {
		return this.isASC;
	}

	public void setASC(boolean isASC) {
		this.isASC = isASC;
	}

	public List<T> getResults() {
		return this.results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return (this.totalCount + this.pageSize - 1) / this.pageSize;
	}

	public int getCurrentPage() {
		return this.currentPage;
	}

	public void setCurrentPage(int currentPage) {
		if (currentPage <= 0)
			currentPage = 1;
		this.currentPage = currentPage;
	}

	public int getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public int getResultsFrom() {
		return (this.currentPage - 1) * this.pageSize + 1;
	}

	public int getResultsEnd() {
		return this.pageSize * this.currentPage;
	}
}
