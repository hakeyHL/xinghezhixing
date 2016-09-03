<%@ page language="java" contentType="text/html; charset=UTF-8"
         isErrorPage="true" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <link href="${ctx}/css/bootstrap.min.css" rel="stylesheet"
          media="screen">
    <link href="${ctx}/css/pay_main.css" rel="stylesheet">
    <link href="${ctx}/css/public.css" rel="stylesheet" media="screen">
    <link href="${ctx}/css/main.css" rel="stylesheet">
    <link href="${ctx}/css/icomoon.min.css" rel="stylesheet">
    <link href="${ctx}/css/b.css" rel="stylesheet">
    <link href="${ctx}/css/f.css" rel="stylesheet">
    <link href="${ctx}/css/t.css" rel="stylesheet">
</head>

<body>
<!-- Content -->
<section id="content" class="container">
    <header class="p-header">
        <h2 class="p-title">出错啦！</h2>
    </header>

    <!-- Labels -->
    <div class="c-block" id="errorMessage">
        <h3 class="block-title">错误消息</h3>

        <p>
            <s:actionerror/>
        </p>
        <h4>
				<span class="label label-default"><s:property
                        value="%{exception.message}"/></span>
        </h4>
    </div>

    <div class="divider"></div>

    <div class="accordion">
        <h3 class="block-title">技术细节</h3>
        <div class="panel-group block" id="errorAccordion">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <a class="accordion-toggle active" data-toggle="collapse"
                           data-parent="#errorAccordion" href="#collapseErrorDetail">
                            技术细节 </a>
                    </h3>
                </div>
                <div id="collapseErrorDetail" class="panel-collapse collapse in">
                    <div class="panel-body">
                        <p>
                            <s:property value="%{exceptionStack}"/>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</body>

</html>


