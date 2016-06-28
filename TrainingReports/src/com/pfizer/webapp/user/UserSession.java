package com.pfizer.webapp.user; 

import com.pfizer.db.LaunchMeeting;
import com.pfizer.db.MenuList;
import com.pfizer.db.P2lTrack;
import com.pfizer.hander.UserHandler;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.webapp.wc.components.report.phasereports.CourseSearchForm;
import com.tgix.Utils.Util;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class UserSession {
	public static final String ATTRIBUTE = "USER_SESSION";
	
	private boolean isLoggedIn = false;
	private boolean isAdmin = false;
	private boolean isSuperAdmin = false;
    private boolean isSpecialRoleUser = false;
	private User orignalUser;
	private UserFilter userFilter = null;
	private OverallProcessor overallProcessor = null;
	private User user;
	private String bounceBackUrl;
    private String currentSlice = "";
    private P2lTrack track = null;
	private List allP2lTracks;
    private CourseSearchForm csForm = null;
    private MenuList mList = null;    
    private LaunchMeeting launchMeetingTrack  = null;
    
    private String currentActivity = "";
    
    /* Addition for SCE Feedback form enhancement */
    private boolean isFeedbackUser=false;
    /* End of addition */
     //added for TRT major enhancement 3.6- F6
    private String DelegatedUserId;
    // Added for TRT Phase 2 Enhancement  - HQ Users
    private boolean isHQUser = false;
     
	public UserSession() {} 
	
    public MenuList getMenuList() {
        return mList;
    }
    public void setMenuList( MenuList ml ) {
        this.mList = ml;
    }
    
    public void setAllP2lTracks( List tracks ) {
        this.allP2lTracks = tracks;
    }
    public void setCourseSearchForm(CourseSearchForm form) {
        this.csForm = form;
    }
    public CourseSearchForm getCourseSearchForm() {
        return csForm;
    }
    public List getAllP2lTracks() {
        return this.allP2lTracks;
    }
    public void setCurrentSlice( String str ) {
        this.currentSlice = str;
    }
    public P2lTrack getTrack() {
        return track;
    }
    public void setTrack( P2lTrack track ) {
        this.track = track;
    }
    
    public LaunchMeeting getLaunchMeetingTrack() {
        return launchMeetingTrack;
    }
    public void setLaunchMeetingTrack( LaunchMeeting track ) {
        this.launchMeetingTrack = track;
    }
    
    public String getCurrentSlice() {
        return currentSlice;
        
    }
	public void setIsAdmin( boolean flag ) {
		isAdmin = flag;
	}
	public void setIsSuperAdmin( boolean flag ) {
		isSuperAdmin = flag;
	}
    
    public void setIsSpecialRoleUser( boolean flag ) {
		isSpecialRoleUser = flag;
	}
	public boolean isSpecialRoleUser() {
		return isSpecialRoleUser;
	}
    
    //
     //added for TRT major enhancement 3.6- F6
	public void setIsDelegatedUser( String Id ) {
        DelegatedUserId = Id;
	}
    public String getIsDelegatedUser(){
        return DelegatedUserId;
    }
    //ends here
	public User getOrignalUser() {
		return orignalUser;
	}
	public boolean isAdmin() {
		if (user != null && user.isAdmin()) {
			return true;
		}		
		return isAdmin;
	}
	public boolean isTsrAdmin() {
		if (orignalUser != null && orignalUser.isTsrAdmin()) {
			return true;
		}		
		return false;
	}
	public boolean isSuperAdmin() {
		if (user != null && user.isSuperAdmin()) {
			return true;
		}		
		return isSuperAdmin;
	}
    
     /* Addition for SCE Feedback form enhancement */
    public boolean isFeedbackUser()
    {
        if(user !=null && user.isFeedbackUser())
        {
            return true;
        }
        return isFeedbackUser;
    }
    /*End of addition */
	
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setBounceBack( String url ) {
		bounceBackUrl = url;
	}
	public String getBounceBack() {
		return bounceBackUrl;
	}
	
	public User getUser(String userId) {
        System.out.print("Inside getUser");
		ServiceFactory factory = Service.getServiceFactory(); 
		UserHandler uHandler = 	factory.getUserHandler();
		User u = uHandler.getUserByEmployeeId( userId );
		if ( u != null ) {
			isLoggedIn = true;
		}
		this.setUser(u);
		return u;
	}

	public User getUser(String userId, String ntId, String domain) 
	{
		ServiceFactory factory = Service.getServiceFactory(); 
		UserHandler uHandler = 	factory.getUserHandler();
		User u = uHandler.getUserByUp( userId , ntId, domain );
		if ( u != null && u.getValidUser()) {
			isLoggedIn = true;
			if ( User.USER_TYPE_ADMIN.equals( u.getRole() ) ) {
				isAdmin = true;
				orignalUser = u;
			}
			if ( User.USER_TYPE_TSR.equals( u.getRole() ) ) {
				isAdmin = true;
				orignalUser = u;
			}
			if ( User.USER_TYPE_SUPER_ADMIN.equals( u.getRole() ) ) {
				orignalUser = u;
				isAdmin = true;
				isSuperAdmin = true;
            System.out.println("\n\n\norignalUser:" + orignalUser.getRole());
			}
            /* Addition for SCE Feedback form enhancement */
            if( User.USER_TYPE_FBU.equals(u.getRole()))
            {
                orignalUser = u;
            }
            if(u.isSpecialRole()){
                isSpecialRoleUser = true;
            }
            /* End of addition */
		}
		this.setUser(u);
		return u;
	}

	public void reset() {
        System.out.println("\n\n\n\n\n\nhelllllloo" + orignalUser);
		user = orignalUser;
	}
	public void setUser( User user ) {
		this.user = user;
	}
	
	public void setUserFilter( UserFilter filter) {
		this.userFilter = filter;
	}
	
	public UserFilter getUserFilter() {
		if (userFilter == null) {
			userFilter = new UserFilter();
		}
		return userFilter;
	}
		
	public void setOverallProcessor( OverallProcessor processor ) {
		this.overallProcessor = processor;
	}
	public OverallProcessor getOverallProcessor() {
		return overallProcessor;
	}
	
	public TerritoryFilterForm getUserFilterForm() {
		 TerritoryFilterForm terrFilter = getUserFilter().getFilterForm();
		
		if (terrFilter == null) {
			terrFilter = getNewTerritoryFilterForm();
		}
		return terrFilter;
	}
	
	/**
	 * Gets a new TerritoryFilterForm with inital default values
	 */
	public TerritoryFilterForm getNewTerritoryFilterForm() {
		TerritoryFilterForm terrFilter = null;
		
		terrFilter = new TerritoryFilterForm();
		User u = getUser();
        ArrayList firstDropDown= new ArrayList();
        
    if(u.getUserTerritoryOld()!=null){
		if ( u.getUserTerritoryOld().getArea() == null) {
			terrFilter.setArea("All");
			terrFilter.setRegion("All");
			terrFilter.setDistrict("All");
			return terrFilter;
		} else {		
			Area area = u.getUserTerritoryOld().getArea();
			terrFilter.setArea( area.getCode() );
			
			if ( area.getRegion() == null ) {
				terrFilter.setRegion("All");
				terrFilter.setDistrict("All");					
			} else {
				terrFilter.setRegion( area.getRegion().getCode() );
				
				if ( area.getRegion().getDistrict() == null ) {
					terrFilter.setDistrict( "All" );										
				} else {
					terrFilter.setDistrict( area.getRegion().getDistrict().getId() );
				}
                }
			
            }
        }
		System.out.println("USER SESSION-FIRST GEO"+u.getUserTerritory().getFirstDropdown());
            if(u.getUserTerritory().getFirstDropdown()!=null)
           { 
            try{
                firstDropDown= u.getUserTerritory().getFirstDropdown();                         
                System.out.println("UserSession"+firstDropDown.size());
                //System.out.println("UserSession"+firstDropDown.get(0).toString());
                List firstDD = firstDropDown;
                terrFilter.setLevel1("All");
                terrFilter.setLevel2("All");
                terrFilter.setLevel3("All");
                terrFilter.setLevel4("All");
                terrFilter.setLevel5("All");
                terrFilter.setLevel6("All");
                terrFilter.setLevel7("All");
                terrFilter.setLevel8("All");
                terrFilter.setLevel9("All");
                terrFilter.setLevel10("All");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
           } 
			
		return terrFilter;
	}
	
	public void setUserFilterForm( TerritoryFilterForm form ) {
		getUserFilter().setFilterForm(form);
	}
	
	/**
	 * This method is used to get the UserSession object from the request object.  If it doesn't exsit 
	 * it will create a new object and put it in the session.
	 */
	public static UserSession getUserSession( HttpServletRequest request )
	{
		UserSession uSession = (UserSession)request.getSession(true).getAttribute( UserSession.ATTRIBUTE );
		
		if (uSession == null) 
		{
			uSession = new UserSession();
			request.getSession().setAttribute( UserSession.ATTRIBUTE, uSession );			
		}
		
		return uSession;
	}
    
    /*Added for RBU */	
      public void setCurrentActivity( String str ) {
        this.currentActivity = str;
    }
     public String getCurrentActivity() {
        return currentActivity;
        
	}	
    // Added for TRT Phase 2 -HQ Users
    public void setIsHQUser( boolean flag ) {
		isHQUser = flag;
	}
    public boolean isHQUser() {
		if (user != null && user.isHQUser()) {
			return true;
		}		
		return isHQUser;
	}

} 
