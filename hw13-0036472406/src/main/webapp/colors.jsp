<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<style type="text/css">
			@import url("/webapp2/style");
		</style>
	</head>
	
	<body>
		<p>
		Change colors by clicking the following links:
		<br><a href="/webapp2/setcolor?color=white">WHITE</a>
		<br><a href="/webapp2/setcolor?color=red">RED</a>
		<br><a href="/webapp2/setcolor?color=green">GREEN</a>
		<br><a href="/webapp2/setcolor?color=cyan">CYAN</a>
		</p>
		
		<p><a href="/webapp2/index.jsp" >Return to home page</a>
		(page must be refreshed for changes to take effect).
	</body>
</html>