<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<style type="text/css">
			@import url("/webapp2/style");
		</style>
	</head>
	
	<body>
		<p>The sines and cosines of the requested range of angles is as follows:</p>
	
		<table>
			<thead>
				<tr>
					<th>value (in degrees)</th>
					<th>sin(value)</th>
					<th>cos(value)</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="values" items ="${trigValues}">
					<tr>
						<td>${values.value}</td>
						<td>${values.roundedSin}</td>
						<td>${values.roundedCos}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	
	</body>
</html>