<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<style>
    .p_list_image {
        width: 40px;
        max-height: 60px;
    }
</style>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">客户端设备列表</h1>
        </div>
        <!-- /.col-lg-12 -->
        <form action="/client/list" method="get">

            <div class="col-lg-2">
                <div name="deviceName" class="input-group">
                    <span class="input-group-addon">设备名称</span>
                    <input type="text" class="form-control" name="deviceName" value="${deviceName}">
                </div>
            </div>

            <div class="col-lg-12" style="margin: 5px"></div>

            <div class="col-lg-2">
                <div name="appType" class="input-group">
                    <span class="input-group-addon">app版本</span>
                    <select id="appTypeSelect" class="form-control" name="appTypeString">
                        <option value="" >选择版本</option>
                        <option value="APP" <c:if test="${appTypeString=='APP'}">selected</c:if>>
                            APP
                        </option>
                        <option value="PLUGIN" <c:if test="${appTypeString=='PLUGIN'}">selected</c:if>>PLUGIN</option>
                        <option value="PLUGINPUSH" <c:if test="${appTypeString=='PLUGINPUSH'}">selected</c:if>>PLUGINPUSH</option>
                        <option value="PLUGIN_UI" <c:if test="${appTypeString=='PLUGIN_UI'}">selected</c:if>>PLUGIN_UI</option>
                        <option value="SDK" <c:if test="${appTypeString=='SDK'}">selected</c:if>>SDK</option>
                    </select>
                </div>
            </div>
            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">marketChannel</span>
                    <select id="marketChannelSelect" class="form-control" name="marketChannelString">
                        <option value="">选择渠道</option>
                        <option value="OFFICIAL" <c:if test="${marketChannelString=='OFFICIAL'}">selected</c:if>>官网</option>
                        <option value="GOOGLEPLAY" <c:if test="${marketChannelString=='GOOGLEPLAY'}">selected</c:if>>GOOGLEPLAY</option>
                        <option value="PREASSEMBLE" <c:if test="${marketChannelString=='PREASSEMBLE'}">selected</c:if>>预装</option>
                        <option value="WANGMENG" <c:if test="${marketChannelString=='WANGMENG'}">selected</c:if>>网盟</option>
                        <option value="SHANCHUAN" <c:if test="${marketChannelString=='SHANCHUAN'}">selected</c:if>>闪传</option>
                        <option value="DUOBAO" <c:if test="${marketChannelString=='DUOBAO'}">selected</c:if>>夺宝</option>
                    </select>
                </div>
            </div>

            <div class="col-lg-12" style="margin: 5px"></div>

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">起始时间</span>
                    <input size="16" type="text" class="form-control form_datetime" id="startTime" name="startTime"
                           value="${startTime}">
                </div>
                <script>
                    $("#startTime").datepicker();
                </script>
            </div>

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">结束时间</span>
                    <input size="16" type="text" class="form-control form_datetime" id="endTime" name="endTime"
                           value="${endTime}">
                </div>
                <script>
                    $("#endTime").datepicker();
                </script>
            </div>

            <div class="col-lg-2">
                <button type="submit" class="btn btn-primary">查询</button>
            </div>
        </form>

    </div>

    <div class="row">
        <div class="col-lg-12">
            <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                <thead>
                <tr>
                    <td>设备名称</td>
                    <td>电商APP</td>
                    <td>APP版本</td>
                    <td>分辨率</td>
                    <td>系统版本</td>
                    <td>创建时间</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${devices}" var="device">
                    <tr>
                            <%--<td>${device.id}</td>--%>
                        <td><a href="/client/detail/${device.id}">${device.deviceName}</a></td>
                        <td>${device.shopApp}</td>
                        <td>${device.appType}_${device.marketChannel}_${device.appVersion}</td>
                        <td>${device.screen}</td>
                        <td>${device.osVersion}</td>
                        <td>${device.createTime}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <jsp:include page="../include/page.jsp"/>
</div>

<jsp:include page="../include/footer.jsp"/>
