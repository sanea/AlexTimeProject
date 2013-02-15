<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h3>Tasks for user</h3>
<table class="table">
    <c:forEach items="${taskList}" var="task">
        <tr class="<c:if test="${task.taskByTaskId.activeForUser}">success</c:if>">
            <td>
                    ${task.taskByTaskId.taskName}
            </td>
            <td>
                    ${task.taskByTaskId.taskType}
            </td>
            <td>
                    ${task.taskByTaskId.taskPrice}
            </td>
            <td>
                <c:if test="${task.taskByTaskId.taskType == 1}">
                    <button type="button"
                            class="btn btn-primary <c:if test="${task.taskByTaskId.activeForUser}">active</c:if>"
                            onclick="startTask(${task.taskByTaskId.id}, this)">
                        <c:if test="${task.taskByTaskId.activeForUser}">End Task</c:if>
                        <c:if test="${!task.taskByTaskId.activeForUser}">Start Task</c:if>

                    </button>
                </c:if>
                <c:if test="${task.taskByTaskId.taskType == 2}">
                    <button class="btn" onclick="executeTask(${task.taskByTaskId.id})">Click</button>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>
<script type="text/javascript">
    function startTask(id, el) {
        if ($(el).hasClass('active')) {
            console.log('end task with id=' + id);
            //$(el).removeClass('active').html('Start Task');
            loadContent('<s:url value="/task/end/" htmlEscape="true"/>' + id);
        } else {
            console.log('Start task with id=' + id);
            //$(el).addClass('active').html('End Task');
            loadContent('<s:url value="/task/start/" htmlEscape="true"/>' + id);
        }
        return false;
    }
    function executeTask(id) {
        console.log('Execute task with id=' + id);
        loadContent('<s:url value="/task/start/" htmlEscape="true"/>' + id);
    }
</script>