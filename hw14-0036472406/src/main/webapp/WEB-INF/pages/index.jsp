<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<style type="text/css">
			@import url("/voting-app/style.jsp");
		</style>
	</head>
	
	<body>
		<h2>Biranje glasanja</h2>
		<p>
		Pozdrav! Molim odaberite jedno od sljedećih glasanja:
		</p>
		
		<ol>
		<c:forEach var="poll" items="${polls}">
			<li><a href="/voting-app/servleti/glasanje?pollId=${poll.id}">${poll.title}</a></li>
		</c:forEach>
		</ol>
	
	</body>
</html>