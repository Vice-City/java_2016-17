<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<style type="text/css">
			@import url("/voting-app/style.jsp");
		</style>
	</head>
	
	<body>
		<h1>${poll.title}:</h1>
		<p>${poll.message}</p>
			
		<ol>
			<c:forEach var="pollOption" items="${pollOptions}">
				<li><a href="/voting-app/servleti/glasanje-glasaj?id=${pollOption.id}">${pollOption.optionTitle}</a></li>
			</c:forEach>
		</ol>
	</body>
</html>