<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>

<head>
    <%@ include file="include/header.jsp"%>
    <spring:url value="/logout" var="logout_url"/>

    <title>Welcome to Spring MVC Java Config</title>
</head>

<body>

<div class="main">
    <h1>Where to?</h1>

     <a href="${logout_url}">Logout</a>
    <%--<ul>--%>
        <%--<li> <a href="customerList">Customer List</a> </li>--%>
        <%--<li> <a href="customerForm">Add Customer</a> </li>--%>
    <%--</ul>--%>
</div>

</body>

<footer>
    <%@ include file="include/footer.jsp"%>
</footer>

</html>
