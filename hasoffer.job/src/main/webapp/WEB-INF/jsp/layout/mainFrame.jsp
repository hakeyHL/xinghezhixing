<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%
    String contextPath1 = request.getContextPath();
%>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <title>JOB列表查询</title>
    <%@ include file="/common/commonCss.jsp" %>
    <link rel="stylesheet" href="<%=contextPath1%>/css/page.css">
    <link rel="stylesheet" href="<%=contextPath1%>/css/icomoon.min.css"
          rel="stylesheet">
    <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon">

    <script src="${ctx}/js/WdatePicker.js"></script>
    <script src="<%=contextPath1%>/js/jquery-1.10.2.min.js"></script>
    <script src="<%=contextPath1%>/js/jquery.pager.js"></script>
    <!-- 新引入的jQuery -->
    <script src="<%=contextPath1%>/js/jquery-ui-1.10.3.min.js"></script>

    <%@ include file="/common/formValidate.jsp" %>

    <script type="text/javascript">
        $(document).ready(function () {

            $("#layer").validationEngine();

            $("#pause").click(function () {
                var triggerName = $("input[type=radio]:checked").parent().parent().find(".triggerClass").html();
                $(this).attr("href", "${ctx}/layout/pause?triggerName=" + triggerName);
            });

            $("#resume").click(function () {
                var triggerName = $("input[type=radio]:checked").parent().parent().find(".triggerClass").html();
                $(this).attr("href", "${ctx}/layout/resumeTrigger?triggerName=" + triggerName);
            });

            $("#goback").click(function () {
                window.location.href = "${ctx}/layout/showHome";
            });

            $("#layer").hide();

            $("#runNow").click(function () {
                var triggerName = $("input[type=radio]:checked").parent().parent().find(".triggerClass").html();
                $("#layer").dialog();
                jQuery("#layer").validationEngine("attach", {
                    maxErrorsPerField: 1,
                    onValidationComplete: function (form, status) {
                        if (status) {
                            var targetBizDate = $("input[type=date]").val();
                            var officeId = $("input[type=text]").val();
                            debugger;
                            window.location.href = "${ctx}/layout/runNow?triggerName=" + triggerName + "&targetBizDate=" + targetBizDate + "&officeId=" + officeId;
                        }
                    }
                });
            });

            <%--$("#shutDownNow").click(function () {--%>
            <%--var triggerName = $("input[type=radio]:checked").parent().parent().find(".triggerClass").html();--%>
            <%--$(this).attr("href", "${ctx}/layout/shutDownNow?triggerName=" + triggerName);--%>
            <%--});--%>
            /*  var triggerName =  $("input[type=radio]:checked").parent().parent().find(".triggerClass").html();
             var targetBizDate= prompt("请输入时间");
             var officeId =prompt("请输入机构ID");
             //var radioObj=$("table#dt_basic tbody input[type=radio]:checked");
             //var JobDataMap=radioObj.parent().parent().find("td:eq(8)").text();*/

            /* $.ajax({
             url: '
            ${ctx}/layout/ajaxShowHome.action',
             type: 'POST',
             dataType: "json",
             success: function(result) {
             }
             });*/

        });
    </script>
</head>
<body class="scrollY frameContent" style="OVERFLOW-X: scroll">
<div class="col-infos">
    <h2 style="display: inline;"><i class="fa fa-bar-chart-o"></i>任务统计列表</h2>
</div>
<div class="table-responsive">
    <table id="dt_basic" class="table table-hover table-bordered" style="margin-top:5px;">
        <thead>
        <tr>
            <th colspan="1" style=" text-align: center;">序号</th>
            <th colspan="1" style=" text-align: center;">状态</th>
            <th colspan="1" style=" text-align: center;">触发名称</th>
            <th colspan="1" style=" text-align: center;">触发组</th>
            <th colspan="1" style=" text-align: center;">上次时间</th>
            <th colspan="1" style=" text-align: center;">下次时间</th>
            <th colspan="1" style=" text-align: center;">工作名称</th>
            <th colspan="1" style=" text-align: center;">工作组</th>
            <th colspan="1" style=" text-align: center;">工作类</th>
            <th colspan="1" style=" text-align: center;">工作数据</th>
        </tr>
        </thead>

            <tbody>
            <c:forEach items="${infos}" var="quartzJobInfo">
                        <tr class="odd">
                        <td style="vertical-align: middle; text-align: center;">
                        <input type="radio" name="job" value="${quartzJobInfo.triggerName}"/>
                        </td>
                        <td style="vertical-align: middle; text-align: center;">${quartzJobInfo.state}</td>
                        <td style="vertical-align: middle; text-align: center;" class="triggerClass">${quartzJobInfo.triggerName}</td>
                        <td style="vertical-align: middle; text-align: center;">${quartzJobInfo.triggerGroup}</td>
                        <td style="vertical-align: middle; text-align: center;"><fmt:formatDate value="${quartzJobInfo.lastFireTime}" pattern="yyyy-MM-dd HH:mm:ss.S"/></td>
                        <td style="vertical-align: middle; text-align: center;"><fmt:formatDate value="${quartzJobInfo.nextFireTime}" pattern="yyyy-MM-dd HH:mm:ss.S"/></td>
                        <td style="vertical-align: middle; text-align: center;">${quartzJobInfo.jobName}</td>
                        <td style="vertical-align: middle; text-align: center;">${quartzJobInfo.jobGroup}</td>
                        <td style="vertical-align: middle; text-align: center;">${quartzJobInfo.jobClass}</td>
                        <td style="vertical-align: middle; text-align: center;">${quartzJobInfo.data}</td>
                </tr>
            </c:forEach>
            </tbody>
        <tr>
            <td colspan="10" style="vertical-align: middle; text-align: center; ">
                <a href="#" class="btn btn-danger" id="runNow">立即执行</a>
                <a href="#" class="btn btn-info" id="pause">暂停</a>
                <a href="#" class="btn btn-warning" id="resume">恢复</a>
                <%--<a href="#" class="btn btn-warning" id="shutDownNow">关闭任务</a>--%>
            </td>
        </tr>
    </table>
</div>

<div class="modal fade" style="overflow: hidden;">
    <div class="modal-dialog" style="width: 500px; height: 400px;">
        <div class="modal-content">
            <div class="modal-body">
                <form action="" id="layer">
                    <!-- 在此填写窗口内容，自定义HTML -->
                    <table class="table table-hover table-bordered" style="background-color: #F5F5F5;">
                        <tr class="odd">
                            <td style="vertical-align: middle; text-align: center;">
                                <label>日期</label></td>
                            <td style="vertical-align: middle; text-align: center;">
                                <input id="date" type="date" class="form-control "
                                       style="background:#fff url(<%=contextPath1%>/js/skin/datePicker.gif) no-repeat right;height:34px"
                                       onClick="WdatePicker()" data-errormessage-value-missing="* 请输入日期"/></td>
                        </tr>
                        <tr class="odd">
                            <td style="vertical-align: middle; text-align: center;">
                                <label>机构编号</label></td>
                            <td style="vertical-align: middle; text-align: center;">
                                <input name="officeId" type="text" class="form-control "/></td>
                        </tr>
                        <tr class="odd">
                            <td colspan="2" style="vertical-align: middle; text-align: center;">
                                <button id="saveForm" type="submit" class="btn btn-primary">确定</button>
                                &nbsp;&nbsp;
                                <button id="goback" type="button" class="btn btn-default">返回</button>
                            </td>
                            <!-- <td style="vertical-align: middle; text-align: center;"><button id="goback" type="button" class="btn btn-default">返回</button></td> -->
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>