<%--
  Created by IntelliJ IDEA.
  User: ACER
  Date: 30/10/2024
  Time: 9:22 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>WELLCOME TO MyGTel</title>
</head>
<body>
<% String userId = (String) session.getAttribute("userId"); %>
<% String login = "Chào mừng bạn: " + userId; %>
<h1><%=login%></h1>
</body>
</html>
