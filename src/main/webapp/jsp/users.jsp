<%@ page import="ru.itis.models.User" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: tron5
  Date: 27.11.2024
  Time: 9:11
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<form action="/profile" method="post">
    <input name="first_name" placeholder="Your name">
    <input name="last_name" placeholder="Your surname">
    <input type="submit" value="Send">
</form>
<table>
    <tr>
        <th>ID</th>
        <th>FIRST NAME</th>
        <th>LAST NAME</th>
        <th>AGE</th>
    </tr>

    <c:forEach items="${usersForJsp}" var="user">
        <tr>
            <td>${user.id}</td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.age}</td>
        </tr>
    </c:forEach>

    <%--    <%--%>
    <%--        List<User> users = (List<User>) request.getAttribute("usersForJsp");--%>
    <%--        for (int i = 0; i < users.size(); i++) {%>--%>
    <%--    <tr>--%>
    <%--        <td><%=users.get(i).getId()%></>--%>
    <%--        <td><%=users.get(i).getFirstName()%></>--%>
    <%--        <td><%=users.get(i).getLastName()%></>--%>
    <%--        <><%=users.get(i).getAge()%></>--%>
    <%--    </tr>--%>
    <%--    <%}%>--%>

</table>
</body>
</html>