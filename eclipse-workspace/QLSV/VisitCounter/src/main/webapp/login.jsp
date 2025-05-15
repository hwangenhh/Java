<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Login</title></head>
<body>
    <form action="login" method="post">
        <label>Tên người dùng:</label>
        <input type="text" name="username" /><br/>
        <label>Mật khẩu:</label>
        <input type="password" name="password" /><br/>
        <input type="submit" value="Đăng nhập" />
    </form>
</body>
</html>
