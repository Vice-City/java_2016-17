<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<style type="text/css">
			@import url("/webapp2/style");
			
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
				<th>Bend</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="band" items="${bandToVoteMap }">
				<tr>
					<td>${band.key.name}</td>
					<td>${band.value}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<p><a href="/webapp2/glasanje?
		<c:forEach var="band" items="${bandToVoteMap}">
			${band.key.name }=${band.value }&
		</c:forEach> ">Glasaj ponovno!</a>
	
	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart" src="/webapp2/glasanje-grafika" />
	
	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a href="/webapp2/glasanje-xls">ovdje</a>.
	</p>
	
	<h2>Razno</h2>
	<p>Primjeri pjesama pobjedničkih bendova:</p>
	<ul>
		<c:forEach var="winningBand" items="${winningBands}">
			<li><a href="${winningBand.link}" target="blank" >${winningBand.name}</a></li>
		</c:forEach>
	</ul>
	
	<p><a href="/webapp2/index.jsp" >Povratak na početnu stranicu.</a>
</body>
</html>