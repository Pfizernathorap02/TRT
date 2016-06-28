package com.pfizer.webapp.user; 



import java.util.ArrayList;

import java.util.Iterator;

import java.util.List;



public class Region {

	private	String name;

	private String code;

	private Area area;

	private List districts = new ArrayList();

	 

	public Region( String name, String code ) {

		this.name = name;

		this.code = code;

	}	 

	public Region( String name, String code, Area area ) {

		this.name = name;

		this.code = code;

		this.area = area;

	}	 



	public String getName() {

		return name;

	}

	public String getCode() {

		return code;

	}

	public Area getArea() {

		return area;

	}

	

	public District getDistrict() {

		if (districts.size() == 1) {

			return (District)districts.get(0);

		}

		return null;

	}

	

	public List getDistricts() {

		return districts;

	}



	public void addDistrict(District district) {

		if ( !this.districts.contains( district ) ) {

			this.districts.add( district );

		}

	}

		

	public void addDistricts( List districts ) {

		District temp;

		

		for ( Iterator it = districts.iterator(); it.hasNext(); ) {

			temp = (District)it.next();			

			if ( !this.districts.contains( temp ) ) {

				this.districts.add( temp );

			}

		}		

	}





	public String toString() {

		StringBuffer sb = new StringBuffer();

		//sb.append( "\n" + this.getClass().getName() + "\n" );

		//sb.append( "code: " + code + "\n" );

		//sb.append( "name: " + name + "\n" );

		for (Iterator it = districts.iterator(); it.hasNext(); ) {

			sb.append((District)it.next());

		}

		return sb.toString();		

	}



	

	/**

	 * 

	 */

	public boolean equals( Object obj ) {

		return (obj instanceof Region && 

					this.getCode().equals( ((Region)obj).getCode()) );

	} 

	

	/**

	 * wrote this because i needed really needed to override equals().

	 */

	public int hashCode() { 

		int hash = 1;

		hash = hash * 31 + getCode().hashCode();

		hash = hash * 31 

        + (getCode() == null ? 0 : getCode().hashCode());

		return hash;

	}		

} 

