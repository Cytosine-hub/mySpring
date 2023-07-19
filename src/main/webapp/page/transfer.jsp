<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2022/2/19
  Time: 15:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>转账</title>
</head>
<body>
<form method="get" action="/account/transfer">
    收款账号<input name="inAccountId">
    付款账号<input name="outAccountId">
    支付密码<input name="payPassword">
    转账金额<input name="money">
    手机号码<input name="mobile">
    <button type="submit">转账</button>
</form>
</body>
</html>
