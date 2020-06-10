<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Добавить</title>
    <style>
        body {
            font-family: "Helvetica Neue";
            font-size: 20px;
        }
        input {
            background-color: #DCDCDC;
            font-size: 20px;
            border-radius: 5px;
            border: 5px;
            height: 30px;
        }
        a {
            text-decoration: none;
        }
        div {
            width: 250px;
            height: 250px;
            position: absolute;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            margin: auto;
        }
    </style>
</head>
<body>
    <div>
        <form method="post" action="meals">
            <p>ID:<br/><input type="text" readonly="readonly" name="id" value="<c:out value="${meal.getId()}"/>"></p>
            <p><label for="dateTime">Дата и время:</label><br/><input type="datetime-local" id="dateTime" name="dateTime" value="<c:out value="${meal.getDateTime()}"/>"></p>
            <p><label for="description">Описание:</label><br/><input type="text" id="description" placeholder="Описание" name="description" value="<c:out value="${meal.getDescription()}"/>"></p>
            <p><label for="calories">Калории:</label><br/><input type="number" id="calories" name="calories" placeholder="Кол-во калорий" value="<c:out value="${meal.getCalories()}"/>"></p>
            <p><input type="submit" value="Send"></p>
        </form>
    </div>
</body>
</html>