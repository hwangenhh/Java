<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hiển thị thời gian</title>
    <script>
        function startClock(format) {
            function pad(n) { return n < 10 ? '0' + n : n; }

            function updateClock() {
                const now = new Date();
                let str = "";

                if (format === "HH:mm:ss") {
                    str = pad(now.getHours()) + ":" + pad(now.getMinutes()) + ":" + pad(now.getSeconds());
                } else if (format === "dd/MM/yyyy") {
                    str = pad(now.getDate()) + "/" + pad(now.getMonth() + 1) + "/" + now.getFullYear();
                } else if (format === "dd/MM/yyyy HH:mm:ss") {
                    str = pad(now.getDate()) + "/" + pad(now.getMonth() + 1) + "/" + now.getFullYear()
                        + " " + pad(now.getHours()) + ":" + pad(now.getMinutes()) + ":" + pad(now.getSeconds());
                } else {
                    str = now.toLocaleString();
                }

                document.getElementById("clock").innerText = str;
            }

            updateClock(); // chạy ngay lần đầu
            setInterval(updateClock, 1000);
        }
    </script>
</head>
<body <c:if test="${not empty format}">onload="startClock('${format}')"</c:if>>

<h2>Chọn định dạng thời gian</h2>
<form action="${pageContext.request.contextPath}/time" method="post">
    <input type="text" name="format" placeholder="Ví dụ: HH:mm:ss hoặc dd/MM/yyyy" required />
    <button type="submit">Hiển thị</button>
</form>

<c:if test="${not empty error}">
    <p style="color:red;"><c:out value="${error}" /></p>
</c:if>

<c:if test="${not empty format}">
    <h3>Thời gian hiện tại: <span id="clock"></span></h3>
</c:if>

</body>
</html>
