<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Wave - Launch</title>
<link rel="stylesheet" href="<c:url value="/resources/screen.css" />" type="text/css" media="screen, projection">
</head>
<body>

<div id="wrapper">
<div id="formBox">
<form:form modelAttribute="circuit" action="" method="post">
	<form:errors path="*" element="div" class="errorBox"/>

	<div class="fieldBox">
	<form:label for="circuitId" path="circuitId">Circuit id:</form:label>
	<form:input path="circuitId"/>
	</div>

	<div class="buttonBox">
	<input type="submit" name="Analyse" value="Analyse"/>
	</div>
</form:form>
</div>
</div>

</body>
</html>