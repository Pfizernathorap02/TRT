<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.MenuList"%>
<%@ page import="com.pfizer.db.P2lTrack"%>
<%@ page import="com.pfizer.db.P2lTrackPhase"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.EditArchiveWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ProductSelectWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%            
	EditArchiveWc wc = (EditArchiveWc)request.getAttribute(EditArchiveWc.ATTRIBUTE_NAME);
    Vector renderMenu = wc.getMenu();     
    boolean groupAbove=false;
%>
<%! 
    public String putSpace(int level){
        String output = "";
        for(int i=1;i<level;i++){
            output = output+"&nbsp;&nbsp;&nbsp;&nbsp;";
        }        
        return output; 
    }
%>
<script type="text/javascript">


function getDetails(reportType,trackId,trackName,menuId){
window.name = "main";
    var fldEditted
    var selObjName = 'editType_'+menuId;
    var i;
    var selObj = document.getElementById(selObjName);
    if (reportType=="REGULAR"){
        for (i = 0; i < selObj.options.length; i++) {
               if (selObj.options[i].selected) {
				var selectedValue = selObj.options[i].value;
				if (selectedValue=="Delete"){
                    confirmDelete(menuId,trackId,   trackName) ;
                    return;
                } else if (selectedValue=="Restore"){
                    document.editArchive.command.value = "restore";
                    document.editArchive.trackID.value = menuId;
                    document.editArchive.submit();
                    return;
                }                
            }
        }
    }else if (reportType=="GROUP"){
        for (i = 0; i < selObj.options.length; i++) {
               if (selObj.options[i].selected) {
				var selectedValue = selObj.options[i].value;
				if (selectedValue=="Delete"){
                    confirmDelete(menuId,trackId, trackName) ;
                    return;
                } else if (selectedValue=="Restore"){
                    document.editArchive.command.value = "restore";
                    document.editArchive.trackID.value = menuId;
                    document.editArchive.submit();
                    return;
                }
            }
        }
    }
    else if (reportType=="LAUNCH"){
        for (i = 0; i < selObj.options.length; i++) {
            if (selObj.options[i].selected) {
				var selectedValue = selObj.options[i].value;
				if (selectedValue=="Delete"){
                    confirmDelete(menuId,trackId,trackName) ;
                    return;
				} else if (selectedValue=="Restore"){
                    document.editArchive.command.value = "restore";
                    document.editArchive.trackID.value = menuId;
                    document.editArchive.submit();
                    return;
                }
			}
        }
    }
    else if (reportType=="FORECAST"){
        for (i = 0; i < selObj.options.length; i++) {
               if (selObj.options[i].selected) {
				var selectedValue = selObj.options[i].value;
				if (selectedValue=="Delete"){
                    confirmDelete(menuId,trackId,trackName);
                    return;
				} else if (selectedValue=="Restore"){
                    document.editArchive.command.value = "restore";
                    document.editArchive.trackID.value = menuId;
                    document.editArchive.submit();
                    return;
                }
			}
        }
    } else if (reportType == "MANAGEMENT"){
        for (i = 0; i < selObj.options.length; i++) {
               if (selObj.options[i].selected) {
				var selectedValue = selObj.options[i].value;
				if (selectedValue=="Delete"){
                    confirmDelete(menuId,trackId,trackName);
                    return;
				} else if (selectedValue=="Restore"){
                    document.editArchive.command.value = "restore";
                    document.editArchive.trackID.value = menuId;
                    document.editArchive.submit();
                    return;
                }
			}
        }
    } else if (reportType == "GAP"){
            for (i = 0; i < selObj.options.length; i++) {
               if (selObj.options[i].selected) {
				var selectedValue = selObj.options[i].value;
			    if (selectedValue=="Delete"){
                    confirmDelete(menuId,trackId,trackName);
                    return;
				} else if (selectedValue=="Restore"){
                    document.editArchive.command.value = "restore";
                    document.editArchive.trackID.value = menuId;
                    document.editArchive.submit();
                    return;
                }
			}
        }
    }
    document.editArchive.trackID.value = trackId;
    document.editArchive.trackName.value = trackName;
    document.editArchive.submit();
}

function moveUp(rowIndex)
{
    var row = new Number(rowIndex);
 var sorts = document.getElementsByName("sort");
   var isAGroup = document.getElementsByName("isagroup").item(row).value;
   var numberOfGroupElements = 0;
    if (isAGroup == 'true')
    {
        numberOfGroupElements = 1;
        for (i=(row+1);i<sorts.length;i++)
        {
             if (document.getElementsByName("inagroup").item(i).value == 'true')
            {
               numberOfGroupElements++;
            }else
            {
                break;
            } 
        }
        for (j=0;j<numberOfGroupElements;j++)
        {
            moveElementUp(row,false);
            row++;
        }
    }else if (document.getElementsByName("isagroup").item(row-1).value == "true")
    {
        moveDown(row-1);
    }else   moveElementUp(row,false);
    //groupingButtons();
    setDisplay();
}

function moveDown(rowIndex)
{
   var row = new Number(rowIndex);
   var sorts = document.getElementsByName("sort");
   var isAGroup = document.getElementsByName("isagroup").item(row).value;
   var inAGroup = document.getElementsByName("inagroup").item(row).value;
   var numberOfGroupElements = 0;
   var disableDown = "false";
    if (isAGroup == "true")
    {
        numberOfGroupElements = 1;
        for (i=(row+1);i<sorts.length;i++)
        {
             if (document.getElementsByName("inagroup").item(i).value == 'true')
            {
               numberOfGroupElements++;
            }else
            {
                break;
            } 
        }
        row = row + numberOfGroupElements - 1;
        if (row == sorts.length) disableDown = 'true';
        for (j=numberOfGroupElements;j>0;j--)
        {
            moveElementDown(row,false);
            row--;
        }
        if (disableDown == 'true') 
        {
            document.getElementsByName("enabledown").item(row).value = 'false';
        }
    }else if (inAGroup == true){
     moveElementDown(row,false);
    }else  if (document.getElementsByName("isagroup").item(row+1).value == "true")
    {
        moveUp(row+1);
    }else  moveElementDown(row,false);
    //groupingButtons();
    setDisplay();

}


function moveElementUp(row,updateGrouping)
{
    var prevRow = row-1;
    var x= document.getElementById("name_"+row).innerHTML;
    var y= document.getElementById("name_"+prevRow).innerHTML;
    document.getElementById("name_"+row).innerHTML = y;
    document.getElementById("name_"+prevRow).innerHTML = x;
    x= document.getElementById("access_"+row).innerHTML;
    y= document.getElementById("access_"+prevRow).innerHTML;
    document.getElementById("access_"+row).innerHTML = y;
    document.getElementById("access_"+prevRow).innerHTML = x;
    x= document.getElementById("action_"+row).innerHTML;
    y= document.getElementById("action_"+prevRow).innerHTML;
    document.getElementById("action_"+row).innerHTML = y;
    document.getElementById("action_"+prevRow).innerHTML = x;
    var sorts = document.getElementsByName("sort");
    var sortx = sorts.item(row).value;
    var sorty = sorts.item(prevRow).value;
    document.getElementsByName("sort").item(prevRow).value=sortx;
    document.getElementsByName("sort").item(row).value=sorty;
    //if (updateGrouping == true) groupingButtons();
   // setDisplay();
}

function moveElementDown(row,updateGrouping)
{
    var nextRow = row+1;
    var x= document.getElementById("name_"+row).innerHTML;
    var y= document.getElementById("name_"+nextRow).innerHTML;
    document.getElementById("name_"+row).innerHTML = y;
    document.getElementById("name_"+nextRow).innerHTML = x;
    x= document.getElementById("access_"+row).innerHTML;
    y= document.getElementById("access_"+nextRow).innerHTML;
    document.getElementById("access_"+row).innerHTML = y;
    document.getElementById("access_"+nextRow).innerHTML = x;
    x= document.getElementById("action_"+row).innerHTML;
    y= document.getElementById("action_"+nextRow).innerHTML;
    document.getElementById("action_"+row).innerHTML = y;
    document.getElementById("action_"+nextRow).innerHTML = x;
    var sorts = document.getElementsByName("sort");
    var sortx = sorts.item(row).value;
    var sorty = sorts.item(nextRow).value;
    document.getElementsByName("sort").item(nextRow).value=sortx;
    document.getElementsByName("sort").item(row).value=sorty;
   // groupingButtons();
   // if (updateGrouping == true) groupingButtons();
  // setDisplay();
}


function groupingButtons()
{
    var sorts = document.getElementsByName("sort");
    var enableups = document.getElementsByName("enableup");
    var enabledowns = document.getElementsByName("enabledown");
    var groups = document.getElementsByName("isagroup");
    var inagroups = document.getElementsByName("inagroup");
    var belowgroups = document.getElementsByName("belowagroup");
    groupabove = false;
    var data = "";
    for(i=1;i<sorts.length;i++)
    {
           // setGroupParameters(i);
            enableIntoGroup(i);
            enableOutOfGroup(i);
            enableUp(i);
            enableDown(i);
            data = data + "\n" + i + " ";
            data = data + " enableup " + enableups.item(i).value;
            data = data + " enabledown " + enabledowns.item(i).value;
             data = data + " inagroups " + inagroups.item(i).value;
    }
  //  alert (data);
}

function setGroupParameters(index)
{
    if (document.getElementsByName("isagroup").item(index).value == 'true')
    {
        document.getElementsByName("inagroup").item(index).value =  "false";
        document.getElementsByName("belowagroup").item(index).value =  "false";
    }else if (document.getElementsByName("isagroup").item(index-1).value == 'true' ||
         document.getElementsByName("inagroup").item(index-1).value == 'true' ||
         document.getElementsByName("inagroup").item(index).value == 'true')
    {
        if (document.getElementsByName("inagroup").item(index).value == 'false')
        {
            document.getElementsByName("inagroup").item(index).value = "false";
            document.getElementsByName("belowagroup").item(index).value =  "true";                    
        } else
        {
            document.getElementsByName("inagroup").item(index).value="true";
            document.getElementsByName("belowagroup").item(index).value="false";
            if(document.getElementsByName("firstchild").item(index).value=="true")
            {
                 document.getElementsByName("enableup").item(index).value = 'false';
                 document.getElementsByName("enabledown").item(index).value = 'true';
                 enableUp(index);
                 enableDown(index);
             }else if(document.getElementsByName("lastchild").item(index).value=="true")
            {
                document.getElementsByName("enableup").item(index).value = 'true';
                document.getElementsByName("enabledown").item(index).value = 'false';
                enableUp(index);
                enableDown(index);
            }else 
            {
                document.getElementsByName("enableup").item(index).value = 'true';
                document.getElementsByName("enabledown").item(index).value = 'true';
                enableUp(index);
                enableDown(index);
            }    
        }
    } else
    {
        document.getElementsByName("inagroup").item(index).value =  "false";
        document.getElementsByName("belowagroup").item(index).value =  "false";
    }
}

function setDisplay()
{
    var sorts = document.getElementsByName("sort");
    var enableups = document.getElementsByName("enableup");
    var enabledowns = document.getElementsByName("enabledown");
    var isagroups = document.getElementsByName("isagroup");
    var inagroups = document.getElementsByName("inagroup");
    var belowgroups = document.getElementsByName("belowagroup");
    var data=" ";
    for (i=0;i<sorts.length;i++)
    {
        var numberOfGroupElements = 0;
        data = data + "\n";
        if (i==0)
        {
            if (sorts.length>0)  document.getElementsByName("enabledown").item(i).value="true";
            else document.getElementsByName("enabledown").item(i).value="false";          
            document.getElementsByName("enableup").item(i).value="false";
            document.getElementsByName("inagroup").item(i).value="false";
            document.getElementsByName("belowagroup").item(i).value="false";
        }else if (isagroups.item(i).value == "true")
        {
            document.getElementsByName("inagroup").item(i).value="false";
            document.getElementsByName("belowagroup").item(i).value="false";
            document.getElementsByName("enableup").item(i).value="true";
            document.getElementsByName("enabledown").item(i).value="false";
            var j=i+1;
            for (j;j<sorts.length;j++)
            {
                if (document.getElementsByName("inagroup").item(j).value == "false")
                {
                   document.getElementsByName("enabledown").item(i).value="true"; 
                   break;
                }
             }
        }else
        {
            if (inagroups.item(i).value == "true")
            {
                document.getElementsByName("belowagroup").item(i).value="false";
                if (isagroups.item(i-1).value == "true")
                {
                    document.getElementsByName("enableup").item(i).value="false";
                }else
                {
                    document.getElementsByName("enableup").item(i).value="true";
                }
                if (((i+1) < sorts.length) && (inagroups.item(i+1).value == "false"))
                {
                    document.getElementsByName("enabledown").item(i).value="false";
                }else
                {
                    document.getElementsByName("enabledown").item(i).value="true";
                }
            }else
            {
                 if ((isagroups.item(i-1).value == "true") || (inagroups.item(i-1).value == "true"))
                 {
                    document.getElementsByName("belowagroup").item(i).value="true";
                 }
                 else{
                  document.getElementsByName("belowagroup").item(i).value="false";
                 }
                 if ((i+1) == sorts.length) 
                {
                    document.getElementsByName("enabledown").item(i).value="false";
                    document.getElementsByName("enableup").item(i).value="true";
                }
            }
         }
          data =  data + " " + i + " " + document.getElementsByName("enableup").item(i).value;
        data = data + " " +  document.getElementsByName("enabledown").item(i).value;
                data = data + " " +  document.getElementsByName("isagroup").item(i).value;
                data = data + " " +  document.getElementsByName("inagroup").item(i).value;
                data = data + " " +  document.getElementsByName("belowagroup").item(i).value;
        updateDisplay(i);
    }
 //   alert(data);

}

function updateDisplay(index)
{
    enableIntoGroup(index);
    enableOutOfGroup(index);
    enableUp(index);
    enableDown(index);
}

function enableIntoGroup(index)
{
    if (document.getElementsByName("belowagroup").item(index).value == 'true')
    {
        document.getElementById("intogroup_"+index).style.display='block';
    } else
    {
        document.getElementById("intogroup_"+index).style.display='none';
    }
}

function enableOutOfGroup(index)
{
     if (document.getElementsByName("inagroup").item(index).value == 'true')
    {
        document.getElementById("outofgroup_"+index).style.display='block';
    } else
    {
        document.getElementById("outofgroup_"+index).style.display='none';
    }
}

function enableUp(index)
{
     if (document.getElementsByName("enableup").item(index).value == "true")
    {
        document.getElementById("enable_up"+index).style.display='block';
    } else
    {
        document.getElementById("enable_up"+index).style.display='none';
    }
}

function enableDown(index)
{
     if (document.getElementsByName("enabledown").item(index).value == "true")
    {
        document.getElementById("enable_down"+index).style.display='block';
    } else
    {
        document.getElementById("enable_down"+index).style.display='none';
    }
}

function addToGroup(index)
{
    document.getElementsByName("parent_group_id").item(index).disabled = false;
    document.getElementsByName("child_group_id").item(index).disabled = false;
    document.getElementsByName("parent_group_id").item(index).value = document.getElementsByName("group_Id").item(index-1).value;
    document.getElementsByName("child_group_id").item(index).value = document.getElementsByName("menu_Id").item(index).value;
    document.getElementsByName("group_Id").item(index).value = document.getElementsByName("parent_group_id").item(index).value;
    document.getElementsByName("removefromgroup").item(index).disabled = true;
    document.getElementsByName("inagroup").item(index).value =  "true";
    document.getElementsByName("belowagroup").item(index).value =  "false";
    var menuId =  document.getElementsByName("menu_Id").item(index).value;
    document.getElementById("childLabel_" + menuId).style.fontStyle="italic";
    var childContents = document.getElementById("childLabel_" + menuId).innerHTML;
    childContents = "&nbsp;&nbsp;&nbsp;&nbsp;" + childContents;
    document.getElementById("childLabel_" + menuId).innerHTML = childContents;
    if (document.getElementsByName("inagroup").item(index-1).value ==  "true")
    {
        document.getElementsByName("enabledown").item(index-1).value =  "true";
        document.getElementsByName("enabledown").item(index).value =  "false";
    }
    setDisplay();
}


function removeFromGroup(index)
{
    document.getElementsByName("removefromgroup").item(index).disabled = false;
    document.getElementsByName("removefromgroup").item(index).value = document.getElementsByName("menu_Id").item(index).value;
    document.getElementsByName("parent_group_id").item(index).disabled = true;
    document.getElementsByName("child_group_id").item(index).disabled = true;
    document.getElementsByName("group_Id").item(index-1).value = "";
    document.getElementsByName("inagroup").item(index).value =  "false";
    document.getElementsByName("belowagroup").item(index).value =  "true";
    var menuId =  document.getElementsByName("menu_Id").item(index).value;
    var childContents = document.getElementById("childLabel_" + menuId).innerHTML;
    document.getElementById("childLabel_" + menuId).style.fontStyle="";
    var contentLength = childContents.length;
    childContents = childContents.substring(24,contentLength);
    document.getElementById("childLabel_" + menuId).innerHTML = childContents;
    moveItemBelowGroup(index);
    setDisplay();
}

function moveItemBelowGroup(index)
{
    var sorts = document.getElementsByName("sort");
    var inagroups = document.getElementsByName("inagroup");
    for(i=0;i<sorts.length;i++)
    {
        if (i > index)
        {
            if (inagroups.item(i).value == "false") return;
            if (inagroups.item(i).value == "true") 
            {
                moveElementUp(i,false);
            }
        }
        
    }
}

 function copyMove(idChild,idParent,action) {  
      document.editArchive.idChild.value = idChild;
      document.editArchive.idParent.value = idParent;
      document.editArchive.command.value = action;
      document.editArchive.submit();
    }



<!--
   function confirmDelete(idDel,idTrackId,trackName) {  
      var msg = "Do you want to delete '"+ trackName +"' ?";
      document.editArchive.delID.value = idDel;
      document.editArchive.delIDTrack.value = idTrackId;
      document.editArchive.command.value = "deleteMenu";
      if ( confirm(msg) ) {        
        document.editArchive.submit();
      }
    }
//-->
</script>

<table class="no_space_width" width="90%" height="0%"> 
    <tr>
        <td>
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
        </td>
        <td colspan="=2">
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="25">
        </td>
    </tr>
    <tr>
        <td>
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
        </td>
        <td>
        	<div class="breadcrumb">
                <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / 
                <%=wc.getMenuName()%>&nbsp;Archive
        	</div>
        </td>
	</tr>
</table>


<TABLE class="basic_table"> 
<TR>
<TD rowspan="2"><IMG src="/TrainingReports/resources/images/spacer.gif" width="15"></TD>
<TD align="left">
</TD>
</TR>
<TR><TD>

<TABLE class="basic_table"><TR><TD><IMG src="/TrainingReports/resources/images/spacer.gif" width="15" height="1"></TD><TD>
&nbsp;

<FORM action="" method="post" name="editArchive">
            <TABLE class="blue_table" width="100%">
            <TR><TH align="left">
                <%=wc.getMenuName()%> Archive                            
            </TH></TR>    
            <TR><TD align="center"><%=wc.getErrorMsg()%></TD></TR>
            <TR><TD><TABLE width="100%">
            <tr>
                <td style="border:0px">Sort</td>
                <td style="border:0px" align="center">Name</td>
                <td style="border:0px">Access</td>
                <td style="border:0px">Action</td>
            </tr>
            <% int elementCount =0;      
                        String groupId = "";                                     
            for(int i=0;i<renderMenu.size();i++){
                // System.out.println("group id" + groupId);
              MenuList prevMenu = null;
              MenuList nextMenu = null;
               if (i > 0) prevMenu = (MenuList)renderMenu.elementAt(i-1);
                MenuList menu = (MenuList)renderMenu.elementAt(i);    
            if (i < (renderMenu.size()-1)) nextMenu = (MenuList)renderMenu.elementAt(i+1);
               // System.out.println("---- --- " + menu.getLabel() + " " + menu.getLevel());                                                    
                String checkedActive = (menu.getActive().equalsIgnoreCase("1"))?"checked":"";
                String checkedNonActive = (menu.getActive().equalsIgnoreCase("0"))?"checked":"";
                boolean displayLink = (menu.getUrl()!=null&&(menu.getUrl().trim().length()!=0))?true:false; 
                if (menu.getTrackId() != null && menu.getTrackId().startsWith("GROUP")) displayLink = true;
                //System.out.println(displayLink);
            %>
          <TR><TD>
           
            <%
            boolean enable_up = false;
            boolean enable_down = false;
            boolean inAGroup = false;
            boolean isAGroup = false;
            boolean belowAGroup = false;
            boolean firstChild = false;
            boolean lastChild = false;
            
            if (i < (renderMenu.size()-1)) enable_down = true;
            if ((i > 0) && (i < renderMenu.size())) enable_up = true;
            
            if (!Util.isEmpty(menu.getTrackId()) && !menu.getTrackId().startsWith("GROUP")) {
                // System.out.println(menu.getLabel() +" is not a group ");
                if (((new Integer(menu.getLevel())).intValue() == 2)) {
                    inAGroup = true;
                    groupAbove = true;
                   // System.out.println(menu.getLabel() +" in a group " + inAGroup);
                }
                else if (groupAbove == true) 
                {
                    
                    belowAGroup = true;
                    groupAbove = false; 
                    groupId = "";          
                        
                  // System.out.println(menu.getLabel() +" below a group " + belowAGroup);
                }
            } else 
            {
                if (!Util.isEmpty(menu.getTrackId()) && menu.getTrackId().startsWith("GROUP")){
                isAGroup = true;
                groupId = menu.getId();
               //  System.out.println("is a group" + groupId);
                groupAbove = true; 
                }
            }
            
            if (inAGroup){
              //  System.out.println("parent group id" + groupId);
              if (nextMenu != null){
                if (((new Integer(nextMenu.getLevel())).intValue() == 1)) lastChild = true;
              }
              if (prevMenu != null){
                if (((new Integer(prevMenu.getLevel())).intValue() == 1)) firstChild = true;
              }
                
                if (firstChild) enable_up = false;
                if (lastChild) enable_down = false;
            }else{
                if (i==0 && i<(renderMenu.size()-1)) enable_down = true;
                if (i>0) enable_up = true;
            }
          //  System.out.println(menu.getLabel() +" below a group " + belowAGroup);
            
            %>
            <table>
            <tr>
            <td width="18px" style="border:0px"><div id="enable_down<%=i%>" <%if (enable_down) {%>style="display:block"<%}else { %>style="display:none"<%}%> title="Move one level down the list." onclick="moveDown(<%=i%>);"><img src="<%=AppConst.IMAGE_DIR%>/arrow_down.gif"></div></td>
            <td width="18px" style="border:0px"><div id="enable_up<%=i%>" <% if (enable_up){%> style="display:block"<%}else{%> style="display:none"<%}%> title="Move one level up the list." onclick="moveUp(<%=i%>);"><img src="<%=AppConst.IMAGE_DIR%>/arrow_up.gif"></div></td>
            <td width="18px" style="border:0px"><div id="outofgroup_<%=i%>" <%if (inAGroup) {%>style="display:block"<%}else { %>style="display:none"<%}%> title="Remove from the Group." onclick="removeFromGroup(<%=i%>);"><img src="<%=AppConst.IMAGE_DIR%>/arrow_left.gif"></div>
            <div id="intogroup_<%=i%>" <% if (belowAGroup){%> style="display:block"<%}else{%> style="display:none"<%}%> title="Add to the Group above." onclick="addToGroup(<%=i%>);"><img src="<%=AppConst.IMAGE_DIR%>/arrow_right.gif"></div></td>
           
            </tr>
            </table>
            
            <input type="hidden" name="enableup" <%if (enable_down){%>value="true"<%}else{%>value="false"<%}%> disabled>
            <input type="hidden" name="enabledown" <%if (enable_down){%>value="true"<%}else{%>value="false"<%}%> disabled>
            <input type="hidden" name="removefromgroup" value="" disabled>
            <input type="hidden" name="parent_group_id" value="" disabled>
            <input type="hidden" name="child_group_id" value="" disabled>
            <input type="hidden" name="sort" value="<%=menu.getId()%>">
           
             </td>
             <TD align="left" valign="top">   
               <div id="name_<%=i%>">
              <div id="childLabel_<%=menu.getId()%>" <% if (inAGroup){%> style="font-style:italic">
              <%=putSpace((new Integer(menu.getLevel())).intValue())%> 
              <%} else {%>><%}%>                                              
              <%
                    if (isAGroup){%> <A style="font-weight:bold"><%=menu.getLabel()%></A><%}
                    else{
                        if(displayLink){%><A href="<%=menu.getUrl()%>">
                    <%=menu.getLabel()%></A><%}                    
                    else{%><%=menu.getLabel()%><%}
                        }%></div>
                <input type="hidden" name="isagroup" <%if (isAGroup){%>value="true"<%}else {%>value="false"<%}%> disabled>                  
                <input type="hidden" name="inagroup" <%if (inAGroup){%>value="true"<%}else {%>value="false"<%}%> disabled>
                <input type="hidden" name="belowagroup" <%if (belowAGroup){%>value="true"<%}else {%>value="false"<%}%> disabled>
                <input type="hidden" name="menu_Id" value="<%=menu.getId()%>" disabled> 
                <input type="hidden" name="group_Id" value="<%=groupId%>" disabled>  
                <input type="hidden" name="firstchild" <%if (firstChild){%>value="true"<%}else {%>value="false"<%}%> disabled>
                <input type="hidden" name="lastchild" <%if (lastChild){%>value="true"<%}else {%>value="false"<%}%> disabled>
                
                </div>
                </TD>
                    <%if(!wc.getMenuName().equalsIgnoreCase("Special Events")){
                        %>
                    <td style="padding=0">
                     <div id="access_<%=i%>">
                    <select   style="border:0px" name="access_<%=menu.getId()%>">
                    <%=HtmlBuilder.getOptionsFromLabelValue(MenuList.getAccessList(),menu.getAllow())%>
                    </select>  
                    </div>                  
                    </td>
                    <%}else{
                        System.out.println("Special events menu -----   -----------------");%>
                    <td style="padding=0">
                    <div id="access_<%=i%>">                  
                    <select   style="border:0px" name="access_<%=menu.getId()%>">
                    <%=HtmlBuilder.getOptionsFromLabelValue(MenuList.getSpecialAccessList(),menu.getAllow())%>
                    </select>    
                    </div>                
                    </td>
                    <%}%>
                    <td style="padding=0">
                    <div id="action_<%=i%>">
                    <input type="hidden" name="defaultStatus_<%=i%>" value="<%=menu.getActive()%>" >  
                     <% if (!Util.isEmpty( menu.getId() )) { %>
                    <select name="editType_<%=menu.getId()%>" style="width:180px">
                        <option value="Restore">Restore</option>
                         <option value="Delete">Delete</option>
                     </select><%
                    } else {%>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                               
                    <% } %>

                     <% if(menu.getTrackId()!=null){
                      if(!menu.getTrackId().startsWith("LAUNCH") && !menu.getTrackId().startsWith("FORECAST") && !menu.getTrackId().startsWith("MANAGEMENT") && (!menu.getTrackId().startsWith("GAP") && (!menu.getTrackId().startsWith("GROUP")))){
                      %><input type="button" name="go" value="GO" onclick="javascript:getDetails('REGULAR','<%=menu.getTrackId()%>',escape('<%=menu.getLabel1()%>'),'<%=menu.getId()%>');"><%
                      }
                       else if(menu.getTrackId().startsWith("LAUNCH")){
                       %><input type="button" name="go" value="GO" onclick="javascript:getDetails('LAUNCH','<%=menu.getTrackId()%>',escape('<%=menu.getLabel1()%>'),'<%=menu.getId()%>');"><%
                       }
                     else if(menu.getTrackId().startsWith("FORECAST")){%>
                        <input type="button" name="go" value="GO" onclick="javascript:getDetails('FORECAST','<%=menu.getTrackId()%>',escape('<%=menu.getLabel1()%>'),'<%=menu.getId()%>');">
                            <input type="hidden" name="Forecast" value="" ><%}
                    else if(menu.getTrackId().startsWith("MANAGEMENT")){%>
                     <input type="button" name="go" value="GO" onclick="javascript:getDetails('MANAGEMENT','<%=menu.getTrackId()%>',escape('<%=menu.getLabel1()%>'),'<%=menu.getId()%>');">
                             <input type="hidden" name="Management" value="" ><%}
                    else if(menu.getTrackId().startsWith("GAP")){%>
                     <input type="button" name="go" value="GO" onclick="javascript:getDetails('GAP','<%=menu.getTrackId()%>',escape('<%=menu.getLabel1()%>'),'<%=menu.getId()%>');">
                             <input type="hidden" name="Gap" value="" ><%}
                    else if(menu.getTrackId().startsWith("GROUP")){%>
                     <input type="button" name="go" value="GO" onclick="javascript:getDetails('GROUP','<%=menu.getTrackId()%>',escape('<%=menu.getLabel1()%>'),'<%=menu.getId()%>');">
                             <input type="hidden" name="Group" value="" ><%}
                    %>               </div> </TD></TR>
              <%}else{%>                
                <input type="button" name="go" value="GO" onclick="javascript:getDetails('REGULAR','<%=menu.getTrackId()%>',escape('<%=menu.getLabel1()%>'),'<%=menu.getId()%>');">
                <%}
                
                                                                   
            }%>
            
            <TR>
              <td style="border:0px" colspan="4" align="center">
                <input type="hidden" name="id" value="<%=wc.getRootID()%>">
                <input type="hidden" name="name" value="<%=wc.getMenuName()%>">
                <input type="hidden" name="delID" value="">
                <input type="hidden" name="delIDTrack" value="">
                <input type="hidden" name="command" value="updateStatus">
                <input type="submit" name="update" value="Save">
                <input type="reset" name="reset" value="Reset">
                <input type="hidden" name="trackID" value="">
                <input type="hidden" name="trackName" value="">
                <input type="hidden" name="idChild" value="">
                <input type="hidden" name="idParent" value="">
                
            </td>
            </TR>
            </TABLE>
             
        </TD></TR></TABLE>
                        
</FORM>
</TD></TR>
</TABLE>

<%=wc.getErrorMsg()%>
</TD></TR></TABLE>


    