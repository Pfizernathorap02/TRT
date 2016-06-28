package com.tgix.html;



/**

 * <p>Title: LabelValueBean</p>

 *  <p>Description: Special wrapper that contains

 * pair value/label. For example, value may be ID of country, and label - name

 * of country.</p>

 */



public class LabelValueBean implements LabelValue {

	private String value = null;

	private String label = null;



	public LabelValueBean() {

	}



	public LabelValueBean(String label, String value) {

		this.value = value;

		this.label = label;

	}



	public LabelValueBean(String label, Long value) {

		this.value = value.toString();

		this.label = label;

	}



	public LabelValueBean(String label, long value) {

		this.value = Long.toString(value);

		this.label = label;

	}



	public void setLabel(String label) {

		this.label = label;

	}



	public String getLabel() {

		return label;

	}



	public void setValue(String value) {

		this.value = value;

	}



	public String getValue() {

		return value;

	}

}