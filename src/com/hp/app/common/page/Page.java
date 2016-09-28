package com.hp.app.common.page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象，规则：
 *     pageTotal由dataCount和pageSize决定；
 *     pageNo应该大于0并且不大于pageTotal
 *     firstRow和maxRow由dataCount、pageSize和pageNo决定；
 * 两种数据传播方式：
 *     页面翻页：flag、pageSize和pageNo(翻页前的页面)作为初始值
 *     查询后返回页面进行展现：dataCount和pageSize作为初始值
 * @author Administrator
 *
 */
public class Page implements Serializable {
	private static final long serialVersionUID = -4419520535234187142L;
	public static final Integer DEFAULT_PAGE_NO = 1;
	public static final Integer DEFAULT_PAGE_SIZE = 10;

	private List pageData;
	private String flag;
	private Integer pageNo;								//当前显示的页数
	private Integer pageSize;
	private Integer dataCount;							//数据的总条数
	private Integer pageNoGoto;
	private Sort sort;

	public Page() {
		this(DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);
	}
	
	public Page(final Integer pageNo) {
		this(pageNo, DEFAULT_PAGE_SIZE);
	}
	public Page(final Integer pageNo, final Integer pageSize) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
	
	public Page(final Page page) {
		this.pageData = page.pageData;
		this.flag = page.flag;
		this.pageNo = page.pageNo;
		this.pageSize = page.pageSize;
		this.dataCount = page.dataCount;
		this.pageNoGoto = page.pageNoGoto;
		this.sort = page.sort;
	}
	
	/**
	 * 计算要查询第几页
	 * @return
	 */
	private int updatePageNo() {
		if ("fore".equals(flag)) {
			pageNo = pageNo - 1;
		} else if ("next".equals(flag)) {
			pageNo = pageNo + 1;
		} else if ("top".equals(flag)) {
			pageNo = 1;
		} else if ("bottom".equals(flag)) {
			pageNo = getPageTotal();
		} else if ("goto".equals(flag)) {
			pageNo = pageNoGoto;
		}
		
		if (getPageTotal() == 0) {
			pageNo = 0;
		} else if (pageNo > getPageTotal()) {
			pageNo = getPageTotal();
		} else if (pageNo < 1) {
			pageNo = 1;
		}
		flag = "";
		return pageNo;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNoGoto() {
		return pageNoGoto;
	}

	public void setPageNoGoto(Integer pageNoGoto) {
		this.pageNoGoto = pageNoGoto;
	}

	public Integer getPageTotal() {
		return (int) Math.ceil(dataCount.doubleValue() / pageSize.doubleValue());
	}

	public int getFirstRow() {
		return (pageNo - 1) * pageSize + 1;
	}

	public int getMaxRow() {
		return (int) (pageNo * pageSize > dataCount ? dataCount : pageNo * pageSize);
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	public Integer getDataCount() {
		return dataCount;
	}

	public void setDataCount(Integer dataCount) {
		this.dataCount = dataCount;
		updatePageNo();
	}
	
	public void setDataCount(Long dataCount) {
		this.dataCount = dataCount.intValue();
		updatePageNo();
	}

	public List getPageData() {
		return pageData;
	}

	public void setPageData(List pageData) {
		this.pageData = pageData;
	}

}
