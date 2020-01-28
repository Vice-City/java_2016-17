<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <head>
  	<style type="text/css">
	  @import url("/blog/style.jsp");
	</style>
  </head>

  <body>
	<div class="container">
	
	<div class="innerContainer">
	  <c:choose>
	  	<c:when test="${currentUserId==null}">
	  	  You are not logged in as a user.
	  	</c:when>
	  	<c:otherwise>
			Hi, ${currentUserFirstName} ${currentUserLastName}! You are logged in as ${currentUserNick}.<br> 
			<a href="/blog/servleti/logout">Click here to log out.</a>
	  	</c:otherwise>
	  </c:choose>
	</div>
	
	<br>
	
	<div class="innerContainer">
	<c:choose>
		<c:when test="${entriesByNick.isEmpty()}">
			<c:choose>
				<c:when test="${servingNick.equals(currentUserNick)}">
					You don't have any blog entries yet!
				</c:when>
				<c:otherwise>
					User ${servingNick} doesn't have any blog entries!
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<p>Blog entries by ${servingNick}:</p>
			<ol>
			<c:forEach var="entry" items="${entriesByNick}">
				<li><a href="/blog/servleti/author/${servingNick}/${entry.id}">${entry.title}</a></li>
			</c:forEach>
			</ol>
		</c:otherwise>
	</c:choose>

  <c:if test="${currentUserNick.equals(servingNick)}">
  	<p><a href="/blog/servleti/author/${servingNick}/new">Add new entry.</a></p>
  </c:if>
  </div>
  
  <br>
  
  <div class="innerContainer">
  	<p><a href="/blog/servleti/main">Back to main page.</a></p>
  </div>
  
  </div>

  </body>
</html>