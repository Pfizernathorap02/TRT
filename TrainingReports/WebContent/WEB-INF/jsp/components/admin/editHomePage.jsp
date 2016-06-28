<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.HomePageConfigWC"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%> --%>
<%-- <%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%>
<%-- <%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>


<script type="text/javascript" src="/TrainingReports/resources/js/jquery.js"></script>

<%
	HomePageConfigWC wc = (HomePageConfigWC)request.getAttribute(HomePageConfigWC.ATTRIBUTE_NAME);
   // List sectionList = wc.getSectionList();
   // List idList = wc.getIdList();
    int size=wc.getSectionList().size()-1;
   // List checkedValuesList =wc.getcheckedValuesList();
    boolean preview = false;
    List sectionNamesList = new ArrayList();
    List minimizeList = new ArrayList();
    List trackIdList = new ArrayList();
      if (request.getSession().getAttribute("preview") != null){
            String previewString = request.getSession().getAttribute("preview").toString();
            System.out.println("previewString    76777777777777777777777"+previewString);
             if (previewString.equals("true")) preview = true;
                   if (request.getSession().getAttribute("sectionNamesList") != null){
                        sectionNamesList = (List)request.getSession().getAttribute("sectionNamesList");
                        System.out.println("sectionNamesList in jsp page=="+sectionNamesList);
                    }
                if (request.getSession().getAttribute("minimizeList") != null){
                     minimizeList = (List)request.getSession().getAttribute("minimizeList");
                    System.out.println("minimize in jsp page=="+minimizeList);
                }
                 if (request.getSession().getAttribute("trackIdList") != null){
                     trackIdList = (List)request.getSession().getAttribute("trackIdList");
                    System.out.println("minimize in jsp page=="+trackIdList);
                }
     //  if (sectionNamesList.size() == 0 || minimizeList.size() == 0 || trackIdList.size() == 0)   preview = false;     
            
     }
     
     if(!preview) {
        sectionNamesList = wc.getSectionList();
        minimizeList = wc.getcheckedValuesList();
        trackIdList = wc.getIdList();
     }
  
  //  String[] sectionNames = request.getParameterValues("sectionName");
  //  session.setAttribute( "sectionName", sectionNames );
  //  System.out.println("session attr=="+session.getAttribute("sectionName"));
%>
<script type="text/javascript">
    var myW;
    
function openPreview(){
 //   alert('in preview');
     myW = window.open("","myW","height=500,width=600,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes");
     myW.location =  '/TrainingReports/previewHomePage';
     return true;
}
    
function openPreviewWindow() { 
   if (myW != null)
        {
            if (!myW.closed) {
            myW.focus();
            } 
        }
    window.name = "main";
    
  //  myW = window.open("","myW","height=500,width=600,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes");
 //   myW.location =  '/TrainingReports/previewHomePage.do';
    document.homePageConfgiuration.submit();
    // myW.target = 'm';
   // alert(document.hiddenform.name);
 //   var w = window.setTimeout("document.hiddenform.submit();",500);
} 
    
    function movedown(idA,idB){
        var cellA=document.getElementById('event'+idA);   
        var cellB=document.getElementById('event'+idB);
    
        var minA=document.getElementById('chk'+idA); 
        var minB=document.getElementById('chk'+idB);
        
        var trackIdA=document.getElementsByName("trackId");
        var trackIdB=document.getElementsByName("trackId");
       
    
        if(cellA&&cellB){
            var temp = cellA.innerHTML;
            cellA.innerHTML = cellB.innerHTML;
            cellB.innerHTML = temp;
        }
        if(minA&&minB){
            var temp = minA.innerHTML;
            minA.innerHTML = minB.innerHTML;
            minB.innerHTML = temp;
        }
        
        if(trackIdA&&trackIdB)
         {
            var temp = document.getElementsByName("trackId").item(idA).value;
            document.getElementsByName("trackId").item(idA).value=document.getElementsByName("trackId").item(idB).value;
            document.getElementsByName("trackId").item(idB).value=temp;
        //    alert("B=="+document.getElementsByName("trackId").item(idB).value);
        //    alert("A=="+document.getElementsByName("trackId").item(idA).value);
         }
    
    }    
 
    
     function moveup(idA,idB){
       
        var cellA=document.getElementById('event'+idA);   
        var cellB=document.getElementById('event'+idB);
        
        var minA=document.getElementById('chk'+idA); 
        var minB=document.getElementById('chk'+idB);
        
        var trackIdA=document.getElementsByName("trackId");
        var trackIdB=document.getElementsByName("trackId");
        
        if(cellA&&cellB){
            var temp = cellA.innerHTML;
            cellA.innerHTML = cellB.innerHTML;
            cellB.innerHTML = temp;
        }
        if(minA&&minB){
            var temp = minA.innerHTML;
            minA.innerHTML = minB.innerHTML;
            minB.innerHTML = temp;
        }
       
        if(trackIdA&&trackIdB)
         {
            var temp = document.getElementsByName("trackId").item(idA).value;
         //   alert(temp);
            document.getElementsByName("trackId").item(idA).value=document.getElementsByName("trackId").item(idB).value;
            document.getElementsByName("trackId").item(idB).value=temp;
         //   alert("B=="+document.getElementsByName("trackId").item(idB).value);
         //   alert("A=="+document.getElementsByName("trackId").item(idA).value);
         }
    }    
   
    function homePageSubmit(){
        
            var minimize=document.getElementsByName('minimize'); 
            var min = document.getElementsByName('min');
          for(var i=0;i<minimize.length;i++){
            if(minimize[i].checked==true){
                 min[i].value = 'Y';
                 min[i].checked=true;
            }
            else{
                min[i].value = 'N';
                min[i].checked=false;
            }
        }
            
    }
    function resetPage(id){
        document.getElementById('res').value = "";
        document.homePageConfgiuration.submit();
    }
    function softDelete(id)
    {
      //  alert(id);
        var msg = "Deleting section will delete the reports present in it. Are you sure you want to delete ?";
        //document.getElementById('del').value = id;
        if ( confirm(msg) ) { 
            document.getElementById('del').value = id;
            document.homePageConfgiuration.submit();
        }
        
    }
</script>
<%-- <netui:html> --%>
<html>
<body>
<table class="basic_table" align="center" >
<tr>
    <td rowspan="2">&nbsp;&nbsp;</td>
    <td>&nbsp;</td>
    <td rowspan="2">&nbsp;&nbsp;</td>
</tr>
<tr>
    <td>
   
    <table class="no_space_width"  height="0%"> 
    <tr>
        <td>
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
        </td>
        <td colspan="=2">
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
        </td>
    </tr>
    <tr>
    <td>
        <div class="breadcrumb">
           <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / 
           Home Page Configuration
        </div>
    </td>
    </tr>
    </table>
    
    <table width="100%">
        <tr>
            <td align="center"> 
                <div class="breadcrumb"><b>Home Page Configuration </b></div></td>
        </tr>
    </table>
<!-- <table class="blue_table_without_border">
    <tr><td><b>Instructions</b></td></tr>
    <tr><td>2. Click on 'Save' button to save home page configurations.</td></tr>
    <tr><td>2. Click on 'Preview' button for a preliminary view of the home page after configuration.</td></tr>
    <tr><td>4. Enter a section name and click on 'Add New Section' to add a section to the home page.</td></tr>
 </table>  -->
<table class="blue_table_without_border" align="center">
        <tr>
            <td><span id="msg" style="color:red;"><%=wc.getErrorMsg()%></span></td>
        </tr>
</table>   
         <!--<form name="homePageConfgiuration" id="homePageConfgiuration" action="/TrainingReports/admin/editHomePage.do" method="post" >-->
        <form name="homePageConfgiuration" id="homePageConfgiuration" method="post" action="/TrainingReports/admin/editHomePage"> 
           <table width="60%" align="center" class="blue_table" id="tbl">
           <tbody id="tbd">
           <tr><th>Sort Order</th>
                <th>Sections</th>
                <th>Minimize</th>
                <th></th>
            </tr>
           <% for(int i=0;i<sectionNamesList.size();i++) {%>
            <tr><td>
                    <%if(i!=0 && i!=size){%>
                        <img id="downpr" onclick="movedown(<%=i%>,<%=i+1%>)" src="/TrainingReports/resources/images/arrow_down.gif" height="17" width="20">
                        <img id="upr" onclick="moveup(<%=i%>,<%=i-1%>)" src="/TrainingReports/resources/images/arrow_up.gif" height="17" width="20">
                        <input type="hidden" name="trackId" id="trackId" value="<%=trackIdList.get(i)%>">
                      
                   <%}%>
                    <%if(i==0){%>
                       <img id="downpr" onclick="movedown(<%=i%>, <%=i+1%>)" src="/TrainingReports/resources/images/arrow_down.gif" height="17" width="20">
                       <input type="hidden" name="trackId" id="trackId" value="<%=trackIdList.get(i)%>">
                       
                    <%}%>
                    <%if(i==size){%>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img id="upr" onclick="moveup(<%=i%>,<%=i-1%>)" src="/TrainingReports/resources/images/arrow_up.gif" height="17" width="20">
                        <input type="hidden" name="trackId" id="trackId" value="<%=trackIdList.get(i)%>">
                       
                   <%--   <!--   <input type="hidden" name="trackId" id="trackId" value="<%=//idList.get(i)%>"> --> --%>
                    <%}%>
                </td>
                <td  id="<%="event"+i%>">
                        <input name="sectionName" type="text" value="<%=sectionNamesList.get(i)%>">
                        <input type="hidden" name="sortorder" value="<%=i%>">    </td>
                <td id="<%="chk"+i%>">
                        <input name="minimize" type="checkbox" onclick="homePageSubmit();" <%if(minimizeList!=null && minimizeList.get(i).equals("Y")){%>checked="checked"<%}%>>
                        <input type="hidden" name="min" id="min" <%if(minimizeList!=null && minimizeList.get(i).equals("Y")){%>value="Y"<%}else{%>value="N"<%}%>></td>
                <td><input type="button" name="delete" value="Delete" onclick="javascript:softDelete(<%=trackIdList.get(i)%>);"/></td>
                
            </tr>
           <% } %>
           </tbody>
           </table> 
           <br>
           <table width="30%" align="center"><tr><td><input type="submit" name="preview" value="Preview" onclick='openPreviewWindow();'></td>
           <td><input type="submit" name="save" value="Save"></td>
           <td><input type="button" name="reset" value="Reset" onclick="javascript:resetPage();" ></td></tr></table> 
           
           <input type="hidden" name="del" id="del" value="">
            <input type="hidden" name="res" id="res" value="reset">
           <br><br>
           <table width="40%" align="center" border="1" cellpadding="10px">
            <tr>
                <td><input type="text" id="newSection" name="newSection" value=""></td>
                <td><input type="submit" name="addSection" value="Add New Section"></td>
            </tr>
           </table> 
           
        </form>
        
       <!-- <form name="hiddenform" id="hiddenform" method="post"  >
               <input type="button" value="Preview" onclick='openPreviewWindow();'>
       </form> -->
    </td>
    
</tr>
  <%--  <%//request.getSession().removeAttribute("sectionNamesList");%> --%>
    
</table>
</body>
</html>
<%-- </netui:html> --%>
