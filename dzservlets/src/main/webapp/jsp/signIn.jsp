<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Вход</title>
</head>
<body>
<h1>Вход</h1>

<form action="/signIn" method="post">
    <label for="identifier">Email или Имя пользователя:</label>
    <input type="text" id="identifier" name="identifier" placeholder="Введите email или имя профиля" required>
    <br>
    <label for="password">Пароль:</label>
    <input type="password" id="password" name="password" placeholder="Введите пароль" required>
    <br>
    <button type="submit">Войти</button>
</form>

<!-- Отображение ошибок -->
<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>

<p>Нет аккаунта? <a href="/signUp">Регистрация</a></p>
</body>
</html>
