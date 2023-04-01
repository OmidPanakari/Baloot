<%--
  Created by IntelliJ IDEA.
  User: ompan
  Date: 4/1/2023
  Time: 1:57 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>400 Error</title>
</head>
<body>
<%
  String message = (String) request.getAttribute("message");
%>
<a href="/">Home</a>
<h1>400<br><%=message%></h1>
</body>
</html>
