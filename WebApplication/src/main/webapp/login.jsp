<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Please Login</title>
    <link href="resources/styles/login.css" rel="stylesheet"/>
</head>
<body>

<div class="container">
    <form class="form-signin" action="${pageContext.servletContext.contextPath}/postlogin" method="POST">
        <h2 class="form-signin-heading">Please sign in</h2>
        <input type="text" name="j_username" class="input-block-level" placeholder="username">
        <input type="password" name="j_password" class="input-block-level" placeholder="Password"> <br/>
        <input type="checkbox" name="_spring_security_remember_me" value="remember-me"> Remember me <br/>
        <button class="btn btn-primary" type="submit">Sign in</button>
        <c:if test="${pageContext.request.getParameter('login_error') eq '' and not empty sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}">
            <div class="alert  alert-error">
                <strong>Warning!</strong> Your login attempt was not successful, try again.<br/> Caused :
                    ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
            </div>
        </c:if>
    </form>
</div>

</body>
</html>