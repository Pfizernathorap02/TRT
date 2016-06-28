package com.pfizer.webapp.user; 



import java.util.ArrayList;

import java.util.Iterator;

import java.util.List;



public class Area {

	public static final String AREA_NATIONAL	= "NATL";

	public static final String AREA_CARI		= "CARI";

	public static final String AREA_CENT		= "CENT";

	public static final String AREA_EAST		= "EAST";

	public static final String AREA_ECH			= "ECH";

	public static final String AREA_WCH			= "WCH";

	public static final String AREA_WEST		= "WEST";

	

	private String description;

	private String code;

	

	private List regions = new ArrayList();

	

	public Area() {

	}

	

	public Area( String code, String description ) {

		this.description = description;

		this.code = code;

	}

	

	public void addRegion(Region region) {

		if ( this.regions.contains( region ) ) {

			Region curr = (Region)this.regions.get( this.regions.indexOf( region ) );

			curr.addDistricts( region.getDistricts() );

		} else {

			this.regions.add( region );

		}

	}

	

	public void addRegions(List regions) {

		Region temp;

		

		for ( Iterator it = regions.iterator(); it.hasNext(); ) {

			temp = (Region)it.next();

			

			// if there is already a region object for this

			// region, then add it's district objects

			if ( this.regions.contains( temp ) ) {

				Region curr = (Region)this.regions.get( this.regions.indexOf( temp ) );

				curr.addDistricts( temp.getDistricts() );

			} else {

				this.regions.add( temp );

			}

		}

	}

	

	/**

	 * Will only return a region object if this area object

	 * has ONE region in it's list of regions.  It will return null otherwise.

	 */ 

	public Region getRegion() {

		if (regions.size() == 1) {

			return (Region)regions.get(0);

		}

		return null;

	}

	

	public List getRegions() {

		return regions;

	} 

	public String getDescription() {

		return description;

	}

	

	

	public String getCode() {

		return code;

	}

	

	public String toString() {

		StringBuffer sb = new StringBuffer();

		sb.append( "\n" + this.getClass().getName() + "\n" );

		sb.append( "       code: " + code + "\n" );

		sb.append( "description: " + description + "\n" );

		for (Iterator it = regions.iterator(); it.hasNext(); ) {

			sb.append((Region)it.next());

		}

		return sb.toString();		

	}

	

	

	/**

	 * 

	 */

	public boolean equals( Object obj ) {

		return (obj instanceof Area && 

					this.getCode().equals( ((Area)obj).getCode()) );

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

