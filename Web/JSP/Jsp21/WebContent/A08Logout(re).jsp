<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그아웃 페이지</title>
</head>
<body>
	<%
		session.invalidate();
		response.sendRedirect("A03Login.jsp");
	%>

</body>
</html>