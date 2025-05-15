<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Visit Counter </title>
</head>
<body>
<h2>Số lượt truy cập: ${applicationScope.visitCount}</h2>

    <c:if test="${applicationScope.visitCount > 10}">
    
        <p><strong>Trang đã được truy cập hơn 10 lần!</strong></p>
    </c:if>
   <%-- Gỡ lỗi: kiểm tra xem biến có tồn tại không --%>
<p>Debug: visitCount = ${applicationScope.visitCount}</p>
   

</body>
</html>