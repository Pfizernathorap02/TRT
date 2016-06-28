<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.MenuList"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.utils.ReadProperties"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.components.PreviewHomePageWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ProductSelectWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.*"%>


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
	PreviewHomePageWc wc = (PreviewHomePageWc)request.getAttribute(PreviewHomePageWc.ATTRIBUTE_NAME);
    Hashtable renderMenu = wc.getMenu(); 
    
    int numberBox = wc.getMenuHeader().size();
    int seperatorHalf = (numberBox%2==0)?numberBox/2:(numberBox+1)/2;
    Enumeration keys = wc.getMenuHeader().elements();


    List sectionNamesList = wc.getSectionNameList();
 //   System.out.println("in preview home page.jsp sectionNamesList== "+sectionNamesList);
    List trackIdList = new ArrayList();
    trackIdList = wc.getTrackIdList();
    List minimizeList = wc.getMinimizeList();
   System.out.println(sectionNamesList.size()+"sizeeeeeee");
    System.out.println(wc.getMenuHeader()+"hhhhhhhhhhhhh");
        if (sectionNamesList.size() != wc.getMenuHeader().size()) 
        System.out.println("Exception here"); 
    
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
</script>

<TABLE class="basic_table" onclick="window.close();" >
<TR>
<TD rowspan="2"><IMG src="/TrainingReports/resources/images/spacer.gif" width="15"></TD>
<TD align="left">
<div id=header_title style="color:#1f61a9;">Welcome to Training Reporting Tool</div>
</TD>
 <!--added for TRT major enhancement 3.6- F6-->
<TD align ="right">
<% String res = UserSession.getUserSession(request).getIsDelegatedUser();
if (res != null) {%>
 <!-- Infosys migrated code weblogic to jboss changes start here
 <a href="/TrainingReports/switchUser.do">Switch User</a> -->
  <a href="/TrainingReports/switchUser">Switch User</a>
   <!-- Infosys migrated code weblogic to jboss changes end here -->
   <%}%></TD> 
</TR>
<TR><TD>
<!--ends here-->

<TABLE class="basic_table"><TR><TD><IMG src="/TrainingReports/resources/images/spacer.gif" width="15"></TD><TD>
<h4>Please select Report Type:</h4>

<TABLE>
<TR><TD align="left" valign="top">
<% 
    
    int index=0;
    int even=0;
    int odd=1;
    int indexElement = 0;
    ReadProperties props= new ReadProperties();
  for(int j=0;j<sectionNamesList.size();j++){
    if (index < seperatorHalf) {
        indexElement = even;
        even = even + 2;
    }else
    {
        indexElement = odd;
        odd=odd+2; 
    }
    
     keys = wc.getMenuHeader().elements();
     while(keys.hasMoreElements()){
   
         MenuList headerList = (MenuList)keys.nextElement();
  
        String header = headerList.getLabel();
        String headerLabel="";
        if(sectionNamesList!=null){
            headerLabel = sectionNamesList.get(indexElement).toString();
        }
        else{
            headerLabel = headerList.getLabel();
        }
        String id = headerList.getId();
       
       // System.out.println("CLUSTER OF THE EMPLOYEE"+wc.getUser().getDisplayCluster());
        boolean allowHeader = true; 
     if(id.equals(trackIdList.get(indexElement))){  
  //    if(trackIdList.contains(id)){  
       // System.out.println("User Name-------------"+wc.getUser().getName());   
       // System.out.println("Admin user-------------"+wc.getUser().isAdmin());   
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
           
            if(minimizeList.get(indexElement).equals("Y")){
                
                 style_minimize = "display:block;";
                 style_maximize = "display:none;";
            }  %>
            <div id="sectionHide<%=index%>" style="<%=style_minimize%>">
            <TABLE class="blue_table" width="350px">
            <TR>
            <TH align="left"><a onclick="maximize('<%=index%>');" style="cursor:pointer">[+]</a>&nbsp;&nbsp;<%=headerLabel%></TH>
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
            <TH align="left"><a onclick="minimize('<%=index%>');" style="cursor:pointer">[-]</a>&nbsp;&nbsp;<%=headerLabel%></TH>
            <TH align="right">&nbsp;
            <%if(wc.getUser().isSuperAdmin()){%>                
                <a href="adminHome/editMenu?id=<%=headerList.getId()%>&name=<%=header%>">
                <font style="color:white">[edit]</font>
                </a>               
            <%}%>
            </TH>
            </TR>
            
            <TR><TD align="left" valign="top" colspan=2> 
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
            <%                                
            Vector list = (Vector)renderMenu.get(header); 
              //  System.out.println("Is User a Super Admin?"+wc.getUser().isSuperAdmin());
            //  System.out.println(list);
            
              if (list != null && list.size() > 0 ) {
                for(int i=0;i<list.size();i++){            
                    MenuList menu = (MenuList)list.elementAt(i);      
                //    System.out.println("menu.getId()"+menu.getId());
               //     System.out.println("menu.getLabel()"+menu.getLabel());              
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
                         else if(wc.getUser().isSuperAdmin())
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
                          
                            //if(Util.splitFields(menu.getBusUnit(),wc.getUser().getBusinessUnit()) || Util.splitFields(menu.getSalesOrg(),wc.getUser().getSalesOrganization()) || Util.splitFields(menu.getRole(),wc.getUser().getRoleDesc()) || Util.splitFields(menu.getFeedbackUsers(),wc.getUser().getName()))
                            if(Util.splitFields(menu.getBusUnit(),wc.getUser().getBusinessUnit()) && Util.splitFields(menu.getSalesOrg(),wc.getUser().getSalesOrganization()) && Util.splitFields(menu.getRole(),wc.getUser().getRoleDesc()))
                            {
                                allowrow = true;  
                            }
                        }            
                                                                 
                    } 
                    if ( allowrow) {
                    boolean displayLink = (menu.getUrl()!=null&&(menu.getUrl().trim().length()!=0))?true:false;      
                %>      
                <%=putSpace((new Integer(menu.getLevel())).intValue())%>          
                <%if(displayLink){
                    if(menu.getUrl().toLowerCase().indexOf("pfieldnet")>1)
                    {
                %>
                     <A href="#">
                    <%} else{%>                    
                    <A href="#"> 
                    <%}%>                    
                    <%}%>
                    <%=menu.getLabel()%>
                <%if(displayLink){%>
                </A>
                <%}%>                
                <BR>
            <%      
                    }       
                }
                             index = index+1; 
                             //System.out.println("section index = " +  index);
        }else{
                    index = index+1;
            }
            %><BR></TD></TR></TABLE></div><%         
            if(index==seperatorHalf){
                %>
                </TD>
                <TD><IMG src="/TrainingReports/resources/images/spacer.gif" width="15"></TD>
                <TD align="left" valign="top">
                <%                                               
            }
        }                        
       
    }    
   
    } // end of if
           
  } // end of for added
 // request.getSession().removeAttribute("sectionNamesList");
 // request.getSession().removeAttribute("minimizeList");
        request.getSession().removeAttribute("sectionNamesList");
        request.getSession().removeAttribute("minimizeList");
        request.getSession().removeAttribute("trackIdList");
        request.getSession().removeAttribute("preview");
%>
</TD></TR>
</TABLE>

</TD></TR></TABLE>

</TD></TR></TABLE>
    