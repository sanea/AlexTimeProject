<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h3>Tasks for user</h3>

<p>
    <button class="btn btn-small" type="button" onclick="loadContent('<s:url value="/stat" htmlEscape="true"/>')">
        Update
    </button>
</p>
<table class="table">
    <tr>
        <th>
            Username
        </th>
        <th>
            task name
        </th>
        <th>
            Start
        </th>
        <th>
            End
        </th>
        <th>
            Task status(progress, completed)
        </th>
        <th>
            Time spent(min)
        </th>
    </tr>
    <c:forEach items="${taskStatusList}" var="taskStatus">
        <tr class="<c:if test="${taskStatus.status eq 'p'}">success</c:if>">
            <td>
                    ${taskStatus.usersByUsername.username}
            </td>
            <td>
                    ${taskStatus.taskByTaskId.taskName}
            </td>
            <td>
                    ${taskStatus.startTimestamp}
            </td>
            <td>
                    ${taskStatus.endTimestamp}
            </td>
            <td>
                    ${taskStatus.status}
            </td>
            <td>
                    ${taskStatus.timeSpent}
            </td>
        </tr>
    </c:forEach>
</table>