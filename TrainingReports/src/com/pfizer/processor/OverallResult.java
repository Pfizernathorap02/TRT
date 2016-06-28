package com.pfizer.processor; 

import com.pfizer.db.Employee;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class OverallResult  {

	private Employee employee;
	private boolean passedPassFail = false;

	// setting these to be true because these are not required.  They will be validated as 
	// false in the OverallProcessor
	private boolean passedAttendance = true;
	private boolean passedSce = true;
	
	private Map statusMap= new HashMap();
	
	public  OverallResult( Employee employee ) {
		this.employee = employee;
	}
	
	public void put( String key, int value) {
		statusMap.put(key,new Integer(value));
	}
	public void put( String key, String value) {
		statusMap.put(key,value);
	}
	public Map getStatusMap() {
		return statusMap;
	}
	public Employee getEmployee() {
		return employee;
	}
		
	public boolean isPassed() {
		if ( passedAttendance
				&& passedPassFail
				&& passedSce ) {
			return true;
		}
		return false;	
	}
	
	/**
	 * This checks if the employee has not passed but is onleave.
	 */
	public boolean isOnLeave() {
		if (isPassed()) {
			return false;
		}
		
		if ("On-Leave".equals(employee.getEmployeeStatus())) {
			return true;
		}
		return false;
	}
	
	public void setAttendanceFlag(boolean flag) {
		this.passedAttendance = flag;
	}
	public void setPassedTestFlag(boolean flag) {
		this.passedPassFail = flag;
	}
	public void setPassedSceFlag(boolean flag) {
		this.passedSce = flag;
	}
	

    public int compareTo(Object arg0) {
		OverallResult or = (OverallResult)arg0;
		int tmpComp = employee.getAreaCd().compareTo( or.getEmployee().getAreaCd() );
		if ( tmpComp == 0) {
			tmpComp = employee.getRegionCd().compareTo(or.getEmployee().getRegionCd());
		}
		if ( tmpComp == 0) {
			tmpComp = employee.getDistrictId().compareTo(or.getEmployee().getDistrictId());
		}
		return tmpComp;
	}

	public static Comparator byTerritory = new Comparator() {
		public int compare( Object one, Object two ) {
			OverallResult or = (OverallResult)one;			
			OverallResult or2 = (OverallResult)two;
			
			int tmpComp = 0;
			String tmpOne = or.getEmployee().getAreaCd();
			String tmpTwo = or2.getEmployee().getAreaCd();
			String roleOne = or.getEmployee().getRole();
			String roleTwo = or2.getEmployee().getRole();
			
			tmpComp = nullSafe( tmpOne, tmpTwo);
			
			
			// check Manager status
			if ( tmpComp == 0 ) {				
				if ("VP".equals( roleOne) && "VP".equals( roleTwo )) {
					tmpComp = 0;
				}
				if ("VP".equals( roleOne)) {
					return -1;
				}
				if ("VP".equals( roleTwo )) {
					return 1;
				}	
			}
			
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getRegionCd();
				tmpTwo = or2.getEmployee().getRegionCd();

				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
			
			

			if ( tmpComp == 0 ) {
				if ("RM".equals( roleOne) && "RM".equals( roleTwo )) {
					tmpComp = 0;
				}
				if ("RM".equals( roleOne)) {
					return -1;
				}
				if ("RM".equals( roleTwo )) {
					return 1;
				}	
				
				if ("ARM".equals( roleOne) && "ARM".equals( roleTwo )) {
					tmpComp = 0;
				}
				if ("ARM".equals( roleOne)) {
					return -1;
				}
				if ("ARM".equals( roleTwo )) {
					return 1;
				}	


				tmpOne = or.getEmployee().getDistrictDesc();
				tmpTwo = or2.getEmployee().getDistrictDesc();
				
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}

			if ( tmpComp == 0 ) {
				if ("DM".equals( roleOne) && "DM".equals( roleTwo )) {
					tmpComp = 0;
				}
				if ("DM".equals( roleOne)) {
					return -1;
				}
				if ("DM".equals( roleTwo )) {
					return 1;
				}	
			}

			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getLastName();
				tmpTwo = or2.getEmployee().getLastName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getPreferredName();
				tmpTwo = or2.getEmployee().getPreferredName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}

			return tmpComp;
		}
		
		private int nullSafe( String one, String two) {
			if (one == null && two == null) {
				return 0;
			}
			if (one == null) {
				return -1;
			}
			if (two == null) {
				return 1;
			}
			return one.compareTo( two ); 
		}
		
	};

	public static Comparator byDistrict = new Comparator() {
		public int compare( Object one, Object two ) {
			OverallResult or = (OverallResult)one;			
			OverallResult or2 = (OverallResult)two;
			
			int tmpComp = 0;
			String tmpOne = or.getEmployee().getDistrictDesc();
			String tmpTwo = or2.getEmployee().getDistrictDesc();
			
			tmpComp = nullSafe( tmpOne, tmpTwo);

			// check Manager status
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getRole();
				tmpTwo = or2.getEmployee().getRole();
				
				if ("RM".equals( tmpOne) && "RM".equals( tmpTwo )) {
					tmpComp = 0;
				}
				if ("RM".equals( tmpOne)) {
					return -1;
				}
				if ("RM".equals( tmpTwo )) {
					return 1;
				}
	
				if ("ARM".equals( tmpOne) && "ARM".equals( tmpTwo )) {
					tmpComp = 0;
				}
				if ("ARM".equals( tmpOne)) {
					return -1;
				}
				if ("ARM".equals( tmpTwo )) {
					return 1;
				}
	
	
				if ("DM".equals( tmpOne) && "DM".equals( tmpTwo )) {
					tmpComp = 0;
				}
				if ("DM".equals( tmpOne)) {
					return -1;
				}
				if ("DM".equals( tmpTwo )) {
					return 1;
				}
			}			

			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getLastName();
				tmpTwo = or2.getEmployee().getLastName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getPreferredName();
				tmpTwo = or2.getEmployee().getPreferredName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}

			return tmpComp;
		}
		
		private int nullSafe( String one, String two) {
			if (one == null && two == null) {
				return 0;
			}
			if (one == null) {
				return -1;
			}
			if (two == null) {
				return 1;
			}
			return one.compareTo( two ); 
		}
		
	};
	
	public static Comparator byLname = new Comparator() {
		public int compare( Object one, Object two ) {
			OverallResult or = (OverallResult)one;			
			OverallResult or2 = (OverallResult)two;
			
			int tmpComp = 0;
			String tmpOne; 
			String tmpTwo; 
			

			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getLastName();
				tmpTwo = or2.getEmployee().getLastName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getPreferredName();
				tmpTwo = or2.getEmployee().getPreferredName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}

			return tmpComp;
		}
		
		private int nullSafe( String one, String two) {
			if (one == null && two == null) {
				return 0;
			}
			if (one == null) {
				return -1;
			}
			if (two == null) {
				return 1;
			}
			return one.compareTo( two ); 
		}
		
	};

	public static Comparator byFname = new Comparator() {
		public int compare( Object one, Object two ) {
			OverallResult or = (OverallResult)one;			
			OverallResult or2 = (OverallResult)two;
			
			int tmpComp = 0;
			String tmpOne; 
			String tmpTwo; 
			
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getPreferredName();
				tmpTwo = or2.getEmployee().getPreferredName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}

			return tmpComp;
		}
		
		private int nullSafe( String one, String two) {
			if (one == null && two == null) {
				return 0;
			}
			if (one == null) {
				return -1;
			}
			if (two == null) {
				return 1;
			}
			return one.compareTo( two ); 
		}
		
	};

	public static Comparator byRole = new Comparator() {
		public int compare( Object one, Object two ) {
			OverallResult or = (OverallResult)one;			
			OverallResult or2 = (OverallResult)two;
			
			int tmpComp = 0;
			String tmpOne; 
			String tmpTwo; 
			
			// check Manager status
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getRole();
				tmpTwo = or2.getEmployee().getRole();
				
				
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}			




			return tmpComp;
		}
		
		private int nullSafe( String one, String two) {
			if (one == null && two == null) {
				return 0;
			}
			if (one == null) {
				return -1;
			}
			if (two == null) {
				return 1;
			}
			return one.compareTo( two ); 
		}
		
	};

	public static Comparator byEmplid = new Comparator() {
		public int compare( Object one, Object two ) {
			OverallResult or = (OverallResult)one;			
			OverallResult or2 = (OverallResult)two;
			
			int tmpComp = 0;
			String tmpOne; 
			String tmpTwo; 
			
			// check Manager status
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getEmplId();
				tmpTwo = or2.getEmployee().getEmplId();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}			
			return tmpComp;
		}
		
		private int nullSafe( String one, String two) {
			if (one == null && two == null) {
				return 0;
			}
			if (one == null) {
				return -1;
			}
			if (two == null) {
				return 1;
			}
			return one.compareTo( two ); 
		}
		
	};

	public static Comparator byTeam = new Comparator() {
		public int compare( Object one, Object two ) {
			OverallResult or = (OverallResult)one;			
			OverallResult or2 = (OverallResult)two;
			
			int tmpComp = 0;
			String tmpOne; 
			String tmpTwo; 
			
			// check Manager status
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getTeamCode();
				tmpTwo = or2.getEmployee().getTeamCode();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}		

			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getLastName();
				tmpTwo = or2.getEmployee().getLastName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getPreferredName();
				tmpTwo = or2.getEmployee().getPreferredName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
				
			return tmpComp;
		}
		
		private int nullSafe( String one, String two) {
			if (one == null && two == null) {
				return 0;
			}
			if (one == null) {
				return -1;
			}
			if (two == null) {
				return 1;
			}
			return one.compareTo( two ); 
		}
		
	};

	public static Comparator byAttendance = new Comparator() {
		public int compare( Object one, Object two ) {
			OverallResult or = (OverallResult)one;			
			OverallResult or2 = (OverallResult)two;
			
			int tmpComp = 0;
			String tmpOne; 
			String tmpTwo; 
			
			// check Manager status
			if ( tmpComp == 0 ) {
				tmpOne = (String)or.getStatusMap().get("attendance");
				tmpTwo = (String)or2.getStatusMap().get("attendance");
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}		

			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getLastName();
				tmpTwo = or2.getEmployee().getLastName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getPreferredName();
				tmpTwo = or2.getEmployee().getPreferredName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
				
			return tmpComp;
		}
		private int nullSafe( String one, String two) {
			if (one == null && two == null) {
				return 0;
			}
			if (one == null) {
				return -1;
			}
			if (two == null) {
				return 1;
			}
			return one.compareTo( two ); 
		}
		
	};

	public static Comparator bySce = new Comparator() {
		public int compare( Object one, Object two ) {
			OverallResult or = (OverallResult)one;			
			OverallResult or2 = (OverallResult)two;
			
			int tmpComp = 0;
			String tmpOne; 
			String tmpTwo; 
			
			// check Manager status
			if ( tmpComp == 0 ) {
				tmpOne = (String)or.getStatusMap().get("sce");
				tmpTwo = (String)or2.getStatusMap().get("sce");
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}		

			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getLastName();
				tmpTwo = or2.getEmployee().getLastName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getPreferredName();
				tmpTwo = or2.getEmployee().getPreferredName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
				
			return tmpComp;
		}
		
		private int nullSafe( String one, String two) {
			if (one == null && two == null) {
				return 0;
			}
			if (one == null) {
				return -1;
			}
			if (two == null) {
				return 1;
			}
			return one.compareTo( two ); 
		}
		
	};
	
	public static Comparator byOverall = new Comparator() {
		public int compare( Object one, Object two ) {
			OverallResult or = (OverallResult)one;			
			OverallResult or2 = (OverallResult)two;
			
			int tmpComp = 0;
			String statusOne = "";
			String statusTwo = "";
			if (or.isPassed()) {
				statusOne = "Complete";
			} else if (or.isOnLeave()) {
				statusOne = "On Leave";
			} else {
				statusOne = "Not Complete";
			}
			if (or2.isPassed()) {
				statusTwo = "Complete";
			} else if (or2.isOnLeave()) {
				statusTwo = "On Leave";
			} else {
				statusTwo = "Not Complete";
			}
			
			return statusOne.compareTo( statusTwo ); 
		}		
	};

	public static Comparator byTest1 = new Comparator() {
		public int compare( Object one, Object two ) {
			OverallResult or = (OverallResult)one;			
			OverallResult or2 = (OverallResult)two;
			
			int tmpComp = 0;
			String tmpOne; 
			String tmpTwo; 
			
			// check Manager status
			if ( tmpComp == 0 ) {
				Integer sOne = (Integer)or.getStatusMap().get("test1");
				Integer sTwo = (Integer)or2.getStatusMap().get("test1");
				tmpComp = nullSafe( sOne, sTwo);
			}		

			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getLastName();
				tmpTwo = or2.getEmployee().getLastName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getPreferredName();
				tmpTwo = or2.getEmployee().getPreferredName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
				
			return tmpComp;
		}
		
		private int nullSafe( Comparable one, Comparable two) {
			if (one == null && two == null) {
				return 0;
			}
			if (one == null) {
				return -1;
			}
			if (two == null) {
				return 1;
			}
			return one.compareTo( two ); 
		}
		
	};

	public static Comparator byTest2 = new Comparator() {
		public int compare( Object one, Object two ) {
			OverallResult or = (OverallResult)one;			
			OverallResult or2 = (OverallResult)two;
			
			int tmpComp = 0;
			String tmpOne; 
			String tmpTwo; 
			
			// check Manager status
			if ( tmpComp == 0 ) {
				Integer sOne = (Integer)or.getStatusMap().get("test2");
				Integer sTwo = (Integer)or2.getStatusMap().get("test2");
				tmpComp = nullSafe( sOne, sTwo);
			}		

			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getLastName();
				tmpTwo = or2.getEmployee().getLastName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getPreferredName();
				tmpTwo = or2.getEmployee().getPreferredName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
				
			return tmpComp;
		}
		
		private int nullSafe( Comparable one, Comparable two) {
			if (one == null && two == null) {
				return 0;
			}
			if (one == null) {
				return -1;
			}
			if (two == null) {
				return 1;
			}
			return one.compareTo( two ); 
		}
		
	};
	public static Comparator byTest3 = new Comparator() {
		public int compare( Object one, Object two ) {
			OverallResult or = (OverallResult)one;			
			OverallResult or2 = (OverallResult)two;
			
			int tmpComp = 0;
			String tmpOne; 
			String tmpTwo; 
			
			// check Manager status
			if ( tmpComp == 0 ) {
				Integer sOne = (Integer)or.getStatusMap().get("test3");
				Integer sTwo = (Integer)or2.getStatusMap().get("test3");
				tmpComp = nullSafe( sOne, sTwo);
			}		

			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getLastName();
				tmpTwo = or2.getEmployee().getLastName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getPreferredName();
				tmpTwo = or2.getEmployee().getPreferredName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
				
			return tmpComp;
		}
		
		private int nullSafe( Comparable one, Comparable two) {
			if (one == null && two == null) {
				return 0;
			}
			if (one == null) {
				return -1;
			}
			if (two == null) {
				return 1;
			}
			return one.compareTo( two ); 
		}
		
	};

	public static Comparator byTest4 = new Comparator() {
		public int compare( Object one, Object two ) {
			OverallResult or = (OverallResult)one;			
			OverallResult or2 = (OverallResult)two;
			
			int tmpComp = 0;
			String tmpOne; 
			String tmpTwo; 
			
			// check Manager status
			if ( tmpComp == 0 ) {
				Integer sOne = (Integer)or.getStatusMap().get("test4");
				Integer sTwo = (Integer)or2.getStatusMap().get("test4");
				tmpComp = nullSafe( sOne, sTwo);
			}		

			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getLastName();
				tmpTwo = or2.getEmployee().getLastName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
			if ( tmpComp == 0 ) {
				tmpOne = or.getEmployee().getPreferredName();
				tmpTwo = or2.getEmployee().getPreferredName();
				tmpComp = nullSafe( tmpOne, tmpTwo);
			}
				
			return tmpComp;
		}
		
		private int nullSafe( Comparable one, Comparable two) {
			if (one == null && two == null) {
				return 0;
			}
			if (one == null) {
				return -1;
			}
			if (two == null) {
				return 1;
			}
			return one.compareTo( two ); 
		}
		
	};

} 
