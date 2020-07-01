<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>文件上传(表单)</h2>
<form action="${ctx}/file/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file">
    <input type="submit" value="提交">
</form>

<hr>
<h2>文件下载</h2>
<a href="${ctx}/file/download">文件下载</a>
</body>
</html>
