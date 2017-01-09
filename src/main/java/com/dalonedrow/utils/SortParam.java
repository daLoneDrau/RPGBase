package com.dalonedrow.utils;

public class SortParam {
	private boolean	ascending	= true;
	private String	dataElement;

	public String getDataElement() {
		return dataElement;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(final boolean ascending) {
		this.ascending = ascending;
	}

	public void setDataElement(final String dataElement) {
		this.dataElement = dataElement;
	}

	@Override
	public String toString() {
		return "Sort By [Element name=" + dataElement + ", ascending order: "
				+ ascending + "]";
	}

}
