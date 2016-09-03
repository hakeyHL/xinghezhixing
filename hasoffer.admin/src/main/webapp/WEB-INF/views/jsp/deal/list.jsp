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
    <!-- 删除结果提示 -->
    <div class="alert alert-success" id="delete_success" role="alert" style="display: none">删除成功</div>
    <div class="alert alert-warning" id="delete_fail" role="alert" style="display: none">删除失败</div>

    <div class="col-lg-12" style="margin: 10px"></div>

    <!-- 文件下载 -->
    <div class="row" style="margin: 5px; font-size: 12px">
        <span>Excel模板下载: <a href="<%=contextPath%>/deal/download">下载链接</a></span>
    </div>

    <!-- 导入结果提示 -->
    <div class="modal fade in" id="import_result" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" style="display: none;top:20%">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="myModalLabel">导入结果</h4>
                </div>
                <div class="modal-body">
                    <ul>
                        <li>本次导入表格共<span id="totalRows"></span>条</li>
                        <li>创建deal成功数量<span id="successRows"></span></li>
                        <li>创建失败数量<span id="failRows"></span></li>
                        <li>因网站名/deal名称/deal跳转链接为空失败<span id="nullRows"></span>条</li>
                        <li>因deal链接重复失败<span id="repeatRows"></span>条</li>
                        <li>其他失败<span id="otherFailRows"></span>条</li>
                    </ul>
                </div>
                <div class="modal-footer">
                    <button id="confirm_button" type="button" class="btn btn-primary">确定</button>
                </div>
            </div>
        </div>
    </div>


    <!-- 信息删除确认 -->
    <div class="modal fade" id="deleteModel">
        <div class="modal-dialog">
            <div class="modal-content message_align">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">×</span></button>
                    <h4 class="modal-title">提示信息</h4>
                </div>
                <div class="modal-body">
                    <p>您确认要删除吗？</p>
                </div>
                <div class="modal-footer">
                    <input type="hidden" id="url"/>
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <a onclick="urlSubmit()" class="btn btn-success" data-dismiss="modal">确定</a>
                </div>
            </div>
        </div>
    </div>

    <!-- 批量删除确认 -->
    <div class="modal fade" id="batchDeleteModel">
        <div class="modal-dialog">
            <div class="modal-content message_align">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">×</span></button>
                    <h4 class="modal-title">提示信息</h4>
                </div>
                <div class="modal-body">
                    <p>您确认要删除选中记录吗？</p>
                </div>
                <div class="modal-footer">
                    <input type="hidden" id="batchUrl"/>
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <a onclick="batchUrlSubmit()" class="btn btn-success" data-dismiss="modal">确定</a>
                </div>
            </div>
        </div>
    </div>


    <div class="col-lg-12" style="margin: 5px"></div>

    <div class="row">
        <form action="import" enctype="multipart/form-data" method="post" id="form">
            <div class="col-lg-12">
                <span class="modal-title">请选择Excel文件:</span>
                <input type="file" name="multiFile" id="multiFile" class="file-loading" style="display: inline;"/>
            </div>
        </form>
    </div>

    <div class="col-lg-12" style="margin: 10px"></div>

    <div class="row">
        <div class="col-lg-12">
            <button type="button" class="btn btn-primary" onclick="batchDelete('<%=contextPath%>/deal/batchDelete')"
                    data-toggle="modal" data-target="#confirm-delete">批量删除
            </button>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                <thead>
                <tr>
                    <td><input type="checkbox" id="checkAll"/>全选</td>
                    <td>创建时间</td>
                    <td>Deal来源网站</td>
                    <td>Deal图片</td>
                    <td>是否在banner展示</td>
                    <td>是否在前台展示</td>
                    <td>Deal标题</td>
                    <td>价格描述</td>
                    <td>生效时间</td>
                    <td>失效时间</td>
                    <td colspan="2">操作</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${datas}" var="data">
                    <tr>
                        <td><input type="checkbox" name="subBox" value="${data.id}"/></td>
                        <td>${fn:substring(data.createTime, 0, 10)}</td>
                        <td>${data.website}</td>
                        <td>
                            <img src="${data.listPageImage}" class="img-rounded">
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${data.push == 'true'}">
                                    是
                                </c:when>
                                <c:when test="${data.push == 'false'}">
                                    否
                                </c:when>
                            </c:choose>
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${data.display == 'true'}">
                                    是
                                </c:when>
                                <c:when test="${data.display == 'false'}">
                                    否
                                </c:when>
                            </c:choose>
                        </td>

                        <td>${data.title}</td>
                        <td>${data.priceDescription}</td>
                        <td>${fn:substring(data.createTime, 0, 10)}</td>
                        <td>${fn:substring(data.expireTime, 0, 10)}</td>
                        <td><a href="detail/${data.id}">编辑</a></td>
                        <td><a href="javascript:void(0)"
                               onclick="deleteById('<%=contextPath%>/deal/delete/${data.id}')"
                               data-toggle="modal" data-target="#confirm-delete">删除</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <jsp:include page="../include/page.jsp"/>
    </div>

    <script>
        $(function () {
            $('#multiFile').change(function () {
                $("#form").ajaxSubmit({
                    //定义返回JSON数据，还包括xml和script格式
                    dataType: 'json',
                    beforeSend: function () {
                        //表单提交前做表单验证
                    },
                    success: function (data) {
                        if (data.success) {
                            $("#totalRows").html(data.totalRows);
                            $("#successRows").html(data.successRows);
                            $("#failRows").html(data.failRows);
                            $("#nullRows").html(data._nullRows);
                            $("#repeatRows").html(data.repeatRows);
                            $("#otherFailRows").html(data.otherFailRows);
                            $('#import_result').modal('show');
                            $("#confirm_button").click(function () {
                                $('#import_result').modal('hide');
                                window.location.reload();
                            });
                        } else {
                            BootstrapDialog.show({
                                title: '导入失败',
                                message: '请检查Excel格式，重新导入!'
                            });
                        }


                    }
                });
            });


            //全选/全不选
            $("#checkAll").click(function () {
                $("input[name='subBox']:checkbox").prop("checked", this.checked);
            });
            var $subBox = $("input[name='subBox']");
            $subBox.click(function () {
                $("#checkAll").prop("checked", $subBox.length == $("input[name='subBox']:checked").length ? true : false);
            });

        });

        function deleteById(url) {
            $('#url').val(url);//给会话中的隐藏属性URL赋值
            $('#deleteModel').modal();
        }

        function urlSubmit() {
            var url = $.trim($("#url").val());//获取会话中的隐藏属性URL
            $.ajax({
                url: url,
                type: 'GET',
                success: function (result) {
                    if (result) {
                        $("#delete_success").css("display", "block").hide(3000);
                        window.location.reload();
                    }
                },
                error: function (xhr, textStatus, errorThrown) {
                    $("#delete_fail").css("display", "block").hide(3000);
                }
            });
        }

        function batchDelete(batchUrl) {

            var arr = new Array();
            $("input[name='subBox']:checked").each(function () {
                arr.push($(this).val());
            });

            if (arr.length == 0) {
                BootstrapDialog.show({
                    title: '提示',
                    message: '请选择要删除的记录!'
                });

                return false;
            }

            $('#batchUrl').val(batchUrl);//给会话中的隐藏属性URL赋值
            $('#batchDeleteModel').modal();
        }

        function batchUrlSubmit() {
            var url = $.trim($("#batchUrl").val());//获取会话中的隐藏属性URL

            var arr = new Array();
            $("input[name='subBox']:checked").each(function () {
                arr.push($(this).val());
            });

            $.ajax({
                url: url,
                type: 'GET',
                data: {"ids": arr},
                dataType: "json",
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                success: function (result) {
                    if (result) {
                        $("#delete_success").css("display", "block").hide(3000);
                        window.location.reload();
                    }
                },
                error: function (xhr, textStatus, errorThrown) {
                    $("#delete_fail").css("display", "block").hide(3000);
                }
            });
        }


    </script>
    <jsp:include page="../include/footer.jsp"/>
