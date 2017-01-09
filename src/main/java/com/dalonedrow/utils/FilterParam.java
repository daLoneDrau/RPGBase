package com.dalonedrow.utils;

public class FilterParam {
	/** the data element. */
	private String	dataElement;
	/** the max value. */
	private String	maxValue;
	/** the min value. */
	private String	minValue;
	/** the operand. */
	private String	operand;
	/** the value. */
	private String	value;

	public String getDataElement() {
		return dataElement;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public String getOperand() {
		return operand;
	}

	public String getValue() {
		return value;
	}

	public void setDataElement(final String dataElement) {
		this.dataElement = dataElement;
	}
	public void setMaxValue(final String maxValue) {
		this.maxValue = maxValue;
	}
	public void setMinValue(final String minValue) {
		this.minValue = minValue;
	}
	public void setOperand(final String operand) {
		this.operand = operand;
	}
	public void setValue(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Filter Item [Element name : " + dataElement + ", operand is : "
				+ operand + ", value : " + value
				+ ", maximum value=" + maxValue + ", minimum value=" + minValue
				+ "]";
	}

}
