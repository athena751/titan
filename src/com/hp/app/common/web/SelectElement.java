package com.hp.app.common.web;

public class SelectElement {

	private String value;
	private String label;
	private boolean selected = false;

	public SelectElement(String value, String label) {
		this.value = value;
		this.label = label;
	}

	public SelectElement(String value, String label, boolean selected) {
		this.value = value;
		this.label = label;
		this.selected = selected;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int hashCode() {
		int result = 1;
		result = 31 * result + (this.label == null ? 0 : this.label.hashCode());
		result = 31 * result + (this.value == null ? 0 : this.value.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SelectElement other = (SelectElement) obj;
		if (this.label == null) {
			if (other.label != null)
				return false;
		} else if (!this.label.equals(other.label))
			return false;
		if (this.value == null) {
			if (other.value != null)
				return false;
		} else if (!this.value.equals(other.value))
			return false;
		return true;
	}
}
