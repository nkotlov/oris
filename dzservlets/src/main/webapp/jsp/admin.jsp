<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Админ</title>
</head>
<body>
<h1>Админ</h1>
<p>Добро пожаловать, ${sessionScope.username}!</p>
<a href="/catalog">Каталог</a>
<br>
<form action="/catalog" method="post">
    <button type="submit">Выйти</button>
</form>
</body>
</html>
