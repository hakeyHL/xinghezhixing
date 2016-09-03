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
            <h1 class="page-header">设备记录</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>

    <div class="row">
        <div class="col-lg-4">
            <table class="table">
                <tr>
                    <td>设备名称</td>
                    <td>${device.deviceName}</td>
                </tr>
                <tr>
                    <td>电商应用</td>
                    <td>${device.shopApp}</td>
                </tr>
                <tr>
                    <td>app版本</td>
                    <td>${device.appType}_${device.appVersion}</td>
                </tr>
                <tr>
                    <td>屏幕分辨率</td>
                    <td>${device.screen}</td>
                </tr>
                <tr>
                    <td>系统版本</td>
                    <td>${device.osVersion}</td>
                </tr>
                <tr>
                    <td>创建时间</td>
                    <td>${device.createTime}</td>
                </tr>
            </table>
        </div>

        <div class="col-lg-8">
            <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                <thead>
                <tr>
                    <td>创建时间</td>
                    <td>访问接口</td>
                    <td>设备上的电商APP</td>
                    <td>app版本</td>
                    <td>app类型</td>
                    <td>网络状态</td>
                    <td>渠道</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${logs}" var="log">
                    <tr>
                            <%--<td>${device.id}</td>--%>
                        <td><fmt:formatDate value="${log.createTime}"
                                            pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                        <td>
                                ${log.requestUri}
                            <br/>
                                ${log.query}
                        </td>
                        <td>${log.shopApp}</td>
                        <td>${log.appVersion}</td>
                        <td>${log.curNetState}</td>
                        <td>${log.marketChannel}</td>
                        <td>${log.appType}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <jsp:include page="../include/page.jsp"/>
</div>

<jsp:include page="../include/footer.jsp"/>