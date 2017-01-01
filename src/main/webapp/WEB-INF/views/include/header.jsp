<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <spring:url var="application_css" value="/resources/css/application.css"/>
    <spring:url var="default_css" value="/resources/css/default.css"/>

    <link href="${application_css}" rel="stylesheet">
    <link href="${default_css}" rel="stylesheet">

    <style>
        .header {background-color: cadetblue; padding: 1em 1em 1em 1em}
    </style>
</head>

<body>
    <div class="header">
        <h1>Spring MVC Apache Shiro Demo</h1>
    </div>
</body>
</html>