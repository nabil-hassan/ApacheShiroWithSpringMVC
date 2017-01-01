<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>

<head>
    <title>Login</title>
    <%@ include file="include/header.jsp"%>
    <spring:url var="login_form_submit" value="/loginForm/submit"/>
    <spring:url var="create_user_form_submit" value="/createUserForm/submit"/>
</head>

<body>

<div id="loginForm" class="smallForm">
    <h3>Already have a user? Login here</h3>

    <form action="${login_form_submit}" method="post">
        <div id="usernameField" class="formField">
            <span class="formField">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" value="${username}"/>
            </span>
        </div>

        <div id="passwordField" class="formField">
            <span>
                <label for="password">Password</label>
                <input type="password" id="password" name="password" value="${password}"/>
            </span>
        </div>

        <div id="rememberMeField" class="formField">
            <span>
                <label for="rememberMe">Remember me</label>
                <input type="checkbox" id="rememberMe" name="rememberMe"/>
            </span>
        </div>

        <div id="submitField" class="formSubmitField">
            <span>
                <button id="submit">Login</button>
            </span>
        </div>

        <div>
            <p class="errorMessage">${loginErrorMsg}</p>
        </div>
    </form>
</div>

<div id="signupForm" class="smallForm">
    <h3>Need to create a new user? </h3>

    <form action="${create_user_form_submit}" method="post">
        <div id="newUsernameField" class="formField">
            <span class="formField">
                <label for="username">Username</label>
                <input type="text" id="newUsername" name="username" value="${newUsrUsername}"/>
            </span>
        </div>

        <div id="newPasswordField" class="formField">
            <span>
                <label for="password">Password</label>
                <input type="password" id="newPassword" name="password" value="${newUsrPassword}"/>
            </span>
        </div>

        <div id="newUserRememberMeField" class="formField">
            <span>
                <label for="rememberMe">Remember me</label>
                <input type="checkbox" id="newUserRememberMe" name="rememberMe"/>
            </span>
        </div>

        <div id="newUserSubmitField" class="formSubmitField">
            <span>
                <button id="newSubmit">Create User</button>
            </span>
        </div>

        <div>
            <p class="errorMessage">${createUserErrorMsg}</p>
        </div>
    </form>
</div>

</body>

</html>