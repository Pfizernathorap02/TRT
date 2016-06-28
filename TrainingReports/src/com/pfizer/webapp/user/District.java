package com.pfizer.webapp.user; 



public class District {

	private	final String name;

	private final String id;

	private Region region;

	

	public District( String name, String id ) {

		this.name = name;

		this.id = id;

	}

	  

	public District( String name, String id, Region region ) {

		this.name = name;

		this.id = id;

		this.region = region;

	}  

	

	public String getName() {

		return name;

	}

	public String getId() {

		return id;

	}

	public Area getArea() {

		return region.getArea();

	}

	public Region getRegion() {

		return region;

	}





	public String toString() {

		StringBuffer sb = new StringBuffer();

		//sb.append( "\n" + this.getClass().getName() + "\n" );

		//sb.append( "  id: " + id + "\n" );

		//sb.append( "name: " + name + "\n" );

		return sb.toString();		

	}



	/**

	 * 

	 */

	public boolean equals( Object obj ) {

		return (obj instanceof District && 

					this.getId().equals( ((District)obj).getId()) );

	} 

	

	/**

	 * wrote this because i needed really needed to override equals().

	 */

	public int hashCode() { 

		int hash = 1;

		hash = hash * 31 + getId().hashCode();

		hash = hash * 31 

        + (getId() == null ? 0 : getId().hashCode());

		return hash;

	}	



} 

