<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Счетчик каллорий</title>
    <style>
        body {
            font-family: "Helvetica Neue";
            font-size: 20px;
        }
        table {
            border: 0px;
            width: 100%;
        }
        tr, td {
            height: 40px;
            font-size: 20px;
        }
        tr:nth-child(odd) {
            background-color: #DCDCDC;
        }
        button {
            font-size: 20px;
            background-color: #87CEFA;
            border-radius: 5px;
            border: 5px;
            height: 30px;
        }
        a {
            text-decoration: none;
        }
    </style>
</head>
<body>
<h1 style="text-align: center;">Счетчик калорий</h1>
    <p></p>
    <table cellspacing="0" cellpadding="10">
        <tbody>
            <tr>
                <td><strong>Дата и время</strong></td>
                <td><strong>Описание</strong></td>
                <td><strong>Калории</strong></td>
                <td></td>
                <td></td>
            </tr>
<%--        Проход циклом по переданному отфильтрованному списку, вытаскиание параметров у каждого элемента и размещение в таблице--%>
            <c:forEach var="meal" items="${meals}">
                <c:if test="${meal.isExcess()}"><tr style="color: #ff0000"></c:if>
                <c:if test="${!meal.isExcess()}"><tr style="color: #32cd32"></c:if>
                    <td>${meal.getDateTime().toLocalDate()} ${meal.getDateTime().toLocalTime()}</td>
                    <td>${meal.getDescription()}</td>
                    <td>${meal.getCalories()}</td>
                    <td><button><a href="meals?action=edit&id=<c:out value="${meal.getId()}"/>">Изменить</a></button></td>
                    <td><button><a href="meals?action=delete&id=<c:out value="${meal.getId()}"/>">Удалить</a></button></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <p></p>
    <div align="center"><button><a href="meals?action=add">Добавить</a></button></div>
</body>
</html>
