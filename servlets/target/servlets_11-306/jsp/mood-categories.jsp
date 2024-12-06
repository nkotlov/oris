<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Mood Categories</title>
</head>
<body>
<h1>Mood Categories</h1>
<form method="post" action="/moods">
    <input type="text" name="name" placeholder="Category Name" required>
    <button type="submit">Add Category</button>
</form>

<h2>All Mood Categories</h2>
<ul>
    <c:forEach var="category" items="${categories}">
        <li>${category.name}</li>
    </c:forEach>
</ul>
</body>
</html>
