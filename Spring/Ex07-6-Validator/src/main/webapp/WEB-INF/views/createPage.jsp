<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>createPage</title>
</head>
<body>
	createPage.jsp

	<br />
	
	<%
		String conPath = request.getContextPath();
	%>
	
	<form action="<%=conPath%>/student/create">
		이름 : <input type="text" name="name" value="${student.name }"> <br />
		아이디 : <input type="text" name="id" value="${student.id }"> <br />
		<input type="submit" value="전송"> <br />
	</form>
</body>
</html>