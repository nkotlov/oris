<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Collections</title>
</head>
<body>
<h1>Collections</h1>
<form method="post" action="/collections">
    <input type="text" name="name" placeholder="Collection Name" required>
    <textarea name="description" placeholder="Description"></textarea>
    <button type="submit">Add Collection</button>
</form>

<h2>All Collections</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
    </tr>
    <c:forEach items="${collections}" var="collection">
        <tr>
            <td>${collection.id}</td>
            <td>${collection.name}</td>
            <td>${collection.description}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
