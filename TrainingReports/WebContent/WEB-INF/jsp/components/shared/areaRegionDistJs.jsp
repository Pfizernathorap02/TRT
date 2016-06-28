<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.user.Area"%>

<%@ page import="com.pfizer.webapp.user.District"%>

<%@ page import="com.pfizer.webapp.user.Region"%>

<%@ page import="com.pfizer.webapp.user.UserTerritory"%>

<%@ page import="com.pfizer.webapp.wc.components.shared.AreaRegionDistJsWc"%>

<%@ page import="com.pfizer.webapp.user.TerritoryFilterForm"%>

<%@ page import="com.tgix.Utils.Util"%>

<%@ page import="java.util.*"%>

<%

	AreaRegionDistJsWc wc = (AreaRegionDistJsWc)request.getAttribute(AreaRegionDistJsWc.ATTRIBUTE_NAME);

	List areas = wc.getUserTerritory().getAreas();

	TerritoryFilterForm tff = wc.getFilterForm();

%>



<%!

	public String getStringForRegion(List regions,String jsObjectName) {

		StringBuffer sb = new StringBuffer();

		Region regTemp;

		sb.append(addOptionString(jsObjectName,"All","All"));

		sb.append(".child = new tgixSelect('" + TerritoryFilterForm.FIELD_DISTRICT +"');\n") ;

		String regObjTemp = jsObjectName+".getOption('All').child"; 

		sb.append( addOptionString( regObjTemp, "All", "All" )  + ";\n");

		String disObjTemp;

		for ( Iterator it = regions.iterator(); it.hasNext(); ) {

			regTemp = (Region)it.next();

			sb.append(addOptionString(jsObjectName,regTemp.getName(),regTemp.getCode()));

			sb.append(".child = new tgixSelect('" + TerritoryFilterForm.FIELD_DISTRICT +"');\n") ;

			disObjTemp = jsObjectName+".getOption('" + regTemp.getCode() + "').child"; 

			sb.append( addOptionString( disObjTemp, "All", "All" )  + ";\n");			

			List districts = regTemp.getDistricts();

			District disTemp;

			String tmpObjName;

			for ( Iterator itx = districts.iterator(); itx.hasNext(); ) {

				disTemp = (District)itx.next();

				tmpObjName = jsObjectName+".getOption('" + regTemp.getCode() + "').child"; 

				sb.append( addOptionString( tmpObjName, disTemp.getName(), disTemp.getId() )  + ";\n");

			}

		}

		

		return sb.toString();

	}





	public String getStringForArea(List areas,String jsObjectName) {

		//Collections.sort(areas);

		

		StringBuffer sb = new StringBuffer();

		String areaObjName;

		String regObjName;

		String disObjName;

		

		sb.append(addOptionString(jsObjectName,"All","All"));

		sb.append(".child = new tgixSelect('" + TerritoryFilterForm.FIELD_REGION +"');\n") ;

		regObjName = jsObjectName+".getOption('All').child"; 

		sb.append( addOptionString( regObjName, "All", "All" ));

		sb.append(".child = new tgixSelect('" + TerritoryFilterForm.FIELD_DISTRICT +"');\n") ;

		disObjName = regObjName + ".getOption('All').child";

		sb.append( addOptionString( disObjName, "All", "All" ) + ";\n");

		

		for ( Iterator ita = areas.iterator(); ita.hasNext(); ) {

			Region regTemp;

			Area areTemp = (Area)ita.next();

			sb.append(addOptionString(jsObjectName,areTemp.getDescription(),areTemp.getCode()));

			sb.append(".child = new tgixSelect('" + TerritoryFilterForm.FIELD_REGION +"');\n") ;

			regObjName = jsObjectName+".getOption('" + areTemp.getCode() + "').child"; 

			sb.append( addOptionString( regObjName, "All", "All" ));	

			sb.append(".child = new tgixSelect('" + TerritoryFilterForm.FIELD_DISTRICT +"');\n") ;		

			disObjName = regObjName + ".getOption('All').child";

			sb.append( addOptionString( disObjName, "All", "All" ) + ";\n");	

			List regions = 	areTemp.getRegions();

			//Collections.sort(regions);		

			for ( Iterator it = regions.iterator(); it.hasNext(); ) {

				regTemp = (Region)it.next();

				sb.append(addOptionString(regObjName,regTemp.getName(),regTemp.getCode()));

				sb.append(".child = new tgixSelect('" + TerritoryFilterForm.FIELD_DISTRICT +"');\n") ;

				String disObjTemp = regObjName+".getOption('" + regTemp.getCode() + "').child"; 

				sb.append( addOptionString( disObjTemp, "All", "All" ) + ";\n");	

				List districts = regTemp.getDistricts();

				//Collections.sort(districts);	

				District disTemp;

				disObjName = regObjName+".getOption('" + regTemp.getCode() + "').child";

				

				for ( Iterator itx = districts.iterator(); itx.hasNext(); ) {

					disTemp = (District)itx.next();

					sb.append( addOptionString( disObjName, disTemp.getName(), disTemp.getId() )  + ";\n");

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

<script language="JavaScript1.2">





var dynamicSelect;



<% if ( wc.getUserTerritory().getType() == UserTerritory.TYPE_NATIONAL ) { %>

	dynamicSelect = new tgixSelect('<%=TerritoryFilterForm.FIELD_AREA%>');

	<% List regions = ( (Area)areas.get( 0 ) ).getRegions();%>

	<%=getStringForArea(areas,"dynamicSelect")%>	

<% } else if ( wc.getUserTerritory().getType() == UserTerritory.TYPE_AREA ) {%>

	dynamicSelect = new tgixSelect('<%=TerritoryFilterForm.FIELD_REGION%>');

	<% List regions = ( (Area)areas.get( 0 ) ).getRegions();%>

	<%=getStringForRegion(regions,"dynamicSelect")%>

<% } else if ( wc.getUserTerritory().getType() == UserTerritory.TYPE_REGION ) {%>

	dynamicSelect = new tgixSelect('<%=TerritoryFilterForm.FIELD_DISTRICT%>');

	<%=addOptionString("dynamicSelect","All","All")%>

	<%	List districts = ( (Region)( (Area)areas.get( 0 ) ).getRegions().get( 0 ) ).getDistricts();

	

		for ( Iterator it = districts.iterator(); it.hasNext(); ) { 

			District dis = (District)it.next();

	%>

			<%=addOptionString("dynamicSelect",dis.getName(),dis.getId())%>

	<%	} %>

		

<% } %>







function dropDownPopulate() {

	populateTgixSelect(dynamicSelect);

	<% if ( !Util.isEmpty( tff.getArea() ) ) { %>

		var tmpSel = document.getElementById( '<%=TerritoryFilterForm.FIELD_AREA%>' );

		if (tmpSel != null) {

			var opTemp;

			for( var x=0; x< tmpSel.options.length; x++ ) {

				if ( tmpSel.options[x].value == '<%=tff.getArea()%>' ) {

					tmpSel.options[x].selected = true;

					updateTgixSelect(tmpSel,dynamicSelect)

				} 

			}

		}

	<% } %>



	<% if ( !Util.isEmpty( tff.getRegion() ) ) { %>

		var tmpSel = document.getElementById( '<%=TerritoryFilterForm.FIELD_REGION%>' );

		if (tmpSel != null) {

			var opTemp;

			for( var x=0; x< tmpSel.options.length; x++ ) {

				if ( tmpSel.options[x].value == '<%=tff.getRegion()%>' ) {

					tmpSel.options[x].selected = true;

					updateTgixSelect(tmpSel,dynamicSelect)

				} 

			}

		}

	<% } %>



	<% if ( !Util.isEmpty( tff.getDistrict() ) ) { %>

		var tmpSel = document.getElementById( '<%=TerritoryFilterForm.FIELD_DISTRICT%>' );

		if (tmpSel != null) {		

			var opTemp;

			for( var x=0; x< tmpSel.options.length; x++ ) {

				if ( tmpSel.options[x].value == '<%=tff.getDistrict()%>' ) {

					tmpSel.options[x].selected = true;

					updateTgixSelect(tmpSel,dynamicSelect)

				} 

			}

		}

	<% } %>



}

</script>