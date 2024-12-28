<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Каталог</title>
</head>
<body>
<h1>Каталог</h1>

<c:if test="${not empty sessionScope.username}">
    <p>Добро пожаловать, ${sessionScope.username}!</p>
    <form action="/catalog" method="post">
        <button type="submit">Выйти</button>
    </form>
</c:if>

</body>
</html>
