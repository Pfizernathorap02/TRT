<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpReport"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.db.Feedbackuserlist"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.report.ClassFilterForm"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.GeneralSessionWc"%>
<%@ page import="com.pfizer.webapp.wc.components.user.HQuserlistWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.pfizer.db.NAUserSearch"%>
<%@ page import="com.pfizer.hander.UserHandler"%>
<%@ page import="com.pfizer.service.ServiceFactory"%>
<%@ page import="com.pfizer.service.Service"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<%@ page import="com.pfizer.webapp.wc.components.user.EditGroupWc"%>
<%@ page import="com.pfizer.webapp.wc.components.user.FbuserlistWc"%>
<%@ page import="com.pfizer.db.UserGroups"%>
<%@ page import="com.pfizer.webapp.wc.components.user.HQuserlistWc"%>
<%@ page import="com.pfizer.db.HQuserlist"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>

<%
    EditGroupWc wc= (EditGroupWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);           
    //wc.setBuSalesDataMap();
    HashMap completeUserGroupMap = wc.getBuSalesDataMap();
    UserGroups ug = wc.getUserAccess();
  //  System.out.println("CompleteUserGroupMap"+completeUserGroupMap);
    System.out.print("InitialBU :"+ug.getBusUnit());
    System.out.print("InitialSA :"+ug.getSalesOrg());
    
    AppQueryStrings app=new AppQueryStrings();
    String message2= app.getMessage2();
    ServiceFactory factory = Service.getServiceFactory();
    UserHandler uh = factory.getUserHandler();
    List result = uh.getfeedBackUsers();
    List resultHQ=uh.getHQUsers();
        
    //FbuserlistWc fb = (FbuserlistWc)request.getAttribute(FbuserlistWc.ATTRIBUTE_NAME);
    FbuserlistWc fb = new FbuserlistWc(result);
        System.out.println("Results"+result.size());
        fb.getResults();   
        
    HQuserlistWc hq = new HQuserlistWc(resultHQ);
        System.out.println("HQ Results"+result.size());
        hq.getResults(); 
        
     
%>

<%!

	public String getStringForBU(HashMap completeUserGroupMap, String jsObjectName) {
		//Collections.sort(areas);		
        //SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = new StringBuffer();
		String buObjName;
		String salesObjName;
        String roleObjName;

		sb.append(addOptionString(jsObjectName," All"," All"));
		sb.append(".child = new tgixSelect('" + UserGroups.FIELD_SALESORG +"');\n") ;
		salesObjName = jsObjectName+".getOption(' All').child"; 
		sb.append( addOptionString( salesObjName, " All", " All" ));
        sb.append(".child = new tgixSelect('" + UserGroups.FIELD_ROLE +"');\n") ;
		roleObjName = salesObjName+".getOption(' All').child"; 
		sb.append( addOptionString( roleObjName, " All", " All" ) + ";\n");
		for ( Iterator it = completeUserGroupMap.keySet().iterator(); it.hasNext(); ) 
		{
			String salesTemp;
            String buTemp = (String)it.next();
			//System.out.println(buTemp);
            
            sb.append(addOptionString(jsObjectName,buTemp,buTemp));
            sb.append(".child = new tgixSelect('" + UserGroups.FIELD_SALESORG +"');\n") ;
        	salesObjName = jsObjectName+".getOption('" + buTemp + "').child"; 
    		sb.append( addOptionString( salesObjName, " All", " All" ));	
            sb.append(".child = new tgixSelect('" + UserGroups.FIELD_ROLE +"');\n") ;		
            roleObjName = salesObjName + ".getOption(' All').child";
            sb.append( addOptionString( roleObjName, " All", " All" ) + ";\n");
              
			ArrayList forEachBUTemp = (ArrayList)completeUserGroupMap.get(buTemp);
			for(int i = 0;i<forEachBUTemp.size();i++)
			{
				HashMap saleRoleMap = (HashMap)forEachBUTemp.get(i);
				for ( Iterator itx = saleRoleMap.keySet().iterator(); itx.hasNext(); ) 
				{
					salesTemp = (String)itx.next();
					//System.out.println(" "+salesTemp);
                    sb.append(addOptionString(salesObjName,salesTemp,salesTemp));
					sb.append(".child = new tgixSelect('" + UserGroups.FIELD_ROLE +"');\n") ;
					String roleObjTemp = salesObjName+".getOption('" + salesTemp + "').child"; 
					sb.append( addOptionString( roleObjTemp, " All", " All" ) + ";\n");	
					                    
					ArrayList roleList = (ArrayList)(saleRoleMap.get(salesTemp)); 
                    String role;
                    roleObjName = salesObjName+".getOption('" + salesTemp + "').child";
					for(int j = 0;j<roleList.size();j++){
                        role = (String)roleList.get(j);
                        sb.append( addOptionString( roleObjName, role, role )  + ";\n");
						//System.out.println("  "+role);
					}		
				}
			}	
		}
		return sb.toString();
	}

	public String addOptionString(String jsObjName,String name,String value) {
		StringBuffer sb = new StringBuffer();
		sb.append(jsObjName + ".addOption('" + name + "','" + value + "')");
		return sb.toString();
	}

%>
<script src="/TrainingReports/resources/js/tgixDynamicSelect.js"></script>
<script language="JavaScript1.2">
var dynamicSelect;
dynamicSelect = new tgixSelect('<%=UserGroups.FIELD_BUSUNIT%>');
<%=getStringForBU(completeUserGroupMap,"dynamicSelect")%>

function dropDownPopulate() {
	populateTgixSelect(dynamicSelect);
    
   <% if ( ug.getSalesOrgsList().size() > 0 ) { %>
    <% for (int i = 0; i < ug.getSalesOrgsList().size(); i++) {%>
        var tmpSel = document.getElementById( '<%=UserGroups.FIELD_SALESORG%>' );
        addOption(tmpSel,"<%=ug.getSalesOrgsList().get(i).toString()%>","<%=ug.getSalesOrgsList().get(i).toString()%>");
    <% } %>
   <% } %>
    
   <% if ( ug.getRolesList().size() > 0 ) { %>
    <% for (int i = 0; i < ug.getRolesList().size(); i++) {%>
        var tmpSel = document.getElementById( '<%=UserGroups.FIELD_ROLE%>' );
        
		 <% if(ug.getRolesList().get(i)!= null) {%>
        
        addOption(tmpSel,"<%=ug.getRolesList().get(i).toString()%>","<%=ug.getRolesList().get(i).toString()%>");
        
        <%} %>
        
    <% } %>
   <% } %>

	<% if ( !Util.isEmpty( ug.getBusUnit() ) ) { %>
		var tmpSel = document.getElementById( '<%=UserGroups.FIELD_BUSUNIT%>' );
		if (tmpSel != null) {
			var opTemp; 
			for( var x=0; x< tmpSel.options.length; x++ ) {
				if ( tmpSel.options[x].value == '<%=ug.getBusUnit()%>' ) {
					tmpSel.options[x].selected = true;               
					updateTgixSelect(tmpSel,dynamicSelect)
				} 
			}
		}
	<% } %>


	<% if ( !Util.isEmpty( ug.getSalesOrg() ) ) { %>
		var tmpSel = document.getElementById( '<%=UserGroups.FIELD_SALESORG%>' );
		if (tmpSel != null) {
			var opTemp;
			for( var x=0; x< tmpSel.options.length; x++ ) {
				if ( tmpSel.options[x].value == '<%=ug.getSalesOrg()%>' ) {
					tmpSel.options[x].selected = true;
					updateTgixSelect(tmpSel,dynamicSelect)
				} 
			}
		}
	<% } %>
    
     <% if ( !Util.isEmpty( ug.getRole() ) ) { %>
		var tmpSel = document.getElementById( '<%=UserGroups.FIELD_ROLE%>' );
		if (tmpSel != null) {
			var opTemp;
			for( var x=0; x< tmpSel.options.length; x++ ) {
				if ( tmpSel.options[x].value == '<%=ug.getRole()%>' ) {
					tmpSel.options[x].selected = true;          
					updateTgixSelect(tmpSel,dynamicSelect)
				} 
			}
		}
	<% } %>   
}

  function fnPopulateNextDD(lstbxFrom)
  {
        var varFromBox = document.getElementById(lstbxFrom);
        if(varFromBox.name == '<%=UserGroups.FIELD_BUSUNIT%>')
            updateSalesOrg(varFromBox,dynamicSelect);
        if(varFromBox.name == '<%=UserGroups.FIELD_SALESORG%>') {
            updateRole(varFromBox,dynamicSelect);
        }     
        if(varFromBox.name == '<%=UserGroups.FIELD_ROLE%>') {
            populateRoleTextBox(varFromBox);
        }
        if(varFromBox.name == '<%=UserGroups.FIELD_FBU_SELECTED%>') {
            populateFBuserTextBox(varFromBox);
        }     
  }

function goToGroupList() {
		window.location="/TrainingReports/admin/group";
	}
    
    function moveright(NotSelected,sel_columns) {
                var checkIfAnySelected=false;
                var SI=0;
               
                var tmpNSelect = document.getElementById(NotSelected);
                var tmpSelect  = document.getElementById(sel_columns);
                for(SI=0; SI<tmpNSelect.length;) {
                
                
                    if(tmpNSelect.options[SI].selected == true) {                    
                        checkIfAnySelected=true;
                        var mcvalue = tmpNSelect.options[SI].text;
                        TL = tmpSelect.length++;
                        tmpSelect.options[TL].text = mcvalue;
                        tmpSelect.options[TL].id=tmpNSelect.options[SI].id;
                        tmpSelect.options[TL].value=tmpNSelect.options[SI].value;
                        tmpNSelect.remove(SI);
                        SI=0;                   
                    }else {
                        SI++;
                    }
                } 
                       
                if (!checkIfAnySelected) {
                    alert("Please select column to add");
                }              
            }
            
            function moveleft(NotSelected,sel_columns) {
                var checkIfAnySelected=false;
                var tmpNSelect = document.getElementById(NotSelected);
                var tmpSelect  = document.getElementById(sel_columns);
                
                for(SI=0; SI<tmpSelect.length;) {                
                    if(tmpSelect.options[SI].selected) {                    
                        checkIfAnySelected=true;     
                        var mcvalue = tmpSelect.options[SI].text;
                        TL = tmpNSelect.length++;
                        tmpNSelect.options[TL].text = mcvalue;
                        tmpNSelect.options[TL].id=tmpSelect.options[SI].id;
                        tmpNSelect.options[TL].value=tmpSelect.options[SI].value;
                        tmpSelect.remove(SI); 
                        SI=0;
                    } else {
                        SI++;
                    }
                }            
                if (!checkIfAnySelected) {
                    alert("Please select column to remove");       
                }     
            }
            
            function validate(element,sel_columns){   
                   var tmpSelect  = document.getElementById(sel_columns); 
                   var populateUTextbox = document.getElementById(element);
                   var selectedU=null;
                TL = tmpSelect.length; 
                
                if(TL < 1) {
                    alert("Please select at least one column to show");    
                    return false;
                }               
                
                for (var u = 0; u <TL; u++) {
                    tmpSelect.options[u].selected=true;
                    if(u==0)
                    {
                    selectedU=tmpSelect.options[u].value;
                    }
                    else
                    {
                    selectedU= selectedU+';'+tmpSelect.options[u].value;
                    }
                  }                
                  
                  populateUTextbox.value=selectedU;
                      
               }
</script>
<STYLE type="text/css"> input.text { width: 300px; background-color: #FFFFFF; } </STYLE>
<html>
    <head>
        <title> Add/Edit User Group </title>
    </head>
    <body>
        <div style="margin-left:10px;margin-right:10px">
            <h3 style="MARGIN-TOP:15PX"> Add/Edit User Group </h3>
            <table class="basic_table" style="margin-left:10px;margin-right:10px">
                <form class="form_basic" action="/TrainingReports/admin/savegroup.action" method="post">
                    <input type="hidden" name="<%=UserGroups.FIELD_ID%>" value="<%=ug.getGroupId()%>">
                    <tr>
                        <td></td>
                        <td width="30"></td>
                        <td>
        <%
                if (message2 != null && !"".equals(message2.trim())) {
                %>
                <%=message2%>
                <%
                }
        %>
                        </td>
                    </tr>
                    <tr>
                        <td align="right"><label>Group Name:</label></td>
                        <td width="30"></td>
                        <td><input type="text" class="text" size="30" value="<%=Util.toEmpty(ug.getGroupName())%>" name="<%=UserGroups.FIELD_GROUPNAME%>"></td>
                    </tr>
                    <tr></tr>
                    <tr></tr>
                    <tr></tr>
                    <tr>
                        <td align="right"><label>Business Unit:</label></td>
                        <td width="30"></td>
                        <td>
                        <select STYLE="Width:200" id="<%=wc.getUserAccess().FIELD_BUSUNIT%>" name="<%=wc.getUserAccess().FIELD_BUSUNIT%>" multiple> </select>
                        <input type = "button" id = "selectBU" value = "Select" onclick = "fnPopulateNextDD('<%=wc.getUserAccess().FIELD_BUSUNIT%>')">
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>
                        <input class="text" type="text" name="<%=wc.getUserAccess().FIELD_BUSUNIT_SELECTED%>" id="<%=wc.getUserAccess().FIELD_BUSUNIT_SELECTED%>" value="<%=Util.toAll(ug.getSelectedBU())%>">
                        </td>
                    </tr>
                    <tr></tr>
                    <tr></tr>
                    <tr></tr>
                    <tr width="40">
                        <td align="right"><label>Sales Organization:</label></td>
                        <td width="30"></td>
                        <td>
                        <select STYLE="Width:200" id="<%=wc.getUserAccess().FIELD_SALESORG%>" name="<%=wc.getUserAccess().FIELD_SALESORG%>" multiple> </select>
                        <input type = "button" id = "selectSalesOrg" value = "Select" onclick = "fnPopulateNextDD('<%=wc.getUserAccess().FIELD_SALESORG%>')">
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>
                        <input class="text" type="text" name="<%=wc.getUserAccess().FIELD_SALESORG_SELECTED%>" id="<%=wc.getUserAccess().FIELD_SALESORG_SELECTED%>" value="<%=Util.toAll(ug.getSelectedSalesorg())%>">
                        </td>
                    </tr>
                    <tr></tr>
                    <tr></tr>
                    <tr></tr>
                    <tr>
                        <td align="right"><label>Role:</label></td>
                        <td width="30"></td>
                        <td>
                        <select STYLE="Width:200" id="<%=wc.getUserAccess().FIELD_ROLE%>" name="<%=wc.getUserAccess().FIELD_ROLE%>" multiple> </select>
                        <input type = "button" id = "selectRole" value="Select" onclick = "fnPopulateNextDD('<%=wc.getUserAccess().FIELD_ROLE%>')">
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>
                        <input class="text" type="text" name="<%=wc.getUserAccess().FIELD_ROLE_SELECTED%>" id="<%=wc.getUserAccess().FIELD_ROLE_SELECTED%>" value="<%=Util.toAll(ug.getSelectedRole())%>">
                        </td>
                    </tr>
                    <tr></tr>
                    <tr></tr>
                    <tr></tr>
                    <tr>
                        <td align="right"><label>Feedback Users:</label></td>
                        <td></td>
                        <td>
                        <!-- Added from here-->
                        <TABLE class="basic_table" >
                            <TR>
                                <TD WIDTH="45%" align="left">
                                <label>Available Feedback Users</label></TD>
                                <TD WIDTH="10%" align="left" valign="middle" height="30">
                                &nbsp; </TD>
                                <TD WIDTH="45%" align="left">
                                <label>Selected Feedback Users</label>
                                </TD>
                            </TR>
                            <TR>
                                <TD WIDTH="45%" align="left">
                                <select multiple size="10" STYLE="Width:200" name="NotSelectedFB" id ="NotSelectedFB">
                                             <%
                                            boolean oddEvenFlag = false;
                                            
				for (Iterator it = fb.getResults().iterator(); it.hasNext();) {
					Feedbackuserlist fblist = (Feedbackuserlist)it.next();
					oddEvenFlag = !oddEvenFlag; 
                    //String Name = fblist.getLName()+","+fblist.getFName();
                    String Name = fblist.getFName()+" "+fblist.getLName();
                    if (Name != null)
                    {                  
                     %>
                                    <option value="<%=Name%>">
                                        <%=Name%>
                                    </option>
                                                <%}
                                                else
                                                {
                                                    System.out.println("No Feedback user");
                                                }
                }%>
                                </select>
                                </TD>
                                <TD WIDTH="10%" align="center" valign="middle">
                                <img src="/TrainingReports/resources/images/btn_fwd.gif" alt="Move Right" id="ToRight" STYLE = "cursor: hand;" onclick="javascript:moveright('NotSelectedFB','sel_columnsFB');"/>
                                <br/>
                                <br/>
                                <img src="/TrainingReports/resources/images/btn_Rev.gif" alt="Move Left" id="ToLeft" STYLE = "cursor: hand;" onclick="javascript:moveleft('NotSelectedFB','sel_columnsFB');"/>
                                <br/>
                                <br/>
                                <input type = "button" id = "selectFBuser" value="Select" onclick = "validate('selectedFBU','sel_columnsFB')">
                                </TD>
                                <TD WIDTH="45%" align="center">
                                <select multiple size="10" STYLE="Width:200" name="sel_columnsFB" id="sel_columnsFB">
                                             <%
                                            String selectedUsers = ug.getSelectedFBU();
                                            if(selectedUsers !=null && !selectedUsers.equalsIgnoreCase("None"))
                                            {
                                            String str = selectedUsers;
                                                String[] users = str.split (";");
                                                for (int i=0; i < users.length; i++){
                                                  System.out.println (users[i]);
                                                 %>
                                    <option value="<%=users[i].toUpperCase()%>">
                                        <%=users[i].toUpperCase()%>
                                    </option>
                                                <%
                                             }
                                            }
                                            %>
                                </select>
                                </TD>
                            </TR>
                            <TR>
                                <TD>
                                <input class="text" type="text" name="<%=wc.getUserAccess().FIELD_FBU_SELECTED%>" id="<%=wc.getUserAccess().FIELD_FBU_SELECTED%>" value="<%=Util.toAll(ug.getSelectedFBU())%>">
                                </TD>
                            </TR>
                        </TABLE>
                        <!--  End here-->
                        </tr>
                        <tr>
                        <td></td>
                        <td></td>
                        <td> </td>
                    </tr>
                    <tr></tr>
                    <tr></tr>
                    <tr></tr>
                    <tr></tr>
                    <tr>
                        <td align="right"><label>HQ Users:</label></td>
                        <td></td>
                        <td>
                        <!-- Added from here-->
                        <TABLE class="basic_table" >
                            <TR>
                                <TD WIDTH="45%" align="left">
                                <label>Available HQ Users</label></TD>
                                <TD WIDTH="10%" align="left" valign="middle" height="30">
                                &nbsp; </TD>
                                <TD WIDTH="45%" align="left">
                                <label>Selected HQ Users</label>
                                </TD>
                            </TR>
                            <TR>
                                <TD WIDTH="45%" align="left">
                                <select multiple size="10" STYLE="Width:200" name="NotSelectedHQ" id ="NotSelectedHQ">
                                             <%
                                            boolean oddEvenFlagHQ = false;
                                            
				for (Iterator it = hq.getResults().iterator(); it.hasNext();) {
					HQuserlist hqlist = (HQuserlist)it.next();
					oddEvenFlagHQ = !oddEvenFlagHQ; 
                    //String Name = fblist.getLName()+","+fblist.getFName();
                    String Name = hqlist.getFName()+" "+hqlist.getLName();
                    if (Name != null)
                    {                  
                     %>
                                    <option value="<%=Name%>">
                                        <%=Name%>
                                    </option>
                                                <%}
                                                else
                                                {
                                                    System.out.println("No HQ user");
                                                }
                }%>
                                </select>
                                </TD>
                                <TD WIDTH="10%" align="center" valign="middle">
                                <img src="/TrainingReports/resources/images/btn_fwd.gif" alt="Move Right" id="ToRight" STYLE = "cursor: hand;" onclick="javascript:moveright('NotSelectedHQ','sel_columnsHQ');"/>
                                <br/>
                                <br/>
                                <img src="/TrainingReports/resources/images/btn_Rev.gif" alt="Move Left" id="ToLeft" STYLE = "cursor: hand;" onclick="javascript:moveleft('NotSelectedHQ','sel_columnsHQ');"/>
                                <br/>
                                <br/>
                                <input type = "button" id = "selectHQuser" value="Select" onclick = "validate('selectedHQU','sel_columnsHQ')">
                                </TD>
                                <TD WIDTH="45%" align="center">
                                <select multiple size="10" STYLE="Width:200" name="sel_columnsHQ" id="sel_columnsHQ">
                                             <%
                                            String selectedUsersHQ = ug.getSelectedHQU();
                                            if(selectedUsersHQ !=null && !selectedUsersHQ.equalsIgnoreCase("None"))
                                            {
                                            String str = selectedUsersHQ;
                                                String[] users = str.split (";");
                                                for (int i=0; i < users.length; i++){
                                                  System.out.println (users[i]);
                                                 %>
                                    <option value="<%=users[i].toUpperCase()%>">
                                        <%=users[i].toUpperCase()%>
                                    </option>
                                                <%
                                             }
                                            }
                                            %>
                                </select>
                                </TD>
                            </TR>
                            <TR>
                                <TD>
                                <input class="text" type="text" name="<%=wc.getUserAccess().FIELD_HQU_SELECTED%>" id="<%=wc.getUserAccess().FIELD_HQU_SELECTED%>" value="<%=Util.toAll(ug.getSelectedHQU())%>">
                                </TD>
                            </TR>
                        </TABLE>
                        </tr>
                        <!--  End here-->
                        <tr>
                        <td></td>
                        <td></td>
                        <td> </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>
                        <input type="submit" value=" Save ">
                        <input type="reset" value=" Reset " onclick="dropDownPopulate();">
                        <input type="button" value=" Cancel " onclick="goToGroupList();">
                        </td>
                    </tr>
                    <tr>
                        <td height="20"></td>
                    </tr>
                </form>
            </table>
        </div>
    </body>
</html>
<script type="text/javascript" language="JavaScript">

	// initializes the onload function

	document.onload = dropDownPopulate();

</script>