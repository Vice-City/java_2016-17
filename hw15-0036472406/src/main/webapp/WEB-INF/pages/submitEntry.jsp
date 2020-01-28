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
      <c:when test="${entryForm.id.isEmpty()}">
	  <h2>New entry</h2>
	  </c:when>
	  <c:otherwise>
      <h2>Edit entry</h2>
	  </c:otherwise>
	</c:choose>
	
 	<form action="save" method="post">
 	
	<input type="hidden" name="id" value='<c:out value="${entryForm.id}"/>' size="20"><br>
	
	Title <input type="text" name="title" value='<c:out value="${entryForm.title}"/>' size="20"><br>
	<c:if test="${entryForm.hasError('title')}">
	<div class="error"><c:out value="${entryForm.getErrorMessage('title')}"/></div>
	</c:if>
	
	Text <input type="text" name="text" value='<c:out value="${entryForm.text}"/>' size="20"><br>
	<c:if test="${entryForm.hasError('text')}">
	<div class="error"><c:out value="${entryForm.getErrorMessage('text')}"/></div>
	</c:if>
	
	<input type="submit" name="method" value="Save">
	<input type="submit" name="method" value="Cancel">
	
	</form>	
 	
 	</div>
 	</div>
  </body>
</html>