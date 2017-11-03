<%--
  Created by IntelliJ IDEA.
  User: x3408
  Date: 2017/11/1
  Time: 13:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <%
    request.getSession();
    request.getRequestDispatcher("/TeacherServiceServlet").forward(request, response);
  %>
  ${sessionScope.get("课程名")}
  </body>
</html>
