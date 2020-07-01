<%--
  Created by IntelliJ IDEA.
  User: 半夏Koreyoshi
  Date: 2020/6/21
  Time: 21:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>用户新增</title>
    <link rel="stylesheet" href="${ctx}/static/bootstrap-3.3.5/css/bootstrap.min.css"/>
    <script src="${ctx}/static/jquery/jquery-1.9.1.min.js"></script>
    <!-- 搬运到java时删除 -->
    <link href="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="../../../../../../../../../../嗨，大学/大三下/一体化3/src/main/webapp/static/commons.js"></script>
    <!-- 搬运到java时删除 -->
</head>
<body>
<div class="container">
    <tags:nav/>
    <div class="page-header">
        <h3>用户管理${action}</h3>
    </div>
    <form method="post" action="${ctx}/user/${action}">
        <input type="hidden" name="id" value="${user.id}">
        <!-- 所有input的name和value属性要后端根据属性值改 -->
        <p>账号：<input type="text" class="inpu-medium" name="username" value="${user.username}"></p>
        <p>密码：<input type="text" class="input-medium" name="password" value="${user.password}"></p>
        <p>昵称：<input type="text" class="input-medium" name="name" value="${user.name}"></p>
        <p>年龄：<input type="text" class="input-medium" name="age" value="${user.age}"></p>
        <p>性别：<select class="input-medium" name="sex" value="${user.sex}">
            <option value="1">男</option>
            <option value="2">女</option>
        </select></p>
        <p><button type="submit" class="btn btn-primary">保存</button></p>
    </form>
</div>

<script src="${ctx}/static/bootstrap-3.3.5/js/bootstrap.min.js"></script>
<!-- 搬运到java时删除 -->
<script src="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
<!-- 搬运到java时删除 -->
</body>
</html>
