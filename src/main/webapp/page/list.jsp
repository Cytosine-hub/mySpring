<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2022/2/19
  Time: 15:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>查询交易记录</title>
</head>
<body>
<form method="get" action="/record/list">
    账号<input name="accountId">
    <button type="submit">查询</button>
</form>
</body>
</html>
