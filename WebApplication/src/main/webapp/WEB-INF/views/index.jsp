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
            //close error on click
            $('#error').click(function () {
                $(this).empty().hide();
            });
            //back to top button
            $(window).scroll(function () {
                if ($(this).scrollTop() != 0) {
                    $('#backToTop').fadeIn();
                } else {
                    $('#backToTop').fadeOut();
                }
            });
            $('#backToTop').click(function () {
                $('body,html').animate({scrollTop: 0}, 800);
                return false;
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
                    $(activeEl).addClass("active");
            });

            return false;
        }

        function dialogLoadContent(url, title, saveClick) {
            $('#dialogHeader').html(title ? title : 'Dialog');
            $('#dialogLoadingImg').show();
            $('#dialogError').hide();
            $('#dialogBody').empty();
            $('#dialogSaveBtn').hide().unbind('click');

            if (saveClick && jQuery.isFunction(saveClick)) {
                $('#dialogSaveBtn').show().click(function() {
                    saveClick();
                    $('#dialog').modal('hide');
                });
            }
            $('#dialog').modal({
                keyboard: false
            });

            $('#dialogBody').load(url, function() {
                $('#dialogLoadingImg').hide();
            });
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

            <a class="brand" style="color: #ffffff;" href='<s:url value="/" htmlEscape="true"/>'>Web Application</a>

            <div class="nav-collapse collapse">
                <p class="navbar-text pull-right">
                    Logged in as <a href="#" class="navbar-link"><sec:authentication property="principal.username"/></a>
                </p>
                <ul class="nav" id="nav">
                    <li class="active"><a href='<s:url value="/" htmlEscape="true"/>'>Home</a></li>
                    <sec:authorize access="hasRole('MANAGE_TASK')">
                        <li>
                            <a href="#user"
                               onclick="loadContent('<s:url value="/task"
                                                            htmlEscape="true"/>', $(this).parent()); return false;">
                                Assigned tasks
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasRole('STAT')">
                        <li class="dropdown" id="statNav">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Statistics<b
                                    class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li>
                                    <a href="#user" onclick="loadContent('<s:url value="/stat"
                                                                                 htmlEscape="true"/>', document.getElementById('statNav')); return false;">
                                        Online tasks
                                    </a>
                                </li>
                                <li>
                                    <a href="#user" onclick="loadContent('<s:url value="/stat/all"
                                                                                 htmlEscape="true"/>', document.getElementById('statNav')); return false;">
                                        General Stat
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasRole('EDIT_TASKS')">
                        <li>
                            <a href="#user" onclick="loadContent('<s:url value="/task/edit"
                                                                         htmlEscape="true"/>', $(this).parent()); return false;">
                                Tasks Edit
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasRole('EDIT_USERS')">
                        <li>
                            <a href="#user" onclick="loadContent('<s:url value="/user"
                                                                         htmlEscape="true"/>', $(this).parent()); return false;">
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
        <h1><fmt:message key="welcome.title"/></h1>
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

    <hr/>
    <footer>
        <p class="pull-right"><a id="backToTop" href="#" style="display: none;">Back to top</a></p>

        <p>&copy; 2013 Company, Inc. &middot; <a href="#">Privacy</a> &middot; <a href="#">Terms</a></p>
    </footer>
</div>


<!-- Modal -->
<div id="dialog" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="dialogHeader">Modal header</h3>
    </div>
    <div id="dialogLoadingImg" style="padding-top: 15px; width: 32px; height: 32px; margin: 0 auto; display: none;">
        <img src="resources/images/loading.gif" alt="loading"/>
    </div>
    <div id="dialogError" class="alert alert-error" style="display: none;">
    </div>
    <div id="dialogBody" class="modal-body">
        <p>One fine body…</p>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
        <button id="dialogSaveBtn" class="btn btn-primary">Save changes</button>
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