<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" session="false" %>
<%
    String contextPath2 = request.getContextPath();
%>

<%@include file="/common/taglibs.jsp" %>
<!doctype html>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <title>管理员登录</title>
    <link rel="stylesheet" href="<%=contextPath2%>/css/b.css">
    <link rel="stylesheet" href="<%=contextPath2%>/css/f.css">
    <link rel="stylesheet" href="<%=contextPath2%>/css/g.css">
    <link rel="stylesheet"
          href="<%=contextPath2%>/css/tip-darkgray/tip-darkgray.css">

    <!--[if IE]>
    <!--<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>-->
    <![endif]-->
    <script src="../../../js/jquery-1.10.2.min.js"></script>
    <script src="../../../js/jquery.poshytip.min.js"></script>
    <script src="../../../js/login.js"></script>
    <link rel="shortcut icon" href="../../../images/favicon.ico" type="image/x-icon">

    <style type="text/css">
        .modal-dialog {
            width: 400px;
            left: 50%;
            top: 50%;
            margin-left: -180px !important;
            margin-top: -150px !important;
            position: fixed !important;
        }
    </style>

</head>
<body class="login">
<div class="col-center">
    <div class="well no-padding">
        <h1 class="main-title">Jobs 登录</h1>
            <!-- use param.error assuming FormLoginConfigurer#failureUrl contains the query parameter error -->
            <form role="form" action="${ctx}/login" method="post">
                <fieldset>
                    <div class="form-group">
                        <input class="form-control" placeholder="用户名" name="name" type="text" autofocus>
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control" placeholder="密码" name="password" value="" />
                    </div>
                    <button type="submit" class="btn btn-lg btn-success btn-block">登录</button>
                </fieldset>
            </form>
            <div style="color: red">
                ${error}
            </div>
    </div>
</div>
</body>
</html>