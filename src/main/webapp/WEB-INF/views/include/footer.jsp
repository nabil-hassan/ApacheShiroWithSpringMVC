<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <style>
        .footer {background-color: #7d8084; padding: 0.5em 0.5em 0.5em 0.5em; position:fixed;
            bottom:0; width: 100%; text-align: center;}
        .footerField {margin-right:3em;}
        .footerField label {font-weight:800;}
    </style>
</head>

<body>
<div class="footer">
    <span class="footerField">
        <label>Username:</label>
        <%= session.getAttribute( "username" ) %>
    </span>
    <span class="footerField">
        <label>Login Time:</label>

        <fmt:formatDate type="both"
                        pattern="yyyy-MM-dd HH:mm:ss"
                        value="${sessionScope.loginTime}" />
    </span>
</div>
</body>
</html>