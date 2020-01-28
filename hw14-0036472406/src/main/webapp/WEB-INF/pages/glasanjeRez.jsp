<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<style type="text/css">
			@import url("/voting-app/style.jsp");
			
			table.rez td {
				text-align: center;
			}
		</style>
	</head>
	
	<body>
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	
	<table border="1" class="rez">
		<thead>
			<tr>
				<th>Opcija</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="pollOption" items="${results}">
				<tr>
					<td>${pollOption.optionTitle}</td>
					<td>${pollOption.votesCount}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<p><a href="/voting-app/servleti/glasanje?pollId=${pollId}">Glasaj ponovno!</a></p>
	
	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart" src="/voting-app/servleti/glasanje-grafika?pollId=${pollId}" />
	
	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a href="/voting-app/servleti/glasanje-xls?pollId=${pollId}">ovdje</a>.
	</p>
	
	<h2>Razno</h2>
	<p>Reprezentativni primjeri pobjednika:</p>
	<ul>
		<c:forEach var="pollOption" items="${winningResults}">
			<li><a href="${pollOption.optionLink}" target="blank" >${pollOption.optionTitle}</a></li>
		</c:forEach>
	</ul>
	
	<p><a href="/voting-app/index.html">Povratak na početnu stranicu.</a>
</body>
</html>