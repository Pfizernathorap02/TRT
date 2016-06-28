package com.pfizer.db; 



public class Product {

	private String productCode;

	private String productDesc;

	

	public Product(){}

	

	public void setProductCode( String code ) {

		this.productCode = code;

	}	 

	public String getProductCode() {

		return this.productCode;

	}

	

	public void setProductDesc( String desc ) {

		this.productDesc = desc;

	}

	public String getProductDesc() {

		return this.productDesc;

	}

	

	public String toString() {

		StringBuffer sb = new StringBuffer();

		sb.append( "\n" + this.getClass().getName() + "\n" );

		sb.append( "productCode: " + productCode + "\n" );

		sb.append( "productDesc: " + productDesc + "\n" );

		

		return sb.toString();

	}
    
    /**

	 * 

	 */

	public boolean equals( Object obj ) {

		return (obj instanceof Product && 

					this.getProductCode().equals( ((Product)obj).getProductCode()) );

	} 

	

	/**

	 * wrote this because i needed really needed to override equals().

	 */

	public int hashCode() { 

		int hash = 1;

		hash = hash * 31 + getProductCode().hashCode();

		hash = hash * 31 

        + (getProductCode() == null ? 0 : getProductCode().hashCode());

		return hash;

	}	

} 

