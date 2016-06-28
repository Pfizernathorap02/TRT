<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.MenuList"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.EditMenuWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ProductSelectWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.Vector"%>

<%! 
    public String putSpace(int level){
        String output = "";
        for(int i=1;i<level;i++){
            output = output+"&nbsp;&nbsp;&nbsp;&nbsp;";
        }        
        return output; 
    }
%>
<%            
	EditMenuWc wc = (EditMenuWc)request.getAttribute(EditMenuWc.ATTRIBUTE_NAME);
    Vector renderMenu = wc.getMenu();     
%>

<script type="text/javascript">
<!--
   function confirmDelete(idDel) {         
      var msg = "Do you want to continue ?";
      document.editMenu.delID.value = idDel;
      document.editMenu.command.value = "deleteMenu";
      if ( confirm(msg) ) {        
        document.editMenu.submit();
      }
    }
//-->
</script>

<TABLE class="basic_table"> 
<TR>
<TD rowspan="2"><IMG src="/TrainingReports/resources/images/spacer.gif" width="15"></TD>
<TD align="left">
<div id=header_title style="color:#1f61a9;">Training Reporting Tool</div>
</TD>
</TR>
<TR><TD>

<TABLE class="basic_table"><TR><TD><IMG src="/TrainingReports/resources/images/spacer.gif" width="15"></TD><TD>
&nbsp;

<FORM action="" method="post" name="editMenu">
            <TABLE class="blue_table" width="90%">
            <TR><TH align="left">
                <%=wc.getMenuName()%>                            
            </TH></TR>    
            
            <TR><TD><TABLE width="90%">
            
            <%                                            
            for(int i=0;i<renderMenu.size();i++){            
                MenuList menu = (MenuList)renderMenu.elementAt(i);                                                        
                String checkedActive = (menu.getActive().equalsIgnoreCase("1"))?"checked":"";
                String checkedNonActive = (menu.getActive().equalsIgnoreCase("0"))?"checked":"";
                boolean displayLink = (menu.getUrl()!=null&&(menu.getUrl().trim().length()!=0))?true:false;      
            %>
            <TR><TD align="left" valign="top" style="border:0px">                                      
            <%=putSpace((new Integer(menu.getLevel())).intValue()*2)%>          
                <%if(displayLink){%>
                    <A href="<%=menu.getUrl()%>"><%}%>
                    <%=menu.getLabel()%>
                <%if(displayLink){%>
                </A>
                <%}%>                
                </TD><TD style="border:0px">
                <%if(displayLink){%>                                                                            
                    <input type="radio" name="menuID_<%=menu.getId()%>" value="1" <%=checkedActive%> > Enable
                    <input type="radio" name="menuID_<%=menu.getId()%>" value="0" <%=checkedNonActive%> > Disable
                    <input type="hidden" name="defaultStatus_<%=i%>" value="<%=menu.getActive()%>" >                                     
                    &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:confirmDelete(<%=menu.getId()%>)">
                    [Delete]
                    </a>                                
                <%}else{%>                
                    &nbsp;                
                <%}%>                
                </TD></TR>                                                      
            <%}%>
            
            <TR><TD align="right" colspan="2" style="border:0px">    
                <input type="hidden" name="id" value="<%=wc.getRootID()%>">
                <input type="hidden" name="name" value="<%=wc.getMenuName()%>">
                <input type="hidden" name="delID" value="">
                <input type="submit" name="update" value="Update">
                <input type="reset" name="reset" value="Reset">
            </TD></TR>

            </TABLE>
             
        </TD></TR></TABLE>
                        
</FORM>
</TD></TR>
</TABLE>


</TD></TR></TABLE>
    