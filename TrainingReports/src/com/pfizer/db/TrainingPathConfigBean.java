package com.pfizer.db; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import com.tgix.html.LabelValueBean;

public class TrainingPathConfigBean 
{ 
    private HashMap salesDescMap=new HashMap();
    private HashMap buDescMap=new HashMap();
    private HashMap roleDescMap=new HashMap();
    private LinkedHashMap courseDescMap=new LinkedHashMap();
    private LinkedHashMap courseAliasMap=new LinkedHashMap();
    private String configId;
    
    private List allRoleCodes;
    private List allSalesOrg;
    private List allCourseCodes;
    private List allBusinessUnit;
  
  
    private List salesOrgList = new ArrayList();
    private List businessUnitList = new ArrayList();
    private List roleList = new ArrayList();
    private List courseList = new ArrayList();
    
    
    public HashMap getSalesDescList(){
     return this.salesDescMap;
    }
    
    public HashMap getBuDescList(){
        return this.buDescMap;
    }
    public HashMap getRoleDescList(){
        return this.roleDescMap;
    }
    public LinkedHashMap getCourseDescList(){
        return this.courseDescMap;
    }
    public LinkedHashMap getCourseAliasList(){
        return this.courseAliasMap;
    }
    
    public void setConfigId(String configId){
        this.configId=configId;
    }
    public String getConfigId(){
        return this.configId;
    }
    
    public void addSalesDesc(String Code,String salesDesc)
    {
        this.salesDescMap.put(Code,salesDesc);
    }
    public void addBuDesc(String Code,String buDesc)
    {
        this.buDescMap.put(Code,buDesc);
    }
     public void addRoleDesc(String Code,String roleDesc)
    {
        this.roleDescMap.put(Code,roleDesc);
    }
     public void addCourseDesc(String Code,String courseDesc)
    {
        this.courseDescMap.put(Code,courseDesc);
    }
     public void addCourseAlias(String Code,String courseDesc)
    {
        this.courseAliasMap.put(Code,courseDesc);
    }
    
    
    
    public void setAllRoleCodes(List roleCodes){
        this.allRoleCodes=roleCodes;
    }
    public List getAllRoleCodes()
    {
        return this.allRoleCodes;
    }
   
    public void setAllSalesOrg(List salesOrg){
        this.allSalesOrg=salesOrg;
    }
    public List getAllSalesOrg()
    {
        return this.allSalesOrg;
    }
    
    public void setAllBusinessUnit(List businessUnit){
        this.allBusinessUnit=businessUnit;
    }
    public List getAllBusinessUnit()
    {
        return this.allBusinessUnit;
    }
    
    
    public void setAllSalesOrgList(LabelValueBean labelValBean){
        this.salesOrgList.add(labelValBean);
    }
    public List getAllSalesOrgList()
    {
        return this.salesOrgList;
    }
   
     public void setAllBusinessUnitList(LabelValueBean labelValBean){
        this.businessUnitList.add(labelValBean);
    }
    public List getAllBusinessUnitList()
    {
        return this.businessUnitList;
    }
   
    public void setAllRoleList(LabelValueBean labelValBean){
        this.roleList.add(labelValBean);
    }
    public List getAllRoleList()
    {
        return this.roleList;
    }
} 
