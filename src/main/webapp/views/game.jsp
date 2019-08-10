<%@ page import="com.ex.task.Task" %>
<%@ page import="java.util.ArrayList" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%@page contentType="text/html;charset=UTF-8" language="java"%>

<html>
<head>
    <link rel="stylesheet" href="../resources/css/font.css" type="text/css">
    <title> Игра быки и коровы </title>
    <link href="${contextPath}/resources/css/font.css" rel="stylesheet">
    <meta id="_csrf_token" value="${_csrf.token}"/>

    <script src="http://code.jquery.com/jquery-2.2.4.js"
            type="text/javascript"></script>
    <script src="${contextPath}/resources/js/app-ajax.js" type="text/javascript"></script>
    <script>
        var url = "${contextPath}/newAttempt";
    </script>

    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</head>

<body>
<ul>
    <li class="rating"><a href="${contextPath}/rating">Рейтинг игроков</a></li>
    <button style="float:right" class="button" type="submit" onclick="document.forms['logoutForm'].submit()">Выйти</button>


    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

       <p class="cherk"> Вы вошли как: ${pageContext.request.userPrincipal.name}


    </c:if>

</ul>
<h1 style="margin-top: 50px"> Игра быки и коровы </h1>
<h2> Я загадал число, попробуй угадать его... </h2>
<table>
    <tr >
        <th style="width: 800px"> <textarea  readonly id="textarea"></textarea></th>
        <th style="width: 800px"> <textarea  readonly id="textareaHistory"></textarea></th>
    </tr>

    <tr>
         <th><div class="text">Поле для ввода числа:</div>
        <input class="input"  type="text" id = "inputNumber" maxlength='4' minlength='4'
                 placeholder="Введите 4-х значное число"  onkeyup="return proverka(this);"
                 onchange="return proverka(this); " required /> </th>

        <th>Здесь будет средний рейтинг</th>


        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </tr>

    <tr>
        <th> <span id="error"> </span></th>
        <th><button  class="button" type="submit" id="history">Посмотреть историю</button></th>
    </tr>
    <tr>
        <th><button  class="button" type="submit"  id="toSend">Отправить</button></th>
        <th> <button  class="button" type="submit" id="clearHistory">Очистить историю</button></th>
    </tr>

</table>
</body>
</html>
