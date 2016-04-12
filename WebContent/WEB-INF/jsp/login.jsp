<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Wave - Login</title>
<link rel="stylesheet" href="<c:url value="/resources/screen.css" />" type="text/css" media="screen, projection">
</head>
<body onload='document.f.j_username.focus();'>

<div id="wrapper">
<div id="formBox">
<form name="f" action="<c:url value='j_spring_security_check' />" method="post">
	<c:if test="${not empty sessionScope[\"SPRING_SECURITY_LAST_EXCEPTION\"]}">
		<div class="errorBox">
			${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
		</div>
	</c:if>

	<div class="fieldBox">
	<label for="userId">User:</label>
	<input type="text" id="userId" name="j_username"/>
	</div>

	<div class="fieldBox">
	<label for="password">Password:</label>
	<input type="password" id="password" name="j_password"/>
	</div>

	<div class="buttonBox">
	<input type="submit" name="Login" value="Login"/>
	</div>
</form>
</div>
</div>

</body>
</html>