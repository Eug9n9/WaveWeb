<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="refresh" content="${refreshInterval}">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Wave - Please wait</title>
<link rel="stylesheet" href="<c:url value="/resources/screen.css" />" type="text/css" media="screen, projection">
</head>
<body>

<div id="wrapper">
Analysing the circuit '${analysis.circuitId}', please wait...
<span id="timeLeft">${analysis.timeLeft}</span>
<script language="javascript">
function updateTimeLeft() {
	var el = document.getElementById("timeLeft");
	var timeLeft = parseInt(el.innerHTML);
	timeLeft--;
	el.innerHTML = timeLeft;
	if (timeLeft > ${analysis.timeLeft - refreshInterval}) {
		setTimeout("updateTimeLeft()", 1000);
	}
}
setTimeout("updateTimeLeft()", 1000);
</script>
</div>

</body>
</html>