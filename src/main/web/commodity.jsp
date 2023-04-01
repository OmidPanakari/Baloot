<%@ page import="com.baloot.core.entities.Commodity" %>
<%@ page import="com.baloot.core.entities.Comment" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: ompan
  Date: 3/31/2023
  Time: 1:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Commodity</title>
    <style>
        li {
            padding: 5px;
        }
        table {
            width: 100%;
            text-align: center;
        }
    </style>
</head>
<body>
<%
    String username = (String) request.getSession(false).getAttribute("username");
    Commodity commodity = (Commodity) request.getAttribute("commodity");
    List<Comment> comments = (List<Comment>) request.getAttribute("comments");
    List<Commodity> suggestions = (List<Commodity>) request.getAttribute("suggestions");
%>
<a href="/">Home</a>
<br>
<span>username: <%=username%></span>
<br>
<ul>
    <li id="id">Id: <%=commodity.getId()%></li>
    <li id="name">Name: <%=commodity.getName()%></li>
    <li id="providerName">Provider Name: <%=commodity.getProviderId()%></li>
    <li id="price">Price: <%=commodity.getPrice()%></li>
    <li id="categories">Categories: <%=commodity.getCategoryString()%></li>
    <li id="rating">Rating: <%=commodity.getRating()%></li>
    <li id="inStock">In Stock: <%=commodity.getInStock()%></li>
</ul>

<label>Add Your Comment:</label>
<form action="" method="post">
    <input type="text" name="comment" value="" />
    <button type="submit" name="action" value="comment">submit</button>
</form>
<br>
<form action="" method="POST">
    <label>Rate(between 1 and 10):</label>
    <input type="number" id="quantity" name="quantity" min="1" max="10">
    <button type="submit" name="action" value="rate">Rate</button>
</form>
<br>
<form action="" method="POST">
    <button type="submit" name="action" value="add">Add to BuyList</button>
</form>
<br />
<table>
    <caption><h2>Comments</h2></caption>
    <tr>
        <th>username</th>
        <th>comment</th>
        <th>date</th>
        <th>likes</th>
        <th>dislikes</th>
    </tr>
    <% for (Comment comment : comments) {%>
    <tr>
        <td><%=comment.getUsername()%></td>
        <td><%=comment.getText()%></td>
        <td><%=comment.getDate()%></td>
        <td>
            <form action="" method="POST">
                <label for="like_comment"><%=comment.getLikes()%></label>
                <input
                        id="like_comment"
                        type="hidden"
                        name="commentId"
                        value=<%=comment.getId()%>
                />
                <button type="submit" name="action" value="like">like</button>
            </form>
        </td>
        <td>
            <form action="" method="POST">
                <label for="dislike_comment"><%=comment.getDislikes()%></label>
                <input
                        id="dislike_comment"
                        type="hidden"
                        name="commentId"
                        value=<%=comment.getId()%>
                />
                <button type="submit" name="action" value="dislike">dislike</button>
            </form>
        </td>
    </tr>
    <%}%>
</table>
<br><br>
<table>
    <caption><h2>Suggested Commodities</h2></caption>
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
    <% for (Commodity suggestion : suggestions) { %>
    <tr>
        <td><%=suggestion.getId()%></td>
        <td><%=suggestion.getName()%></td>
        <td><%=suggestion.getProviderName()%></td>
        <td><%=suggestion.getPrice()%></td>
        <td><%=suggestion.getCategoryString()%></td>
        <td><%=suggestion.getRating()%></td>
        <td><%=suggestion.getInStock()%></td>
        <td><a href="<%="/commodities/" + suggestion.getId()%>">Link</a></td>
    </tr>
    <% } %>
</table>
</body>
</html>
