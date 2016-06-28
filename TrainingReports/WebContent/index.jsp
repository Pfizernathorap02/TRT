<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>
 
<html>
<script language="javascript">
   
   function redirectFunction(){        
      document.forms[0].action = "begin"; 
        document.forms[0].submit();
    }
   </script>

<body onload="redirectFunction()">
<form>
</form>
</body>
</html>
