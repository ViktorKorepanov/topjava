<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Calories counter</title>
    <style>
        body {
            font-family: "Helvetica Neue";
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
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2 style="text-align: center;">Meals</h2>
    <table cellspacing="0">
        <tbody>
            <tr>
                <td><strong>Date/Time</strong></td>
                <td><strong>Description</strong></td>
                <td><strong>Calories</strong></td>
            </tr>
            <c:forEach var="meal" items="${meals}">
                <c:if test="${meal.isExcess()}"><tr style="color: #ff0000"></c:if>
                <c:if test="${!meal.isExcess()}"><tr style="color: #32cd32"></c:if>
                <td>${meal.getDateTime().toLocalDate()} ${meal.getDateTime().toLocalTime()}</td>
                    <td>${meal.getDescription()}</td>
                    <td>${meal.getCalories()}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
