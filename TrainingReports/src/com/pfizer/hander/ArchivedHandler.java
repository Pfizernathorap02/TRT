package com.pfizer.hander; 
 
import com.pfizer.utils.DBUtil;
import com.pfizer.webapp.AppConst;

import java.util.ArrayList;
import java.util.List;

public class ArchivedHandler 
{ 
    public List executeSql( String sql ) {
        List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE);
        return result;
    }
  
     public List getLabelList(String headerId) {
        List ret = new ArrayList();
        String sql = "select training_report_label from training_report where training_report_id='"+headerId+"'";
        List result = executeSql(sql);
        //log.info(sql);
        System.out.println(sql);
        return result;
    }
  
    public List getLabelUrlList(String headerId) {
        List ret = new ArrayList();
        // for admin 
        String sql = "select TRAINING_REPORT_LABEL,TRAINING_REPORT_URL,LEVEL,TRACK_ID from TRAINING_REPORT START WITH PARENT ='"+
                    headerId+"' AND ACTIVE=0 and delete_flag ='N' CONNECT BY PRIOR TRAINING_REPORT_ID = PARENT ORDER SIBLINGS BY SORT_ORDER ";
        
        List result = executeSql(sql);
        //log.info(sql);
        System.out.println(sql);
        return result;
    }
   
    public List getLabelUrlListUsers(String headerId,String groupName) {
        List ret = new ArrayList();
        // for other users
        String sql = "select training_report_label,training_report_url,LEVEL,TRACK_ID from training_report START WITH parent='"+headerId+"' and active='0' and delete_flag='N' and (allow_group ='"+groupName+"' or allow_group is null) "+
                    " CONNECT BY PRIOR TRAINING_REPORT_ID = PARENT ORDER SIBLINGS BY SORT_ORDER ";
        List result = executeSql(sql);
        //log.info(sql);
       System.out.println(sql);
        return result;
    }
    public List getCountArchivedReports(String headerId,String groupName){
        String sql = " select * from training_report where active=0 and parent='"+headerId+"' and delete_flag='N' ";
        List result = executeSql(sql);
       // System.out.println("sql in getCountArchivedReports=="+sql);
        return result;
    }
    public List getCountArchivedReportsUsers(String headerId,String groupName){
        String sql = " select * from training_report where active=0 and parent='"+headerId+"' and (allow_group ='"+groupName+"' or allow_group is null) and delete_flag='N' ";
        List result = executeSql(sql);
     //   System.out.println("sql in getCountArchivedReports=="+sql);
        return result;
    }
    public List getSectionDisplay(String headerId,String groupName){
        String sql = " select * from training_report where active=1 and parent='"+headerId+"' and (allow_group ='"+groupName+"' or allow_group is null) and delete_flag='N' ";
        List result = executeSql(sql);
     //   System.out.println("sql in handler=="+sql);
        return result;
    }
    
    
} 
