<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>

<html>
<head>
	<meta charset="utf-8">
	<title>HasOffer-Admin</title>
	<link rel="shortcut icon" href="/static/image/favicon.ico">
	<!-- css -->
	<link rel="stylesheet" href="<%=contextPath%>/static/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="<%=contextPath%>/static/css/bootstrap/bootstrap-fileupload.css">
	<link rel="stylesheet" href="<%=contextPath%>/static/css/smart/timeline.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=contextPath%>/static/css/smart/sb-admin-2.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=contextPath%>/static/css/font-awesome/css/font-awesome.min.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=contextPath%>/static/css/main.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=contextPath%>/static/css/jqueryui/jquery-ui-1.9.2.custom.css" />
	<link rel="stylesheet" href="<%=contextPath%>/static/js/morrisjs/morris.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=contextPath%>/static/js/metisMenu/dist/metisMenu.min.css" rel="stylesheet">


	<!-- js-core -->
	<script src="<%=contextPath%>/static/js/jquery/jquery-2.1.1.min.js"></script>
	<script src="<%=contextPath%>/static/js/jqueryui/jquery-ui-1.9.2.custom.min.js"></script>
    <script src="<%=contextPath%>/static/js/jquery/jquery.form.js"></script>
	<script src="<%=contextPath%>/static/js/bootstrap/bootstrap.min.js"></script>
	<script src="<%=contextPath%>/static/js/bootstrap/bootstrap-typeahead.min.js"></script>
    <script src="<%=contextPath%>/static/js/bootstrap/bootstrap-fileupload.js"></script>
    <script src="<%=contextPath%>/static/js/bootstrap/bootstrap-dialog.js"></script>

    <script src="<%=contextPath%>/static/js/jquery/validate/jquery.validate.min.js"></script>

	<%--<script src="<%=contextPath%>/static/js/datetime-picker/bootstrap-datetimepicker.min.js"></script>--%>
	<%--<script src="<%=contextPath%>/static/js/datetime-picker/bootstrap-datetimepicker.zh-CN.js"></script>--%>

	<script src="<%=contextPath%>/static/js/hcharts/highcharts.js"></script>

	<script src="<%=contextPath%>/static/js/metisMenu/dist/metisMenu.min.js"></script>
	<script src="<%=contextPath%>/static/js/morrisjs/morris.min.js"></script>
	<script src="<%=contextPath%>/static/js/raphael/raphael-min.js"></script>

	<script src="<%=contextPath%>/static/js/smart/sb-admin-2.js"></script>

	<script src="<%=contextPath%>/static/js/util/http.js"></script>

	<style>
		/*覆盖导航栏原有的bootstrap样式*/
		.topBanner_nav > li > a:hover, .topBanner_nav > li > a:focus {
			text-decoration: none;
			background-color: #656565
		}
	</style>
</head>
<body class="container-fluid">