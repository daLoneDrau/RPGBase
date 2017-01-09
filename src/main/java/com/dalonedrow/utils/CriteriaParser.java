package com.dalonedrow.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;

/**
 * @author drau
 */
@SuppressWarnings("rawtypes")
public final class CriteriaParser implements Comparator {
	/** the list of filters. */
	private List<FilterParam>	filters;
	/** the list of sort parameters. */
	private List<SortParam>		sorts;
	/**
	 * Applies filters to a list.
	 * @param <T> the list type parameter
	 * @param list the list of values
	 * @param clazz the class of the values being filtered
	 * @param values the array to fill with return values
	 * @return {@link T}[]
	 * @throws RPGException if there is an error with a filter
	 */
	public <T> T[] applyFilters(final T[] list, final Class clazz,
			T[] values) throws RPGException {
		try {
			for (int i = filters.size() - 1; i >= 0; i--) {
				FilterParam myFilter = filters.get(i);
				String javaFieldName = myFilter.getDataElement();
				Field field = clazz.getDeclaredField(
						javaFieldName);
				field.setAccessible(true);
				for (int j = list.length - 1; j >= 0; j--) {
					T item = list[j];
					Object fieldValue = field.get(item);
					switch (myFilter.getOperand()) {
					case ">=<":
						values = filterEqualsBetween(
								fieldValue, myFilter, item, values);
						break;
					case "><":
						values = filterBetween(
								fieldValue, myFilter, item, values);
						break;
					case "==":
						values = filterEquals(
								fieldValue, myFilter, item, values);
						break;
					case ">=":
						values = filterGreaterThanEquals(
								fieldValue, myFilter, item, values);
						break;
					case "<=":
						values = filterLessThanEquals(
								fieldValue, myFilter, item, values);
						break;
					case "!=":
						values = filterNotEquals(
								fieldValue, myFilter, item, values);
						break;
					case ">":
						values = filterGreaterThan(
								fieldValue, myFilter, item, values);
						break;
					case "<":
						values = filterLessThan(
								fieldValue, myFilter, item, values);
						break;
					default:
						values =
								ArrayUtilities.getInstance().extendArray(item,
										values);
					}
				}
			}
		} catch (NoSuchFieldException e) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Invalid filter field", e);
		} catch (IllegalArgumentException e) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Invalid filter field", e);
		} catch (IllegalAccessException e) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Invalid filter field", e);
		}
		return values;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(final Object o1, final Object o2) {
		int compare = 0;
		try {
			for (int i = 0, len = sorts.size(); i < len; i++) {
				SortParam myFilter = sorts.get(i);
				String javaFieldName = myFilter.getDataElement();
				Field field = o1.getClass().getDeclaredField(
						javaFieldName);
				field.setAccessible(true);
				Object fieldValue1 = field.get(o1);
				Object fieldValue2 = field.get(o2);
				if (fieldValue1 instanceof Integer) {
					if ((Integer) fieldValue1 < (Integer) fieldValue2) {
						compare = -1;
						if (!myFilter.isAscending()) {
							compare = 1;
						}
					} else if ((Integer) fieldValue1 > (Integer) fieldValue2) {
						compare = 1;
						if (!myFilter.isAscending()) {
							compare = -1;
						}
					}
				} else if (fieldValue1 instanceof Float) {
					if ((Float) fieldValue1 < (Float) fieldValue2) {
						compare = -1;
						if (!myFilter.isAscending()) {
							compare = 1;
						}
					} else if ((Float) fieldValue1 > (Float) fieldValue2) {
						compare = 1;
						if (!myFilter.isAscending()) {
							compare = -1;
						}
					}
				} else if (fieldValue1 instanceof Double) {
					if ((Double) fieldValue1 < (Double) fieldValue2) {
						compare = -1;
						if (!myFilter.isAscending()) {
							compare = 1;
						}
					} else if ((Double) fieldValue1 > (Double) fieldValue2) {
						compare = 1;
						if (!myFilter.isAscending()) {
							compare = -1;
						}
					}
				} else if (fieldValue1 instanceof Long) {
					if ((Long) fieldValue1 < (Long) fieldValue2) {
						compare = -1;
						if (!myFilter.isAscending()) {
							compare = 1;
						}
					} else if ((Long) fieldValue1 > (Long) fieldValue2) {
						compare = 1;
						if (!myFilter.isAscending()) {
							compare = -1;
						}
					}
				} else if (fieldValue1 instanceof String) {
					compare = ((String) fieldValue1).compareTo(
							(String) fieldValue2);
					if (!myFilter.isAscending()) {
						compare = -compare;
					}
				} else if (fieldValue1 instanceof char[]) {
					compare = new String((char[]) fieldValue1)
							.compareTo(new String((char[]) fieldValue2));
					if (!myFilter.isAscending()) {
						compare = -compare;
					}
				}
				if (compare != 0) {
					// if not equal, no need to perform any more sorting
					// - break out of loop
					break;
				}
			}
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return compare;
	}
	/**
	 * Filters a list for values between the filter parameters. If the item is
	 * valid against the filter parameters, it is added to the list of values
	 * recovered.
	 * @param <T> the list type parameter
	 * @param fieldValue the field value
	 * @param filterParam the filter parameters
	 * @param item the item being validated against the filter parameters
	 * @param values the list of values recovered.
	 * @return {@link T}[]
	 */
	private <T> T[] filterBetween(final Object fieldValue,
			final FilterParam filterParam, final T item, T[] values) {
		if (fieldValue instanceof Integer) {
			int min = Integer.parseInt(filterParam.getMinValue());
			int max = Integer.parseInt(filterParam.getMaxValue());
			if ((Integer) fieldValue > min
					&& (Integer) fieldValue < max) {
				values = ArrayUtilities.getInstance()
						.extendArray(item, values);
			}
		} else if (fieldValue instanceof Float) {
			float min = Float.parseFloat(filterParam.getMinValue());
			float max = Float.parseFloat(filterParam.getMaxValue());
			if ((Float) fieldValue > min
					&& (Float) fieldValue < max) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Double) {
			double min = Double.parseDouble(filterParam.getMinValue());
			double max = Double.parseDouble(filterParam.getMaxValue());
			if ((Double) fieldValue > min
					&& (Double) fieldValue < max) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Long) {
			long min = Long.parseLong(filterParam.getMinValue());
			long max = Long.parseLong(filterParam.getMaxValue());
			if ((Long) fieldValue > min
					&& (Long) fieldValue < max) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof String) {
			int minCompare = ((String) fieldValue).compareTo(
					filterParam.getMinValue());
			int maxCompare = ((String) fieldValue).compareTo(
					filterParam.getMaxValue());
			if (minCompare > 0
					&& maxCompare < 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof char[]) {
			int minCompare = new String((char[]) fieldValue).compareTo(
					filterParam.getMinValue());
			int maxCompare = new String((char[]) fieldValue).compareTo(
					filterParam.getMaxValue());
			if (minCompare > 0
					&& maxCompare < 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		}
		return values;
	}
	/**
	 * Filters a list for values equal to the filter parameter. If the item is
	 * valid against the filter parameters, it is added to the list of values
	 * recovered.
	 * @param <T> the list type parameter
	 * @param fieldValue the field value
	 * @param filterParam the filter parameters
	 * @param item the item being validated against the filter parameters
	 * @param values the list of values recovered.
	 * @return {@link T}[]
	 */
	private <T> T[] filterEquals(final Object fieldValue,
			final FilterParam filterParam, final T item, T[] values) {
		if (fieldValue instanceof Integer) {
			int val = Integer.parseInt(filterParam.getValue());
			if ((Integer) fieldValue == val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Float) {
			float val = Float.parseFloat(filterParam.getValue());
			if ((Float) fieldValue == val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Double) {
			double val = Double.parseDouble(filterParam.getValue());
			if ((Double) fieldValue == val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Long) {
			long val = Long.parseLong(filterParam.getValue());
			if ((Long) fieldValue == val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Boolean) {
			boolean val = Boolean.valueOf(filterParam.getValue());
			if ((Boolean) fieldValue == val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof String) {
			int compare = ((String) fieldValue).compareTo(
					filterParam.getValue());
			if (compare == 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof char[]) {
			int compare = new String((char[]) fieldValue)
					.compareTo(filterParam.getValue());
			if (compare == 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		}
		return values;
	}
	/**
	 * Filters a list for values equal to or between the filter parameters. If
	 * the item is valid against the filter parameters, it is added to the list
	 * of values recovered.
	 * @param <T> the list type parameter
	 * @param fieldValue the field value
	 * @param filterParam the filter parameters
	 * @param item the item being validated against the filter parameters
	 * @param values the list of values recovered.
	 * @return {@link T}[]
	 */
	private <T> T[] filterEqualsBetween(final Object fieldValue,
			final FilterParam filterParam, final T item, T[] values) {
		if (fieldValue instanceof Integer) {
			int min = Integer.parseInt(filterParam.getMinValue());
			int max = Integer.parseInt(filterParam.getMaxValue());
			if ((Integer) fieldValue >= min
					&& (Integer) fieldValue <= max) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Float) {
			float min =
					Float.parseFloat(filterParam.getMinValue());
			float max =
					Float.parseFloat(filterParam.getMaxValue());
			if ((Float) fieldValue >= min
					&& (Float) fieldValue <= max) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Double) {
			double min = Double.parseDouble(filterParam.getMinValue());
			double max = Double.parseDouble(filterParam.getMaxValue());
			if ((Double) fieldValue >= min
					&& (Double) fieldValue <= max) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Long) {
			long min = Long.parseLong(filterParam.getMinValue());
			long max = Long.parseLong(filterParam.getMaxValue());
			if ((Long) fieldValue >= min
					&& (Long) fieldValue <= max) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof String) {
			int minCompare = ((String) fieldValue).compareTo(
					filterParam.getMinValue());
			int maxCompare = ((String) fieldValue).compareTo(
					filterParam.getMaxValue());
			if (minCompare >= 0
					&& maxCompare <= 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof char[]) {
			int minCompare = new String((char[]) fieldValue)
					.compareTo(filterParam.getMinValue());
			int maxCompare = new String((char[]) fieldValue)
					.compareTo(filterParam.getMaxValue());
			if (minCompare >= 0
					&& maxCompare <= 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		}
		return values;
	}
	/**
	 * Filters a list for values greater than the filter parameter. If the item
	 * is valid against the filter parameters, it is added to the list of values
	 * recovered.
	 * @param <T> the list type parameter
	 * @param fieldValue the field value
	 * @param filterParam the filter parameters
	 * @param item the item being validated against the filter parameters
	 * @param values the list of values recovered.
	 * @return {@link T}[]
	 */
	private <T> T[] filterGreaterThan(final Object fieldValue,
			final FilterParam filterParam, final T item, T[] values) {
		if (fieldValue instanceof Integer) {
			int val = Integer.parseInt(filterParam.getValue());
			if ((Integer) fieldValue > val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Float) {
			float val = Float.parseFloat(filterParam.getValue());
			if ((Float) fieldValue > val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Double) {
			double val = Double.parseDouble(filterParam.getValue());
			if ((Double) fieldValue > val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Long) {
			long val = Long.parseLong(filterParam.getValue());
			if ((Long) fieldValue > val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof String) {
			int compare = ((String) fieldValue).compareTo(
					filterParam.getValue());
			if (compare > 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof char[]) {
			int compare = new String((char[]) fieldValue)
					.compareTo(filterParam.getValue());
			if (compare > 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		}
		return values;
	}
	/**
	 * Filters a list for values greater than or equal to the filter parameter.
	 * If the item is valid against the filter parameters, it is added to the
	 * list of values recovered.
	 * @param <T> the list type parameter
	 * @param fieldValue the field value
	 * @param filterParam the filter parameters
	 * @param item the item being validated against the filter parameters
	 * @param values the list of values recovered.
	 * @return {@link T}[]
	 */
	private <T> T[] filterGreaterThanEquals(final Object fieldValue,
			final FilterParam filterParam, final T item, T[] values) {
		if (fieldValue instanceof Integer) {
			int val = Integer.parseInt(filterParam.getValue());
			if ((Integer) fieldValue >= val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Float) {
			float val = Float.parseFloat(filterParam.getValue());
			if ((Float) fieldValue >= val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Double) {
			double val = Double.parseDouble(filterParam.getValue());
			if ((Double) fieldValue >= val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Long) {
			long val = Long.parseLong(filterParam.getValue());
			if ((Long) fieldValue >= val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof String) {
			int compare = ((String) fieldValue).compareTo(
					filterParam.getValue());
			if (compare >= 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof char[]) {
			int compare = new String((char[]) fieldValue)
					.compareTo(filterParam.getValue());
			if (compare >= 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		}
		return values;
	}
	/**
	 * Filters a list for values less than the filter parameter. If the item is
	 * valid against the filter parameters, it is added to the list of values
	 * recovered.
	 * @param <T> the list type parameter
	 * @param fieldValue the field value
	 * @param filterParam the filter parameters
	 * @param item the item being validated against the filter parameters
	 * @param values the list of values recovered.
	 * @return {@link T}[]
	 */
	private <T> T[] filterLessThan(final Object fieldValue,
			final FilterParam filterParam, final T item, T[] values) {
		if (fieldValue instanceof Integer) {
			int val = Integer.parseInt(filterParam.getValue());
			if ((Integer) fieldValue < val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Float) {
			float val = Float.parseFloat(filterParam.getValue());
			if ((Float) fieldValue < val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Double) {
			double val = Double.parseDouble(filterParam.getValue());
			if ((Double) fieldValue < val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Long) {
			long val = Long.parseLong(filterParam.getValue());
			if ((Long) fieldValue < val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof String) {
			int compare = ((String) fieldValue).compareTo(
					filterParam.getValue());
			if (compare < 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof char[]) {
			int compare = new String((char[]) fieldValue)
					.compareTo(filterParam.getValue());
			if (compare < 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		}
		return values;
	}
	/**
	 * Filters a list for values less than or equal to the filter parameter. If
	 * the item is valid against the filter parameters, it is added to the list
	 * of values recovered.
	 * @param <T> the list type parameter
	 * @param fieldValue the field value
	 * @param filterParam the filter parameters
	 * @param item the item being validated against the filter parameters
	 * @param values the list of values recovered.
	 * @return {@link T}[]
	 */
	private <T> T[] filterLessThanEquals(final Object fieldValue,
			final FilterParam filterParam, final T item, T[] values) {
		if (fieldValue instanceof Integer) {
			int val = Integer.parseInt(filterParam.getValue());
			if ((Integer) fieldValue <= val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Float) {
			float val = Float.parseFloat(filterParam.getValue());
			if ((Float) fieldValue <= val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Double) {
			double val = Double.parseDouble(filterParam.getValue());
			if ((Double) fieldValue <= val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Long) {
			long val = Long.parseLong(filterParam.getValue());
			if ((Long) fieldValue <= val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof String) {
			int compare = ((String) fieldValue).compareTo(
					filterParam.getValue());
			if (compare <= 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof char[]) {
			int compare = new String((char[]) fieldValue)
					.compareTo(filterParam.getValue());
			if (compare <= 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		}
		return values;
	}
	/**
	 * Filters a list for values not equal to the filter parameter. If the item
	 * is valid against the filter parameters, it is added to the list of values
	 * recovered.
	 * @param <T> the list type parameter
	 * @param fieldValue the field value
	 * @param filterParam the filter parameters
	 * @param item the item being validated against the filter parameters
	 * @param values the list of values recovered.
	 * @return {@link T}[]
	 */
	private <T> T[] filterNotEquals(final Object fieldValue,
			final FilterParam filterParam, final T item, T[] values) {
		if (fieldValue instanceof Integer) {
			int val = Integer.parseInt(filterParam.getValue());
			if ((Integer) fieldValue != val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Float) {
			float val = Float.parseFloat(filterParam.getValue());
			if ((Float) fieldValue != val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Double) {
			double val = Double.parseDouble(filterParam.getValue());
			if ((Double) fieldValue != val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Long) {
			long val = Long.parseLong(filterParam.getValue());
			if ((Long) fieldValue != val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof Boolean) {
			boolean val = Boolean.valueOf(filterParam.getValue());
			if ((Boolean) fieldValue != val) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof String) {
			int compare = ((String) fieldValue).compareTo(
					filterParam.getValue());
			if (compare != 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		} else if (fieldValue instanceof char[]) {
			int compare = new String((char[]) fieldValue)
					.compareTo(filterParam.getValue());
			if (compare != 0) {
				values = ArrayUtilities.getInstance().extendArray(item, values);
			}
		}
		return values;
	}
	/**
	 * Gets the value for the filters.
	 * @return {@link List<FilterParam>}
	 */
	public List<FilterParam> getFilters() {
		return filters;
	}
	/**
	 * Gets the value for the sorts.
	 * @return {@link List<SortParam>}
	 */
	public List<SortParam> getSorts() {
		return sorts;
	}
	/**
	 * Creates a multi-value filter parameter.
	 * @param inString the {@link String} being parsed
	 * @param inOperand the filter operand
	 * @return {@link FilterParam}
	 */
	private FilterParam mkFilterItemMultiValue(final String inString,
			final String inOperand) {
		/*
		 * Validate Arguments
		 */
		if (inString == null || inString.isEmpty()) {
			return null;
		}
		if (inOperand == null || inOperand.isEmpty()) {
			return null;
		}
		String[] myArray;
		FilterParam myFilter = new FilterParam();
		myArray = inString.split(inOperand);
		myFilter.setOperand(inOperand);
		myFilter.setDataElement(myArray[0]);
		myFilter.setMinValue(myArray[1].split("\\;")[0]);
		myFilter.setMaxValue(myArray[1].split("\\;")[1]);
		return myFilter;
	}
	/**
	 * Creates a single-value filter parameter.
	 * @param inString the {@link String} being parsed
	 * @param inOperand the filter operand
	 * @return {@link FilterParam}
	 */
	private FilterParam mkFilterItemSingleValue(final String inString,
			final String inOperand) {
		/*
		 * Validate Arguments
		 */
		if (inString == null || inString.isEmpty()) {
			return null;
		}
		if (inOperand == null || inOperand.isEmpty()) {
			return null;
		}
		String[] myArray;
		FilterParam myFilter = new FilterParam();
		myFilter.setOperand(inOperand);
		myArray = inString.split(inOperand);
		myFilter.setDataElement(myArray[0]);
		myFilter.setValue(myArray[1]);
		return myFilter;
	}
	/**
	 * Parses all filters.
	 * @param list the list of strings to parse
	 * @throws RPGException if one occurs
	 */
	public void parseFilters(final String inArgs) throws RPGException {
		/*
		 * Validate the incoming arguments
		 */
		if (inArgs == null || inArgs == "") {
			return;
		}
		List<FilterParam> myFilters = new ArrayList<FilterParam>();
		String[] myString = inArgs.split(",");

		for (int i = 0, len = myString.length; i < len; i++) {
			/*
			 * DO NOT CHANGE THE ORDER OF EXECUTION
			 */
			if (myString[i] == null || myString[i].isEmpty()) {
				throw new RPGException(ErrorMessage.MISSING_OPERAND,
						"Cannot parse filters, no filters were supplied");
			} else if (myString[i].contains(">=<")) {
				myFilters.add(mkFilterItemMultiValue(myString[i], ">=<"));
				continue;
			} else if (myString[i].contains("><")) {
				myFilters.add(mkFilterItemMultiValue(myString[i], "><"));
				continue;
			} else if (myString[i].contains("==")) {
				myFilters.add(mkFilterItemSingleValue(myString[i], "=="));
				continue;
			} else if (myString[i].contains(">=")) {
				myFilters.add(mkFilterItemSingleValue(myString[i], ">="));
				continue;
			} else if (myString[i].contains("<=")) {
				myFilters.add(mkFilterItemSingleValue(myString[i], "<="));
				continue;
			} else if (myString[i].contains("!=")) {
				myFilters.add(mkFilterItemSingleValue(myString[i], "!="));
				continue;
			} else if (myString[i].contains(">")) {
				myFilters.add(mkFilterItemSingleValue(myString[i], ">"));
				continue;
			} else if (myString[i].contains("<")) {
				myFilters.add(mkFilterItemSingleValue(myString[i], "<"));
				continue;
			} else {
				throw new RPGException(ErrorMessage.INVALID_OPERAND, "");
			}
		}
		setFilters(myFilters);
		return;
	}
	/**
	 * Parses all sorts.
	 * @param inArgs the {@link String} being parsed
	 * @throws RPGException if one occurs
	 */
	public void parseSorts(final String inArgs) {
		/*
		 * Validate the incoming arguments
		 */
		if (inArgs == null || inArgs == "") {
			return;
		}
		List<SortParam> mySorts = new ArrayList<SortParam>();
		String[] myString = inArgs.split("\\,");

		for (int i = 0, len = myString.length; i < len; i++) {
			if (myString[i].startsWith("+")) {
				SortParam mySortItem = new SortParam();
				mySortItem.setAscending(false);
				mySortItem.setDataElement(myString[i].substring(1));
				mySorts.add(mySortItem);
			} else if (myString[i].startsWith("-")) {
				SortParam mySort = new SortParam();
				mySort.setAscending(true);
				mySort.setDataElement(myString[i].substring(1));
				mySorts.add(mySort);
			} else {
				SortParam mySort = new SortParam();
				mySort.setAscending(false);
				mySort.setDataElement(myString[i]);
				mySorts.add(mySort);
			}
		}
		setSorts(mySorts);
		return;
	}
	/**
	 * Sets the filters.
	 * @param myFilters the list of filters
	 */
	public void setFilters(final List<FilterParam> myFilters) {
		filters = myFilters;
	}
	/**
	 * Sets the sorts.
	 * @param mySorts the list of sorts
	 */
	public void setSorts(final List<SortParam> mySorts) {
		sorts = mySorts;
	}
	/**
	 * Sorts a list.
	 * @param <T> the list type parameter
	 * @param list the list of values
	 * @param clazz the class of the values being filtered
	 * @param values the array to fill with return values
	 * @return {@link T}[]
	 * @throws RPGException if there is an error with a filter
	 */
	public <T> T[] sortList(final T[] list, final Class clazz, T[] values)
			throws RPGException {
		try {
			for (int i = 0, len = filters.size(); i < len; i++) {
				FilterParam filterParam = filters.get(i);
				String javaFieldName = filterParam.getDataElement();
				Field field = clazz.getDeclaredField(
						javaFieldName);
				field.setAccessible(true);
				for (int j = list.length - 1; j >= 0; j--) {
					T item = list[j];
					Object fieldValue = field.get(item);
					switch (filterParam.getOperand()) {
					case ">=<":
						values = filterEqualsBetween(
								fieldValue, filterParam, item, values);
						break;
					case "><":
						values = filterBetween(
								fieldValue, filterParam, item, values);
						break;
					case "==":
						values = filterEquals(
								fieldValue, filterParam, item, values);
						break;
					case ">=":
						values = filterGreaterThanEquals(
								fieldValue, filterParam, item, values);
						break;
					case "<=":
						values = filterLessThanEquals(
								fieldValue, filterParam, item, values);
						break;
					case "!=":
						values = filterNotEquals(
								fieldValue, filterParam, item, values);
						break;
					case ">":
						values = filterGreaterThan(
								fieldValue, filterParam, item, values);
						break;
					case "<":
						values = filterLessThan(
								fieldValue, filterParam, item, values);
						break;
					default:
						values =
								ArrayUtilities.getInstance().extendArray(item,
										values);
					}
				}
			}
		} catch (NoSuchFieldException e) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Invalid filter field", e);
		} catch (IllegalArgumentException e) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Invalid filter field", e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Invalid filter field", e);
		}
		return values;
	}
}