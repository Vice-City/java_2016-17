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
 	<h1>${currentEntry.title}</h1>
 	<p>${currentEntry.text}<p>
 	Posted on: ${currentEntry.createdAt}
 	<c:if test="${currentEntry.lastModifiedAt!=null}">
 	<br>Last modified: ${currentEntry.lastModifiedAt}
 	</c:if>
 	
 	<c:if test="${currentUserNick.equals(servingNick)}">
  	  <p><a href="/blog/servleti/author/${currentUserNick}/edit?entryId=${currentEntry.id}">Edit entry.</a></p>
 	</c:if>
 	</div>
 	
 	<br>
 	
 	<div class="innerContainer">
 	<c:choose>
 		<c:when test="${blogComments.isEmpty()}">
 			 <p>No comments have been made yet!</p>
 		</c:when>
 		<c:otherwise>
		 	 <c:forEach var="comment" items="${blogComments}">
		 	 <div class="commentContainer">
		 	 <p>${comment.message}</p>
		     Posted by ${comment.usersEMail} on ${comment.postedOn}.
		 	 </div>
		 	 <br>
		 	 </c:forEach>
 		</c:otherwise>
 	</c:choose>
 	
 	<h2>Post new comment</h2>
 	
	<form method="post">
	
	Message <input type="text" name="message" value='<c:out value="${commentForm.message}"/>' size="20"><br>
	<c:if test="${commentForm.hasError('message')}">
	<div class="error"><c:out value="${commentForm.getErrorMessage('message')}"/></div>
	</c:if>
	
	Email <input type="text" name="usersEMail" value='<c:out value="${commentForm.usersEMail}"/>' size="20"><br>
	<c:if test="${commentForm.hasError('usersEMail')}">
	<div class="error"><c:out value="${commentForm.getErrorMessage('usersEMail')}"/></div>
	</c:if>
	
	<input type="submit" name="method" value="Post">
	
	</form>
	</div>
	
	<br>
	
	<div class="innerContainer">
  		<a href="/blog/servleti/author/${servingNick}">Back to blog entries.</a><br>
  		<a href="/blog/servleti/main">Back to main page.</a>
	</div>
	
	</div>
	
  </body>
</html>