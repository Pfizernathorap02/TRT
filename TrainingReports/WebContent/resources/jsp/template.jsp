<!--Generated by Weblogic Workshop-->

<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%--  <%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>

<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>

<%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%> 




<html>

  <head>

    <netui:base />

    <title><netui-template:attribute name="title"/></title>

    <link href="<%=request.getContextPath()%>/resources/css/style.css" type="text/css" rel="stylesheet">

  </head>

  <body style="margin:0">

    <jsp:include page="/resources/jsp/header.jsp" />

    <br/>

    <netui-template:includeSection name="bodySection"></netui-template:includeSection>

  </body>


</html>