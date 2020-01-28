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
			<h2>Login</h2>
			<form action="main" method="post">
			
			Nickname <input type="text" name="nick" value='<c:out value="${loginForm.nick}"/>'><br>
			<c:if test="${loginForm.hasError('nick')}">
			<div class="error"><c:out value="${loginForm.getErrorMessage('nick')}"/></div>
			</c:if>
			
			Password <input type="password" name="password" value='<c:out value="${loginForm.password}"/>'><br>
			<c:if test="${loginForm.hasError('password')}">
			<div class="error"><c:out value="${loginForm.getErrorMessage('password')}"/></div>
			</c:if>
	
			<input type="submit" name="method" value="Login">
			
			</form>
			
			<p>Not an existing user? <a href="/blog/servleti/register" >Register here!</a></p>
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
	 <c:when test="${registeredUsers.isEmpty()}">
	   No users have yet created blogs!
	 </c:when>
	 <c:otherwise>
	   <h2>Registered users' blogs:</h2>
	   <ul>
	   <c:forEach var="user" items="${registeredUsers}">
	   	<li><a href="/blog/servleti/author/${user.nick}/">${user.nick}</a></li>
	   </c:forEach>
	   </ul>
	 </c:otherwise>
	</c:choose>
	
	</div>
	</div>

  </body>
</html>