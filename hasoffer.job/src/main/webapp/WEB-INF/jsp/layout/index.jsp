<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String contextPath2 = request.getContextPath();
%>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/taglibs.jsp" %>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <title>Jobs-Monitor</title>
    <link rel="stylesheet" href="<%=contextPath2%>/css/bootstrap3.css">
    <link rel="stylesheet" href="<%=contextPath2%>/css/doctype.css">
    <link rel="stylesheet" href="<%=contextPath2%>/css/font-awesome.min.css">
    <script src="<%=contextPath2%>/js/skin.js"></script>
    <style>
        body {
            overflow: hidden;
        }

        #left-panel {
            background: #333;
        }
    </style>
    <link rel="shortcut icon" href="<%=contextPath2%>/images/favicon.ico"
          type="image/x-icon">
    <meta name="author" content="Piaoyis,me@piaoyis.com">
</head>
<body class="mainbg">
<%@ include file="./header.jsp" %>

<aside id="left">

<%@ include file="./menu.jsp" %>
</aside>

<section id="content">
    <div id="right" class="main">
        <div id="mainFrame" class="mainFrame">
            <iframe id="frameContent" src="${ctx}/layout/showHome" frameborder="0"></iframe>
        </div>
    </div>
</section>
</body>
</html>