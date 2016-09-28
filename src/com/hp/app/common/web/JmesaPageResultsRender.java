package com.hp.app.common.web;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeFactory;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.component.HtmlRow;
import org.jmesa.view.html.component.HtmlTable;
import org.jmesa.view.html.toolbar.AbstractToolbar;

import com.hp.app.dao.PageResults;

public class JmesaPageResultsRender implements IPageResultsRender {

	// private PageResults pageResults;
	private String tableWidth;
	private String tableId;
	private String keyProperty;
	private String keyGroupName;
	private HttpServletRequest request;
	private Map<String, ColumnConfig> cache = new LinkedHashMap<String, ColumnConfig>();

	public JmesaPageResultsRender(HttpServletRequest request, String tableId,
			ColumnConfig[] configs) {
		this.tableId = tableId;

		this.request = request;
		if (configs != null)
			for (ColumnConfig c : configs)
				this.cache.put(c.getPropertyId(), c);
	}

	public void setKeyProperty(String keyProperty, String keyGroupName) {
		this.keyProperty = keyProperty;
		this.keyGroupName = keyGroupName;
	}

	public void addColumnConfig(ColumnConfig config) {
		this.cache.put(config.getPropertyId(), config);
	}

	public <T> String render(PageResults<T> pageResults) {
		TableFacade tableFacade = TableFacadeFactory.createSpringTableFacade(
				this.tableId, this.request);
		Set<String> properSet = this.cache.keySet();

		if ((properSet == null) || (properSet.isEmpty()))
			return null;
		tableFacade.setColumnProperties((String[]) properSet
				.toArray(new String[0]));

		tableFacade.setMaxRows(pageResults.getPageSize());
		tableFacade.setTotalRows(pageResults.getTotalCount());
		tableFacade.setItems((Collection<T>) pageResults.getResults());
		tableFacade.setMaxRowsIncrements(new int[] { 10, 25, 50, 100 });

		tableFacade.setToolbar(new PageResultsToolbar(pageResults));

		HtmlTable table = (HtmlTable) tableFacade.getTable();

		table.getTableRenderer().setWidth(this.tableWidth);

		HtmlRow row = table.getRow();

		for (ColumnConfig c : this.cache.values()) {
			HtmlColumn column = row.getColumn(c.getPropertyId());
			if (c.sortable)
				column.setSortable(Boolean.valueOf(true));
			else
				column.setSortable(Boolean.valueOf(false));
			if (c.propertyId.equals(this.keyProperty))
				column
						.setTitle("<input type=\"checkbox\" id=\"super_checkbox_"
								+ this.keyGroupName
								+ "\" onclick=\"jQuery('input[@name="
								+ this.keyGroupName
								+ "]').each(function() {this.checked = jQuery('#super_checkbox_"
								+ this.keyGroupName
								+ "').attr('checked');});\"/>" + c.getTitile());
			else {
				column.setTitle(c.titile);
			}
			column.setFilterable(Boolean.valueOf(false));
			if (c.getWidth() != null)
				column.setWidth(c.getWidth());
			column.getCellRenderer().setCellEditor(getAutoCellEditor());
		}
		HtmlColumn idColumn = row.getColumn(this.keyProperty);
		idColumn.getCellRenderer().setCellEditor(new CellEditor() {
			public Object getValue(Object item, String property, int rowcount) {
				Object value = new BasicCellEditor().getValue(item, property,
						rowcount);
				HtmlBuilder html = new HtmlBuilder();
				html.input().type("checkbox").name(
						JmesaPageResultsRender.this.keyGroupName).value(
						(String) String.class.cast(value)).end();
				return html.toString();
			}
		});
		return tableFacade.render();
	}

	private CellEditor getAutoCellEditor() {
		return new CellEditor() {
			public Object getValue(Object item, String property, int rowcount) {
				Object value = new BasicCellEditor().getValue(item, property,
						rowcount);
				if (Boolean.class.isInstance(value)) {
					HtmlBuilder html = new HtmlBuilder();
					if (((Boolean) Boolean.class.cast(value)).booleanValue())
						html.input().type("checkbox").checked().end();
					else {
						html.input().type("checkbox").end();
					}
					return html.toString();
				}
				return value;
			}
		};
	}

	public String getTableWidth() {
		return this.tableWidth;
	}

	public void setTableWidth(String tableWidth) {
		this.tableWidth = tableWidth;
	}

	public String getTableId() {
		return this.tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public static class ColumnConfig {
		private String propertyId;
		private String titile;
		private String width;
		private boolean sortable;

		public ColumnConfig(String propertyId, String title, boolean sortable,
				String width) {
			this.propertyId = propertyId;
			this.titile = title;
			this.sortable = sortable;
			this.width = width;
		}

		public ColumnConfig(String propertyId, String title, boolean sortable) {
			this(propertyId, title, sortable, null);
		}

		public ColumnConfig(String propertyId, String title) {
			this(propertyId, title, false, null);
		}

		public String getPropertyId() {
			return this.propertyId;
		}

		public void setPropertyId(String propertyId) {
			this.propertyId = propertyId;
		}

		public String getTitile() {
			return this.titile;
		}

		public void setTitile(String titile) {
			this.titile = titile;
		}

		public boolean isSortable() {
			return this.sortable;
		}

		public void setSortable(boolean sortable) {
			this.sortable = sortable;
		}

		public int hashCode() {
			int result = 1;
			result = 31
					* result
					+ (this.propertyId == null ? 0 : this.propertyId.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ColumnConfig other = (ColumnConfig) obj;
			if (this.propertyId == null) {
				if (other.propertyId != null)
					return false;
			} else if (!this.propertyId.equals(other.propertyId))
				return false;
			return true;
		}

		public String getWidth() {
			return this.width;
		}

		public void setWidth(String width) {
			this.width = width;
		}
	}

	public static class PageResultsToolbar extends AbstractToolbar {
		private PageResults page;

		public PageResultsToolbar(PageResults page) {
			this.page = page;
		}

		public String render() {
			String tableId = getCoreContext().getLimit().getId();
			int[] pageSizes = getMaxRowsIncrements();
			if ((pageSizes == null) || (pageSizes.length == 0))
				pageSizes = new int[] { this.page.getPageSize() };
			StringBuffer ret = new StringBuffer();
			ret
					.append("<script lanuage=\"Javascript\">")
					.append("\n")
					.append("function changePage(page){")
					.append("\n")
					.append(
							"    document.getElementById('pageNo').selectedIndex=document.getElementById('pageNo').selectedIndex+page;")
					.append("\n")
					.append("    getFormByTableId('" + tableId + "').submit();")
					.append("\n")
					.append("}")
					.append("\n")
					.append("function changePageSize(){")
					.append("\n")
					.append(
							"    document.getElementById('pageNo').selectedIndex=0;")
					.append("\n")
					.append("    getFormByTableId('" + tableId + "').submit();")
					.append("\n").append("}").append("\n").append("</script>");
			ret.append("<div align=\"right\" style=\"font-size: 12px;\">");
			ret
					.append("共")
					.append(this.page.getPageCount())
					.append("页 ")
					.append("这是第")
					.append(this.page.getCurrentPage())
					.append("页 ")
					.append(
							"每页<SELECT id=\"pageSize\" name=\"pageSize\" onchange=\"changePageSize()\">");
			for (int size : pageSizes) {
				ret.append("<OPTION value=\"").append(size).append("\"");
				if (this.page.getPageSize() == size)
					ret.append(" selected");
				ret.append(">").append(size).append("</OPTION>");
			}
			ret.append("</SELECT>条记录 ");

			ret
					.append(
							"<input type=\"button\"  value=\"上一页\" onclick=\"changePage(-1)\">")
					.append(
							"<input type=\"button\"  value=\"下一页\" onclick=\"changePage(+1)\">")
					.append(
							"第<SELECT id=\"pageNo\" name=\"pageNo\" onchange=\"changePage(0)\">");
			for (int i = 1; i <= this.page.getPageCount(); i++) {
				ret.append("<OPTION value=\"").append(i).append("\"");
				if (this.page.getCurrentPage() == i)
					ret.append(" selected");
				ret.append(">").append(i).append("</OPTION>");
			}
			ret.append("</SELECT>页");
			ret.append("</div>");
			return ret.toString();
		}
	}
}
