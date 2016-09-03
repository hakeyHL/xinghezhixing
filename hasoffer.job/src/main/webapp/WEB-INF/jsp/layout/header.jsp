<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<link rel="stylesheet" href="${ctx}/css/bootstrap3.css">
<link rel="stylesheet" href="${ctx}/css/doctype.css">
<header id="header" class="navbar navbar-default">
    <nav class="collapse navbar-collapse">
        <a href="#" class="navbar-brand">Jobs-Monitor</a>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="#">
                <s:property value="userSessionInfo.userNickName"/>，您已经成功登录</a></li>
            <li><a href="${ctx}/logout"><i class="fa fa-sign-out"></i>退出登录</a></li>
        </ul>
    </nav>
</header>
