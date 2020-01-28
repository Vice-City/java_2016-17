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
	  <c:when test="${currentUserId!=null}">
	  	You are already logged in! Why would you want to make multiple accounts? :]<br>
  		<a href="/blog/servleti/main">Back to blog list.</a>
	  </c:when>
	  <c:otherwise>
		<form action="register" method="post">
		
		Nickname <input type="text" name="nick" value='<c:out value="${registerForm.nick}"/>' size="20"><br>
		<c:if test="${registerForm.hasError('nick')}">
		<div class="error"><c:out value="${registerForm.getErrorMessage('nick')}"/></div>
		</c:if>
		
		Password <input type="text" name="password" value='<c:out value="${registerForm.password}"/>' size="20"><br>
		<c:if test="${registerForm.hasError('password')}">
		<div class="error"><c:out value="${registerForm.getErrorMessage('password')}"/></div>
		</c:if>
		
		First name <input type="text" name="firstName" value='<c:out value="${registerForm.firstName}"/>' size="20"><br>
		<c:if test="${registerForm.hasError('firstName')}">
		<div class="error"><c:out value="${registerForm.getErrorMessage('firstName')}"/></div>
		</c:if>
		
		Last name <input type="text" name="lastName" value='<c:out value="${registerForm.lastName}"/>' size="20"><br>
		<c:if test="${registerForm.hasError('lastName')}">
		<div class="error"><c:out value="${registerForm.getErrorMessage('lastName')}"/></div>
		</c:if>
		
		Email <input type="text" name="email" value='<c:out value="${registerForm.email}"/>' size="20"><br>
		<c:if test="${registerForm.hasError('email')}">
		<div class="error"><c:out value="${registerForm.getErrorMessage('email')}"/></div>
		</c:if>
		
		<input type="submit" name="method" value="Register">
		<input type="submit" name="method" value="Cancel">
		
		</form>
	  </c:otherwise>
	</c:choose>
	
	</div>
	</div>

  </body>
</html>