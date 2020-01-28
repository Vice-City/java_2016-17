<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<style type="text/css">
			@import url("/webapp2/style");
		</style>
	</head>
	
	<body>
		<h1>Error!</h1>
		<p>Received invalid values!</p>
		
		<table>
			<thead>
				<tr>
					<th>Value</th>
					<th>Received</th>
					<th>Expected</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="errorValue" items="${errorValues}">
					<tr>
						<td>${errorValue.name}</td>
						<td>${errorValue.value}</td>
						<td>${errorValue.expected}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<p><a href="/webapp2/index.jsp" >Return to index.</a><p>
	</body>
</html>