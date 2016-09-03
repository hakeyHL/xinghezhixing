<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    String contextPath = request.getContextPath();
%>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>


<div id="page-wrapper">
    <div class="modal fade in" id="push_result" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" style="display: none;top:20%">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="myModalLabel">导入结果</h4>
                </div>
                <div class="modal-body">
                    <ul>
                        <li>本次推送类型：<span id="type"></span>${push.pushType}</li>
                        <li>本次共推 :<span id="totalRows">${push.pushCount}</span>个</li>
                        <li>本次推送成功数量: <span id="successRows">${push.successCount}个</span></li>
                        <li>本次推送失败数量:<span id="failRows">${push.failedCount}个</span></li>
                    </ul>
                </div>
                <div class="modal-footer">
                    <button id="confirm_button" type="button" class="btn btn-primary">确定</button>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">推送消息</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <div class="row">
        <div class="panel panel-default">
            <div class="panel-heading">推送简介</div>
            <div class="panel-body">
                <p>
                    推送类型声明:
                </p>

                <p>
                    推送用户数量声明:
                </p>

                <p>其他声明:</p>
            </div>
        </div>

    </div>
    <div class="col-lg-12" style="margin: 20px"></div>
    <form class="form-horizontal" action="<%=contextPath%>/push/pushMessage" enctype="text/plain"
          method="post" id="pushForm">
        <div class="form-group">
            <label class="col-sm-3 control-label">推送类型 pushType </label>

            <div class="col-sm-7">
                <label class="radio inline">
                    <input type="radio" name="pushType" id="singlePush" value="single">
                    Single Push
                </label>
                <label class="radio inline">
                    <input type="radio" name="pushType" id="groupPush" value="group" checked>
                    Group Push
                </label>
            </div>
        </div>
        <div class="form-group" id="gcmTokenDiv">
            <label class="col-sm-3 control-label">gcmToken</label>

            <div class="col-sm-7">
                <input type="text" name="gcmToken" class="form-control input-large" placeholder="gcmToken">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">outline</label>

            <div class="col-sm-7">
                <input type="text" name="outline" chclass="form-control input-large" value="" placeholder="outline">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">title</label>

            <div class="col-sm-7">
                <input type="text" name="title" class="form-control" value="" placeholder="title">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">content</label>

            <div class="col-sm-7">
                <input type="text" name="content" class="form-control" value="" placeholder="content">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">要push的app版本version</label>

            <div class="col-sm-7">
                <div class="col-lg-8">
                    <c:forEach items="${versions}" var="version">
                        <div class="checkbox-inline">
                            <label class="checkbox-inline">
                                <input type="checkbox" name="version" id="version" value="${version}">${version}
                            </label>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">推送内容类型type</label>

            <div class="col-sm-7">
                <div class="col-lg-8">
                    <div class="active">
                        <label class="radio-inline">
                            <input type="radio" name="messageType" id="type1" value="MAIN">MAIN
                        </label>
                    </div>
                    <div class="active">
                        <label class="radio-inline">
                            <input type="radio" name="messageType" id="type2" value="DEEPLINK">DEEPLINK
                        </label>
                    </div>
                    <div class="active">
                        <label class="radio-inline">
                            <input type="radio" name="messageType" id="type3" value="WEBVIEW">WEBVIEW
                        </label>
                    </div>
                    <div class="active">
                        <label class="radio-inline">
                            <input type="radio" name="messageType" id="type4" value="GOOGLEPLAY">GOOGLEPLAY
                        </label>
                    </div>
                    <div class="active">
                        <label class="radio-inline">
                            <input type="radio" name="messageType" id="type5" value="PRODUCT">PRODUCT
                        </label>
                    </div>
                    <div class="active">
                        <label class="radio-inline">
                            <input type="radio" name="messageType" id="type6" value="DEAL">DEAL
                        </label>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">类型值</label>

            <div class="col-sm-7">
                <input type="text" name="value" class="form-control" value="" placeholder="请根据推送类型填写相应值">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">推送安装了那些app的用户</label>

            <div class="col-sm-7">
                <div class="col-lg-8">
                    <select multiple class="form-control" name="website">
                        <c:forEach items="${websites}" var="website">
                            <option
                                    value="${website.name()}">${website.name()}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">渠道</label>

            <div class="col-sm-7">
                <div class="col-lg-8">
                    <c:forEach items="${channels}" var="channel">
                        <div class="active checkbox-inline">
                            <label class="checkbox-inline">
                                <input type="checkbox" name="channel" id="channel"
                                       value="${channel.name()}">${channel.name()}
                            </label>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">此次推送用户的数量number</label>

            <div class="col-sm-7">
                <input type="text" name="number" class="form-control" value="" placeholder="此处不填,推送给所有符合条件的用户">
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                <button id="push" type="button" class="btn btn-large btn-block btn-primary">
                    推送
                </button>
            </div>
        </div>
    </form>
</div>
<script>

    $().ready(function () {
        $('#push').click(function () {
            $("#pushForm").ajaxSubmit({
                //定义返回JSON数据，还包括xml和script格式
                dataType: 'json',
                beforeSend: function () {
                    //表单提交前做表单验证
                },
                success: function (data) {
                    if (data.success) {
                        $("#totalRows").html(data.totalRows);
                        $("#successRows").html(data.successCount);
                        $("#failRows").html(data.failedCount);
                        $("#type").html(data.pushType);
                        $('#push_result').modal('show');
                        $("#push_result").click(function () {
                            $('#push_result').modal('hide');
                            window.location.reload();
                        });
                    } else {
                        BootstrapDialog.show({
                            title: '推送失败',
                            message: '请联系管理员 ! '
                        });
                    }
                }
            });
        });
        $("#gcmTokenDiv").hide();
        var singlePush = $("#singlePush");
        var groupPush = $("#groupPush");
        singlePush.on("click", function () {
            groupPush.attr("checked", false);
            singlePush.attr("checked", "checked");
            $("#gcmTokenDiv").show();
        });
        groupPush.on("click", function () {
            singlePush.attr("checked", false);
            groupPush.attr("checked", "checked");
            $("#gcmTokenDiv").hide();
        });
    });


</script>

<jsp:include page="../include/footer.jsp"/>
