<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.hander.MoveCopyHandler"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<html>
<%
//System.out.println("request.getParameter(id)" + request.getParameter("id"));

String id = "";

if (request.getParameter("id")!= null 
                && request.getParameter("id").toString().trim().length() > 0)
{
    id=request.getParameter("id").toString();
}

MoveCopyHandler mcHandler = new MoveCopyHandler();

List sectionList = mcHandler.getSectionList();
List groupList = mcHandler.getSectionGroupList();
List sectionGroupList = mcHandler.getSectionGroupList();
boolean group = false;

//System.out.println("sectionList" + sectionList.size());
//System.out.println("groupList" + groupList.size());
System.out.println("sectionGroupList" + sectionGroupList.size());

if (request.getParameter("group")!= null 
                && request.getParameter("group").toString().trim().length() > 0)
{
    //System.out.println("is a group");
    group = true;
}

%>

<script type="text/javascript">
function display()
{
  var sectionSelObj = document.getElementById('section');
  for (i = 0; i < sectionSelObj.options.length; i++) {
   if (document.getElementById("section"+sectionSelObj.options[i].value) != null)
        document.getElementById("section"+sectionSelObj.options[i].value).style.display='none';
    if (sectionSelObj.options[i].selected) {
        var selectedSectionValue = sectionSelObj.options[i].value;
        // alert("section"+selectedValue);
        if (document.getElementById("section"+selectedSectionValue) != null)
        document.getElementById("section"+selectedSectionValue).style.display='block';
    }
  }
}

function copy(id)
{
 var parentId = getParentId();
 //alert (id + " copy to "+parentId);
 document.getElementById("parentId").value=parentId;
  document.getElementById("action").value="copy";
  window.opener.copyMove(id,parentId,"copy");
 window.close();
}

function move(id)
{
 var parentId = getParentId();
// alert (id + " move to "+parentId);
 document.getElementById("parentId").value=parentId;
  document.getElementById("action").value="move";
   window.opener.copyMove(id,parentId,"move");
   window.close();
}

function getParentId()
{
    var sectionSelObj = document.getElementById('section');
    for (i = 0; i < sectionSelObj.options.length; i++) 
    {
        if (sectionSelObj.options[i].selected) 
        {
            var selectedSectionValue = sectionSelObj.options[i].value;
            // alert("section"+selectedValue);
           // alert("section selected " + selectedSectionValue);
            var groupSelObj = document.getElementById(selectedSectionValue);
            if (groupSelObj != null)
            {
               // alert("group " + selectedSectionValue);
                for (j = 0; j < groupSelObj.options.length; j++) 
                {
                    if (groupSelObj.options[j].selected) {
                        var selectedGroupValue = groupSelObj.options[j].value;
                        if (selectedGroupValue != 'none'){
                           // alert("final selected" + selectedGroupValue);
                            return selectedGroupValue;
                        }
                    }
                }
            }
            //alert("final selected " + selectedSectionValue);
            return selectedSectionValue;
        }
    }
}

</script>

<head>

<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/header.css"/>
<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/trainning.css"/>

<title>
        TRT - Copy/Move
    </title>
</head>
<body bgcolor="white">
<% if (sectionList != null && sectionList.size() > 0 && 
        groupList != null && groupList.size() > 0 && id.length()>0) {%>
    <table class="blue_table" bgcolor="white" width="600px">
<% String trackName = "";
if (request.getParameter("trackName")!= null 
                && request.getParameter("trackName").toString().trim().length() > 0)
                {
                    trackName = "'" + request.getParameter("trackName").toString() + "' to";
                }%>    
     <tr><th colspan="2" align="left">Copy/Move <%=trackName%></th></tr>
    <tr><td>Select Section</td>
        <td>
        <select name="section" id="section" onchange="display();" style="width:180px">
            <% for (int i=0;i<sectionList.size();i++) {
                 HashMap sectionDataMap=(HashMap)sectionList.get(i);
                  String sectionId = sectionDataMap.get("SECTION_ID").toString();
                  String sectionLabel = sectionDataMap.get("SECTION_LABEL").toString();
                %>
             <option value="<%=sectionId%>" <%if (i==0){%>selected <%}%>><%=sectionLabel%></option>
             <%}%>
        
        </select>
        
        </td></tr><%if (!group){%>
        <tr><td>Select Group</td>
        <td>
               
            <% String currentSectionId = "";
            HashMap sectionDataMap=(HashMap)sectionList.get(0);
                  String sectionId = sectionDataMap.get("SECTION_ID").toString();
            for (int i=0;i<groupList.size();i++) {
                HashMap groupDataMap=(HashMap)groupList.get(i);
                  String groupId = groupDataMap.get("GROUP_ID").toString();
                  String groupLabel = groupDataMap.get("GROUP_LABEL").toString();
                    
                  System.out.println(groupId + " " + groupLabel + groupDataMap.get("SECTION_ID").toString());
                  if (!currentSectionId.equals(groupDataMap.get("SECTION_ID").toString()))
                  {
                    if (currentSectionId.trim().length() != 0) {%> </select></div><%}
                    boolean firstSection = false;
                    currentSectionId = groupDataMap.get("SECTION_ID").toString();
                    System.out.println("currentSectionId " + currentSectionId);
                    System.out.println("sectionId " + sectionId);
                    if (sectionId.equals(currentSectionId)) firstSection = true;
                  %><div id="section<%=currentSectionId%>" <%if (firstSection){%>style="display:block"<%}else{%>style="display:none"<%}%>> 
                    <select name="<%=currentSectionId%>" id="<%=currentSectionId%>" style="width:180px">
                     <option value="none" selected>      -- None --    </option>
                  <%}%>
             <option value="<%=groupId%>"><%=groupLabel%></option>
             
             <%}%> </select></div>
       
        
        </td></tr><%}%>
        <tr><td colspan="2"><input type=button value="Copy" onclick="copy('<%=id%>');">&nbsp;&nbsp;<input type=button value="Move" onclick="move('<%=id%>');">&nbsp;&nbsp;<input type=button value="Cancel" onclick="window.close();"></td></tr>
    </table>
    <input name="id" type=hidden value="<%=id%>">
    <input id ="parentId" name="parentId" type=hidden value="">
    <input name="action" type=hidden value="">
    <% }else {%>
    No section/group to move to.
<%}%>
</body>
</html>