<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
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
    <form method="get" action="meals">
        <p>Time from: <input type="time" id="timeFrom" name="timeFrom" value="<c:out value="${timeFrom}"/>"></p>
        <p>Time to: <input type="time" id="timeTo" name="timeTo" value="<c:out value="${timeTo}"/>"></p>
        <p>Date from: <input type="date" id="dateFrom" name="dateFrom" value="<c:out value="${dateFrom}"/>"></p>
        <p>Date to: <input type="date" id="dateTo" name="dateTo" value="<c:out value="${dateTo}"/>"></p>
        <button type="">Apply</button>
    </form>
</section>
</body>
</html>