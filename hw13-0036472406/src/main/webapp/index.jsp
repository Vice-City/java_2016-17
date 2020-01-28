<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<style type="text/css">
			@import url("/webapp2/style");
		</style>
	</head>
	
	<body>
		<h2>Background color</h2>
		<p>
		Hey there! You can change the background color by
		<a href="/webapp2/colors.jsp">clicking this link</a>.
		</p>
	
		<h2>Trigonometric values</h2>
		<p>
		<a href="/webapp2/trigonometric?a=0&b=90">Click here</a>
		for trigonometric values for degrees from 0 to 90.
		</p>
		
		<form action="trigonometric" method="GET">	
		Start angle:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
		End angle:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
		<input type="submit" value="Tabulate"><input type="reset" value="Reset">
		</form>
		
		<h2>Funny story</h2>
		<p><a href="/webapp2/stories/funny.jsp">Click here</a> to read a funny story.</p>
		
		<h2>OS usage statistics</h2>
		<p><a href="/webapp2/report.jsp">Click here</a> to view some OS usage statistics.</p>
		
		<h2>Power calculator</h2>
		<p>
			<a href="/webapp2/powers2?a=1&b=100&n=3">Click here</a>
			to download an Excel spreadsheet with powers of a predefined range of numbers.
		</p>
		
		<form action="powers2" method="GET">	
		Start value:<br><input type="number" name="a" min="-100" max="100" step="1" value="0"><br>
		End value:<br><input type="number" name="b" min="-100" max="100" step="1" value="100"><br>
		Power value:<br><input type="number" name="n" min="1" max="5" step="1" value="3"><br>
		<input type="submit" value="Download spreadsheet"><input type="reset" value="Reset">
		</form>
		
		<h2>Running time</h2>
		<p>
			<a href="/webapp2/appinfo.jsp">Click here</a>
			to check how long this webapp has been running for.
		</p>
		
		<h2>Vote for your favorite band</h2>
		<p>
			<a href="/webapp2/glasanje">Click here</a>
			to vote for your favorite band! And maybe learn a bit of croatian. :]
		</p>
	
	</body>
</html>