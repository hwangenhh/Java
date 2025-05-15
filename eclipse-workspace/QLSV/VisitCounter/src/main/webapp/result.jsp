<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Kết quả</title></head>
<body>
    <c:choose>
        <c:when test="${not empty requestScope.message}">
            <p><strong><c:out value="${requestScope.message}" /></strong></p>
        </c:when>
        <c:otherwise>
            <p>Không có thông tin đăng nhập.</p>
        </c:otherwise>
    </c:choose>
</body>
</html>
