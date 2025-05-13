<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Hiển thị thời gian</title>
</head>
<body>
    <h2>Chọn định dạng thời gian:</h2>
    <form action="time" method="post">
        <input type="text" name="format" placeholder="VD: HH:mm:ss hoặc dd/MM/yyyy" 
               value="${param.format != null ? param.format : ''}" />
        <input type="submit" value="Hiển thị" />
    </form>

    <c:if test="${not empty time}">
        <p>Thời gian hiện tại: <strong><c:out value="${time}"/></strong></p>
    </c:if>

    <c:if test="${not empty error}">
        <p style="color:red;"><c:out value="${error}"/></p>
    </c:if>
</body>
</html>
