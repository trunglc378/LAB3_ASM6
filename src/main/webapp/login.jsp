<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="WEB-INF/css/login.css">
</head>
<body>
<h2>Login to MyGSeT Portal</h2>
<%
    String loginFail = (String) request.getAttribute("loginFail");
    if (loginFail != null) {
%>
<p class="error"><%=loginFail%>
</p>
<%--<p>${loginFail}</p>--%>
<%
    }
%>
<form action="login" method="post">
    <label for="userId">User ID:</label>
    <input type="text" id="userId" name="userId" maxlength="16" required>
    <br>
    <label for="password">Password:</label>
    <input type="password" id="password" name="password" maxlength="8" required>
    <br>
    <input type="submit" value="Submit">
    <input type="reset" value="Clear">
</form>
<a href="#">Forgot Password?</a>
</body>
</html>