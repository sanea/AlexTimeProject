<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Web Application</title>
    <link href="resources/bootstrap/css/bootstrap.css" rel="stylesheet">
    <style>
        body {
            padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
        }
    </style>
    <link href="resources/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
    <script src="resources/js/jquery-1.9.1.js"></script>
    <script src="resources/bootstrap/js/bootstrap.js"></script>
    <script>

        $(function () {
            $(document).ajaxError(function () {
                $("#nav li").removeClass("active");
                $('#ajaxContent').empty();
                $("#error").html("Error requesting page.").show();
            });
            $('#error').click(function () {
                $(this).empty().hide();
            });
        });

        function loadContent(url, activeEl) {
            $('#error').hide();
            $('#ajaxContent').empty();
            $("#loadingImg").show();
            $('#ajaxContent').load(url, function () {
                $("#loadingImg").hide();
                $("#nav li").removeClass("active");
                if (activeEl)
                    $(activeEl).parent().addClass("active");
            });

            return false;
        }
    </script>
</head>
<body>

<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <sec:authorize access="hasRole('MANAGE_TASK')"><span class="icon-bar"></span></sec:authorize>
                <sec:authorize access="hasRole('STAT')"><span class="icon-bar"></span></sec:authorize>
                <sec:authorize access="hasRole('EDIT_TASKS')"><span class="icon-bar"></span></sec:authorize>
                <sec:authorize access="hasRole('EDIT_USERS')"><span class="icon-bar"></span></sec:authorize>
                <span class="icon-bar"></span>
            </a>

            <div class="brand" style="color: #ffffff;">Web Application</div>

            <div class="nav-collapse collapse">
                <ul class="nav" id="nav">
                    <li class="active"><a href='<s:url value="/" htmlEscape="true"/>'>Home</a></li>
                    <sec:authorize access="hasRole('MANAGE_TASK')">
                        <li>
                            <a href="#user"
                               onclick="loadContent('<s:url value="/task" htmlEscape="true"/>', this); return false;">
                                Assigned tasks
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasRole('STAT')">
                        <li>
                            <a href="#user"
                               onclick="loadContent('<s:url value="/stat" htmlEscape="true"/>', this); return false;">
                                Statistics
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasRole('EDIT_TASKS')">
                        <li>
                            <a href="#user" onclick="loadContent('<s:url value="/task/edit" htmlEscape="true"/>', this); return false;">
                                Tasks Edit
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasRole('EDIT_USERS')">
                        <li>
                            <a href="#user" onclick="loadContent('<s:url value="/user" htmlEscape="true"/>', this); return false;">
                                Users Edit
                            </a>
                        </li>
                    </sec:authorize>
                    <li><a href="<s:url value="/logout" htmlEscape="true" />">Logout</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <div class="page-header">
        <h1><fmt:message key="welcome.title"/> <sec:authentication property="principal.username"/></h1>
    </div>
    <div id="loadingImg" style="width: 32px; height: 32px; margin: 0 auto; display:none;">
        <img src="resources/images/loading.gif" alt="loading"/>
    </div>

    <div id="ajaxContent">
        <p>Here will be some description of the project<br/>
            And here also we will write what to do!</p>
    </div>
    <div id="error" class="alert alert-error" style="display: none;">
    </div>
</div>


<%--<div id="langmenu">--%>
<%--<a href="?lang=en_us">en</a> | <a href="?lang=ru">ru</a>--%>
<%--</div>--%>
<%--<div id="footer" class="container">--%>
<%--Copyright © Alex, 2013--%>
<%--</div>--%>

</body>
</html>