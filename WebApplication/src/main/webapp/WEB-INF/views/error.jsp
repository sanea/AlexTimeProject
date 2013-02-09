<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${error != null}">
    <h3>Error</h3>

    <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
            ${error}
    </div>
</c:if>