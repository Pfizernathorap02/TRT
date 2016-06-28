package com.pfizer.webapp.user; 



import java.util.ArrayList;

import java.util.Iterator;

import java.util.List;



public class UserTerritoryOld {

	public static final int TYPE_DISTRICT	= 3;

	public static final int TYPE_REGION		= 2;

	public static final int TYPE_AREA		= 1;

	public static final int TYPE_NATIONAL	= 0;

	public static final int TYPE_UNKNOWN	= -1;



	private List areas = new ArrayList();

	

	private int type;

	

	public UserTerritoryOld() {}

	

	

	public int getType() {

		if (areas.size() == 0) {

			return TYPE_UNKNOWN;

		}

		

		if (areas.size() > 1) {

			return TYPE_NATIONAL;

		}

		

		if ( ( (Area)areas.get( 0 ) ).getRegions().size() > 1 ) {

				return TYPE_AREA;

		}

		

		if ( ( (Area)areas.get( 0 ) ).getRegions().size() == 1 &&

				( (Region)( (Area)areas.get( 0 ) ).getRegions().get(0) ).getDistricts().size() > 1) {					

				return TYPE_REGION;

		}

		if ( ( (Area)areas.get( 0 ) ).getRegions().size() == 1 &&

				( (Region)( (Area)areas.get( 0 ) ).getRegions().get(0) ).getDistricts().size() == 1) {					

				return TYPE_DISTRICT;

		}

		

		return TYPE_UNKNOWN;

	}

	

	/**

	 * Adds an area object to it's list. 

	 */

	public void addArea( Area area ) {

		Area tmp;

		if ( areas.contains( area ) ) {

			tmp = (Area)areas.get( areas.indexOf( area ) );

			tmp.addRegions(area.getRegions());

		} else {

			areas.add( area );

		}

	}

		

	/**

	 * for types other than national

	 */

	public Area getArea() {

		if (areas.size() == 1) {

			return (Area)areas.get(0);

		}

		return null;

	}

	

	/**

	 * Only for type national

	 */ 

	public List getAreas() {

		return areas;

	}

	

	public String toString() {

		StringBuffer sb = new StringBuffer();

		for (Iterator it = areas.iterator(); it.hasNext(); ) {

			sb.append((Area)it.next());

		}

		return sb.toString();	

	}

	

} 

