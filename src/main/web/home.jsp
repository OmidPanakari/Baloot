<%--
  Created by IntelliJ IDEA.
  User: ompan
  Date: 3/29/2023
  Time: 1:01 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en"><head>
  <meta charset="UTF-8">
  <title>Home</title>
</head>
<body>
<%
  String username = (String) request.getSession(false).getAttribute("username");
%>
<ul>
  <li id="email">username: <%=username%></li>
  <li>
    <a href="/commodities">Commodities</a>
  </li>
  <li>
    <a href="/buyList">Buy List</a>
  </li>
  <li>
    <a href="/credit">Add Credit</a>
  </li>
  <li>
    <a href="/logout">Log Out</a>
  </li>
</ul>

</body></html>
