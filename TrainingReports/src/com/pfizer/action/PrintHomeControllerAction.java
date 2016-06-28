package com.pfizer.action;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.actionForm.GetBoxesForTRMDatesForm;
import com.pfizer.dao.PrintDB;
import com.pfizer.dao.TransactionDB;
import com.pfizer.db.RBUTrainingWeek;
import com.pfizer.hander.RBUSHandler;
import com.pfizer.utils.Global;
import com.pfizer.utils.ReadProperties;
import com.tgix.Utils.MailUtil;
import com.tgix.printing.EmailInfoBean;
import com.tgix.printing.EmployeeListBean;
import com.tgix.printing.InvitationLetterBean;
import com.tgix.printing.LoggerHelper;
import com.tgix.printing.PersonalizedAgendaBean;
import com.tgix.printing.PersonalizedAgendaBeanP4;
import com.tgix.printing.PrintHandlers;
import com.tgix.printing.RBUBoxDataBean;
import com.tgix.printing.TRMOrderDateBean;
import com.tgix.printing.TrainingWeeks;
import com.tgix.printing.VelocityConvertor;

public class PrintHomeControllerAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{

	
		TransactionDB trDb= new TransactionDB();
		protected static final Log log = LogFactory.getLog(PrintHomeControllerAction.class );
		
		private HttpServletRequest request;
		private HttpServletResponse response;
		PrintDB eFTDB=new PrintDB();
		public TreeMap trmClusterMap; //!!!DO NOT  make it null , its being used for Doing the Initial Mapping and then used Further at couple of Places
		
		public Map employeeReportMap=new TreeMap() ;
	    public Map employeeReportMapWithHSL=new TreeMap() ;
	    public Map employeeReportMapSpecial=new TreeMap();
	    public Map invitationMap = new TreeMap();
	    public Map invitationMapWithHSL =  new TreeMap();
	    public Map invitationMapSpecial =  new TreeMap();
	    public Map productMapping;
	    public Map searchTRMEmployeeMap = new TreeMap();
	    public Map searchmployeeForAgendaMap =  new TreeMap();
	    public Map searchTRMEmployeeMapWithHSL = new TreeMap();
	    public TRMOrderDateBean trmOrderDateBean[];
	    public RBUBoxDataBean rbuBoxDataBean[];
	    public RBUBoxDataBean rbuBoxDataBeanTemp[];
	    public Map personalizedAgendaMap = new TreeMap();
	    public List personalizedAgendaList = new ArrayList();
	    private GetBoxesForTRMDatesForm trmForm=new GetBoxesForTRMDatesForm();
	
	public GetBoxesForTRMDatesForm getTrmForm() {
			return trmForm;
		}

		public void setTrmForm(GetBoxesForTRMDatesForm trmForm) {
			this.trmForm = trmForm;
		}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	
	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public HttpSession getSession() {
		return request.getSession();
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request=request;
	}
	
    public String begin()
    {
        try
        {
        return new String("success");
        }
        catch (Exception e) {
        	Global.getError(getRequest(),e);
        	return new String("failure");
        	}

    }


    public String clusterInfo_AJAX()
    {
        HttpServletRequest req = this.getRequest();
        HttpServletResponse res =this.getResponse();
        StringBuffer sbr=null;
        PrintWriter out =null;
        try{
        String cluster=req.getParameter("ClusterType")==null?"":req.getParameter("ClusterType");
         if(cluster!=null && !cluster.equalsIgnoreCase("")){
            try{
                out = res.getWriter();
                res.setContentType("text/xml"); 
                res.setHeader("Cache-Control", "no-cache"); 
                sbr=new StringBuffer();
                sbr.append("<table width=600 border=1>");
                sbr.append("<tr><td>&nbsp;</td><td>Group ID</td><td>Products</td></tr>");
                if(trmClusterMap!=null){
                TreeMap tempMap=new TreeMap();    
                tempMap=(TreeMap)trmClusterMap.get(cluster);
                for(Iterator iter=tempMap.keySet().iterator();iter.hasNext();){
                    String groupID=(String)iter.next();
                    String products=(String)tempMap.get(groupID);
                    sbr.append("<tr>");
                    sbr.append("<td><input type=\"radio\" name=\"group1\" value="+groupID+"></td>");
                    sbr.append("<td>"+groupID+"</td>");
                    sbr.append("<td>"+products+"</td>");
                    sbr.append("</tr>");
                    }//end of the FOR LOOP
                 
                }
                
                sbr.append("</table>");
                
                out.println(sbr.toString());
            }catch(Exception e){
                //LoggerHelper.logSystemDebug("EXCEPTION BLOCK");
            }
         }else{ 
           res.setStatus(HttpServletResponse.SC_NO_CONTENT); 
                }
        sbr=null;   
        out = null;        
        return null;   
        }
        catch (Exception e) {
        	Global.getError(getRequest(),e);
        	return new String("failure");
        	}

        
    }
    

    public String clusterInfo(){
      HttpServletRequest req = this.getRequest();
       HttpServletResponse res =this.getResponse();
       try{
        String cluster=req.getParameter("ClusterType")==null?"":req.getParameter("ClusterType");
         if(cluster!=null && !cluster.equalsIgnoreCase("")){
         if(trmClusterMap!=null) 
         req.setAttribute("tempMap",trmClusterMap.get(cluster));
         req.setAttribute("displayData","true");
         req.setAttribute("ClusterType",cluster);
         
         }//end of the IF Block
           return new String("success");
       }
       catch (Exception e) {
    	   Global.getError(getRequest(),e);
    	   return new String("failure");
    	   }

    }
    

     public String employeesUnderSelection(){
        HttpServletRequest req = this.getRequest();
        HttpServletResponse res =this.getResponse();
        try{
        EmployeeListBean employeeListBean[];
        EmployeeListBean employeeListBeanWithHSL[];
        EmployeeListBean employeeListBeanSpecial[];
        EmployeeListBean thisEmployeeListBean;
        employeeReportMap=new TreeMap();
        employeeReportMapWithHSL=new TreeMap();
        employeeReportMapSpecial = new TreeMap();
        
        String Selected_Date=req.getParameter("Selected_Date")==null?"":req.getParameter("Selected_Date");
        // Changes made by Jeevan for Box
        
        String Selected_Box = req.getParameter("Selected_Box")==null?"":req.getParameter("Selected_Box");
        
        System.out.println("################# Selected date " + Selected_Date + "Selected Box " + Selected_Box);
        StringBuffer sbr=new StringBuffer();
          if(Selected_Date!=null && !Selected_Date.equalsIgnoreCase("") && !Selected_Box.equals("100")){
           sbr.append(" SELECT DISTINCT EMPLID AS EMPLID,  DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
           sbr.append(" ADDRESS1 AS SHIPADD1, ADDRESS2 AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");
           sbr.append(" PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS ");
           sbr.append(" FROM  V_RBU_SHIPMENT_LETTERS fmoh");            
            
            if(!Selected_Date.equalsIgnoreCase("ALL") && !Selected_Box.equalsIgnoreCase("ALL")){
                sbr.append(" where  to_char(fmoh.DATEORDERED,'MM/DD/YY')='"+Selected_Date+"' ");
                sbr.append(" and  fmoh.BOXID ='"+Selected_Box+"' ");
            }   
            else if(!Selected_Date.equalsIgnoreCase("ALL") && Selected_Box.equalsIgnoreCase("ALL")){
                sbr.append(" where  to_char(fmoh.DATEORDERED,'MM/DD/YY')='"+Selected_Date+"' ");
            }
            else if(Selected_Date.equalsIgnoreCase("ALL") && !Selected_Box.equalsIgnoreCase("ALL")){
                sbr.append(" where  fmoh.BOXID ='"+Selected_Box+"' ");
            }
            
            sbr.append(" order by ORDERNUMBER ");
            LoggerHelper.logSystemDebug("PrintController.jpf  Method:employeesUnderSelection -- SQL Query for Selected Group_ID and Date--"+sbr.toString());
            System.out.println("Query  ###########################" + sbr.toString()); 
            employeeListBean=eFTDB.getEmployeeList(sbr.toString());
            System.out.println(employeeListBean.length+"llllll");
            for(int i=0;i<employeeListBean.length;i++){
                if(employeeReportMap.containsKey(employeeListBean[i].getOrderNumber())){
                  thisEmployeeListBean= (EmployeeListBean)employeeReportMap.get(employeeListBean[i].getOrderNumber()); 
                }else{
                  thisEmployeeListBean=employeeListBean[i];
                }
                thisEmployeeListBean.addProducts(employeeListBean[i].getProducts());
                employeeReportMap.put(employeeListBean[i].getOrderNumber(),thisEmployeeListBean);
                }//end of FOR LOOP
            
            
            // Also get the emaployees with HSL Toviaz
            StringBuffer sbr1=new StringBuffer();
           sbr1.append(" SELECT DISTINCT EMPLID AS EMPLID,  DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
           sbr1.append(" ADDRESS1 AS SHIPADD1, ADDRESS2 AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");
           sbr1.append(" PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS ");
           sbr1.append(" FROM  V_RBU_SHIPMENT_LETTERS_HSL fmoh");            
            
            if(!Selected_Date.equalsIgnoreCase("ALL") && !Selected_Box.equalsIgnoreCase("ALL")){
                sbr1.append(" where  to_char(fmoh.DATEORDERED,'MM/DD/YY')='"+Selected_Date+"' ");
                sbr1.append(" and  fmoh.BOXID ='"+Selected_Box+"' ");
            }   
            else if(!Selected_Date.equalsIgnoreCase("ALL") && Selected_Box.equalsIgnoreCase("ALL")){
                sbr1.append(" where  to_char(fmoh.DATEORDERED,'MM/DD/YY')='"+Selected_Date+"' ");
            }
            else if(Selected_Date.equalsIgnoreCase("ALL") && !Selected_Box.equalsIgnoreCase("ALL")){
                sbr1.append(" where  fmoh.BOXID ='"+Selected_Box+"' ");
            }
            
            sbr1.append(" order by ORDERNUMBER ");
            LoggerHelper.logSystemDebug("PrintController.jpf  Method:employeesUnderSelection -- SQL Query for Selected Group_ID and Date--"+sbr.toString());
            System.out.println("Query with HSL ###########################" + sbr1.toString()); 
            employeeListBeanWithHSL=eFTDB.getEmployeeList(sbr1.toString());
            
             /**
             * Here we will construct a Map having TrmOrderNumber as Key and the EmpBean as a Value
             * Since there are more than one TRM Order assigned to one Employee
             */
                for(int i=0;i<employeeListBeanWithHSL.length;i++){
                if(employeeReportMapWithHSL.containsKey(employeeListBeanWithHSL[i].getOrderNumber())){
                  thisEmployeeListBean= (EmployeeListBean)employeeReportMapWithHSL.get(employeeListBeanWithHSL[i].getOrderNumber()); 
                }else{
                  thisEmployeeListBean=employeeListBeanWithHSL[i];
                }
                thisEmployeeListBean.addProducts(employeeListBeanWithHSL[i].getProducts());
                employeeReportMapWithHSL.put(employeeListBeanWithHSL[i].getOrderNumber(),thisEmployeeListBean);
                }//end of FOR LOOP
                
        }
        else if(Selected_Date!=null && !Selected_Date.equalsIgnoreCase("") && Selected_Box.equals("100")){
           // This condition is for special handling
            System.out.println("Special box selected #########################");
            LoggerHelper.logSystemDebug("Special box selected #########################");
            StringBuffer sbr1=new StringBuffer();
           sbr1.append(" SELECT DISTINCT EMPLID AS EMPLID,  DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
           sbr1.append(" ADDRESS1 AS SHIPADD1, ADDRESS2 AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,"); 
           sbr1.append(" PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS ");
           sbr1.append(" FROM  V_RBU_SHIPMENT_LETTERS_SPECIAL fmoh");            
            
            if(!Selected_Date.equalsIgnoreCase("ALL")){
                sbr1.append(" where  to_char(fmoh.DATEORDERED,'MM/DD/YY')='"+Selected_Date+"' ");
            }
            sbr1.append(" order by ORDERNUMBER ");
            LoggerHelper.logSystemDebug("PrintController.jpf  Method:employeesUnderSelection -- SQL Query for Selected Group_ID and Date--"+sbr.toString());
            System.out.println("Query with Special ###########################" + sbr1.toString()); 
            LoggerHelper.logSystemDebug("Query with Special ###########################" + sbr1.toString());
            employeeListBeanSpecial=eFTDB.getEmployeeList(sbr1.toString());
                for(int i=0;i<employeeListBeanSpecial.length;i++){
                if(employeeReportMapSpecial.containsKey(employeeListBeanSpecial[i].getOrderNumber())){
                  thisEmployeeListBean= (EmployeeListBean)employeeReportMapSpecial.get(employeeListBeanSpecial[i].getOrderNumber()); 
                }else{
                  thisEmployeeListBean=employeeListBeanSpecial[i];
                }
                thisEmployeeListBean.addProducts(employeeListBeanSpecial[i].getProducts());
                employeeReportMapSpecial.put(employeeListBeanSpecial[i].getOrderNumber(),thisEmployeeListBean);
                }
        }
        
        else{
                LoggerHelper.logSystemDebug("PrintController.jpf  Method:employeesUnderSelection -- Error in Getting the Paramerter from the URL  ");
            }
            //req.setAttribute("ALL_PROD",ALL_PROD);
         
            req.setAttribute("Selected_Date",Selected_Date);
            req.setAttribute("Selected_Box",Selected_Box);
            // Infosys code changes starts here
            req.setAttribute("employeeReportMap",employeeReportMap);
            req.setAttribute("employeeReportMapSpecial",employeeReportMapSpecial);
            req.setAttribute("employeeReportMapWithHSL",employeeReportMapWithHSL);
            System.out.println(employeeReportMap.size()+"mappp");
            System.out.println(employeeReportMapSpecial.size()+"Special");
            System.out.println(employeeReportMapWithHSL.size()+"HSL");
        return new String("success");
        }
        catch (Exception e) {
        	Global.getError(getRequest(),e);
        	return new String("failure");
        	}

     }

    public String printInvitation()
    {   
        // Changes made for RBU Shipment
        HttpServletRequest req = this.getRequest();
        HttpServletResponse res =this.getResponse();
        try{
        System.out.println("Before get TRM Dates >>>>>>>" +  new Date());
        trmOrderDateBean=eFTDB.getTRMDates();
        System.out.println("Before get getBoxes >>>>>>>" +  new Date());
        rbuBoxDataBean=eFTDB.getBoxes();
        System.out.println("Before method call >>>>>>>" +  new Date());
        List boxesWithDates = getDatesForBoxes(rbuBoxDataBean);
        System.out.println("After method call >>>>>>>" +  new Date() + boxesWithDates.size());
        
        req.setAttribute("boxes", boxesWithDates);
       // req.setAttribute("firstTimeLoad", "firstTimeLoad");
        /* Infosys code changes starts here*/
        req.setAttribute("trmOrderDateBean", trmOrderDateBean);
        req.setAttribute("rbuBoxDataBean", rbuBoxDataBean);
        /* Infosys code changes ends here*/
        return new String("success");
        }
        catch (Exception e) {
        	Global.getError(getRequest(),e);
        	return new String("failure");
        	}

    }
    
    private List getDatesForBoxes(RBUBoxDataBean[] temp){
       List result =  new ArrayList();
       for(int i=0;i<temp.length;i++){
            System.out.println("came into Dates For Boxes");
            // Get the distince dates for each box
            RBUBoxDataBean box = temp[i];
            String boxId = box.getBoxId();
            // Call the method to get the comma seperated list of dates
            String[] dates = null;
            //dates = PrintHandlers.getDatesForBoxes(boxId);
            dates = eFTDB.getDates(boxId);
            box.setDates(dates);
            result.add(box);
       }
        return result;
        
    }

    public String printInvitationForSelectedGroup()
    {
        HttpServletRequest req = this.getRequest();
        HttpServletResponse res =this.getResponse();
        try{
        InvitationLetterBean[] invitationLetterBeanArray;
        InvitationLetterBean[] invitationLetterBeanArrayWithHSL;
        InvitationLetterBean[] invitationLetterBeanArraySpecial;
        InvitationLetterBean thisInvitationLetterBean;
        String path="";
        invitationMap=new TreeMap();
        invitationMapSpecial = new TreeMap();
        invitationMapWithHSL = new TreeMap();
        String Selected_Date=req.getParameter("Selected_Date")==null?"":req.getParameter("Selected_Date");
        // Changes made by Jeevan for Box
        String Selected_Box = req.getParameter("Selected_Box")==null?"":req.getParameter("Selected_Box");
        String ALL_PROD="";
        String Cluster_CD="";
        String canPath;
        String pathBeforeSlash;
        String serverName ;
         String requestURL = this.getRequest().getRequestURL().toString();
        
        //String sServletPath = req.getServletPath();
        //String sRealPath  =(String) req.getSession().getServletContext().getRealPath("/");
        
        try {                                          

        StringBuffer sbr=new StringBuffer();
        StringBuffer sbr1=new StringBuffer();
        StringBuffer sbr2=new StringBuffer();

        // Changes made by jeevan for RBU Box
        if(Selected_Date!=null && !Selected_Date.equalsIgnoreCase("")
        && Selected_Box!=null && !Selected_Box.equalsIgnoreCase("")
        && !Selected_Box.equals("100")){
           //sbr.append(" SELECT DISTINCT TRAINING_TYPE AS TYPE, PDF_START_DATE as STARTDATE, DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
           //sbr.append(" ADDRESS1 AS SHIPADD1, nvl(ADDRESS2,'  ') AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");
           //sbr.append(" TEAM_DESC AS TEAM, PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS ");
           
           sbr.append(" SELECT DISTINCT EMPLID as EMPLID ,DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
           sbr.append(" ADDRESS1 AS SHIPADD1, ADDRESS2 AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");
           sbr.append(" PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS ");
           //sbr.append(" FROM  V_PWRA_LETTERS where DATEORDERED = to_date('" + Selected_Date + "', 'mm/dd/yy')");
           sbr.append(" FROM  V_RBU_SHIPMENT_LETTERS fmoh ");
           
           if(!Selected_Date.equalsIgnoreCase("ALL") && !Selected_Box.equalsIgnoreCase("ALL")){
                sbr.append(" where  to_char(fmoh.DATEORDERED,'MM/DD/YY')='"+Selected_Date+"' ");
                sbr.append(" and  fmoh.BOXID ='"+Selected_Box+"'  order by ORDERNUMBER, emplid,products");
            }   
            else if(!Selected_Date.equalsIgnoreCase("ALL") && Selected_Box.equalsIgnoreCase("ALL")){
                sbr.append(" where  to_char(fmoh.DATEORDERED,'MM/DD/YY')='"+Selected_Date+"'  order by ORDERNUMBER, emplid,products");
            }
            else if(Selected_Date.equalsIgnoreCase("ALL") && !Selected_Box.equalsIgnoreCase("ALL")){
                sbr.append(" where  fmoh.BOXID ='"+Selected_Box+"' order by ORDERNUMBER");
            }
           
            invitationLetterBeanArray=eFTDB.getInvitationInfo(sbr.toString());
           
          //Now we will genrate Invitations for The Employee
            for(int i=0;i<invitationLetterBeanArray.length;i++){
                thisInvitationLetterBean=invitationLetterBeanArray[i];
                    if(i==0){
                    //This we have to do one time to display attributes in the JSP,Nothing to do with any Business Logic
                    ALL_PROD=thisInvitationLetterBean.getProducts();                    
                    } 
                    thisInvitationLetterBean.setServerName(requestURL);                                       
                    
                   
                     VelocityConvertor.generateInvitations(thisInvitationLetterBean);
                   // path=VelocityConvertor.getURLoftheFile(thisInvitationLetterBean.getOrderNumber().trim()).toString();
                    invitationMap.put(thisInvitationLetterBean.getOrderNumber(),path);
                    thisInvitationLetterBean=null; //Lets make it Eligible for Garbage Collection
                }
             // Get the products with HSL
            sbr1.append(" SELECT DISTINCT EMPLID as EMPLID ,DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
            sbr1.append(" ADDRESS1 AS SHIPADD1, ADDRESS2 AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");
            sbr1.append(" PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS ");
           //sbr.append(" FROM  V_PWRA_LETTERS where DATEORDERED = to_date('" + Selected_Date + "', 'mm/dd/yy')");
           sbr1.append(" FROM  V_RBU_SHIPMENT_LETTERS_HSL fmoh ");
           
           if(!Selected_Date.equalsIgnoreCase("ALL") && !Selected_Box.equalsIgnoreCase("ALL")){
                sbr1.append(" where  to_char(fmoh.DATEORDERED,'MM/DD/YY')='"+Selected_Date+"' ");
                sbr1.append(" and  fmoh.BOXID ='"+Selected_Box+"'  order by ORDERNUMBER, emplid,products");
            }   
            else if(!Selected_Date.equalsIgnoreCase("ALL") && Selected_Box.equalsIgnoreCase("ALL")){
                sbr1.append(" where  to_char(fmoh.DATEORDERED,'MM/DD/YY')='"+Selected_Date+"'  order by ORDERNUMBER, emplid,products");
            }
            else if(Selected_Date.equalsIgnoreCase("ALL") && !Selected_Box.equalsIgnoreCase("ALL")){
                sbr1.append(" where  fmoh.BOXID ='"+Selected_Box+"' order by ORDERNUMBER");
            }
           
           
            LoggerHelper.logSystemDebug("The SQL Query here to Create Inviattions are--"+sbr1.toString());
            invitationLetterBeanArrayWithHSL=eFTDB.getInvitationInfo(sbr1.toString());
          //Now we will genrate Invitations for The Employee
            for(int i=0;i<invitationLetterBeanArrayWithHSL.length;i++){
                thisInvitationLetterBean=invitationLetterBeanArrayWithHSL[i];
                    if(i==0){
                    //This we have to do one time to display attributes in the JSP,Nothing to do with any Business Logic
                    ALL_PROD=thisInvitationLetterBean.getProducts();                    
                    }
                    thisInvitationLetterBean.setServerName(requestURL);                                                                               
                    VelocityConvertor.generateInvitations(thisInvitationLetterBean);
                   // path=VelocityConvertor.getURLoftheFile(thisInvitationLetterBean.getOrderNumber().trim()).toString();
                    invitationMapWithHSL.put(thisInvitationLetterBean.getOrderNumber(),path);
                    thisInvitationLetterBean=null; //Lets make it Eligible for Garbage Collection
                }              
        }
        
        else  if(Selected_Date!=null && !Selected_Date.equalsIgnoreCase("")
        && Selected_Box!=null && !Selected_Box.equalsIgnoreCase("")
        && Selected_Box.equals("100")){
            System.out.println("Special Box selected ###########");
            LoggerHelper.logSystemDebug("Special Box selected ###########");
             sbr2.append(" SELECT DISTINCT EMPLID as EMPLID ,DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
             sbr2.append(" ADDRESS1 AS SHIPADD1, ADDRESS2 AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");  
             sbr2.append(" PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS ");
           //sbr.append(" FROM  V_PWRA_LETTERS where DATEORDERED = to_date('" + Selected_Date + "', 'mm/dd/yy')");
           sbr2.append(" FROM  V_RBU_SHIPMENT_LETTERS_SPECIAL fmoh ");
           
              
           if(!Selected_Date.equalsIgnoreCase("ALL")){
                sbr2.append(" where  to_char(fmoh.DATEORDERED,'MM/DD/YY')='"+Selected_Date+"'  order by ORDERNUMBER, emplid,products");
            }
            LoggerHelper.logSystemDebug("The SQL Query here to Create Inviattions are--"+sbr2.toString());
            invitationLetterBeanArraySpecial=eFTDB.getInvitationInfo(sbr2.toString());
          //Now we will genrate Invitations for The Employee
            for(int i=0;i<invitationLetterBeanArraySpecial.length;i++){
                thisInvitationLetterBean=invitationLetterBeanArraySpecial[i];
                    if(i==0){
                    //This we have to do one time to display attributes in the JSP,Nothing to do with any Business Logic
                    ALL_PROD=thisInvitationLetterBean.getProducts();                    
                    }
                    thisInvitationLetterBean.setServerName(requestURL);                                                                               
                    VelocityConvertor.generateInvitations(thisInvitationLetterBean);
                   // path=VelocityConvertor.getURLoftheFile(thisInvitationLetterBean.getOrderNumber().trim()).toString();
                    invitationMapSpecial.put(thisInvitationLetterBean.getOrderNumber(),path);
                    thisInvitationLetterBean=null; //Lets make it Eligible for Garbage Collection
                } 
        }
        
        else{
            LoggerHelper.logSystemDebug("Class:PrintHomeController.jpf Method:printInvitationForSelectedGroup There is no valid data");
        }
        }catch(Exception e)
        {
        }
            req.setAttribute("Cluster_CD",Cluster_CD);
            req.setAttribute("ALL_PROD",ALL_PROD);
            req.setAttribute("Selected_Date",Selected_Date);
            req.setAttribute("Selected_Box",Selected_Box);
            req.setAttribute("invitationMap", invitationMap);
            req.setAttribute("invitationMapWithHSL", invitationMapWithHSL);
            req.setAttribute("invitationMapSpecial", invitationMapSpecial);
        return new String("success");
        }
        catch (Exception e) {
        	Global.getError(getRequest(),e);
        	return new String("failure");
        	}

    }
    

    public String groupForSelectedCluster_AJAX(){   
         HttpServletRequest req = this.getRequest();
         HttpServletResponse res =this.getResponse();
         try{
         String ClusterType=req.getParameter("ClusterType");
         //LoggerHelper.logSystemDebug("THE CLUSTER HERE IS "+);
         PrintWriter out =null;
         //TRMGroupBean thisTRMGroupBean=null;
           try{
                out = res.getWriter();
                res.setContentType("text/xml"); 
                res.setHeader("Cache-Control", "no-cache");
                //Lets Get ALL THE GROUPS COMMA SEPRATED
                TreeMap tempMap=(TreeMap)trmClusterMap.get(ClusterType);
                String groupID="";
                int i=0;
                for(Iterator iter=tempMap.keySet().iterator();iter.hasNext();){
                    if(i++!=0)groupID=groupID+",";
                    groupID=groupID+iter.next();   
                }
                 out.println(groupID);
                LoggerHelper.logSystemDebug("GROUP ID FOR THE SELECTED"+groupID);
            }catch(Exception e){
                LoggerHelper.logSystemDebug("EXCEPTION BLOCK"+e);
            }
        return null;
         }
         catch (Exception e) {
        	 Global.getError(getRequest(),e);
        	 return new String("failure");
        	 }

    }
    

    public String employeesForSearchTRM(){ 
    	
    	 System.out.println("employeesForSearchTRM");
         HttpServletRequest req = this.getRequest();
         HttpServletResponse res =this.getResponse();
         try{
         String SearchType=req.getParameter("SearchType")==null?"":req.getParameter("SearchType");
         String orderNumber="";
         String Selected_Date="";
         // Changes made by Jeevan for RBU
         String Selected_Box="";
         /**
          * Find ou what kind of Search It is 
          */
         if(SearchType.equalsIgnoreCase("loaOrderNum")){
            orderNumber=req.getParameter("orderNumber")==null?"":req.getParameter("orderNumber");
         }else if(SearchType.equalsIgnoreCase("loaSelect")){
         Selected_Date=req.getParameter("Selected_Date")==null?"":req.getParameter("Selected_Date");
         Selected_Box = req.getParameter("Selected_Box")==null?"":req.getParameter("Selected_Box");
         }
         if(orderNumber != null && orderNumber.indexOf("RBU") == -1){
            // IF the user has only entered order number without RBU append RBU for search
            orderNumber = "RBU"+ orderNumber;
         }
         LoggerHelper.logSystemDebug("THE order no is  "+orderNumber);
         System.out.println("Order number after appending >>>>>> " + orderNumber);
         EmployeeListBean employeeListBean[];
         EmployeeListBean employeeListBeanWithHSL[];
         EmployeeListBean thisEmployeeListBean;
         searchTRMEmployeeMap=new TreeMap();
         searchTRMEmployeeMapWithHSL = new TreeMap();
         
         StringBuffer sbr=new StringBuffer();
         StringBuffer sbr1=new StringBuffer();
         //sbr.append("select distinct cluster_cd as clusterCD,products,person_id as emplid,role_cd as roleCD, ");
            //sbr.append("last_name as lastName,first_name as firstName, ");
            //sbr.append("address1 as address1,address2 as address2,city as city,state as state,zip as zip,country_code as countryCode, inv_id as tnCode,trmid as groupID, ");
            //sbr.append("substr(source_order_id,4,20) as trmOrderNumber, ");
            //sbr.append("area_home_phone as areaCode, home_phone homePhone ");
            //sbr.append("from fft_material_order_history fmoh,fft_trm_groups ftg ");
           //sbr.append(" SELECT DISTINCT EMPLID AS EMPLID, TRAINING_TYPE AS TYPE, DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
           //sbr.append(" ADDRESS1 AS SHIPADD1, nvl(ADDRESS2,'  ') AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");
          // sbr.append(" TEAM_DESC AS TEAM, PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS ");
          // sbr.append(" FROM  V_PWRA_LETTERS");           
           
           sbr.append(" SELECT DISTINCT EMPLID AS EMPLID,  DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
           sbr.append(" ADDRESS1 AS SHIPADD1, ADDRESS2 AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");
           sbr.append(" PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS ");
           sbr.append(" FROM  V_RBU_SHIPMENT_LETTERS");           
            
            if(SearchType.equalsIgnoreCase("loaOrderNum")){
                if(!orderNumber.equalsIgnoreCase(""))  sbr.append(" where source_order_id = '"+orderNumber.trim()+"' ");
            }
            //if(SearchType.equalsIgnoreCase("loaSelect")){
              //  if(!Group_ID.equalsIgnoreCase("") && !Group_ID.equalsIgnoreCase("ALL")) sbr.append(" and ftg.group_id="+Group_ID);
                //if(!Selected_Date.equalsIgnoreCase("") && !Selected_Date.equalsIgnoreCase("ALL")) sbr.append(" and to_char(fmoh.DATEORDERED,'MM/DD/YY')='"+Selected_Date+"' ");
            //}
            sbr.append(" order by ORDERNUMBER ");
            LoggerHelper.logSystemDebug("THE Query Here in employeesForSearchTRM_AJAX is "+sbr.toString());
            employeeListBean=eFTDB.getEmployeeList(sbr.toString());
            // Search HSL also for getting the result
          System.out.println(" search ######## " + sbr.toString());
          LoggerHelper.logSystemDebug("THE Query Here in employeesForSearchTRM_AJAX is "+sbr.toString());  
             /**
             * Here we will construct a Map having TrmOrderNumber as Key and the EmpBean as a Value
             * Since there are more than one TRM Order assigned to one Employee
             */
                for(int i=0;i<employeeListBean.length;i++){
                    if(searchTRMEmployeeMap.containsKey(employeeListBean[i].getOrderNumber())){
                        System.out.println("Map has some values thats not correct ##############" + employeeListBean[i].getOrderNumber());
                      thisEmployeeListBean= (EmployeeListBean)searchTRMEmployeeMap.get(employeeListBean[i].getOrderNumber()); 
                    }else{
                      thisEmployeeListBean=employeeListBean[i];
                    }
                    LoggerHelper.logSystemDebug("HERE FOR EMPLOYEE "+thisEmployeeListBean.getEmplID()+"--WITH MATYER"+thisEmployeeListBean.getEmplID());
                    searchTRMEmployeeMap.put(employeeListBean[i].getOrderNumber(),thisEmployeeListBean);
                            //Lets Intialize the Cluster and the Products as they will be same for the Entire Report
                            //--This has nothing to do with the TNC Code adding logic
                       
                }//end of FOR LOOP
                // Also get the HSL results
                sbr1.append(" SELECT DISTINCT EMPLID AS EMPLID,  DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
                sbr1.append(" ADDRESS1 AS SHIPADD1, ADDRESS2 AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");
                sbr1.append(" PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS ");
                sbr1.append(" FROM  V_RBU_SHIPMENT_LETTERS_HSL");           
            if(SearchType.equalsIgnoreCase("loaOrderNum")){
                if(!orderNumber.equalsIgnoreCase(""))  sbr1.append(" where source_order_id = '"+orderNumber.trim()+"' ");
            }
            sbr1.append(" order by ORDERNUMBER ");
            LoggerHelper.logSystemDebug("THE Query Here in employeesForSearchTRM_AJAX is "+sbr1.toString());
            System.out.println("HSL search ######## " + sbr1.toString());
            employeeListBeanWithHSL=eFTDB.getEmployeeList(sbr1.toString());  
            for(int i=0;i<employeeListBeanWithHSL.length;i++){
                    if(searchTRMEmployeeMapWithHSL.containsKey(employeeListBeanWithHSL[i].getOrderNumber())){
                        System.out.println("Map has some values thats not correct ##############" + employeeListBeanWithHSL[i].getOrderNumber());
                      thisEmployeeListBean= (EmployeeListBean)searchTRMEmployeeMapWithHSL.get(employeeListBeanWithHSL[i].getOrderNumber()); 
                    }else{
                      thisEmployeeListBean=employeeListBeanWithHSL[i];
                    }
                    System.out.println("############# Results for HSL" +employeeListBeanWithHSL[i].getOrderNumber());
                    LoggerHelper.logSystemDebug("HERE FOR EMPLOYEE "+thisEmployeeListBean.getEmplID()+"--WITH MATYER"+thisEmployeeListBean.getEmplID());
                    searchTRMEmployeeMapWithHSL.put(employeeListBeanWithHSL[i].getOrderNumber(),thisEmployeeListBean);
                            //Lets Intialize the Cluster and the Products as they will be same for the Entire Report
                            //--This has nothing to do with the TNC Code adding logic
                       
                }    
                req.setAttribute("searchOrderNum",orderNumber);
                req.setAttribute("Selected_Date",Selected_Date);
                req.setAttribute("SearchType",SearchType);
                req.setAttribute("fromSearch","true");
                //Infosys code changes starts here
                req.setAttribute("employeeReportMap", searchTRMEmployeeMap);
                req.setAttribute("employeeResultMapWithHSL", searchTRMEmployeeMapWithHSL);
                //Infosys code changes ends here
        return new String("success");
         }
         catch (Exception e) {
        	 Global.getError(getRequest(),e);
        	 return new String("failure");
        	 }

    }

    public String employeesForSearchAgenda(){   
         HttpServletRequest req = this.getRequest();
         HttpServletResponse res =this.getResponse();
         try{
         String SearchType=req.getParameter("SearchType")==null?"":req.getParameter("SearchType");
         String employeeId="";
         String lastName = "";
         String firstName = "";
         String WeekId="";
         // Changes made by Jeevan for RBU
         String Selected_Box="";
         if(SearchType.equalsIgnoreCase("loaEmployee")){
            employeeId=req.getParameter("orderNumber")==null?"":req.getParameter("orderNumber");
            lastName=req.getParameter("lastName")==null?"":req.getParameter("lastName");
            firstName=req.getParameter("firstName")==null?"":req.getParameter("firstName");
            WeekId=req.getParameter("week")==null?"":req.getParameter("week");
         }
         System.out.println("First Name ####" + firstName + "LastName ##" + lastName + "Week ###" + WeekId + "EmployeeId " + employeeId) ;
         
         String viewName = "";
         if(WeekId.equals("1")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_1";
         }
         if(WeekId.equals("2")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_2";
         }
         if(WeekId.equals("3")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_3";
         }
         if(WeekId.equals("4")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_4";
         }
         if(WeekId.equals("5")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_5";
         }
         if(WeekId.equals("6")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_6";
         }
         if(WeekId.equals("7")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_7";
         }
         if(WeekId.equals("8")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_8";
         }
         if(WeekId.equals("9")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_9";
         }
         viewName = "V_RBU_PERSONALIZED_AGENDA_";
         LoggerHelper.logSystemDebug("THE order no is  "+employeeId);
         EmployeeListBean employeeListBean[];
         EmployeeListBean thisEmployeeListBean;
         searchmployeeForAgendaMap=new TreeMap();
         StringBuffer sbr=new StringBuffer();
         sbr.append(" SELECT FIRSTNAME as FIRSTNAME, LASTNAME as LASTNAME, EMPLID as EMPLID");
         sbr.append(" from "+viewName+""+WeekId+"  ");
           boolean append = false;
          if(firstName.indexOf("'") != -1){
            firstName = replaceSpecialCharacters(firstName);
        }
         if(lastName.indexOf("'") != -1){
            lastName = replaceSpecialCharacters(lastName);
        }  
          StringBuffer wherec = new StringBuffer(" where ");
        if(firstName != null && !firstName.equals("")){
            wherec.append(" upper(firstname) LIKE '" + firstName.toUpperCase() + "%' ");
            wherec.append(" and upper(lastname) LIKE '" + lastName.toUpperCase() + "%' ");
            append = true;
        }
        else if(lastName != null && !lastName.equals("")){
            wherec.append(" upper(firstname) LIKE '" + firstName.toUpperCase() + "%' ");
            wherec.append(" and upper(lastname) LIKE '" + lastName.toUpperCase() + "%' ");
            append = true;
        }
        if(employeeId.trim() !=null && !employeeId.trim().equals("") ){
            if(append){
                wherec.append("and  EMPLID = '" + employeeId.trim() + "' ");
            }
            else{
                wherec.append("EMPLID = '" + employeeId.trim() + "' ");
            }
            append = true;
        }
         if (wherec.toString().equals(" where ")){
            wherec.setLength(0);
        }
            sbr.append(wherec);
//            sbr.append(" order by emplid ");
            LoggerHelper.logSystemDebug("THE Query Here in employeesForSearchTRM_AJAX is "+sbr.toString());
            employeeListBean=eFTDB.getEmployeeListP4(sbr.toString());
          System.out.println(" search ######## " + sbr.toString());
          LoggerHelper.logSystemDebug("THE Query Here in employeesForSearchTRM_AJAX is "+sbr.toString());  
             /**
             * Here we will construct a Map having TrmOrderNumber as Key and the EmpBean as a Value
             * Since there are more than one TRM Order assigned to one Employee
             */
                for(int i=0;i<employeeListBean.length;i++){
                      thisEmployeeListBean=employeeListBean[i];
                        LoggerHelper.logSystemDebug("HERE FOR EMPLOYEE "+thisEmployeeListBean.getEmplID()+"--WITH MATYER"+thisEmployeeListBean.getEmplID());
                        searchmployeeForAgendaMap.put(employeeListBean[i].getEmplID(),thisEmployeeListBean);
                }//end of FOR LOOP
                
            String sql = "SELECT WEEK_ID as WEEK_ID, to_char(START_DATE, 'MM/DD/YYYY') as START_DATE, to_char(END_DATE, 'MM/DD/YYYY')  as END_DATE, WEEK_NAME as WEEK_NAME from RBU_TRAINING_WEEKS order by week_id asc";
            TrainingWeeks[] trainingWeekAray;
            trainingWeekAray = eFTDB.getTrainingWeeks(sql);
            if(getSession().getAttribute("trainingWeek") == null){
                getSession().setAttribute("trainingWeek", trainingWeekAray);
            }
            req.setAttribute("searchEmployeeNum",employeeId);
            req.setAttribute("lastName",lastName);
            req.setAttribute("firstName",firstName);
            req.setAttribute("week",WeekId);
            req.setAttribute("SearchType",SearchType);
            req.setAttribute("fromSearch","true");
                
        return new String("success");
         }
         catch (Exception e) {
        	 Global.getError(getRequest(),e);
        	 return new String("failure");
        	 }

    }
    
    public String employeesForSearchAgendaP4(){   
         HttpServletRequest req = this.getRequest();
         HttpServletResponse res =this.getResponse();
         try{
         String SearchType=req.getParameter("SearchType")==null?"":req.getParameter("SearchType");
         String employeeId="";
         String lastName = "";
         String firstName = "";
         String WeekId="";
         // Changes made by Jeevan for RBU
         String Selected_Box="";
         if(SearchType.equalsIgnoreCase("loaEmployee")){
            employeeId=req.getParameter("orderNumber")==null?"":req.getParameter("orderNumber");
            lastName=req.getParameter("lastName")==null?"":req.getParameter("lastName");
            firstName=req.getParameter("firstName")==null?"":req.getParameter("firstName");
            WeekId=req.getParameter("week")==null?"":req.getParameter("week");
         }
         System.out.println("First Name ####" + firstName + "LastName ##" + lastName + "Week ###" + WeekId + "EmployeeId " + employeeId) ;
         
         String viewName = "";
         if(WeekId.equals("wave1")){
            viewName = "V_P4_PERSONALIZED_AGENDA_1";
         }
         if(WeekId.equals("wave2")){
            viewName = "V_P4_PERSONALIZED_AGENDA_2";
         }
         if(WeekId.equals("wave3")){
            viewName = "V_P4_PERSONALIZED_AGENDA_3";
         }

         LoggerHelper.logSystemDebug("THE order no is  "+employeeId);
         EmployeeListBean employeeListBean[];
         EmployeeListBean thisEmployeeListBean;
         searchmployeeForAgendaMap=new TreeMap();
         StringBuffer sbr=new StringBuffer();
         sbr.append(" SELECT FIRSTNAME as FIRSTNAME, LASTNAME as LASTNAME, EMPLID as EMPLID");
         sbr.append(" from "+viewName + " ");
           boolean append = false;
          if(firstName.indexOf("'") != -1){
            firstName = replaceSpecialCharacters(firstName);
        }
         if(lastName.indexOf("'") != -1){
            lastName = replaceSpecialCharacters(lastName);
        }  
          StringBuffer wherec = new StringBuffer(" where ");
        if(firstName != null && !firstName.equals("")){
            wherec.append(" upper(firstname) LIKE '" + firstName.toUpperCase() + "%' ");
            wherec.append(" and upper(lastname) LIKE '" + lastName.toUpperCase() + "%' ");
            append = true;
        }
        else if(lastName != null && !lastName.equals("")){
            wherec.append(" upper(firstname) LIKE '" + firstName.toUpperCase() + "%' ");
            wherec.append(" and upper(lastname) LIKE '" + lastName.toUpperCase() + "%' ");
            append = true;
        }
        if(employeeId.trim() !=null && !employeeId.trim().equals("") ){
            if(append){
                wherec.append("and  EMPLID = '" + employeeId.trim() + "' ");
            }
            else{
                wherec.append("EMPLID = '" + employeeId.trim() + "' ");
            }
            append = true;
        }
         if (wherec.toString().equals(" where ")){
            wherec.setLength(0);
        } 
        
            sbr.append(wherec);
//            sbr.append(" order by emplid ");
            LoggerHelper.logSystemDebug("THE Query Here in employeesForSearchTRM_AJAX is "+sbr.toString());
          System.out.println(" search ######## " + sbr.toString());
          // Infosys Code changes starts here
            employeeListBean=eFTDB.getEmployeeListP4(sbr.toString());
            // Infosys Code changes ends here
          LoggerHelper.logSystemDebug("THE Query Here in employeesForSearchTRM_AJAX is "+sbr.toString());  
             /**
             * Here we will construct a Map having TrmOrderNumber as Key and the EmpBean as a Value
             * Since there are more than one TRM Order assigned to one Employee
             */
                for(int i=0;i<employeeListBean.length;i++){
                      thisEmployeeListBean=employeeListBean[i];
                        LoggerHelper.logSystemDebug("HERE FOR EMPLOYEE "+thisEmployeeListBean.getEmplID()+"--WITH MATYER"+thisEmployeeListBean.getEmplID());
                        searchmployeeForAgendaMap.put(employeeListBean[i].getEmplID(),thisEmployeeListBean);
                }//end of FOR LOOP
                
             String sql = "select distinct ('wave' || wave_id) as week_id, ('Wave ' || wave_id) as week_name  from v_p4_class_roster_report order by week_id asc";
            TrainingWeeks[] trainingWeekAray;
            trainingWeekAray = eFTDB.getTrainingWeeks(sql);
            if(getSession().getAttribute("trainingWeek") == null){
                getSession().setAttribute("trainingWeek", trainingWeekAray);
            }
            req.setAttribute("searchEmployeeNum",employeeId);
            req.setAttribute("lastName",lastName);
            req.setAttribute("firstName",firstName);
            req.setAttribute("week",WeekId);
            req.setAttribute("SearchType",SearchType);
            req.setAttribute("fromSearch","true");
                
        return new String("success");
         }
         catch (Exception e) {
        	 Global.getError(getRequest(),e);
        	 return new String("failure");
        	 }

    }
     private String replaceSpecialCharacters(String name){
        
       try{
    	String replaceName = "";
        if(name.indexOf("'") != -1){
            replaceName = name.replaceAll("'", "''");
        }
        return replaceName;
       }
       catch (Exception e) {
    	   Global.getError(getRequest(),e);
    	   return new String("failure");
    	   }

    }
    
    public String printSearchTRMOrder()
    {
    	 try{
        //We have all the info already populated in  searchTRMEmployeeMap, We will create 
         invitationMap=new TreeMap();
         invitationMapWithHSL = new TreeMap();
         String path="";
         InvitationLetterBean[] invitationLetterBeanArray;
         InvitationLetterBean[] invitationLetterBeanArrayWithHSL;
         InvitationLetterBean thisInvitationLetterBean;
         StringBuffer sbr=new StringBuffer();
         StringBuffer sbr1=new StringBuffer();
         // if(searchTRMEmployeeMap!=null && !searchTRMEmployeeMap.isEmpty()
         // || searchTRMEmployeeMapWithHSL!=null && !searchTRMEmployeeMapWithHSL.isEmpty()){
            String getallOrderNumbeforSQL="'"; 
    
                getallOrderNumbeforSQL = getallOrderNumbeforSQL + this.getRequest().getParameter("orderNumber") + "'";
            LoggerHelper.logSystemDebug("Class:PrintHomeController.jpf Method:printSearchTRMOrder Order no" + getallOrderNumbeforSQL);
             System.out.println("Order number after appending >>>>>> " + getallOrderNumbeforSQL);
            
            int commaCount=0;
           /* for(Iterator iter=searchTRMEmployeeMap.keySet().iterator();iter.hasNext();){
                if(commaCount==0)getallOrderNumbeforSQL="";
                else
                getallOrderNumbeforSQL=getallOrderNumbeforSQL+",";
                getallOrderNumbeforSQL=getallOrderNumbeforSQL+"'"+iter.next()+"'";
                commaCount++;
            }*/

            // Changes made for RBBU by Jeevan
           //sbr.append(" SELECT DISTINCT EMPLID AS EMPLID,FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
           //sbr.append(" ADDRESS1 AS SHIPADD1,nvl(ADDRESS2,'  ') AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");
           //sbr.append(" PRODUCTS AS PRODUCTS ");
           //sbr.append(" FROM  V_FFT_LETTER");
           /*sbr.append(" SELECT DISTINCT TRAINING_TYPE AS TYPE, PDF_START_DATE as STARTDATE, DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
           sbr.append(" ADDRESS1 AS SHIPADD1, nvl(ADDRESS2,'  ') AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");
           sbr.append(" TEAM_DESC AS TEAM, PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS ");
           sbr.append(" FROM  V_PWRA_LETTERS");           
           sbr.append(" WHERE SOURCE_ORDER_ID in ("+getallOrderNumbeforSQL+") ");
           sbr.append(" ORDER BY ORDERNUMBER ");*/
           sbr.append(" SELECT DISTINCT  EMPLID as EMPLID, DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
           sbr.append(" ADDRESS1 AS SHIPADD1, ADDRESS2 AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");
           sbr.append(" PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS ");
           sbr.append(" FROM  V_RBU_SHIPMENT_LETTERS");           
           sbr.append(" WHERE SOURCE_ORDER_ID in ("+getallOrderNumbeforSQL+") ");
           sbr.append(" ORDER BY ORDERNUMBER ");
           LoggerHelper.logSystemDebug("The SQL Query here in --printSearchTRMOrder--  to Create Inviattions are--"+sbr.toString());
           System.out.println("Query for print >>>>>>>>>>" + sbr.toString());
           invitationLetterBeanArray=eFTDB.getInvitationInfo(sbr.toString());
           
          //Now we will genrate Invitations for The Employee
            for(int i=0;i<invitationLetterBeanArray.length;i++){
                thisInvitationLetterBean=invitationLetterBeanArray[i];
                    thisInvitationLetterBean.setServerName(this.getRequest().getRequestURL().toString());
                    VelocityConvertor.generateInvitations(thisInvitationLetterBean);
                    //path=VelocityConvertor.getURLoftheFile(thisInvitationLetterBean.getOrderNumber().trim()).toString();
                    invitationMap.put(thisInvitationLetterBean.getOrderNumber(),path);
                    thisInvitationLetterBean=null; //Lets make it Eligible for Garbage Collection
                }
            // Also get the HSL results
           sbr1.append(" SELECT DISTINCT  EMPLID as EMPLID, DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
           sbr1.append(" ADDRESS1 AS SHIPADD1, ADDRESS2 AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");
           sbr1.append(" PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS ");
           sbr1.append(" FROM  V_RBU_SHIPMENT_LETTERS_HSL");           
           sbr1.append(" WHERE SOURCE_ORDER_ID in ("+getallOrderNumbeforSQL+") ");
           sbr1.append(" ORDER BY ORDERNUMBER "); 
           invitationLetterBeanArrayWithHSL=eFTDB.getInvitationInfo(sbr1.toString());
           if(invitationLetterBeanArrayWithHSL != null){
            for(int i=0;i<invitationLetterBeanArrayWithHSL.length;i++){
                thisInvitationLetterBean=invitationLetterBeanArrayWithHSL[i];
                    thisInvitationLetterBean.setServerName(this.getRequest().getRequestURL().toString());
                    VelocityConvertor.generateInvitations(thisInvitationLetterBean);
                    //path=VelocityConvertor.getURLoftheFile(thisInvitationLetterBean.getOrderNumber().trim()).toString();
                    invitationMapWithHSL.put(thisInvitationLetterBean.getOrderNumber(),path);
                    thisInvitationLetterBean=null; //Lets make it Eligible for Garbage Collection
                }
          }   
                          
       // }else{
       //     LoggerHelper.logSystemDebug("Class:PrintHomeController.jpf Method:printSearchTRMOrder There is no valid data");
      //  }
         HttpServletRequest req = this.getRequest();
        req.setAttribute("invitationMap", invitationMap); 
        req.setAttribute("invitationMapWithHSL", invitationMapWithHSL); 
        return new String("success");
    	 }
    	 catch (Exception e) {
    		 Global.getError(getRequest(),e);
    		 return new String("failure");
    		 }

    }

    public String printSearchEmployeeAgenda()
    {
    	try{
        //We have all the info already populated in  searchTRMEmployeeMap, We will create 
         HttpServletRequest req = this.getRequest();
        String employeeNumber="";
         String lastName = "";
         String firstName = "";
         String WeekId="";
         personalizedAgendaList = new ArrayList();
        employeeNumber=req.getParameter("orderNumber")==null?"":req.getParameter("orderNumber");
        lastName=req.getParameter("lastName")==null?"":req.getParameter("lastName");
        firstName=req.getParameter("firstName")==null?"":req.getParameter("firstName");
        WeekId=req.getParameter("week")==null?"":req.getParameter("week");
         System.out.println("First Name ####" + firstName + "LastName ##" + lastName + "Week ###" + WeekId + "EmployeeId " + employeeNumber) ;
         
         String viewName = "";
         if(WeekId.equals("1")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_1";
         }
         if(WeekId.equals("2")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_2";
         }
         if(WeekId.equals("3")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_3";
         }
         if(WeekId.equals("4")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_4";
         }
         if(WeekId.equals("5")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_5";
         }
         if(WeekId.equals("6")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_6";
         } 
         if(WeekId.equals("7")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_7";
         }
         if(WeekId.equals("8")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_8";
         }
         if(WeekId.equals("9")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_9";
         } 
         viewName = "V_RBU_PERSONALIZED_AGENDA_";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
         personalizedAgendaMap=new TreeMap();
         Map processemployeeIds = new TreeMap();
         String path="";
         PersonalizedAgendaBean[] personalizedAgendaBeanArray;
         PersonalizedAgendaBean thisPersonalizedAgendaBean;
         String startDate = "";
         String endDate = "";
         // GEt the week start and end dates
         RBUSHandler handler = new RBUSHandler();
         List weeks = handler.getClassWeeks();
      /*  TrainingWeeks[] trainingWeekAray;
        String sql = "SELECT WEEK_ID as WEEK_ID, to_char(START_DATE, 'MM/DD/YYYY') as START_DATE, to_char(END_DATE, 'MM/DD/YYYY')  as END_DATE, WEEK_NAME as WEEK_NAME from RBU_TRAINING_WEEKS order by week_id asc";
        trainingWeekAray = eFTDB.getTrainingWeeks(sql);
        Date today = new Date();
        try{
         for(int j=0;j<trainingWeekAray.length;j++){ 
             //       if(trainingWeek[j].getStart_date().equals(newDate)){
                Date beginDate = dateFormat1.parse(trainingWeekAray[j].getStart_date());
                        Date finishDate = dateFormat1.parse(trainingWeekAray[j].getEnd_date());
                        if(today.after(beginDate) && today.before(finishDate)){
                            startDate = trainingWeekAray[j].getStart_date();
                            endDate = trainingWeekAray[j].getEnd_date();
                        }
         }  
         
        }
        catch(Exception ex){
        } 
        */            
          RBUTrainingWeek  week = new RBUTrainingWeek();
         week = PrintHandlers.getClassDatesForWeek(WeekId);
         String weekName = "";
         if(week != null){
            startDate = dateFormat.format(week.getStart_date());
            endDate = dateFormat.format(week.getEnd_date());
            weekName = week.getWeek_name();
         }
         StringBuffer sbr=new StringBuffer();
         sbr.append(" SELECT FIRSTNAME as FIRSTNAME, LASTNAME as LASTNAME, EMPLID as EMPLID,MONDAYAMPRODUCT as MONDAYAMPRODUCT , MONDAYAMTABLE as MONDAYAMTABLE,");
         sbr.append(" MONDAYAMROOM as MONDAYAMROOM, MONDAYAMTRAINER as MONDAYAMTRAINER,MONDAYPMPRODUCT as MONDAYPMPRODUCT,MONDAYPMTABLE as MONDAYPMTABLE,MONDAYPMROOM as MONDAYPMROOM,");
         sbr.append(" MONDAYPMTRAINER as MONDAYPMTRAINER,TUESDAYAMPRODUCT as TUESDAYAMPRODUCT,TUESDAYAMTABLE as TUESDAYAMTABLE,TUESDAYAMROOM as TUESDAYAMROOM,TUESDAYAMTRAINER as TUESDAYAMTRAINER, ");
         sbr.append(" TUESDAYPMPRODUCT as TUESDAYPMPRODUCT,TUESDAYPMTABLE as TUESDAYPMTABLE,TUESDAYPMROOM as TUESDAYPMROOM,TUESDAYPMTRAINER as TUESDAYPMTRAINER, WEDNESDAYAMPRODUCT as WEDNESDAYAMPRODUCT,");           
         sbr.append(" WEDNESDAYAMTABLE as WEDNESDAYAMTABLE,WEDNESDAYAMROOM as WEDNESDAYAMROOM,WEDNESDAYAMTRAINER as WEDNESDAYAMTRAINER,WEDNESDAYPMPRODUCT as WEDNESDAYPMPRODUCT,WEDNESDAYPMTABLE as WEDNESDAYPMTABLE,");
         sbr.append(" WEDNESDAYPMROOM as WEDNESDAYPMROOM,WEDNESDAYPMTRAINER as WEDNESDAYPMTRAINER,THURSDAYAMPRODUCT as THURSDAYAMPRODUCT,THURSDAYAMTABLE as THURSDAYAMTABLE,THURSDAYAMROOM as THURSDAYAMROOM,");
         sbr.append(" THURSDAYAMTRAINER as THURSDAYAMTRAINER,THURSDAYPMPRODUCT as THURSDAYPMPRODUCT,THURSDAYPMTABLE as THURSDAYPMTABLE,THURSDAYPMROOM as THURSDAYPMROOM,THURSDAYPMTRAINER as THURSDAYPMTRAINER,");
         sbr.append(" FRIDAYAMPRODUCT as FRIDAYAMPRODUCT,FRIDAYAMTABLE as FRIDAYAMTABLE,FRIDAYAMROOM as FRIDAYAMROOM,FRIDAYAMTRAINER as FRIDAYAMTRAINER,FRIDAYPMPRODUCT as FRIDAYPMPRODUCT,");
         sbr.append(" FRIDAYPMTABLE as FRIDAYPMTABLE,FRIDAYPMROOM as FRIDAYPMROOM,FRIDAYPMTRAINER as FRIDAYPMTRAINER,MONDAYAMSTARTTIME as MONDAYAMSTARTTIME,MONDAYAMENDTIME as MONDAYAMENDTIME,");
         sbr.append(" MONDAYPMSTARTTIME as MONDAYPMSTARTTIME,MONDAYPMENDTIME as MONDAYPMENDTIME,TUESDAYAMSTARTTIME as TUESDAYAMSTARTTIME,TUESDAYAMENDTIME as TUESDAYAMENDTIME, TUESDAYPMSTARTTIME as TUESDAYPMSTARTTIME,");
         sbr.append(" TUESDAYPMENDTIME as TUESDAYPMENDTIME,WEDNESDAYAMSTARTTIME as WEDNESDAYAMSTARTTIME,WEDNESDAYAMENDTIME as WEDNESDAYAMENDTIME, WEDNESDAYPMSTARTTIME as WEDNESDAYPMSTARTTIME,WEDNESDAYPMENDTIME as WEDNESDAYPMENDTIME,");
         sbr.append(" THURSDAYAMSTARTTIME as THURSDAYAMSTARTTIME,THURSDAYAMENDTIME as THURSDAYAMENDTIME, THURSDAYPMSTARTTIME as THURSDAYPMSTARTTIME,THURSDAYPMENDTIME as THURSDAYPMENDTIME,FRIDAYAMSTARTTIME as FRIDAYAMSTARTTIME,");
         sbr.append(" FRIDAYAMENDTIME as FRIDAYAMENDTIME,FRIDAYPMSTARTTIME as FRIDAYPMSTARTTIME, FRIDAYPMENDTIME as FRIDAYPMENDTIME");
         sbr.append(" from "+viewName+""+WeekId+" "); 
        
        String output = ""; 
         if(employeeNumber != null && !employeeNumber.equals("") && employeeNumber.length() > 0){
             StringTokenizer token = new StringTokenizer(employeeNumber, ",");
            
            while(token.hasMoreTokens()){
                 output =output +  "'" + token.nextToken() + "'";
                output = output+ ",";
            }
            output = output.substring(0, output.length() - 1);
         }
         
         boolean append = false;
          if(firstName.indexOf("'") != -1){
            firstName = replaceSpecialCharacters(firstName);
        }
         if(lastName.indexOf("'") != -1){
            lastName = replaceSpecialCharacters(lastName);
        }  
          StringBuffer wherec = new StringBuffer(" where ");
        if(firstName != null && !firstName.equals("")){
            wherec.append(" upper(firstname) LIKE '" + firstName.toUpperCase() + "%' ");
            wherec.append(" and upper(lastname) LIKE '" + lastName.toUpperCase() + "%' ");
            append = true;
        }
        else if(lastName != null && !lastName.equals("")){
            wherec.append(" upper(firstname) LIKE '" + firstName.toUpperCase() + "%' ");
            wherec.append(" and upper(lastname) LIKE '" + lastName.toUpperCase() + "%' ");
            append = true;
        }
        if(employeeNumber.trim() !=null && !employeeNumber.trim().equals("") && employeeNumber.trim().length() > 0 ){
            if(append){
             //  wherec.append("and  EMPLID = '" + employeeNumber.trim() + "' ");
               wherec.append("and  EMPLID in (" + output.trim() + ")");
            }
            else{
                //wherec.append("EMPLID = '" + employeeNumber.trim() + "' ");
                wherec.append("EMPLID in (" + output.trim() + ") ");
            }
            append = true;
        }
         if (wherec.toString().equals(" where ")){
            wherec.setLength(0);
        }
            sbr.append(wherec);
           LoggerHelper.logSystemDebug("The SQL Query here in --printSearchTRMOrder--  to Create Inviattions are--"+sbr.toString());
           System.out.println("Query for print >>>>>>>>>>" + sbr.toString());
           personalizedAgendaBeanArray=eFTDB.getPersonlizedAgendLetters(sbr.toString());
            if(personalizedAgendaBeanArray != null){
            for(int i=0;i<personalizedAgendaBeanArray.length;i++){
                thisPersonalizedAgendaBean=personalizedAgendaBeanArray[i];
                    thisPersonalizedAgendaBean.setServerName(this.getRequest().getRequestURL().toString());
                    thisPersonalizedAgendaBean.setWeekStartDate(startDate);
                    thisPersonalizedAgendaBean.setWeekEndDate(endDate);
                    VelocityConvertor.generatePersonalizedAgenda(thisPersonalizedAgendaBean);
                    //personalizedAgendaMap.put(thisPersonalizedAgendaBean.getEmplId(),path);
                    personalizedAgendaList.add(thisPersonalizedAgendaBean.getEmplId());
                    thisPersonalizedAgendaBean=null; //Lets make it Eligible for Garbage Collection
                }
          } 
        //req.setAttribute("personalizedAgendaMap", personalizedAgendaMap);
        req.setAttribute("personalizedAgendaList", personalizedAgendaList);  
        return new String("success");
    	}
    	catch (Exception e) {
    		Global.getError(getRequest(),e);
    		return new String("failure");
    		}

    }


    public String printSearchEmployeeAgendaP4()
    {
    	try{
        //We have all the info already populated in  searchTRMEmployeeMap, We will create 
         HttpServletRequest req = this.getRequest();
        String employeeNumber="";
         String lastName = "";
         String firstName = "";
         String WeekId="";
         personalizedAgendaList = new ArrayList();
        employeeNumber=req.getParameter("orderNumber")==null?"":req.getParameter("orderNumber");
        lastName=req.getParameter("lastName")==null?"":req.getParameter("lastName");
        firstName=req.getParameter("firstName")==null?"":req.getParameter("firstName");
        WeekId=req.getParameter("week")==null?"":req.getParameter("week");
         System.out.println("First Name ####" + firstName + "LastName ##" + lastName + "Week ###" + WeekId + "EmployeeId " + employeeNumber) ;
         
         String viewName = "";
         if(WeekId.equals("wave1")){
            viewName = "V_P4_PERSONALIZED_AGENDA_1";
         }
         if(WeekId.equals("wave2")){
            viewName = "V_P4_PERSONALIZED_AGENDA_2";
         }
         if(WeekId.equals("wave3")){
            viewName = "V_P4_PERSONALIZED_AGENDA_3";
         }
         
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
         personalizedAgendaMap=new TreeMap();
         Map processemployeeIds = new TreeMap();
         String path="";
         PersonalizedAgendaBeanP4[] personalizedAgendaBeanArray;
         PersonalizedAgendaBeanP4 thisPersonalizedAgendaBean;
         String startDate = "";
         String endDate = "";
         // GEt the week start and end dates
         RBUSHandler handler = new RBUSHandler();
         List weeks = handler.getClassWeeks();
      /*  TrainingWeeks[] trainingWeekAray;
        String sql = "SELECT WEEK_ID as WEEK_ID, to_char(START_DATE, 'MM/DD/YYYY') as START_DATE, to_char(END_DATE, 'MM/DD/YYYY')  as END_DATE, WEEK_NAME as WEEK_NAME from RBU_TRAINING_WEEKS order by week_id asc";
        trainingWeekAray = eFTDB.getTrainingWeeks(sql);
        Date today = new Date();
        try{
         for(int j=0;j<trainingWeekAray.length;j++){ 
             //       if(trainingWeek[j].getStart_date().equals(newDate)){
                Date beginDate = dateFormat1.parse(trainingWeekAray[j].getStart_date());
                        Date finishDate = dateFormat1.parse(trainingWeekAray[j].getEnd_date());
                        if(today.after(beginDate) && today.before(finishDate)){
                            startDate = trainingWeekAray[j].getStart_date();
                            endDate = trainingWeekAray[j].getEnd_date();
                        }
         }  
         
        }
        catch(Exception ex){
        } 
        */            
  //        RBUTrainingWeek  week = new RBUTrainingWeek();
    //     week = PrintHandlers.getClassDatesForWeekP4(WeekId);
      //   String weekName = "";
        // if(week != null){
          //  startDate = dateFormat.format(week.getStart_date());
            //endDate = dateFormat.format(week.getEnd_date());
            //weekName = week.getWeek_name();
         //}
         StringBuffer sbr=new StringBuffer();
         sbr.append(" SELECT * ");
         sbr.append(" from "+ viewName +" ");
         
                 
        String output = ""; 
         if(employeeNumber != null && !employeeNumber.equals("") && employeeNumber.length() > 0){
             StringTokenizer token = new StringTokenizer(employeeNumber, ",");
            
            while(token.hasMoreTokens()){
                 output =output +  "'" + token.nextToken() + "'";
                output = output+ ",";
            }
            output = output.substring(0, output.length() - 1);
         }
         
         boolean append = false;
          if(firstName.indexOf("'") != -1){
            firstName = replaceSpecialCharacters(firstName);
        }
         if(lastName.indexOf("'") != -1){
            lastName = replaceSpecialCharacters(lastName);
        }  
          StringBuffer wherec = new StringBuffer(" where ");
        if(firstName != null && !firstName.equals("")){
            wherec.append(" upper(firstname) LIKE '" + firstName.toUpperCase() + "%' ");
            wherec.append(" and upper(lastname) LIKE '" + lastName.toUpperCase() + "%' ");
            append = true;
        }
        else if(lastName != null && !lastName.equals("")){
            wherec.append(" upper(firstname) LIKE '" + firstName.toUpperCase() + "%' ");
            wherec.append(" and upper(lastname) LIKE '" + lastName.toUpperCase() + "%' ");
            append = true;
        }
        if(employeeNumber.trim() !=null && !employeeNumber.trim().equals("") && employeeNumber.trim().length() > 0 ){
            if(append){
             //  wherec.append("and  EMPLID = '" + employeeNumber.trim() + "' ");
               wherec.append("and  EMPLID in (" + output.trim() + ")");
            }
            else{
                //wherec.append("EMPLID = '" + employeeNumber.trim() + "' ");
                wherec.append("EMPLID in (" + output.trim() + ") ");
            }
            append = true;
        }
         if (wherec.toString().equals(" where ")){
            wherec.setLength(0);
        }
            sbr.append(wherec);
           LoggerHelper.logSystemDebug("The SQL Query here in --printSearchTRMOrder--  to Create Inviattions are--"+sbr.toString());
           System.out.println("Query for print >>>>>>>>>>" + sbr.toString());
           personalizedAgendaBeanArray=eFTDB.getPersonlizedAgendLettersP4(sbr.toString());
            if(personalizedAgendaBeanArray != null){
            for(int i=0;i<personalizedAgendaBeanArray.length;i++){
                thisPersonalizedAgendaBean=personalizedAgendaBeanArray[i];
                    thisPersonalizedAgendaBean.setServerName(this.getRequest().getRequestURL().toString());
                    //thisPersonalizedAgendaBean.setWeekStartDate(startDate);
                    //thisPersonalizedAgendaBean.setWeekEndDate(endDate);
                    VelocityConvertor.generatePersonalizedAgenda(thisPersonalizedAgendaBean);
                    //personalizedAgendaMap.put(thisPersonalizedAgendaBean.getEmplId(),path);
                    personalizedAgendaList.add(thisPersonalizedAgendaBean.getEmplId()+ thisPersonalizedAgendaBean.getWeek_Name());
                    thisPersonalizedAgendaBean=null; //Lets make it Eligible for Garbage Collection
                }
          } 
        //req.setAttribute("personalizedAgendaMap", personalizedAgendaMap);
        req.setAttribute("personalizedAgendaList", personalizedAgendaList);  
        return new String("success");
    	}
    	catch (Exception e) {
    		Global.getError(getRequest(),e);
    		return new String("failure");
    		}

    }

   
    public String emailInvitation()
    {           
        HttpServletRequest req = this.getRequest();
        try{
        if(getSession().getAttribute("Count")!= null){
            getSession().removeAttribute("Count");
        }
        trmOrderDateBean=eFTDB.getTRMDates();
        rbuBoxDataBean=eFTDB.getBoxes();
        req.setAttribute("Command", "PromptForSending");
         if(getSession().getAttribute("Count")!= null){
            req.setAttribute("Count", getSession().getAttribute("Count"));
         }
         req.setAttribute("trmOrderDateBean", trmOrderDateBean);
         req.setAttribute("rbuBoxDataBean", rbuBoxDataBean);
        return new String("success");
        }
        catch (Exception e) {
        	Global.getError(getRequest(),e);
        	return new String("failure");
        	}

    }
    
   
    public String sendEmailInvitation()
    {
        HttpServletRequest req = this.getRequest();
        try{
        req.setAttribute("Command", "DisplayResult");
        trmOrderDateBean=eFTDB.getTRMDates();
        
        int iCount=0;
        String sCount = "0";
       String Selected_Date=req.getParameter("Selected_Date")==null?"":req.getParameter("Selected_Date");
        // Changes made by Jeevan for Box
        String Selected_Box = req.getParameter("Selected_Box")==null?"":req.getParameter("Selected_Box");
        System.out.println("Selected box ################################## " + Selected_Box);
        //Code to Send Email         
        EmailInfoBean emailList[] = null;
        EmailInfoBean emailListWithHSL[] = null;
        EmailInfoBean thisEmployeeListBean;
        String serverName = "";
        //Jeevan on Phone - 02/05
        List processedEmplIds = new ArrayList();
        List processedAllEmplIds = new ArrayList();
        ReadProperties readProperties = new ReadProperties("EmailHandlingProperties.propeties");    
        //emailList=eFTDB.getEmailInfoList();
        // Get the email list based on the selected date and box
          StringBuffer sbr=new StringBuffer();
          if(Selected_Date!=null && !Selected_Date.equalsIgnoreCase("") && !Selected_Box.equals("100")){
           sbr.append(" SELECT DISTINCT EMPLID AS EMPLID,  DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
           sbr.append(" ADDRESS1 AS SHIPADD1, ADDRESS2 AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");
           sbr.append(" PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS, EMAIL_ADDRESS AS EMAILID, CLASS_ID as CLASSID");
           sbr.append(" FROM  V_RBU_INVITATION_REQUIRED fmoh");            
            
            if(!Selected_Date.equalsIgnoreCase("ALL") && !Selected_Box.equalsIgnoreCase("ALL")){
                sbr.append(" where  to_char(fmoh.DATEORDERED,'MM/DD/YY')='"+Selected_Date+"' ");
                sbr.append(" and  fmoh.BOXID ='"+Selected_Box+"' ");
            }   
            else if(!Selected_Date.equalsIgnoreCase("ALL") && Selected_Box.equalsIgnoreCase("ALL")){
                sbr.append(" where  to_char(fmoh.DATEORDERED,'MM/DD/YY')='"+Selected_Date+"' ");
            }
            else if(Selected_Date.equalsIgnoreCase("ALL") && !Selected_Box.equalsIgnoreCase("ALL")){
                sbr.append(" where  fmoh.BOXID ='"+Selected_Box+"' ");
            }
            sbr.append(" order by ORDERNUMBER ");
            LoggerHelper.logSystemDebug("PrintController.jpf  Method:sendEmailInvitation -- SQL Query for Selected Group_ID and Date--"+sbr.toString());
            System.out.println("Query sendEmailInvitation ###########################" + sbr.toString());
            emailList=eFTDB.getEmailList(sbr.toString());
          }
          else if(Selected_Date!=null && !Selected_Date.equalsIgnoreCase("") && Selected_Box.equals("100")){
           sbr.append(" SELECT DISTINCT EMPLID AS EMPLID,  DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,");
           sbr.append(" ADDRESS1 AS SHIPADD1, ADDRESS2 AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,");
           sbr.append(" PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS, EMAIL_ADDRESS AS EMAILID, CLASS_ID as CLASSID");
           sbr.append(" FROM  V_RBU_INVITATION_REQD_ALL_BOX fmoh");            
            
            if(!Selected_Date.equalsIgnoreCase("ALL")){
                sbr.append(" where  to_char(fmoh.DATEORDERED,'MM/DD/YY')='"+Selected_Date+"' ");
            }
            sbr.append(" order by ORDERNUMBER ");
            LoggerHelper.logSystemDebug("PrintController.jpf  Method:sendEmailInvitation -- Without box--"+sbr.toString());
            System.out.println("Query sendEmailInvitation ########################### without box" + sbr.toString());
            emailList=eFTDB.getEmailList(sbr.toString());
          } 
            if(emailList != null){
            for(int iCtr=0;iCtr<emailList.length;iCtr++)
            {  
               System.out.println("GEtting the email info so sending#################");  
               EmailInfoBean emailInfo = emailList[iCtr];
               String sEmployeeID = emailInfo.getEmplID();
               //Check email address
               String sDestEmail = emailInfo.getEmailID();          
               if(sEmployeeID == null || sEmployeeID.length() == 0)
               {
                 continue;
               }
    
               if(sDestEmail == null || sDestEmail.length() == 0)
               {
                 continue;
               }
                
                String requestURL = this.getRequest().getRequestURL().toString();
                System.out.println("######################" + requestURL);
                LoggerHelper.logSystemDebug("######################" + requestURL);
                // IF the environment is local,int and staging do not send emails to
                // actual  people
               String[] email_to = new String[1];  
               String[] email_cc = null;
               String email_bcc = new String();
               String[] bcc = new String[2];
              if(requestURL != null && requestURL.indexOf("wlsdev1.pfizer.com") != -1
              || requestURL != null && requestURL.indexOf("wlsstg5.pfizer.com") != -1
              || requestURL != null && requestURL.indexOf("localhost") != -1){
             
                String value = readProperties.getValue("EMAIL_INVITATION_TO_DEV_INT_STG");
                email_to[0] = value;
                email_bcc = null;
                LoggerHelper.logSystemDebug("#### Request URL IF for INT/DEV/STG for Email Invitation email sending #####" + email_to[0]);
              }
              else if(requestURL != null && requestURL.indexOf("wlsprd4.pfizer.com") != -1){
                 email_to[0] = emailInfo.getEmailID(); 
                 String bcc1 = readProperties.getValue("EMAIL_INVITATION_BCC_PROD1");
                 email_bcc = bcc1; 
                 LoggerHelper.logSystemDebug("#### Request URL IF for PROD for Email Invitation email sending ##### TO " +  email_to[0] + "BCC " + email_bcc);
              }
               try
               {
                 //Subject Line
                 // Changes made by Jeevan for RBU Slaes Training
                 String sSubject = "Invitation for Product Training (PSCPT)";
                 emailInfo.setServerName(req.getRequestURL().toString());
                 //Email Body
                 String sEmailContent = VelocityConvertor.generateInvitations(emailInfo);
                 if(sEmailContent != null && sEmailContent.length() > 0)
                 {                
                    // Log Here email Debugging 
                    LoggerHelper.logSystemDebug("^^^^^ PSCPT EMAIL DEBUG LOG START^^^^^^^^");
                    LoggerHelper.logSystemDebug("^^^^^^^^ EMAIL sEmployeeID :" + sEmployeeID );
                    LoggerHelper.logSystemDebug("^^^^^^^^ EMAIL EMail_TO  :" + email_to[0] );
                    LoggerHelper.logSystemDebug("^^^^^^^^ EMAIL EMail_BCC  :" + email_bcc );
                    LoggerHelper.logSystemDebug("^^^^^ PSCPT EMAIL DEBUG LOG END^^^^^^^^");
                   //Thread.sleep(3600);
                   Thread.sleep(100);
                   // For testing dont check this
                   if(email_to[0] != null && email_to[0].toLowerCase().indexOf("@pfizer.com") != -1)
                  // if(email_to[0] != null)
                   { 
                    System.out.println("Size ##### " +  processedEmplIds.size() + "Content >>> " + processedEmplIds);
                     if(processedEmplIds.size()==0 || !processedEmplIds.contains(emailInfo.getEmplID())){  
                         // Aslo check whether the employee has been sent mail for the products and class combination
                        //String flag = PrintHandlers.getEmailSent(emailInfo);
                         System.out.println("Class dates ############ " + emailInfo.getClassId());
                         MailUtil.sendMessage("traininglogistics@pfizer.com", email_to[0], null,email_bcc, sSubject, sEmailContent, "text/html", "trMailSession");
                         // Changes made for RBU Sales training
                         String sLogCode = "RBU Invitation Email";
                         PrintHandlers.insertEmailDispatch(emailInfo, sLogCode);
                         iCount++; 
                         //jeevan on phone 
                     }
                     if(processedEmplIds!=null && !processedEmplIds.contains(emailInfo.getEmplID())){
                        processedEmplIds.add(emailInfo.getEmplID());
                     }
                   }               
                 }
                
                 
               }
               catch(Exception e)
               {
                
                 log.error(e,e);
                                 // Log Here email Debugging 
                LoggerHelper.logSystemDebug("^^^^^ PDF & SPF EMAIL ERROR LOG START^^^^^^^^");
                LoggerHelper.logSystemDebug("^^^^^^^^ EMAIL sEmployeeID :" + sEmployeeID );
                LoggerHelper.logSystemDebug("^^^^^^^^ EMAIL EMail_TO  :" + email_to[0] );
                LoggerHelper.logSystemDebug("^^^^^^^^ EMAIL EMail_BCC  :" + email_bcc );
                LoggerHelper.logSystemDebug("^^^^^ PDF & SPF EMAIL ERROR LOG END^^^^^^^^");
                e.printStackTrace();
               }
           
        }
          }
 
        sCount = Integer.toString(iCount);
        req.setAttribute("Count", sCount);
        getSession().setAttribute("Count", sCount);
      return emailInvitation();
        }
        catch (Exception e) {
        	Global.getError(getRequest(),e);
        	return new String("failure");
        	}

    }

    /**
     * This method adds days to a given date
     */
    private Date addDaysToDate(Date date, int daysToAdd) {
        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.DAY_OF_MONTH, daysToAdd);
        return now.getTime();
    }

    
    public String processTRMOrders()
    {
    	try{
        PrintHandlers.callTRMOrderProcess();
        HttpServletRequest req = this.getRequest();
        req.setAttribute("TRMCommand", "OrderProcessed");
        return new String("success");
    	}
    	catch (Exception e) {
    		Global.getError(getRequest(),e);
    		return new String("failure");
    		}

    }


    public String getBoxesForTRMDates()
    {
        // Changes made for RBU Shipment for getting the boxes based on the TRM Date
        HttpServletRequest req = this.getRequest();
        try{
        String selectedDate = req.getParameter("TRMorderDate");
        StringBuffer queryStr = new StringBuffer();
        if(!selectedDate.equals("-1")){
            // If the date is all then get all the boxes else get the boxes valid for the day.
            if(selectedDate.equalsIgnoreCase("ALL")){
             queryStr.append(" select distinct prod.BOXID AS BOXID, prod.BOXNAME as BOXNAME, prod.PRODUCTNAME as ProductName, prod.BOXCOMBO as BOXCOMBO"); 
            queryStr.append(" from  rbu_material_order_history mh,V_RBU_BOX_PRODUCT_ASSIGNMENT prod,");
            queryStr.append(" V_RBU_CLASS_ASSIGNMENT v where  mh.PERSON_ID = v.EMPLID and");
            queryStr.append(" prod.PRODUCTNAME = Tbl2str(CAST(MULTISET(");
            queryStr.append(" select distinct DECODE(tp.PRODUCT_DESC,'HS/L Toviaz','Toviaz', 'OAB Toviaz', 'Toviaz', 'Revatio', 'Revatio', 'Revatio No PLC', 'Revatio', 'Aricept PC', 'Aricept','Aricept SM', 'Aricept','Geodon PC', 'Geodon','Geodon SM', 'Geodon','Lyrica PC', 'Lyrica','Lyrica SM', 'Lyrica', PRODUCT_DESC)  from V_RBU_CLASS_ASSIGNMENT tp");
            queryStr.append(" WHERE tp.emplid = mh.PERSON_ID   order by DECODE(tp.PRODUCT_DESC,'HS/L Toviaz','Toviaz', 'OAB Toviaz', 'Toviaz', 'Revatio', 'Revatio', 'Revatio No PLC', 'Revatio', 'Aricept PC', 'Aricept','Aricept SM', 'Aricept','Geodon PC', 'Geodon','Geodon SM', 'Geodon','Lyrica PC', 'Lyrica','Lyrica SM', 'Lyrica', PRODUCT_DESC) asc) AS test_nested_tab)) ");
            queryStr.append("order by prod.boxname asc");
            }            
            else{
            queryStr.append(" select distinct prod.BOXID AS BOXID, prod.BOXNAME as BOXNAME, prod.PRODUCTNAME as ProductName, prod.BOXCOMBO as BOXCOMBO"); 
            queryStr.append(" from  rbu_material_order_history mh,V_RBU_BOX_PRODUCT_ASSIGNMENT prod,");
            queryStr.append(" V_RBU_CLASS_ASSIGNMENT v where  mh.PERSON_ID = v.EMPLID and");
            queryStr.append(" prod.PRODUCTNAME = Tbl2str(CAST(MULTISET(");
            queryStr.append(" select distinct DECODE(tp.PRODUCT_DESC,'HS/L Toviaz','Toviaz', 'OAB Toviaz', 'Toviaz', 'Revatio', 'Revatio', 'Revatio No PLC', 'Revatio', 'Aricept PC', 'Aricept','Aricept SM', 'Aricept','Geodon PC', 'Geodon','Geodon SM', 'Geodon','Lyrica PC', 'Lyrica','Lyrica SM', 'Lyrica', PRODUCT_DESC)  from V_RBU_CLASS_ASSIGNMENT tp");
            queryStr.append(" WHERE tp.emplid = mh.PERSON_ID   order by DECODE(tp.PRODUCT_DESC,'HS/L Toviaz','Toviaz', 'OAB Toviaz', 'Toviaz', 'Revatio', 'Revatio', 'Revatio No PLC', 'Revatio', 'Aricept PC', 'Aricept','Aricept SM', 'Aricept','Geodon PC', 'Geodon','Geodon SM', 'Geodon','Lyrica PC', 'Lyrica','Lyrica SM', 'Lyrica', PRODUCT_DESC) asc) AS test_nested_tab)) ");
            queryStr.append(" and  to_char(mh.DATEORDERED,'MM/DD/YY')='"+selectedDate+"'");
            queryStr.append("order by prod.boxname asc");
            }
            rbuBoxDataBean = eFTDB.getRBUBoxes(queryStr.toString());
        }
        else{
            req.setAttribute("firstTimeLoad", "firstTimeLoad"); 
        }
        
        req.setAttribute("Chose_Date", selectedDate);
        return new String("success");
        }
        catch (Exception e) {
        	Global.getError(getRequest(),e);
        	return new String("failure");
        	}

    }
    
  
    public String createPersonlizedAgendaLetters()
    {
    	try{
    		
        HttpServletRequest req = this.getRequest();
        String WeekId = req.getParameter("WeekId")==null?"":req.getParameter("WeekId");
      //  System.out.println("Week here is ##################### " + WeekId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
         personalizedAgendaMap=new TreeMap();
         Map processemployeeIds = new TreeMap();
         personalizedAgendaList = new ArrayList();
         String path="";
         PersonalizedAgendaBean[] personalizedAgendaBeanArray;
         PersonalizedAgendaBean thisPersonalizedAgendaBean;
         // GEt the week start and end dates
         RBUTrainingWeek  week = new RBUTrainingWeek();
         week = PrintHandlers.getClassDatesForWeek(WeekId);
         String startDate = "";
         String endDate = "";
         String startDateToDisplay = "";
         String endDateToDisplay="";
         String weekName = "";
         if(week != null){
            startDate = dateFormat.format(week.getStart_date());
            endDate = dateFormat.format(week.getEnd_date());
            startDateToDisplay = dateFormat1.format(week.getStart_date());
            endDateToDisplay = dateFormat1.format(week.getEnd_date());
            weekName = week.getWeek_name();
         }
         
         String viewName = "";
         if(WeekId.equals("1")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_1";
         }
         if(WeekId.equals("2")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_2";
         }
         if(WeekId.equals("3")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_3";
         }
         if(WeekId.equals("4")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_4";
         }
         if(WeekId.equals("5")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_5";
         }
         if(WeekId.equals("6")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_6";
         }
         if(WeekId.equals("7")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_7";
         }
         if(WeekId.equals("8")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_8";
         }
         if(WeekId.equals("9")){
            viewName = "V_RBU_PERSONALIZED_AGENDA_9";
         }
          viewName = "V_RBU_PERSONALIZED_AGENDA_";
        
      //   System.out.println("Week Name ######################" + weekName);
         StringBuffer sbr=new StringBuffer();
         sbr.append(" SELECT FIRSTNAME as FIRSTNAME, LASTNAME as LASTNAME, EMPLID as EMPLID,MONDAYAMPRODUCT as MONDAYAMPRODUCT , MONDAYAMTABLE as MONDAYAMTABLE,");
         sbr.append(" MONDAYAMROOM as MONDAYAMROOM, MONDAYAMTRAINER as MONDAYAMTRAINER,MONDAYPMPRODUCT as MONDAYPMPRODUCT,MONDAYPMTABLE as MONDAYPMTABLE,MONDAYPMROOM as MONDAYPMROOM,");
         sbr.append(" MONDAYPMTRAINER as MONDAYPMTRAINER,TUESDAYAMPRODUCT as TUESDAYAMPRODUCT,TUESDAYAMTABLE as TUESDAYAMTABLE,TUESDAYAMROOM as TUESDAYAMROOM,TUESDAYAMTRAINER as TUESDAYAMTRAINER, ");
         sbr.append(" TUESDAYPMPRODUCT as TUESDAYPMPRODUCT,TUESDAYPMTABLE as TUESDAYPMTABLE,TUESDAYPMROOM as TUESDAYPMROOM,TUESDAYPMTRAINER as TUESDAYPMTRAINER, WEDNESDAYAMPRODUCT as WEDNESDAYAMPRODUCT,");           
         sbr.append(" WEDNESDAYAMTABLE as WEDNESDAYAMTABLE,WEDNESDAYAMROOM as WEDNESDAYAMROOM,WEDNESDAYAMTRAINER as WEDNESDAYAMTRAINER,WEDNESDAYPMPRODUCT as WEDNESDAYPMPRODUCT,WEDNESDAYPMTABLE as WEDNESDAYPMTABLE,");
         sbr.append(" WEDNESDAYPMROOM as WEDNESDAYPMROOM,WEDNESDAYPMTRAINER as WEDNESDAYPMTRAINER,THURSDAYAMPRODUCT as THURSDAYAMPRODUCT,THURSDAYAMTABLE as THURSDAYAMTABLE,THURSDAYAMROOM as THURSDAYAMROOM,");
         sbr.append(" THURSDAYAMTRAINER as THURSDAYAMTRAINER,THURSDAYPMPRODUCT as THURSDAYPMPRODUCT,THURSDAYPMTABLE as THURSDAYPMTABLE,THURSDAYPMROOM as THURSDAYPMROOM,THURSDAYPMTRAINER as THURSDAYPMTRAINER,");
         sbr.append(" FRIDAYAMPRODUCT as FRIDAYAMPRODUCT,FRIDAYAMTABLE as FRIDAYAMTABLE,FRIDAYAMROOM as FRIDAYAMROOM,FRIDAYAMTRAINER as FRIDAYAMTRAINER,FRIDAYPMPRODUCT as FRIDAYPMPRODUCT,");
         sbr.append(" FRIDAYPMTABLE as FRIDAYPMTABLE,FRIDAYPMROOM as FRIDAYPMROOM,FRIDAYPMTRAINER as FRIDAYPMTRAINER,MONDAYAMSTARTTIME as MONDAYAMSTARTTIME,MONDAYAMENDTIME as MONDAYAMENDTIME,");
         sbr.append(" MONDAYPMSTARTTIME as MONDAYPMSTARTTIME,MONDAYPMENDTIME as MONDAYPMENDTIME,TUESDAYAMSTARTTIME as TUESDAYAMSTARTTIME,TUESDAYAMENDTIME as TUESDAYAMENDTIME, TUESDAYPMSTARTTIME as TUESDAYPMSTARTTIME,");
         sbr.append(" TUESDAYPMENDTIME as TUESDAYPMENDTIME,WEDNESDAYAMSTARTTIME as WEDNESDAYAMSTARTTIME,WEDNESDAYAMENDTIME as WEDNESDAYAMENDTIME, WEDNESDAYPMSTARTTIME as WEDNESDAYPMSTARTTIME,WEDNESDAYPMENDTIME as WEDNESDAYPMENDTIME,");
         sbr.append(" THURSDAYAMSTARTTIME as THURSDAYAMSTARTTIME,THURSDAYAMENDTIME as THURSDAYAMENDTIME, THURSDAYPMSTARTTIME as THURSDAYPMSTARTTIME,THURSDAYPMENDTIME as THURSDAYPMENDTIME,FRIDAYAMSTARTTIME as FRIDAYAMSTARTTIME,");
         sbr.append(" FRIDAYAMENDTIME as FRIDAYAMENDTIME,FRIDAYPMSTARTTIME as FRIDAYPMSTARTTIME, FRIDAYPMENDTIME as FRIDAYPMENDTIME");
         sbr.append(" from "+viewName+""+WeekId+" ");
           LoggerHelper.logSystemDebug("The SQL Query here in --printSearchTRMOrder--  to Create Inviattions are--"+sbr.toString());
           System.out.println("Query for print >>>>>>>>>>" + sbr.toString());
           personalizedAgendaBeanArray=eFTDB.getPersonlizedAgendLetters(sbr.toString());
            if(personalizedAgendaBeanArray != null){
            for(int i=0;i<personalizedAgendaBeanArray.length;i++){
                thisPersonalizedAgendaBean=personalizedAgendaBeanArray[i];
                    thisPersonalizedAgendaBean.setServerName(this.getRequest().getRequestURL().toString());
                    thisPersonalizedAgendaBean.setWeekStartDate(startDate);
                    thisPersonalizedAgendaBean.setWeekEndDate(endDate);
                     VelocityConvertor.generatePersonalizedAgenda(thisPersonalizedAgendaBean);
                  //  System.out.println("Employee Id here is  " + thisPersonalizedAgendaBean.getEmplId());
                    //personalizedAgendaMap.put(thisPersonalizedAgendaBean.getEmplId(),path);
                    personalizedAgendaList.add(thisPersonalizedAgendaBean.getEmplId());
                    thisPersonalizedAgendaBean=null; //Lets make it Eligible for Garbage Collection
                }
          } 
        req.setAttribute("startDate", startDateToDisplay); 
        req.setAttribute("endDate", endDateToDisplay);   
      //  req.setAttribute("weekName", weekName);   
        req.setAttribute("personalizedAgendaMap", personalizedAgendaMap); 
        return new String("success");
    	}
    	catch (Exception e) {
    		Global.getError(getRequest(),e);
    		return new String("failure");
    		}

    }

    
    public String printPersonalizedAgenda()
    {
    	try{
        // Get the current week start and end date
        RBUSHandler handler = new RBUSHandler();
       // String sql = "SELECT WEEK_ID as WEEK_ID, to_char(START_DATE, 'MM/DD/YYYY') as START_DATE, to_char(END_DATE, 'MM/DD/YYYY')  as END_DATE, WEEK_NAME as WEEK_NAME from RBU_TRAINING_WEEKS order by week_id asc";
        // Training week array to be displayed in the drop down.
        String sql = "select distinct week_id, week_name  from v_rbu_class_roster_report order by week_id asc";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String  WeekId = "";
        TrainingWeeks[] trainingWeekAray;
        trainingWeekAray = eFTDB.getTrainingWeeks(sql);
        this.getRequest().setAttribute("classWeeks", trainingWeekAray);
        return new String("success");
    	}
    	catch (Exception e) {
    		Global.getError(getRequest(),e);
    		return new String("failure");
    		}

    }



    public String createPersonlizedAgendaLettersP4()
    {
        try{
        HttpServletRequest req = this.getRequest();
        String WeekId = req.getParameter("WeekId")==null? "":req.getParameter("WeekId");
        System.out.println("Week here is ##################### " + WeekId);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
        
         personalizedAgendaMap=new TreeMap();
         Map processemployeeIds = new TreeMap();
         personalizedAgendaList = new ArrayList();
         String path="";
         PersonalizedAgendaBeanP4[] personalizedAgendaBeanArray;
         PersonalizedAgendaBeanP4 thisPersonalizedAgendaBean;
/*         // GEt the week start and end dates
         RBUTrainingWeek  week = new RBUTrainingWeek();
         //week = PrintHandlers.getClassDatesForWeek(WeekId);
         String startDate = "";
         String endDate = "";
         String startDateToDisplay = "";
         String endDateToDisplay="";
         String weekName = "";
         if(week != null){
            startDate = dateFormat.format(week.getStart_date());
            endDate = dateFormat.format(week.getEnd_date());
            startDateToDisplay = dateFormat1.format(week.getStart_date());
            endDateToDisplay = dateFormat1.format(week.getEnd_date());
            weekName = week.getWeek_name();
         }
  */       
         String viewName = "";
         if(WeekId.equals("wave1")){
            viewName = "V_P4_PERSONALIZED_AGENDA_1";
         }
         if(WeekId.equals("wave2")){
            viewName = "V_P4_PERSONALIZED_AGENDA_2";
         }
         if(WeekId.equals("wave3")){
            viewName = "V_P4_PERSONALIZED_AGENDA_3";
         }
        
      //   System.out.println("Week Name ######################" + weekName);
         StringBuffer sbr=new StringBuffer();
         sbr.append(" SELECT * ");
         sbr.append(" from "+viewName +" ");
           LoggerHelper.logSystemDebug("The SQL Query here in --printSearchTRMOrder--  to Create Inviattions are--"+sbr.toString());
           System.out.println("Query for print >>>>>>>>>>" + sbr.toString());
           personalizedAgendaBeanArray=eFTDB.getPersonlizedAgendLettersP4(sbr.toString());
            if(personalizedAgendaBeanArray != null){
            for(int i=0;i<personalizedAgendaBeanArray.length;i++){
                thisPersonalizedAgendaBean=personalizedAgendaBeanArray[i];
                    thisPersonalizedAgendaBean.setServerName(this.getRequest().getRequestURL().toString());
                    //thisPersonalizedAgendaBean.setWeekStartDate(startDate);
                    //thisPersonalizedAgendaBean.setWeekEndDate(endDate);
                     VelocityConvertor.generatePersonalizedAgenda(thisPersonalizedAgendaBean);
                  //  System.out.println("Employee Id here is  " + thisPersonalizedAgendaBean.getEmplId());
                    //personalizedAgendaMap.put(thisPersonalizedAgendaBean.getEmplId(),path);
                    personalizedAgendaList.add(thisPersonalizedAgendaBean.getEmplId()+ thisPersonalizedAgendaBean.getWeek_Name());
                    thisPersonalizedAgendaBean=null; //Lets make it Eligible for Garbage Collection
                }
          } 
        //req.setAttribute("startDate", startDateToDisplay); 
        //req.setAttribute("endDate", endDateToDisplay);   
      //  req.setAttribute("weekName", weekName);   
        req.setAttribute("personalizedAgendaMap", personalizedAgendaMap); 
        return new String("success");
        }
        catch (Exception e) {
        	Global.getError(getRequest(),e);
        	return new String("failure");
        	}
    }

   
    public String printPersonalizedAgendaP4()
    {
        try{
    	// Get the current week start and end date
        RBUSHandler handler = new RBUSHandler();
       // String sql = "SELECT WEEK_ID as WEEK_ID, to_char(START_DATE, 'MM/DD/YYYY') as START_DATE, to_char(END_DATE, 'MM/DD/YYYY')  as END_DATE, WEEK_NAME as WEEK_NAME from RBU_TRAINING_WEEKS order by week_id asc";
        // Training week array to be displayed in the drop down.
        String sql = "select distinct ('wave' || wave_id) as week_id, ('Wave ' || wave_id) as week_name  from v_p4_class_roster_report order by week_id asc";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String  WeekId = "";
        TrainingWeeks[] trainingWeekAray;
        trainingWeekAray = eFTDB.getTrainingWeeks(sql);
        this.getRequest().setAttribute("classWeeks", trainingWeekAray);
        return new String("success");
        }
        catch (Exception e) {
        	Global.getError(getRequest(),e);
        	return new String("failure");
        	}

    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
   /* Infosys code changes starts here
    * public static class GetBoxesForTRMDatesForm extends ActionSupport
    {
        private String selectedBox;

        private String selectedDate;

        public void setSelectedDate(String selectedDate)
        {
            this.selectedDate = selectedDate;
        }

        public String getSelectedDate()
        {
            return this.selectedDate;
        }

        public void setSelectedBox(String selectedBox)
        {
            this.selectedBox = selectedBox;
        }

        public String getSelectedBox()
        {
            return this.selectedBox;
        }
    }*/

	@Override
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response=response;
		
	}

	
}


