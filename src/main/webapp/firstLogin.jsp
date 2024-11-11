<%--
  Created by IntelliJ IDEA.
  User: Windows
  Date: 18/10/2024
  Time: 10:37 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="funix.asm.login_mygtel.model.HintQuestion" %>
<% List<HintQuestion> hintQuestions = (List<HintQuestion>) session.getAttribute("hintQuestions");
%>
<html>
<head>
    <title>First Time Login - Set Sercurity Question</title>
    <link rel="stylesheet" type="text/css" href="WEB-INF/css/firstLogin.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="js/validation.js"></script>
</head>
<body>
<h2>First Time Login - Set Security Questions</h2>
<br>
<p>Please answer al least one of the hint questions below, Maximum selection is three question</p>
<form action="setSecurityQuestions" method="post" onsubmit="return dupilicateQuestions()">
    <label for="hintQuestion1">Hint Question 1:</label>
    <select id="hintQuestion1" name="hintQuestion1" required>
        <% for (HintQuestion question : hintQuestions) { %>
        <option value="<%=question.getQuestionId()%>">
            <%=question.getQuestion()%>
        </option>
        <%}%>
    </select>
<br>
    <label for="answer1">Answer 1:</label>
    <input type="text" id="answer1" name="answer1" required>
    <br>
    <label for="hintQuestion2">Hint Question 2:</label>
    <select id="hintQuestion2" name="hintQuestion2" required>
        <% for (HintQuestion question : hintQuestions) { %>
        <option value="<%=question.getQuestionId()%>">
            <%=question.getQuestion()%>
        </option>
        <%}%>
    </select>
    <br>
    <label for="answer2">Answer 2:</label>
    <input type="text" id="answer2" name="answer2" required>
    <br>
    <label for="hintQuestion3">Hint Question 3:</label>
    <select id="hintQuestion3" name="hintQuestion3" required>
        <% for (HintQuestion question : hintQuestions) { %>
        <option value="<%=question.getQuestionId()%>">
            <%=question.getQuestion()%>
        </option>
        <%}%>
    </select>
    <br>
    <label for="answer3">Answer 3:</label>
    <input type="text" id="answer3" name="answer3" required>
    <br>
    <p>Please key in your old password and new password.
        The new password must be different from the old password</p>
    <label for="oldPass"> Old Password: </label>
    <input type="password" id="oldPass" name="oldPass" maxlength="8" required>
    <br>
    <label for="newPass">New Password: </label>
    <input type="password" id="newPass" name="newPass" maxlength="8" required>
    <br>
    <label for="confirmNewPass">Confirm New Password: </label>
    <input type="password" id="confirmNewPass" name="confirmNewPass" maxlength="8" required>
    <br>
    <% String passNotDupilicate = (String) request.getAttribute("error"); %>
    <p><%=passNotDupilicate%></p>
    <br>
    <input type="submit" value="Submit">
    <input type="submit" value="Cancel">
    <input type="reset" value="Clear">
</form>
</body>
</html>
