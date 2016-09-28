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

public class JmesaCollectionRender implements ICollectionRender {

	private String tableWidth;
	private String tableId;
	private String keyProperty;
	private String keyGroupName;
	private HttpServletRequest request;
	private Map<String, ColumnConfig> cache = new LinkedHashMap<String, ColumnConfig>();

	public JmesaCollectionRender(HttpServletRequest request, String tableId,
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

	public <T> String render(Collection<T> objects) {
		TableFacade tableFacade = TableFacadeFactory.createSpringTableFacade(
				this.tableId, this.request);
		Set<String> properSet = this.cache.keySet();

		if ((properSet == null) || (properSet.isEmpty()))
			return null;
		tableFacade.setColumnProperties((String[]) properSet
				.toArray(new String[0]));

		tableFacade.setMaxRows(objects.size());
		tableFacade.setTotalRows(objects.size());
		tableFacade.setItems(objects);
		tableFacade.setMaxRowsIncrements(new int[] { 10, 25, 50, 100 });

		tableFacade.setToolbar(new AbstractToolbar() {
			public String render() {
				return "";
			}
		});
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
						JmesaCollectionRender.this.keyGroupName).value(
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
}
