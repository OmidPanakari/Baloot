<%@ page import="com.baloot.core.entities.Commodity" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: ompan
  Date: 3/31/2023
  Time: 10:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Commodities</title>
    <style>
        table{
            width: 100%;
            text-align: center;
        }
    </style>
</head>
<body>
<%
    String username = (String) request.getSession(false).getAttribute("username");
    List<Commodity> commodities = (List<Commodity>) request.getAttribute("commodities");
    var search = request.getParameter("search");
    var searchType = request.getParameter("searchType");
    var sort = request.getParameter("sort");
%>
<a href="/">Home</a>
<p id="username">username: <%=username%></p>
<br><br>
<form action="" method="GET">
    <label>Search:</label>
    <% if (sort != null) { %>
    <input type="hidden" name="sort" value="<%=sort%>" />
    <% } %>
    <input type="text" name="search" value="">
    <button type="submit" name="searchType" value="category">Search By Cagtegory</button>
    <button type="submit" name="searchType" value="name">Search By Name</button>
    <button type="submit" name="action" value="clear">Clear Search</button>
</form>
<br><br>
<form action="" method="GET">
    <label>Sort By:</label>
    <% if (search != null && searchType != null) { %>
    <input type="hidden" name="search" value="<%=search%>" />
    <input type="hidden" name="searchType" value="<%=searchType%>"/>
    <% } %>
    <button type="submit" name="sort" value="rate">Rate</button>
</form>
<br><br>
<table>
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Provider Name</th>
        <th>Price</th>
        <th>Categories</th>
        <th>Rating</th>
        <th>In Stock</th>
        <th>Links</th>
    </tr>
    <% for (Commodity commodity : commodities) { %>
    <tr>
        <td><%=commodity.getId()%></td>
        <td><%=commodity.getName()%></td>
        <td><%=commodity.getProviderId()%></td>
        <td><%=commodity.getPrice()%></td>
        <td><%=commodity.getCategoryString()%></td>
        <td><%=commodity.getRating()%></td>
        <td><%=commodity.getInStock()%></td>
        <td><a href="<%="/commodities/" + commodity.getId()%>">Link</a></td>
    </tr>
    <% } %>
</table>
</body>
