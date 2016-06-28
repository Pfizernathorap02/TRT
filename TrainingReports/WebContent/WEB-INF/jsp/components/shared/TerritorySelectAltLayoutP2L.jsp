<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>

<%@ page import="com.pfizer.webapp.user.TerritoryFilterForm"%>

<%@ page import="com.pfizer.webapp.user.UserTerritory"%>

<%@ page import="com.pfizer.webapp.wc.components.shared.TerritorySelectWc"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
	TerritorySelectWc wc = (TerritorySelectWc)request.getAttribute(TerritorySelectWc.ATTRIBUTE_NAME);
	List areas = wc.getUserTerritory().getAreas();
	UserTerritory ut = wc.getUserTerritory();
	TerritoryFilterForm tff = wc.getUserFilterForm();
    System.out.println("teamlist:" + tff.getSalesOrgList().size());        
    List firstDropdown = wc.getUserTerritory().getFirstDropdown();  
%>
<script type="text/javascript" language="JavaScript">
	function getNextLevel(id,level) {
        var temp=id.selectedIndex;
        var selected_text = id.options[temp].text;  
        var selected_value = id.options[temp].value;          
        var tmpSel = document.getElementById( '<%=wc.getUserFilterForm().FIELD_SALESORG%>' );
        if(tmpSel.options !=null)
        {
            var selected_sales_text = tmpSel.options[tmpSel.selectedIndex].value;
        }
        else
        {
            var selected_sales_text ='All';
        }  
		window.location="/TrainingReports/p2l/getNextLevel?<%=AppQueryStrings.FIELD_SALES%>=" +selected_text+"&<%=AppQueryStrings.FIELD_SALESVALUE%>="+selected_value+"&<%=AppQueryStrings.FIELD_SALESORG%>="+selected_sales_text+"&<%=AppQueryStrings.FIELD_SALESLEVEL%>="+level;
        
    }
</script>
	<%-- 	Commented for TRT Phase 2 Enhancement	
<div class="chartscontrol_area">
	  <table class="basic_table" border="2">
	        <form action="<%=wc.getPostUrl()%>" method="post" class="form_basic">          
            <%if(tff.getSalesOrgList().size()>2){%>  
            <tr>
                <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="10"></td><td><div class="chartscontrol_areaHdr"><h6>Sales Org:</h6></div></td>                
                <td>                
                    <select id="<%=wc.getUserFilterForm().FIELD_SALESORG%>" name="<%=wc.getUserFilterForm().FIELD_SALESORG%>">
                            <%=HtmlBuilder.getOptionsFromLabelValue(tff.getSalesOrgList(),tff.getSalesOrg())%>
                            <%System.out.println("Sales Org Filter Form value"+tff.getSalesOrg());%>
                    </select>                
                </td>                    
            </tr>
             <%}else{%>
                <input type="hidden" id="<%=wc.getUserFilterForm().FIELD_SALESORG%>" name="<%=wc.getUserFilterForm().FIELD_SALESORG%>">       
            <%}%>               
            <tr>
                    <td align="center" colspan="3"><h5><b>Sales Position Description</b></h5></td>
            </tr> 

                <tr valign="top">
	                <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="0" width="10"></td>
	                <td nowrap valign="bottom">
	                <div class="chartscontrol_areaHdr"><h6>Level 1:</h6></div>
	                </td>
	                <td valign="top">
	                <select id="<%=wc.getUserFilterForm().FIELD_LEVEL1%>" name="<%=wc.getUserFilterForm().FIELD_LEVEL1%>" onchange="return getNextLevel(this,2)"> 
                                 <%=HtmlBuilder.getOptionsFromLabelValue(tff.getFirstList(),tff.getLevel1())%>
                    </select>
                    </td>
	            </tr>

                <%if(tff.getSecondList().size()>0) {%>
                        
	            <tr valign="top">
	                <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="0" width="10"></td>
	                <td nowrap valign="bottom">
	                <div class="chartscontrol_areaHdr"><h6>Level 2:</h6></div>
	                </td>
	                <td valign="top">
	                <select id="<%=wc.getUserFilterForm().FIELD_LEVEL2%>" name="<%=wc.getUserFilterForm().FIELD_LEVEL2%>" onchange="return getNextLevel(this,3)"> 
                                <%=HtmlBuilder.getOptionsFromLabelValue(tff.getSecondList(),tff.getLevel2())%>
                    </select>
                    </td>
	            </tr>
                <%}%>
                 <%if(tff.getThirdList().size()>0) {%>
	            <tr valign="top">
	                <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="0" width="10"></td>
	                <td nowrap valign="bottom">
	                <div class="chartscontrol_areaHdr"><h6>Level 3:</h6></div>
	                </td>
	                <td valign="top">
	                <select id="<%=wc.getUserFilterForm().FIELD_LEVEL3%>" name="<%=wc.getUserFilterForm().FIELD_LEVEL3%>" onchange="return getNextLevel(this,4)"> 
                                <%=HtmlBuilder.getOptionsFromLabelValue(tff.getThirdList(),tff.getLevel3())%>
                    </select>
	                </td>
	            </tr>
                <%}%>
                 <%if(tff.getFourthList().size()>0) {%>
                <tr valign="top">
	                <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="0" width="10"></td>
	                <td nowrap valign="bottom">
	                <div class="chartscontrol_areaHdr"><h6>Level 4:</h6></div>
	                </td>
	                <td valign="top">
	                <select id="<%=wc.getUserFilterForm().FIELD_LEVEL4%>" name="<%=wc.getUserFilterForm().FIELD_LEVEL4%>" onchange="return getNextLevel(this,5)"> 
                                <%=HtmlBuilder.getOptionsFromLabelValue(tff.getFourthList(),tff.getLevel4())%>
                    </select>
	                </td>
	            </tr>  
                <%}%>
                 <%if(tff.getFifthList().size()>0) {%>
                <tr valign="top">
	                <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="0" width="10"></td>
	                <td nowrap valign="bottom">
	                <div class="chartscontrol_areaHdr"><h6>Level 5:</h6></div>
	                </td>
	                <td valign="top">
	                <select id="<%=wc.getUserFilterForm().FIELD_LEVEL5%>" name="<%=wc.getUserFilterForm().FIELD_LEVEL5%>" onchange="return getNextLevel(this,6)"> 
                                <%=HtmlBuilder.getOptionsFromLabelValue(tff.getFifthList(),tff.getLevel5())%>
                    </select>
	                </td>
	            </tr>  
                <%}%>  
                <%if(tff.getSixthList().size()>0) {%>
                <tr valign="top">
	                <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="0" width="10"></td>
	                <td nowrap valign="bottom">
	                <div class="chartscontrol_areaHdr"><h6>Level 6:</h6></div>
	                </td>
	                <td valign="top">
	                <select id="<%=wc.getUserFilterForm().FIELD_LEVEL6%>" name="<%=wc.getUserFilterForm().FIELD_LEVEL6%>" onchange="return getNextLevel(this,7)"> 
                                <%=HtmlBuilder.getOptionsFromLabelValue(tff.getSixthList(),tff.getLevel6())%>
                    </select>
	                </td>
	            </tr>  
                <%}%>
                <%if(tff.getSeventhList().size()>0) {%>
                <tr valign="top">
	                <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="0" width="10"></td>
	                <td nowrap valign="bottom">
	                <div class="chartscontrol_areaHdr"><h6>Level 7:</h6></div>
	                </td>
	                <td valign="top">
	                <select id="<%=wc.getUserFilterForm().FIELD_LEVEL7%>" name="<%=wc.getUserFilterForm().FIELD_LEVEL7%>" onchange="return getNextLevel(this,8)"> 
                                <%=HtmlBuilder.getOptionsFromLabelValue(tff.getSeventhList(),tff.getLevel7())%>
                    </select>
	                </td>
	            </tr>  
                <%}%>
                <%if(tff.getEighthList().size()>0) {%>
                <tr valign="top">
	                <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="0" width="10"></td>
	                <td nowrap valign="bottom">
	                <div class="chartscontrol_areaHdr"><h6>Level 8:</h6></div>
	                </td>
	                <td valign="top">
	                <select id="<%=wc.getUserFilterForm().FIELD_LEVEL8%>" name="<%=wc.getUserFilterForm().FIELD_LEVEL8%>" onchange="return getNextLevel(this,9)"> 
                                <%=HtmlBuilder.getOptionsFromLabelValue(tff.getEighthList(),tff.getLevel8())%>
                    </select>
	                </td>
	            </tr>  
                <%}%> 
                 <%if(tff.getNinthList().size()>0) {%>
                <tr valign="top">
	                <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="0" width="10"></td>
	                <td nowrap valign="bottom">
	                <div class="chartscontrol_areaHdr"><h6>Level 9:</h6></div>
	                </td>
	                <td valign="top">
	                <select id="<%=wc.getUserFilterForm().FIELD_LEVEL9%>" name="<%=wc.getUserFilterForm().FIELD_LEVEL9%>" onchange="return getNextLevel(this,10)"> 
                                <%=HtmlBuilder.getOptionsFromLabelValue(tff.getNinthList(),tff.getLevel9())%>
                    </select>
	                </td>
	            </tr>  
                <%}%>  
                 <%if(tff.getTenthList().size()>0) {%>
                <tr valign="top">
	                <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="0" width="10"></td>
	                <td nowrap valign="bottom">
	                <div class="chartscontrol_areaHdr"><h6>Level 10:</h6></div>
	                </td>
	                <td valign="top">
	                <select id="<%=wc.getUserFilterForm().FIELD_LEVEL10%>" name="<%=wc.getUserFilterForm().FIELD_LEVEL10%>" onchange="return getNextLevel(this,11)"> 
                                <%=HtmlBuilder.getOptionsFromLabelValue(tff.getTenthList(),tff.getLevel10())%>
                    </select>
	                </td>
	            </tr>  
                <%}%>                    
	            <tr>
	                <td colspan="2"><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="0" width="10"></td>
	                <td>
	                <input name="" type="image" src="/TrainingReports/resources/images/btn_getreport.gif" style="margin-top:20px;">
	                </td>
	            </tr>
	            <tr>
	                <td colspan="2"><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="0" width="10"></td>
	                <td> </td>
	            </tr>
	        </form>
	    </table>
	</div>--%>
	<script type="text/javascript" language="JavaScript">
		// initializes the onload function
		//document.onload = dropDownPopulate();
	</script>





