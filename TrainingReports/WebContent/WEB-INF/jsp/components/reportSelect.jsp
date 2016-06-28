<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.MenuList"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.utils.ReadProperties"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.components.ProductSelectWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectWc"%>
<%@ page import="com.pfizer.webapp.wc.components.archivedPageWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%! 
    private static final String SPECIAL  = "Special";
    private static final String TSR_ADMIN  = "TSR Admin";
    private static final String TSR_POWER  = "TSR Powers";
    private static final String POWER  = "Power";
    private static final String TSR_STEERE  = "TSR Steere";
    private static final String ADMIN = "ADMIN";
    private static final String SUPER_ADMIN = "SUPER ADMIN";
    private static final String ONCOLOGY="ONCOLOGY";
    
    public String putSpace(int level){
        String output = "";
        for(int i=1;i<level;i++){
            output = output+"&nbsp;&nbsp;&nbsp;&nbsp;";
        }        
        return output; 
    }
%>
<%            
	ReportSelectWc wc = (ReportSelectWc)request.getAttribute(ReportSelectWc.ATTRIBUTE_NAME);
    List ls=new ArrayList();
    Hashtable renderMenu = wc.getMenu(); 
    int numberBox = wc.getMenuHeader().size();
    int seperatorHalf = (numberBox%2==0)?numberBox/2:(numberBox+1)/2;
    Enumeration keys = wc.getMenuHeader().elements();
    Enumeration keysEven = wc.getMenuHeader().elements();
    Enumeration keysOdd = wc.getMenuHeader().elements();
%>
<script>
function minimize(index)
{

document.getElementById("sectionShow"+index).style.display='none';
document.getElementById("sectionHide"+index).style.display='block';
}

function maximize(index)
{
document.getElementById("sectionHide"+index).style.display='none';
document.getElementById("sectionShow"+index).style.display='block';
}

function collapseGroup(button,groupId)
{
    var divElements = document.getElementsByTagName('span');
    for (i=0;i<divElements.length;i++)
    {
        if (divElements[i].id.indexOf(groupId) > -1)
        {
            if (divElements[i].style.display == 'block') 
            {
                divElements[i].style.display = 'none';
            }else  if (divElements[i].style.display == 'none') 
            {
                divElements[i].style.display = 'block';
            }
        }
    }
    if (button.innerHTML == '+') 
    {
        button.innerHTML = '-';
    }else 
    {
        button.innerHTML = '+';
    }
}
</script>


<TABLE class="basic_table"> 
<TR>
<TD rowspan="2"><IMG src="/TrainingReports/resources/images/spacer.gif" width="15"></TD>
<TD align="left">
<%boolean adm = wc.getUser().isAdmin();
boolean hq = wc.getUser().isHQUser();
boolean sprAdmin = wc.getUser().isSuperAdmin();
if(adm || hq){%>
<div id=header_title style="color:#1f61a9;">Welcome to Training Reporting Tool</div>
<%} else {%>
<div  id=header_title style="color:#1f61a9;">
<a href="/TrainingReports/p2l/employeeSearchDetailPage?emplid=<%=wc.getUser().getEmplid()%>" >My Profile</a>/Training Reports</div>
<%}%>
</TD>
<!--Added for TRT Phase 2 Enhancement-->
<TD align="right">
<% 
// Added for TRT Phase 2 - HQ Users requirement.

//System.out.println("Is admin = "+adm);
//System.out.println("Is HQ = "+hq);
if (!adm && !hq) {%>
<a href="/TrainingReports/newHomePage?emplid=<%=wc.getUser().getEmplid()%>">My Direct Reports</a>&nbsp;&nbsp;&nbsp;
<%}
if(!hq){%>
<a href="/TrainingReports/allEmployeeSearch">Employee Search</a>&nbsp;&nbsp;&nbsp; 

<%}%>
<!--</TD>
<!--end-->
 <!--added for TRT major enhancement 3.6- F6-->
<!--<TD align ="right">-->
<% String res = UserSession.getUserSession(request).getIsDelegatedUser();
if (res != null ) {
    if(!sprAdmin){%>
     <a href="/TrainingReports/switchUser">Switch User</a>
   <%}}%></TD> 
</TR>
<TR><TD colspan="3">
<!--ends here-->

<TABLE class="basic_table"><TR><TD><IMG src="/TrainingReports/resources/images/spacer.gif" width="15"></TD><TD>
<h4>Please select Report Type:</h4>

<TABLE>
<TR><TD align="left" valign="top">
<% 
    
    int index=0;
    int odd=0;
    int even = 0;
    ReadProperties props= new ReadProperties();
    if (keysOdd.hasMoreElements() ) keysOdd.nextElement();
    odd++;
    while(keys.hasMoreElements()){
        MenuList headerList = null;
        if (index < seperatorHalf && keysEven.hasMoreElements()) {
                     headerList = (MenuList)keysEven.nextElement();
                     even++;
        }else
        {
           
             headerList = (MenuList)keysOdd.nextElement();
             odd++;
        }
       
        String header = headerList.getLabel();
        //System.out.println("Archive flag=="+headerList.getArchiveFlag());
       // System.out.println("CLUSTER OF THE EMPLOYEE"+wc.getUser().getDisplayCluster());
        boolean allowHeader = true; 
        //System.out.println("User Name-------------"+wc.getUser().getName());   
        //System.out.println("Admin user-------------"+wc.getUser().isAdmin());   
        if ( !Util.isEmpty(headerList.getAllow())) {  
            allowHeader = false;
            if(TSR_ADMIN.equals(headerList.getAllow())){
                if ( wc.getUser().isAdmin() || wc.getUser().isTsrAdmin()) { 
                    allowHeader = true;       
                } 
            }else if(ADMIN.equals(headerList.getAllow())){
                /*Adding condition for Feedback user for SCE Form enhancement */
                if(wc.getUser().isAdmin()||wc.getUser().isSuperAdmin() || wc.getUser().isFeedbackUser()){
                    allowHeader = true;            
                }
            }else if(SUPER_ADMIN.equals(headerList.getAllow())){
                if(wc.getUser().isSuperAdmin()){
                    allowHeader = true;            
                }
            }
            else if(SPECIAL.equals(headerList.getAllow())){         
                    allowHeader = true;                          
            } 
            else if(wc.getUser().isSuperAdmin())
             {
                allowHeader=true;
             }
              
             /* Added for RBU -- Loop for user specific groups*/  
              /*Adding condition for Feedback user for SCE Form enhancement */          
            else if(!wc.getUser().isAdmin() && !wc.getUser().isTsrAdmin() && !wc.getUser().isSuperAdmin())
            {                           
              // if(Util.splitFields(headerList.getBusUnit(),wc.getUser().getBusinessUnit()) || Util.splitFields(headerList.getSalesOrg(),wc.getUser().getSalesOrganization()) || Util.splitFields(headerList.getRole(),wc.getUser().getRoleDesc()) || Util.splitFields(headerList.getFeedbackUsers(),wc.getUser().getName()))                
                 if(Util.splitFields(headerList.getBusUnit(),wc.getUser().getBusinessUnit()) && Util.splitFields(headerList.getSalesOrg(),wc.getUser().getSalesOrganization()) && Util.splitFields(headerList.getRole(),wc.getUser().getRoleDesc()))                
                {
                    allowHeader = true;  
                }
            }            
                                          
                /* End of addition */       
           /* else if(TSR_POWER.equals(headerList.getAllow())){
                if(wc.getUser().isAdmin()||wc.getUser().isTsrAdmin() 
                    || props.getValue("TSR_POWER1").equals(wc.getUser().getDisplayCluster())
                    || props.getValue("TSR_POWER2").equals(wc.getUser().getDisplayCluster())){
                    allowHeader = true;            
                }
            }else if(TSR_STEERE.equals(headerList.getAllow())){
                if(wc.getUser().isAdmin() 
                    ||wc.getUser().isTsrAdmin() 
                    || props.getValue("TSR_STEERE1").equals(wc.getUser().getDisplayCluster())
                    || props.getValue("TSR_STEERE2").equals(wc.getUser().getDisplayCluster())){
                    allowHeader = true;            
                }
            }else if(SPECIAL.equals(headerList.getAllow())){
                if(wc.getUser().isAdmin()
                    ||wc.getUser().isTsrAdmin()
                    || props.getValue("SPECIAL1").equals(wc.getUser().getDisplayCluster())
                    || props.getValue("SPECIAL2").equals(wc.getUser().getDisplayCluster())
                    || props.getValue("SPECIAL3").equals(wc.getUser().getDisplayCluster())
                    || props.getValue("SPECIAL4").equals(wc.getUser().getDisplayCluster()) 
                    || props.getValue("SPECIAL5").equals(wc.getUser().getDisplayCluster()))
                    || props.getValue("SPECIAL6").equals(wc.getUser().getDisplayCluster())
                    || props.getValue("SPECIAL7").equals(wc.getUser().getDisplayCluster())
                    || props.getValue("SPECIAL8").equals(wc.getUser().getDisplayCluster())                  
                    {
                    allowHeader = true;            
                }
            else if(ONCOLOGY.equals(headerList.getAllow())){
            if(wc.getUser().isAdmin()
                ||wc.getUser().isTsrAdmin()
                || props.getValue("ONCOLOGY").equals(wc.getUser().getDisplayCluster())){
                allowHeader = true;            
            }
            }
            }
            */      
        }   
        %>
        
        <% if (allowHeader) {%>
            <%
            String style_minimize = "display:none;";
            String style_maximize = "display:block;";
          //  System.out.println("headerList.getMinimize()"+headerList.getMinimize());
            if(headerList.getMinimize()!=null || !headerList.getMinimize().equals("null")){
            if(headerList.getMinimize().equals("Y")){
                 style_minimize = "display:block;";
                 style_maximize = "display:none;";
            }
            }%>
            <div id="sectionHide<%=index%>" style="<%=style_minimize%>">
            <TABLE class="blue_table" width="350px">
            <TR>
            <TH align="left"><a onclick="maximize('<%=index%>');" style="cursor:pointer">[+]</a>&nbsp;&nbsp;<%=header%></TH>
            <TH align="right">&nbsp;
            <%if(wc.getUser().isSuperAdmin()){%>                
                <a href="adminHome/editMenu?id=<%=headerList.getId()%>&name=<%=header%>">
                <font style="color:white">[edit]</font>
                </a>               
            <%}%>
            </TH>
            </TR></TABLE></div>
         
              <div id="sectionShow<%=index%>" style="<%=style_maximize%>">
                  <TABLE class="blue_table" width="350px">
            <TR>
            <TH align="left"><a onclick="minimize('<%=index%>');" style="cursor:pointer">[-]</a>&nbsp;&nbsp;<%=header%></TH>
            <TH align="right">&nbsp;
            <%if(wc.getUser().isSuperAdmin()){%>                
                <a href="adminHome/editMenu?id=<%=headerList.getId()%>&name=<%=header%>">
                <font style="color:white">[edit]</font>
                </a>               
            <%}%>
            </TH>
            </TR>
            
            <TR><TD align="left" valign="top" colspan=2>        
            <!-- added for TRT Phase 2 Major Enhancement F1(archived Page)  -->
           
                <%if( headerList.getArchiveFlag())
                { 
                %>
                <div><b><i><span>
           
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            &nbsp;
                <a href="/TrainingReports/archivedPage?id=<%=headerList.getId()%>">
                <span style="color:green;">Archived Reports</span></a> 
                </span></i></b></div>
                <%}%>
            
            <!--  end -->
            <%                                
            Vector list = (Vector)renderMenu.get(header); 
            boolean prevGroupAccess = false;
            boolean prevElementisGroup = false;
            boolean allowChild = false;
            boolean collapsible = false;
            boolean nextChild = false;
            MenuList nextMenu = null;
            String groupId = "";
            if (list != null && list.size() > 0 ) {
              //  System.out.println("Is User a Super Admin?"+wc.getUser().isSuperAdmin());
                for(int i=0;i<list.size();i++){   
                    nextChild = false;         
                    MenuList menu = (MenuList)list.elementAt(i);
                    if (list.size() > (i+1))
                    {
                        nextMenu =  (MenuList)list.elementAt(i+1);                   
                        if ((new Integer(nextMenu.getLevel())).intValue() > 2) nextChild = true;
                    }
                    
                    boolean allowrow = true;
                    if ( !Util.isEmpty(menu.getAllow())) {
                        allowrow = false;
                        if(TSR_ADMIN.equals(menu.getAllow())){
                            if ( wc.getUser().isAdmin() || wc.getUser().isTsrAdmin()) {
                                allowrow  = true;
                            }        
                        }
                        /*Adding condition for Feedback user for SCE Form enhancement */    
                        else if(ADMIN.equals(menu.getAllow())){
                            if(wc.getUser().isAdmin()|| wc.getUser().isSuperAdmin()){
                                allowrow  = true;            
                            }
                        }else if(SUPER_ADMIN.equals(menu.getAllow())){
                            if(wc.getUser().isSuperAdmin()){
                                allowrow  = true;            
                            }
                        }
                        /*else if(SPECIAL.equals(menu.getAllow())){
                            if(wc.getUser().isAdmin() 
                                || wc.getUser().isTsrAdmin() 
                          ){
                                allowrow  = true;            
                            }
                        } */
                            /* Loop added for user Specific groups */
                         else if(wc.getUser().isSuperAdmin()||wc.getUser().isAdmin())
                         {
                            allowrow=true;
                         }
                         /*Condition added for SCE Feedback form enhancement */
                         else if( wc.getUser().isAdmin() && wc.getUser().isFeedbackUser())
                         {
                           if((Util.splitFields(menu.getBusUnit(),wc.getUser().getBusinessUnit()) && Util.splitFields(menu.getSalesOrg(),wc.getUser().getSalesOrganization()) && Util.splitFields(menu.getRole(),wc.getUser().getRoleDesc())) || (Util.splitFields(menu.getFeedbackUsers(),wc.getUser().getName())))
                            {
                                allowrow = true;  
                            } 
                         }
                         /*Adding condition for Feedback user for SCE Form enhancement */                               
                         else if(!wc.getUser().isAdmin() && !wc.getUser().isTsrAdmin() && !wc.getUser().isSuperAdmin() && !wc.getUser().isFeedbackUser()){
                            if (wc.getUser().isHQUser()){
                                if((Util.splitFields(menu.getBusUnit(),wc.getUser().getBusinessUnit()) && Util.splitFields(menu.getSalesOrg(),wc.getUser().getSalesOrganization()) && Util.splitFields(menu.getRole(),wc.getUser().getRoleDesc()))||Util.splitFields(menu.getHQUsers(),wc.getUser().getName()))
                                {
                                    //System.out.println("report select 12");
                                    allowrow = true;  
                                }
                            }
                                         
                            //if(Util.splitFields(menu.getBusUnit(),wc.getUser().getBusinessUnit()) || Util.splitFields(menu.getSalesOrg(),wc.getUser().getSalesOrganization()) || Util.splitFields(menu.getRole(),wc.getUser().getRoleDesc()) || Util.splitFields(menu.getFeedbackUsers(),wc.getUser().getName()))
                            else if(Util.splitFields(menu.getBusUnit(),wc.getUser().getBusinessUnit()) && Util.splitFields(menu.getSalesOrg(),wc.getUser().getSalesOrganization()) && Util.splitFields(menu.getRole(),wc.getUser().getRoleDesc()))
                            {
                                allowrow = true;  
                            }
                         }           
                                                                 
                    } 
                    if (((new Integer(menu.getLevel())).intValue() <= 2)) prevElementisGroup = false;
                    if ((menu.getTrackId() != null) && (menu.getTrackId().startsWith("GROUP"))) 
                    {
                        prevGroupAccess = allowrow;
                        prevElementisGroup = true;
                        groupId = "group_" + menu.getId();
                    }
                    if (((new Integer(menu.getLevel())).intValue() > 2)&& (prevElementisGroup)){
                        
                       allowChild = (prevGroupAccess && allowrow);
                       collapsible = true;
                    }else
                    {
                        allowChild = allowrow;
                        collapsible = false;
                    }
                   // System.out.println("allowrow " + allowrow + " prevGroupAccess " +
                       //         prevGroupAccess + "menu.getLevel()  " + menu.getLevel() + " allowChild "  + allowChild + " id " + menu.getId());
                    if (allowChild) {
                    boolean displayLink = (menu.getUrl()!=null&&(menu.getUrl().trim().length()>1))?true:false;      
                %>      
                       
                <%boolean isaGroup = false;
                if ((menu.getTrackId() != null) && (menu.getTrackId().startsWith("GROUP"))) isaGroup = true;
                if (collapsible){%><span id="<%=groupId+"_"+menu.getId()%>" style="display:none"><%}%>
                <%=putSpace((new Integer(menu.getLevel())).intValue())%> 
                <%if(displayLink){
                    if(menu.getUrl().toLowerCase().indexOf("pfieldnet")>1)
                    {
                %><A target="_blank" href="<%=menu.getUrl()%>"><%
                } else{%><A href="<%=menu.getUrl()%>"><%}}
                    if (isaGroup){
                        if (nextChild){
                %><a style="cursor:hand;" onclick="collapseGroup(this,'<%=groupId%>');">+</a>&nbsp;<%}%><A style="font-weight:bold"><%=menu.getLabel()%></A><%}
                    else{%><%=menu.getLabel()%><%}
                    if(displayLink){%></A><%}%><BR><%
                    if (collapsible){%></span><%}   
                    }       
                }
                index = index+1; 
                //System.out.println("section index = " +  index);
        }else{
                    index = index+1;
            }
            %></TD></TR></TABLE></div><%         
            if(index==seperatorHalf){
                %>
                </TD>
                <TD><IMG src="/TrainingReports/resources/images/spacer.gif" width="15"></TD>
                <TD align="left" valign="top">
                <%                                               
            }
        }                        
       
 //   }    
        if (keysEven.hasMoreElements()){
            
       keysEven.nextElement();  
       even++;
       }
       
       if ( (index > seperatorHalf) && keysOdd.hasMoreElements()){
            keysOdd.nextElement();
            odd++;
       }
       keys.nextElement();

    }
%>
</TD></TR>
</TABLE>

</TD></TR></TABLE>

</TD></TR></TABLE>
    