<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<style type="text/css">
			@import url("/webapp2/style");
		</style>
	</head>
	
	<body>
		<h2>Running time</h2>
		<p>This webapp has been running for ${elapsedTime}.<p>
		
		<p><a href="/webapp2/appinfo.jsp">Refresh!</a></p>
	</body>
</html>