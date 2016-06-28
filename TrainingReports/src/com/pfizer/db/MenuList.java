package com.pfizer.db;

import java.util.List;
import com.tgix.html.LabelValueBean;
import java.util.ArrayList;

public class MenuList {
	/*
	 * private static final String SPECIAL = "Special"; private static final
	 * String TSR_ADMIN = "TSR Admin"; private static final String TSR_POWER =
	 * "TSR Powers"; private static final String POWER = "Power"; private static
	 * final String TSR_STEERE = "TSR Steere"; private static final String ADMIN
	 * = "ADMIN"; private static final String SUPER_ADMIN = "SUPER ADMIN";
	 * private static final String ONCOLOGY="ONCOLOGY";
	 */

	String level;
	String label;
	String url;
	String allow;
	String active;
	String id;
	String trackId;
	String sortorder;
	// Added for TRT Phase 2 enhancement
	String minimize;
	boolean archiveflag;
	// End of addition
	/* Adding extra variables for Group Administration and RBU changes */
	String busUnit;
	String salesOrg;
	String role;
	String userGroup;
	public static ArrayList accessList = new ArrayList();
	public static ArrayList specialAccessList = new ArrayList();

	/* Adding variables for SCE Feedback form enhancement */
	String feedbackUsers;
	String hqUsers;
	/* End of addition */
	// public static List accessList = new ArrayList();

	static {
		/*
		 * LabelValueBean bean = new LabelValueBean("All","");
		 * accessList.add(bean); bean = new LabelValueBean("ADMIN",ADMIN);
		 * accessList.add(bean); bean = new
		 * LabelValueBean("TSR Admin",TSR_ADMIN); accessList.add(bean); bean =
		 * new LabelValueBean("Power",POWER); accessList.add(bean); bean = new
		 * LabelValueBean("TSR or Powers",TSR_POWER); accessList.add(bean); bean
		 * = new LabelValueBean("TSR or Steere",TSR_STEERE);
		 * accessList.add(bean); bean = new LabelValueBean("Oncology",ONCOLOGY);
		 * accessList.add(bean);
		 */
	}
	String label1;

	public String getLabel1() {
		return label1;
	}

	public void setLabel1(String label1) {
		String s1 = "'";
		String[] s2 = null;

		if (label.contains(s1)) {
			s2 = label1.split("'");
			this.label1 = s2[0];
		} else {
			this.label1 = label;
		}

	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String id) {
		this.trackId = id;
	}

	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAllow() {
		return this.allow;
	}

	public void setAllow(String allow) {
		this.allow = allow;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/* Added for RBU changes */
	public static ArrayList getAccessList() {
		return accessList;
	}

	public void setAccessList(LabelValueBean labelValBean) {
		this.accessList.add(labelValBean);
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public String getBusUnit() {
		return busUnit;
	}

	public void setBusUnit(String busUnit) {
		this.busUnit = busUnit;
	}

	public String getSalesOrg() {
		return salesOrg;
	}

	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	/* Adding getter and setter methods for SCE Feedback Enhancement */
	public String getFeedbackUsers() {
		return feedbackUsers;
	}

	public void setFeedbackUsers(String feedbackUsers) {
		this.feedbackUsers = feedbackUsers;
	}

	/* End of addition */

	public String getHQUsers() {
		return hqUsers;
	}

	public void setHQUsers(String hqUsers) {
		this.hqUsers = hqUsers;
	}

	public void clearAccessList() {
		if (this.accessList.size() > 0) {
			this.accessList = new ArrayList();
		}
	}

	public static ArrayList getSpecialAccessList() {
		return specialAccessList;
	}

	public void setSpecialAccessList(LabelValueBean labelValBean) {
		this.specialAccessList.add(labelValBean);
	}

	public void clearSpecialAccessList() {
		if (this.specialAccessList.size() > 0) {
			this.specialAccessList = new ArrayList();
		}
	}

	// Added for TRT major Enhancement Phase 2 (home page configuration )
	public void setMinimize(String minimize) {
		this.minimize = minimize;
	}

	public String getMinimize() {
		return this.minimize;
	}

	public void setArchiveFlag(boolean archiveflag) {
		this.archiveflag = archiveflag;
	}

	public boolean getArchiveFlag() {
		return this.archiveflag;
	}
	// end of addition
}
