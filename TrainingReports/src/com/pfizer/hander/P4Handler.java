package com.pfizer.hander; 

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pfizer.db.Attendance;
import com.pfizer.db.P4ClassData;
import com.pfizer.db.P4ClassRoom;
import com.pfizer.db.P4ClassTable;
import com.pfizer.db.P4RoomGridVO;
import com.pfizer.db.P4Trainee;
import com.pfizer.db.P4TrainingWeek;
import com.pfizer.utils.HibernateUtils;
import com.pfizer.utils.JdbcConnectionUtil;
import com.tgix.Utils.Util;
//import com.pfizer.db.RBUClassData;
//import com.pfizer.db.RBUClassRoom;
//import com.pfizer.db.RBUClassTable;
//import com.pfizer.db.RBURoomGridVO;
//import com.pfizer.db.RBUTrainee;
//import com.pfizer.db.RBUTrainingWeek;
// Code changes Infosys :import db.TrDB;

public class P4Handler {
	protected static final Log log = LogFactory.getLog( EmployeeHandler.class );
	
	
	public P4Handler() {
	}



   /* public List getClassWeeks(){
        List weeks = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select distinct week_id, wave_id from V_P4_CLASS_TABLE where week_id in (select distinct week_id from v_p4_class_roster_report) order by wave_id,week_id");
            
            while(rs.next()){
                P4TrainingWeek week = new P4TrainingWeek();
                week.setWeek_id(rs.getString("week_id"));
                week.setWeek_name("Wave " + rs.getString("wave_id") + " Week " + rs.getString("week_id") );
                week.setWave_id(rs.getString("wave_id"));
                //week.setStart_date(rs.getTimestamp("start_date"));
                //week.setEnd_date(rs.getTimestamp("end_date"));
                
                weeks.add(week);
            }            
            
            
            st.close();
            conn.close();
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}
        return weeks;
        
    }
*/
	// Infosys migrated code changes starts 
	public List getClassWeeks(){
        List weeks = new ArrayList();
        
        Session session = HibernateUtils.getHibernateSession();
        try
        {
    			Query q = session.createSQLQuery("select distinct week_id, wave_id from V_P4_CLASS_TABLE where week_id in (select distinct week_id from v_p4_class_roster_report) order by wave_id,week_id");
    			List list=new ArrayList();
    			list=q.list();
    			Iterator it = list.iterator();
    			while(it.hasNext()) {

    				P4TrainingWeek week = new P4TrainingWeek();
    				Object[] field = (Object[]) it.next();
    				week.setWeek_id(field[0].toString());
    				week.setWeek_name("Wave " +field[1].toString()+ " Week " + field[0].toString());
    				week.setWave_id(field[1].toString());
    				weeks.add(week);	
    			}
        }
    			catch (HibernateException e) {
    				System.out.println();
    				e.printStackTrace();
    				System.out.println("getClassWeeks Hibernatate Exception");
    			} finally {
    				HibernateUtils.closeHibernateSession(session);
    			}
    			
    			return weeks;
    		}

	
	
   /* public List getClassRooms(String week_id, String wave_id){
               // Added to get the list of Guest training claases for this employee
        List roomvos = new ArrayList(); // a list of roomdata of RBURoomGridVO
        
        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a"); 
        
        SimpleDateFormat dateformat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); 
        
        

        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;

        ResultSet rs1 = null;
		Statement st1 = null;
        
        String sql = "select distinct room_id, room_name, week_start_date from v_p4_class_table v  "
                     + " where wave_id = " + wave_id + " and week_id = " + week_id 
                     + " order by room_id ";

        
        try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();                     
            st = conn.createStatement();		
            st1 = conn.createStatement();	
            rs = st.executeQuery(sql);


            //convert relational rows to object hierarchy       
            //Step1. find all rooms for this week 
			while (rs.next()) {
                P4RoomGridVO gvo = new P4RoomGridVO();
                gvo.setRoom_id(rs.getString("room_id"));
                gvo.setRoom_name(rs.getString("room_name"));
                gvo.setWeek_id(week_id);       
                
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setFirstDayOfWeek(GregorianCalendar.MONDAY);
                calendar.setTime(rs.getTimestamp("week_start_date")); 
                
                Date mdate = rs.getTimestamp("week_start_date");
                calendar.get(Calendar.DAY_OF_WEEK);
                Calendar calendar2 = GregorianCalendar.getInstance();
                calendar2.setFirstDayOfWeek(GregorianCalendar.MONDAY);
                calendar2.setTime(calendar.getTime()); 
                calendar2.roll(Calendar.HOUR_OF_DAY, 12);
        
                Date [] weekdays = new Date [5];
                Date [] weekdays2 = new Date [5];
                
       
                for(int i = 0; i < weekdays.length; i ++){
                    weekdays [i] = calendar.getTime();
                    weekdays2 [i] = calendar2.getTime();
                    //System.out.println("date1 " + dateformat1.format(calendar.getTime()));
                    calendar.roll(Calendar.DAY_OF_MONTH, 1);
                    calendar2.roll(Calendar.DAY_OF_MONTH, 1);
                }
                
                
                List roomdatas = new ArrayList();

                //Step2. get data for each room
                        //for each weekday, find classroom data 
                        //for each classroom data, find out table info
                for(int i = 0; i < weekdays.length; i ++){
                    sql = " select distinct class_id class_id, product_cd, product_desc, table_id, trainee_count, gt_count,v.start_date  from v_p4_class_table v "
                         + " where TO_DATE('" + dateformat.format(weekdays2[i]) +  "', 'mm/dd/yyyy hh:mi:ss am') >= v.start_date " 
                         + " AND TO_DATE('" + dateformat.format(weekdays[i]) +  "', 'mm/dd/yyyy hh:mi:ss am') <= v.end_date "                        
                         + " and room_id = '" + gvo.getRoom_id() + "' "
                         + " order by v.start_date, product_cd, table_id";
                    
                    if ("10".equals(gvo.getRoom_id())) {
                        //System.out.println(sql + "\n\n");
                    }
                    rs1 = st1.executeQuery(sql);  
                    
                    P4ClassRoom roomdata = new P4ClassRoom();
                    List p4classes = new ArrayList();
                    P4ClassData p4class = new P4ClassData();
                    List tables = new ArrayList();   
                    P4ClassTable table = new P4ClassTable();            
                    //RBUClassData rbuclass2 = null;
                    //List tables2 = new ArrayList();
                    
                    if(rs1.next()){
                         roomdata.setRoom_id(gvo.getRoom_id());
                         roomdata.setRoom_name(gvo.getRoom_name());
                         //weekdays[i]
                         Calendar ctemp = GregorianCalendar.getInstance();
                         ctemp.setTime(weekdays[i]);
                         roomdata.setWeekday(getWeekDay(i));  
                         roomdata.setDay(weekdays[i]);
                         
                        // RBUClassData rbuclass = new RBUClassData();
                         p4class.setCourseID(rs1.getString("class_id"));
                         p4class.setProductcd(rs1.getString("product_cd"));
                         p4class.setProductdesc(rs1.getString("product_desc"));                                            
                         
                                                 
                         
                         table.setTalbe_id(rs1.getString("table_id"));
                         table.setTraineesCnt(rs1.getInt("trainee_count"));
                         table.setGuestCnt(rs1.getInt("gt_count"));   
                         tables.add(table);
                    }

                    while(rs1.next()){
                        if(rs1.getString("product_cd").equals(p4class.getProductcd())){
                         table = new P4ClassTable();
                         table.setTalbe_id(rs1.getString("table_id"));
                         table.setTraineesCnt(rs1.getInt("trainee_count"));
                         table.setGuestCnt(rs1.getInt("gt_count"));   
                         tables.add(table);    
                        }else{
                          //when new class found - add current class to list, reset it for new class
                          p4class.setTables(tables);
                          p4classes.add(p4class);
                          
                          p4class = new P4ClassData();
                          tables = new ArrayList();
                                                
                          p4class.setCourseID(rs1.getString("class_id"));
                          p4class.setProductcd(rs1.getString("product_cd"));
                          p4class.setProductdesc(rs1.getString("product_desc"));                                            
                        //System.out.println(rs1.getString("product_desc"));
                          table = new P4ClassTable();
                         table.setTalbe_id(rs1.getString("table_id"));
                         table.setTraineesCnt(rs1.getInt("trainee_count"));
                         table.setGuestCnt(rs1.getInt("gt_count"));   
                         tables.add(table);
                        //System.out.println(table2.getTalbe_id());    
                         //   break;
                          
                        }
                    }    
                      
                   p4class.setTables(tables);
                   p4classes.add(p4class);
                   roomdata.setP4Classes(p4classes);
                   
                   
    //                roomdata.setRBUClass(rbuclass);                        


                     
                     while(rs1.next()){
                         RBUClassTable table = new RBUClassTable();
                         table.setTalbe_id(rs1.getString("table_id"));
                         table.setTraineesCnt(rs1.getInt("t_cnt"));
                         table.setGuestCnt(rs1.getInt("g_cnt"));   
                         tables2.add(table);    
                    }  
                    
                    if(rbuclass2 !=null){
                        rbuclass2.setTables(tables2);
                        roomdata.setRBUClass2(rbuclass2);
                         //System.out.println(tables2.size());   
                    }
                    
                    
                    
                   // roomdata.setTables(tables);
                    roomdatas.add(roomdata);
                }
                
                gvo.setRoomdata(roomdatas);                     
                roomvos.add(gvo);
			}

            st.close();
            st1.close();
         
		} catch (Exception e) { 
			e.printStackTrace();
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
            if ( rs1 != null) {
				try {
					rs1.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st1 != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}
        return roomvos;
    }
*/

	// Infosys Migrated code changes starts
	

	
	
	public List getClassRooms(String week_id, String wave_id){
        // Added to get the list of Guest training claases for this employee
		List roomvos = new ArrayList(); // a list of roomdata of RBURoomGridVO
 
		SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a"); 
 
		SimpleDateFormat dateformat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); 
 
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		
	
		ResultSet rs = null;
		Statement st = null;
		

		ResultSet rs1 = null;
		Statement st1 = null;
 
 String sql = "select distinct room_id, room_name, week_start_date from v_p4_class_table v  "
              + " where wave_id = " + wave_id + " and week_id = " + week_id 
              + " order by room_id ";

 
 try {
		 st = conn.createStatement();		
		 st1 = conn.createStatement();	
		 rs = st.executeQuery(sql);
System.out.println(sql+"sql111");

     //convert relational rows to object hierarchy       
     //Step1. find all rooms for this week 
		while (rs.next()) {
         P4RoomGridVO gvo = new P4RoomGridVO();
         gvo.setRoom_id(rs.getString("room_id"));
         gvo.setRoom_name(rs.getString("room_name"));
         gvo.setWeek_id(week_id);       
         
         Calendar calendar = GregorianCalendar.getInstance();
         calendar.setFirstDayOfWeek(GregorianCalendar.MONDAY);
         calendar.setTime(rs.getTimestamp("week_start_date")); 
         
         Date mdate = rs.getTimestamp("week_start_date");
         calendar.get(Calendar.DAY_OF_WEEK);
         Calendar calendar2 = GregorianCalendar.getInstance();
         calendar2.setFirstDayOfWeek(GregorianCalendar.MONDAY);
         calendar2.setTime(calendar.getTime()); 
         calendar2.roll(Calendar.HOUR_OF_DAY, 12);
 
         Date [] weekdays = new Date [5];
         Date [] weekdays2 = new Date [5];
         

         for(int i = 0; i < weekdays.length; i ++){
             weekdays [i] = calendar.getTime();
             weekdays2 [i] = calendar2.getTime();
             //System.out.println("date1 " + dateformat1.format(calendar.getTime()));
             calendar.roll(Calendar.DAY_OF_MONTH, 1);
             calendar2.roll(Calendar.DAY_OF_MONTH, 1);
         }
         
         
         List roomdatas = new ArrayList();

         //Step2. get data for each room
                 //for each weekday, find classroom data 
                 //for each classroom data, find out table info
         for(int i = 0; i < weekdays.length; i ++){
             sql = " select distinct class_id class_id, product_cd, product_desc, table_id, trainee_count, gt_count,v.start_date  from v_p4_class_table v "
                  + " where TO_DATE('" + dateformat.format(weekdays2[i]) +  "', 'mm/dd/yyyy hh:mi:ss am') >= v.start_date " 
                  + " AND TO_DATE('" + dateformat.format(weekdays[i]) +  "', 'mm/dd/yyyy hh:mi:ss am') <= v.end_date "                        
                  + " and room_id = '" + gvo.getRoom_id() + "' "
                  + " order by v.start_date, product_cd, table_id";
             
             if ("10".equals(gvo.getRoom_id())) {
                 //System.out.println(sql + "\n\n");
             }
             rs1 = st1.executeQuery(sql);  
             System.out.println(sql+"sqlllllllll");
             P4ClassRoom roomdata = new P4ClassRoom();
             List p4classes = new ArrayList();
             P4ClassData p4class = new P4ClassData();
             List tables = new ArrayList();   
             P4ClassTable table = new P4ClassTable();            
           /*  RBUClassData rbuclass2 = null;
             List tables2 = new ArrayList();*/
             
            
             
             if(rs1.next()){
                  roomdata.setRoom_id(gvo.getRoom_id());
                  roomdata.setRoom_name(gvo.getRoom_name());
                  //weekdays[i]
                  Calendar ctemp = GregorianCalendar.getInstance();
                  ctemp.setTime(weekdays[i]);
                  roomdata.setWeekday(getWeekDay(i));  
                  roomdata.setDay(weekdays[i]);
                  
                 // RBUClassData rbuclass = new RBUClassData();
                  p4class.setCourseID(rs1.getString("class_id"));
                  p4class.setProductcd(rs1.getString("product_cd"));
                  p4class.setProductdesc(rs1.getString("product_desc"));                                            
                  
                                          
                  
                  table.setTalbe_id(rs1.getString("table_id"));
                  table.setTraineesCnt(rs1.getInt("trainee_count"));
                  table.setGuestCnt(rs1.getInt("gt_count"));   
                  tables.add(table);
             }

             while(rs1.next()){
                 if(rs1.getString("product_cd").equals(p4class.getProductcd())){
                  table = new P4ClassTable();
                  table.setTalbe_id(rs1.getString("table_id"));
                  table.setTraineesCnt(rs1.getInt("trainee_count"));
                  table.setGuestCnt(rs1.getInt("gt_count"));   
                  tables.add(table);    
                 }else{
                   //when new class found - add current class to list, reset it for new class
                   p4class.setTables(tables);
                   p4classes.add(p4class);
                   
                   p4class = new P4ClassData();
                   tables = new ArrayList();
                                         
                   p4class.setCourseID(rs1.getString("class_id"));
                   p4class.setProductcd(rs1.getString("product_cd"));
                   p4class.setProductdesc(rs1.getString("product_desc"));                                            
                 //System.out.println(rs1.getString("product_desc"));
                   table = new P4ClassTable();
                  table.setTalbe_id(rs1.getString("table_id"));
                  table.setTraineesCnt(rs1.getInt("trainee_count"));
                  table.setGuestCnt(rs1.getInt("gt_count"));   
                  tables.add(table);
                 //System.out.println(table2.getTalbe_id());    
                  //   break;
                   
                 }
             }    
               
            p4class.setTables(tables);
            p4classes.add(p4class);
            roomdata.setP4Classes(p4classes);
            System.out.println(roomdata+"ggggggggggggggg");
            
            
           //  roomdata.setRBUClass(rbuclass);                        

            /* 
            while(rs1.next()){
                RBUClassTable table = new RBUClassTable();
                table.setTalbe_id(rs1.getString("table_id"));
                table.setTraineesCnt(rs1.getInt("t_cnt"));
                table.setGuestCnt(rs1.getInt("g_cnt"));   
                tables2.add(table);    
           }  
           
           if(rbuclass2 !=null){
               rbuclass2.setTables(tables2);
               roomdata.setRBUClass2(rbuclass2);
                //System.out.println(tables2.size());   
           }
           */
           
           
          // roomdata.setTables(tables);
            roomdatas.add(roomdata);
         }
         
        
         gvo.setRoomdata(roomdatas);                     
         roomvos.add(gvo);
       
		}

     st.close();
     st1.close();
  
	} catch (Exception e) { 
		e.printStackTrace();
	} finally {
		if ( rs != null) {
			try {
				rs.close();
			} catch ( Exception e2) {
				e2.printStackTrace();
			}
		}
		if ( st != null) {
			try {
				st.close();
			} catch ( Exception e2) {
				e2.printStackTrace();
			}
		}
     if ( rs1 != null) {
			try {
				rs1.close();
			} catch ( Exception e2) {
				e2.printStackTrace();
			}
		}
		if ( st1 != null) {
			try {
				st.close();
			} catch ( Exception e2) {
				e2.printStackTrace();
			}
		}
		if ( conn != null) {
			try {
				conn.close();
			} catch ( Exception e2) {
				e2.printStackTrace();
			}
		}
	}
 return roomvos;
}
	
	private void getWaveWeeks( String sql ) {
		Attendance[] ret = null;
		ResultSet rs = null;
		Statement st = null;
		Connection conn=JdbcConnectionUtil.getJdbcConnection();
	/*
	 * 
Infosys migrated code weblogic to jboss changes start here
	Connection conn = null;
		try {
			Context ctx = new InitialContext();

			Timer timer = new Timer();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  
		
		 Infosys migrated code weblogic to jboss changes end here 
		*/
		try{
		
		    st = conn.createStatement();
			st.setFetchSize(5000);
			ArrayList tempList = new ArrayList();
			
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Attendance curr = new Attendance();
				curr.setEmplid( rs.getString("emplid".toUpperCase()) );
				curr.setStatus( rs.getString("status".toUpperCase()) );
				curr.setCourseDesc( rs.getString("courseDesc".toUpperCase()) );
				java.sql.Date sDate = rs.getDate("startDate".toUpperCase());
				if ( sDate != null ) {
					curr.setStartDate( new Date( sDate.getTime() ));
				}
				tempList.add( curr );
			}
			ret = new Attendance[tempList.size()];
			for ( int j=0; j < tempList.size(); j++ ) {
				ret[j] = (Attendance)tempList.get(j);			
			}

		
		} catch (Exception e) {
			log.error(e,e);
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}

		//return ret;	
	}
	
    
	
	
	/*public P4TrainingWeek getCurrentWeek(){
        P4TrainingWeek week = null;
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select distinct week_id from V_P4_CLASS_TABLE where week_start_date < sysdate and (week_start_date +5)> sysdate");

            while(rs.next()){
                week = new P4TrainingWeek();
                week.setWeek_id(rs.getString("week_id"));
                //week.setWeek_name(rs.getString("week_name"));
                //week.setStart_date(rs.getTimestamp("start_date"));
                //week.setEnd_date(rs.getTimestamp("end_date"));

            }            
            
            
            st.close();
            conn.close();
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}
        return week;
        
    }
*/
   // Infosys migrated code changes
	
	public P4TrainingWeek getCurrentWeek(){
        P4TrainingWeek week = null;
        Session session = HibernateUtils.getHibernateSession();
		try{
			Query q = session.createSQLQuery("select distinct week_id from V_P4_CLASS_TABLE where week_start_date < sysdate and (week_start_date +5)> sysdate");
			List tempList = new ArrayList();
			tempList=q.list();
			Iterator it = tempList.iterator();
			
			while(it.hasNext())
			{
				 week = new P4TrainingWeek();
				 Object[] field = (Object[]) it.next();
				 week.setWeek_id(field[0].toString());	 
			}
		}
		catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			System.out.println();
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			System.out.println("P4TrainingWeek Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return week;
	}

	
	
	
	
	private String getWeekDay(int i){
        switch (i){
           case 0: return P4ClassRoom.MONDAY;
           case 1: return P4ClassRoom.TUESDAY;
           case 2: return P4ClassRoom.WENSEDAY;
           case 3: return P4ClassRoom.THURSDAY;
           case 4: return P4ClassRoom.FRIDAY;
           default: return P4ClassRoom.MONDAY;
        }
    }

    /*public List getRooms(String class_id){
        List rooms = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select distinct m.room_id, room_name,(select count(table_id) from p4_class_classroom_table_map where class_id =" + class_id+ " and room_id = m.room_id) tablecnt "
                        +"from p4_class_classroom_map m, p4_classroom r "
                    + " where r.ROOM_ID = m.ROOM_ID and class_id = " + class_id  + " group by m.room_id, room_name order by m.room_id");
            while(rs.next()){
                P4ClassRoom room = new P4ClassRoom();
                room.setRoom_id(rs.getString("room_id"));
                room.setRoom_name(rs.getString("room_name"));
                room.setAssignedtalbes(rs.getInt("tablecnt"));
                rooms.add(room);               
                
            }            
            
            st.close();
            conn.close();
            
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}
        return rooms;
        
    }
    
    */
    
    
  // Infosys migrated code changes starts   
	public List getRooms(String class_id){
        List rooms = new ArrayList();
        Session session = HibernateUtils.getHibernateSession();
		try{
			Query q = session.createSQLQuery("select distinct m.room_id, room_name,(select count(table_id) from p4_class_classroom_table_map where class_id =" + class_id+ " and room_id = m.room_id) tablecnt "
                    +"from p4_class_classroom_map m, p4_classroom r "
                + " where r.ROOM_ID = m.ROOM_ID and class_id = " + class_id  + " group by m.room_id, room_name order by m.room_id");
			List tempList = new ArrayList();
			tempList=q.list();
			Iterator it = tempList.iterator();
			while(it.hasNext())
			{
				P4ClassRoom room = new P4ClassRoom();
				Object[] field = (Object[]) it.next();
				room.setRoom_id(field[0].toString());
				room.setRoom_name(field[1].toString());
				String s=field[2].toString();
				room.setAssignedtalbes(Integer.parseInt(s));
				rooms.add(room);
			}
		}catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			System.out.println();
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			System.out.println("getUserByNTIdAndDomain Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return rooms;
	}
	
		
   /* public List getTables(String class_id){
        List tables = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select distinct t.table_id , (SELECT COUNT (emplid) FROM V_P4_CLASS_ROSTER_REPORT  WHERE table_id = t.table_id and class_id = t.class_id AND IS_TRAINER = 'N') tnt, "
                            + "(SELECT COUNT (emplid) FROM V_P4_CLASS_ROSTER_REPORT  WHERE table_id = t.table_id  and class_id = t.class_id AND IS_TRAINER = 'Y') gnt from P4_class_classroom_table_map t where t.class_id = " + class_id + " order by t.table_id ");
            
            while(rs.next()){
                
                P4ClassTable table = new P4ClassTable();
                table.setTalbe_id(rs.getString("table_id"));
                table.setTraineesCnt(rs.getInt("tnt"));
                table.setGuestCnt(rs.getInt("gnt"));
                
                tables.add(table);
            }            
            
            st.close();
            conn.close();
            
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}
        return tables;
        
    }
*/

	
	
	public List getTables(String class_id){
        List tables = new ArrayList();
        Session session = HibernateUtils.getHibernateSession();
		try{
			Query q = session.createSQLQuery("select distinct t.table_id , (SELECT COUNT (emplid) FROM V_P4_CLASS_ROSTER_REPORT  WHERE table_id = t.table_id and class_id = t.class_id AND IS_TRAINER = 'N') tnt, "
                    + "(SELECT COUNT (emplid) FROM V_P4_CLASS_ROSTER_REPORT  WHERE table_id = t.table_id  and class_id = t.class_id AND IS_TRAINER = 'Y') gnt from P4_class_classroom_table_map t where t.class_id = " + class_id + " order by t.table_id ");
			List tempList = new ArrayList();
			tempList=q.list();
			Iterator it = tempList.iterator();
			
			while(it.hasNext())
			{
				P4ClassTable table = new P4ClassTable();
				Object[] field = (Object[]) it.next();
				table.setTalbe_id(field[0].toString());
				 BigDecimal b = (BigDecimal) field[1];
				 table.setTraineesCnt(b.intValue());
				 BigDecimal b1 = (BigDecimal) field[2];
				table.setGuestCnt(b1.intValue());
				tables.add(table);
			}}
			catch (HibernateException e) {
				// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
				// "getDistributionListFilters --> HibernateException : ", e);
				System.out.println();
				e.printStackTrace();

				// log.error("HibernateException in getUserByNTIdAndDomain", e);
				System.out.println("getTables Hibernatate Exception");
			} finally {
				HibernateUtils.closeHibernateSession(session);
			}
			return tables;
		}
	
	/*public List getEmpListByTable(String class_id, String table_id, String room_id, String week_id, String wave_id){
        List ts = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();                     
            st = conn.createStatement();	

            if (!Util.isEmpty(table_id)) {            
                rs = st.executeQuery("select EMPLID, FIRST_NAME, LAST_NAME from V_P4_CLASS_ROSTER_REPORT where room_id = " + room_id  + " and wave_id = " + wave_id + " and week_id = " + week_id + " and IS_TRAINER = 'N' and table_id =" 
                            + table_id  + " and class_id = " + class_id + "   ");
            } else {
                rs = st.executeQuery("select EMPLID, FIRST_NAME, LAST_NAME from V_P4_CLASS_ROSTER_REPORT where room_id = " + room_id  + " and wave_id = " + wave_id + " and week_id = " + week_id + " and IS_TRAINER = 'N'  and class_id = " + class_id + "   ");
            }    
            while(rs.next()){
                P4Trainee t = new P4Trainee();
                t.setEmplId(rs.getString("emplid"));
                t.setFirstName(rs.getString("first_name"));
                t.setLastName(rs.getString("LAST_NAME"));
                //t.setRole(rs.getString("FUTURE_ROLE"));
                ts.add(t);
            }            
            st.close();
            conn.close();
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}
        return ts;
        
    }
    
    */

	
	public List getEmpListByTable(String class_id, String table_id, String room_id, String week_id, String wave_id){
        List ts = new ArrayList();
        
        Session session = HibernateUtils.getHibernateSession();
		try{
			Query q=null;
			 if (!Util.isEmpty(table_id)) {  
				 q = session.createSQLQuery("select EMPLID, FIRST_NAME, LAST_NAME from V_P4_CLASS_ROSTER_REPORT where room_id = " + room_id  + " and wave_id = " + wave_id + " and week_id = " + week_id + " and IS_TRAINER = 'N' and table_id =" 
                         + table_id  + " and class_id = " + class_id + "   ");
			 }
	          else {
	        	 q = session.createSQLQuery("select EMPLID, FIRST_NAME, LAST_NAME from V_P4_CLASS_ROSTER_REPORT where room_id = " + room_id  + " and wave_id = " + wave_id + " and week_id = " + week_id + " and IS_TRAINER = 'N'  and class_id = " + class_id + "   ");
	          } 
			
			List tempList = new ArrayList();
			tempList=q.list();
			Iterator it = tempList.iterator();
			
			while(it.hasNext())
			{
				 P4Trainee t = new P4Trainee();
				 Object[] field = (Object[]) it.next();
				 t.setEmplId(field[0].toString());
				 t.setFirstName(field[1].toString());
				 t.setLastName(field[2].toString());
				 ts.add(t);
			}
		}
	catch (HibernateException e) {
		// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
		// "getDistributionListFilters --> HibernateException : ", e);
		System.out.println();
		e.printStackTrace();

		// log.error("HibernateException in getUserByNTIdAndDomain", e);
		System.out.println("getEmpListByTable Hibernatate Exception");
	} finally {
		HibernateUtils.closeHibernateSession(session);
	}
	return ts;
}
	

	/*public List getGuestListByTable(String class_id, String table_id){
        List ts = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select EMPLID, FIRST_NAME, LAST_NAME from V_P4_CLASS_ROSTER_REPORT where  IS_TRAINER = 'Y' and table_id =" 
                            + table_id  + " and class_id = " + class_id + " ");
            
            while(rs.next()){
                P4Trainee t = new P4Trainee();
                t.setEmplId(rs.getString("EMPLID"));
                t.setFirstName(rs.getString("first_name"));
                t.setLastName(rs.getString("LAST_NAME"));
               // t.setRole(rs.getString("FUTURE_ROLE"));
                ts.add(t);
            }            
            st.close();
            conn.close();
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}
        return ts;
        
    }

*/

	
	public List getGuestListByTable(String class_id, String table_id){
        List ts = new ArrayList();
        Session session = HibernateUtils.getHibernateSession();
		try{
			Query q = session.createSQLQuery("select EMPLID, FIRST_NAME, LAST_NAME from V_P4_CLASS_ROSTER_REPORT where  IS_TRAINER = 'Y' and table_id =" 
                    + table_id  + " and class_id = " + class_id + " ");
			List tempList = new ArrayList();
			tempList=q.list();
			Iterator it = tempList.iterator();
			
			while(it.hasNext())
			{
				 P4Trainee t = new P4Trainee();
				 Object[] field = (Object[]) it.next();
				 t.setEmplId(field[0].toString());
				 t.setFirstName(field[1].toString());
				 t.setLastName(field[2].toString());
				 ts.add(t);
			}
		}

	catch (HibernateException e) {
		// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
		// "getDistributionListFilters --> HibernateException : ", e);
		System.out.println();
		e.printStackTrace();

		// log.error("HibernateException in getUserByNTIdAndDomain", e);
		System.out.println("getGuestListByTable Hibernatate Exception");
	} finally {
		HibernateUtils.closeHibernateSession(session);
	}
	return ts;
}
	
	
	/*public void updateTable(Map gMap, Map tMap, String class_id, String assigned_by, String room_id, String table){
        List ts = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();                     
            st = conn.createStatement();	
            
            if (!Util.isEmpty(table)) {
                String sql = "";
                Iterator keyValuePairs1 = tMap.entrySet().iterator();
                for (int i = 0; i < tMap.size(); i++)
                {
                     Map.Entry entry = (Map.Entry) keyValuePairs1.next();
                     String tid = (String)entry.getKey();
                     String table_id =(String) entry.getValue();
                     sql = "UPDATE P4_CLASS_TRAINEE_TABLE_MAP set TABLE_ID = " + table_id +
                            ", ASSIGNED_BY ='" + assigned_by +"' WHERE CLASS_ID = "+ class_id + " AND EMPLID = '" + tid + "'" ;
                     System.out.println(sql);
                     st.executeUpdate(sql);
                }
            }            
            
            Iterator keyValuePairs2 = gMap.entrySet().iterator();
            for (int i = 0; i < gMap.size(); i++)
            {
                 Map.Entry entry = (Map.Entry) keyValuePairs2.next();
                 String gid = (String)entry.getKey();
                 String table_id =(String) entry.getValue();
                 sql = "UPDATE RBU_CLASS_GT_TABLE_MAP set TABLE_ID = " + table_id +
                        ", ASSIGNED_BY ='" + assigned_by +"' WHERE CLASS_ID = "+ class_id + " AND EMPLID = '" + gid + "'" ;
                 //System.out.println(sql);
                 st.executeUpdate(sql);
            }
            
            if(!Util.isEmpty(table) && room_id != null && class_id != null){
                String sql = "UPDATE p4_class_classroom_table_map  set ROOM_ID = "+ room_id + " WHERE TABLE_ID = " 
                       +  table + "AND CLASS_ID =  " + class_id;
                st.executeUpdate(sql);
            } else if (Util.isEmpty(table) && room_id != null && class_id != null) {
                String sql = "UPDATE p4_class_classroom_table_map  set ROOM_ID = "+ room_id + " AND CLASS_ID =  " + class_id;
                st.executeUpdate(sql);
            }
        	st.close();
            conn.close();
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}
        
        
    }

*/


	
	public void updateTable(Map gMap, Map tMap, String class_id, String assigned_by, String room_id, String table){
        List ts = new ArrayList();
        List userList = new ArrayList();
		Session session = HibernateUtils.getHibernateSession();
		
		try{
			if (!Util.isEmpty(table)) {
                String sql = "";
                Iterator keyValuePairs1 = tMap.entrySet().iterator();
                for (int i = 0; i < tMap.size(); i++)
                {
                     Map.Entry entry = (Map.Entry) keyValuePairs1.next();
                     String tid = (String)entry.getKey();
                     String table_id =(String) entry.getValue();
                     sql = "UPDATE P4_CLASS_TRAINEE_TABLE_MAP set TABLE_ID = " + table_id +
                            ", ASSIGNED_BY ='" + assigned_by +"' WHERE CLASS_ID = "+ class_id + " AND EMPLID = '" + tid + "'" ;
                     System.out.println(sql);
                     Query q = session.createSQLQuery(sql);
                }
            }            
            
            Iterator keyValuePairs2 = gMap.entrySet().iterator();
            for (int i = 0; i < gMap.size(); i++)
            {
            	 String sql = "";
                 Map.Entry entry = (Map.Entry) keyValuePairs2.next();
                 String gid = (String)entry.getKey();
                 String table_id =(String) entry.getValue();
                 sql = "UPDATE RBU_CLASS_GT_TABLE_MAP set TABLE_ID = " + table_id +
                        ", ASSIGNED_BY ='" + assigned_by +"' WHERE CLASS_ID = "+ class_id + " AND EMPLID = '" + gid + "'" ;
                 //System.out.println(sql);
                 Query q = session.createSQLQuery(sql);
            }
            
            if(!Util.isEmpty(table) && room_id != null && class_id != null){
                String sql = "UPDATE p4_class_classroom_table_map  set ROOM_ID = "+ room_id + " WHERE TABLE_ID = " 
                       +  table + "AND CLASS_ID =  " + class_id;
                Query q = session.createSQLQuery(sql);
            } else if (Util.isEmpty(table) && room_id != null && class_id != null) {
                String sql = "UPDATE p4_class_classroom_table_map  set ROOM_ID = "+ room_id + " AND CLASS_ID =  " + class_id;
                Query q = session.createSQLQuery(sql);
            }
        	
		}
		catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			System.out.println();
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			System.out.println("updateTable Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}	
				
   /* public List getSceDates(){
        List scedates = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select distinct to_char(exam_taken_date,'MM-DD-YYYY') as date_taken from V_P4_sce_report order by date_taken asc");
            
            while(rs.next()){
                String scedate = rs.getString("date_taken");
                
                scedates.add(scedate);
            }            
            
            
            st.close();
            conn.close();
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}
        return scedates;
        
    }
    */

	public List getSceDates(){
        List scedates = new ArrayList();
        Session session = HibernateUtils.getHibernateSession();
		try{
			Query q = session.createSQLQuery("select distinct to_char(exam_taken_date,'MM-DD-YYYY') as date_taken from V_P4_sce_report order by date_taken asc");
			List tempList = new ArrayList();
			tempList=q.list();
			Iterator it = tempList.iterator();
			
			while(it.hasNext())
			{	
				String field=(String)it.next();
				scedates.add(field);
			}
		}
		catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			System.out.println();
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			System.out.println("getSceDates Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		
		return scedates;
	}
    
    
   /* public List getSceMap(){
        List scenames = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select distinct to_char(exam_taken_date,'MM-DD-YYYY') as date_taken,exam_name from V_P4_sce_report order by date_taken asc");
            
            while(rs.next()){
                Map sce = new HashMap();
                sce.put("exam_name",rs.getString("exam_name"));
                sce.put("date",rs.getString("date_taken"));
                
                scenames.add(sce);
            }            
            
            
            st.close();
            conn.close();
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}
        return scenames;
        
    }
*/

	public List getSceMap(){
        List scenames = new ArrayList();
        Session session = HibernateUtils.getHibernateSession();
		try{
			Query q = session.createSQLQuery("select distinct to_char(exam_taken_date,'MM-DD-YYYY') as date_taken,exam_name from V_P4_sce_report order by date_taken asc");
			List tempList = new ArrayList();
			tempList=q.list();
			Iterator it = tempList.iterator();
			
			while(it.hasNext())
			{
				Map sce = new HashMap();
				
				Object[] field = (Object[]) it.next();
				
				sce.put("exam_name",field[1].toString() );
				sce.put("date",field[0].toString());
				
				 scenames.add(sce);
			}
		}
	catch (HibernateException e) {
		// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
		// "getDistributionListFilters --> HibernateException : ", e);
		System.out.println();
		e.printStackTrace();

		// log.error("HibernateException in getUserByNTIdAndDomain", e);
		System.out.println("getSceMap Hibernatate Exception");
	} finally {
		HibernateUtils.closeHibernateSession(session);
	}
	return scenames;

}
	

	/*public List getSceReport(String date, String name){
        List sce = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        String sql = "";
        if ("all".equals(date) && "all".equals(name)) {
            sql = "select first_name,last_name,test_score,exam_name,to_char(exam_taken_date,'MM-DD-YYYY HH24:MI:SS') as fdate from V_P4_sce_report order by exam_name asc";
        }
        if ("all".equals(date) && !"all".equals(name)) {
            sql = "select first_name,last_name,test_score,exam_name,to_char(exam_taken_date,'MM-DD-YYYY HH24:MI:SS') as fdate from V_P4_sce_report where exam_name = '" + name + "' order by exam_name asc";
        }
        if (!"all".equals(date) && "all".equals(name)) {
            sql = "select first_name,last_name,test_score,exam_name,to_char(exam_taken_date,'MM-DD-YYYY HH24:MI:SS') as fdate from V_P4_sce_report where to_char(exam_taken_date,'MM-DD-YYYY') = '" + date + "' order by exam_name asc";
        }
        if (!"all".equals(date) && !"all".equals(name)) {
            sql = "select first_name,last_name,test_score,exam_name,to_char(exam_taken_date,'MM-DD-YYYY HH24:MI:SS') as fdate from V_P4_sce_report where to_char(exam_taken_date,'MM-DD-YYYY') = '" + date + "' and exam_name ='" + name + "'  order by exam_name asc";
        }

        try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();                     
            st = conn.createStatement();	
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                Map map = new HashMap();
                map.put("first_name",rs.getString("first_name"));
                map.put("last_name",rs.getString("last_name"));
                map.put("test_score",rs.getString("test_score"));
                map.put("exam_name",rs.getString("exam_name"));
                map.put("fdate",rs.getString("fdate"));
                
                sce.add(map);
            }            
            
            
            st.close();
            conn.close();
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}
        return sce;
        
    }
*/

	public List getSceReport(String date, String name){
        List sce = new ArrayList();
        Session session = HibernateUtils.getHibernateSession();
        
        String sql = "";
        if ("all".equals(date) && "all".equals(name)) {
            sql = "select first_name,last_name,test_score,exam_name,to_char(exam_taken_date,'MM-DD-YYYY HH24:MI:SS') as fdate from V_P4_sce_report order by exam_name asc";
        }
        if ("all".equals(date) && !"all".equals(name)) {
            sql = "select first_name,last_name,test_score,exam_name,to_char(exam_taken_date,'MM-DD-YYYY HH24:MI:SS') as fdate from V_P4_sce_report where exam_name = '" + name + "' order by exam_name asc";
        }
        if (!"all".equals(date) && "all".equals(name)) {
            sql = "select first_name,last_name,test_score,exam_name,to_char(exam_taken_date,'MM-DD-YYYY HH24:MI:SS') as fdate from V_P4_sce_report where to_char(exam_taken_date,'MM-DD-YYYY') = '" + date + "' order by exam_name asc";
        }
        if (!"all".equals(date) && !"all".equals(name)) {
            sql = "select first_name,last_name,test_score,exam_name,to_char(exam_taken_date,'MM-DD-YYYY HH24:MI:SS') as fdate from V_P4_sce_report where to_char(exam_taken_date,'MM-DD-YYYY') = '" + date + "' and exam_name ='" + name + "'  order by exam_name asc";
        }

        try {
        	Query q = session.createSQLQuery(sql);
			List tempList = new ArrayList();
			tempList=q.list();
			Iterator it = tempList.iterator();
			while(it.hasNext())
			{
				 Map map = new HashMap();
				 Object[] field = (Object[]) it.next();
	                map.put("first_name",field[0].toString());
	                map.put("last_name",field[1].toString());
	                map.put("test_score",field[2].toString());
	                map.put("exam_name",field[3].toString());
	                map.put("fdate",field[4].toString());  
	                sce.add(map);
	            } 
        }
        catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			System.out.println();
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			System.out.println("getSceReport Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return sce;
	
	}
	
	
/*	public List getSceExamnames(){
        List scenames = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select distinct exam_name from V_P4_sce_report order by exam_name asc");
            
            while(rs.next()){
                String scename = rs.getString("exam_name");
                
                scenames.add(scename);
            }            
            
            
            st.close();
            conn.close();
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}
        return scenames;
        
    }*/
	
	public List getSceExamnames(){
        List scenames = new ArrayList();
       
        Session session = HibernateUtils.getHibernateSession();
		try{
			Query q = session.createSQLQuery("select distinct exam_name from V_P4_sce_report order by exam_name asc");
			List tempList = new ArrayList();
			tempList=q.list();
			Iterator it = tempList.iterator();
			
			while(it.hasNext())
			{
				String field=(String)it.next();
				scenames.add(field);
			}
		}
		catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			System.out.println();
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			System.out.println("getSceExamnames Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		
		return scenames;
	}
		
}
