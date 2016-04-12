<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Wave - Error</title>
<link rel="stylesheet" href="<c:url value="/resources/screen.css" />" type="text/css" media="screen, projection">
</head>
<body>

<div id="wrapper">
<div id="menuBox">
<a href="<c:url value="/" />">Analyse another</a> &nbsp;
<a href="j_spring_security_logout">Logout</a>
</div>

${errorMessage}
</div>

</body>
</html>