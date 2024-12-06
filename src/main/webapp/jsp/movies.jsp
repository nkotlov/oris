<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Movies</title>
</head>
<body>
<h1>Movies</h1>
<form method="post" action="/movies">
    <input type="hidden" name="action" value="create">
    <input type="text" name="title" placeholder="Title" required>
    <textarea name="description" placeholder="Description"></textarea>
    <input type="number" name="releaseYear" placeholder="Release Year" required>
    <button type="submit">Add Movie</button>
</form>

<h2>All Movies</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Description</th>
        <th>Release Year</th>
        <th>Actions</th>
    </tr>
    <c:forEach items="${movies}" var="movie">
        <tr>
            <td>${movie.id}</td>
            <td>${movie.title}</td>
            <td>${movie.description}</td>
            <td>${movie.releaseYear}</td>
            <td>
                <form method="post" action="/movies" style="display: inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${movie.id}">
                    <button type="submit">Delete</button>
                </form>
                <form method="get" action="/edit-movie" style="display: inline;">
                    <input type="hidden" name="id" value="${movie.id}">
                    <button type="submit">Edit</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
