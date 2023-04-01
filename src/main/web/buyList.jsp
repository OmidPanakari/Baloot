<%@ page import="com.baloot.core.entities.User" %>
<%@ page import="com.baloot.core.entities.Commodity" %><%--
  Created by IntelliJ IDEA.
  User: ompan
  Date: 3/31/2023
  Time: 1:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en"><head>
    <meta charset="UTF-8">
    <title>User</title>
    <style>
        li {
            padding: 5px
        }
        table{
            width: 100%;
            text-align: center;
        }
    </style>
</head>
<body>
<%
    User user = (User) request.getAttribute("user");
    var buyListPrice = user.getBuyList().stream().mapToInt(Commodity::getPrice).sum();
%>
<a href="/">Home</a>
<ul>
    <li id="username">Username: <%=user.getBuyList()%></li>
    <li id="email">Email: <%=user.getEmail()%></li>
    <li id="birthDate">Birth Date: <%=user.getBirthDate()%></li>
    <li id="address"><%=user.getAddress()%></li>
    <li id="credit">Credit: <%=user.getCredit()%></li>
    <li>Current Buy List Price: <%=buyListPrice%></li>
    <li>
        <a href="/credit">Add Credit</a>
    </li>
    <li>
        <form action="" method="POST">
            <label>Submit & Pay</label>
            <input id="payment" type="hidden" name="username" value="<%=user.getUsername()%>">
            <br/>
            <label for="discount_text">Discount</label>
            <input id="discount_text" type="text" name="discount" value="" />
            <button type="submit" name="action" value="pay">Payment</button>
        </form>
    </li>
</ul>
<table>
    <caption>
        <h2>Buy List</h2>
    </caption>
    <tbody><tr>
        <th>Id</th>
        <th>Name</th>
        <th>Provider Name</th>
        <th>Price</th>
        <th>Categories</th>
        <th>Rating</th>
        <th>In Stock</th>
        <th></th>
        <th></th>
    </tr>
    <% for (Commodity commodity : user.getBuyList()) { %>
    <tr>
        <td><%=commodity.getId()%></td>
        <td><%=commodity.getName()%></td>
        <td><%=commodity.getProviderName()%></td>
        <td><%=commodity.getPrice()%></td>
        <td><%=commodity.getCategoryString()%></td>
        <td><%=commodity.getRating()%></td>
        <td><%=commodity.getInStock()%></td>
        <td><a href="<%="/commodities/" + commodity.getId()%>">Link</a></td>
        <td>
            <form action="" method="POST">
                <input id="remove_commodity" type="hidden" name="commodityId" value="<%=commodity.getId()%>">
                <button type="submit" name="action" value="remove">Remove</button>
            </form>
        </td>
    </tr>
    <% } %>
    </tbody></table>
</body></html>
