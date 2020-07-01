<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true"%>
<%@ attribute name="paginationSize" type="java.lang.Integer" required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    int current =  page.getNumber() + 1;
    int begin = Math.max(1, current - paginationSize/2);
    int end = Math.min(begin + (paginationSize - 1), page.getTotalPages());

    request.setAttribute("current", current);
    request.setAttribute("begin", begin);
    request.setAttribute("end", end);
%>
<div>
    <ul class="pagination">
        <% if (page.hasPrevious()){%>
        <li><a href="?page=1&sortType=${sortType}&${searchParams}">首页</a></li>
        <li><a href="?page=${current-1}&sortType=${sortType}&${searchParams}">上一页</a></li>
        <%}else{%>
        <li class="disabled"><a href="#">首页</a></li>
        <li class="disabled"><a href="#">上一页</a></li>
        <%} %>

        <c:forEach var="i" begin="${begin}" end="${end}">
            <c:choose>
                <c:when test="${i == current}">
                    <li class="active"><a href="?page=${i}&sortType=${sortType}&${searchParams}">${i}</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="?page=${i}&sortType=${sortType}&${searchParams}">${i}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <% if (page.hasNext()){%>
        <li><a href="?page=${current+1}&sortType=${sortType}&${searchParams}">下一页</a></li>
        <li><a href="?page=${page.totalPages}&sortType=${sortType}&${searchParams}">尾页</a></li>
        <%}else{%>
        <li class="disabled"><a href="#">下一页</a></li>
        <li class="disabled"><a href="#">尾页</a></li>
        <%} %>
    </ul>
    <ul class="pagination">
        <li><a>共${page.totalPages}页，每页${page.size}条，共${page.totalElements}条数据</a></li>
    </ul>
</div>

