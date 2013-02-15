<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h3>User List</h3>
<table class="table table-striped">
    <tr>
        <th>User Name</th>
        <th>User Enabled</th>
    </tr>
    <c:forEach items="${userList}" var="user">
        <tr>
            <td>
                    ${user.username}
            </td>
            <td>
                    ${user.enabled}
            </td>
        </tr>
    </c:forEach>
</table>