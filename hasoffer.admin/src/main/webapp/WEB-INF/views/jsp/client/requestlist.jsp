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
            <h1 class="page-header">客户端请求列表</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>

    <div class="row" style="margin-bottom: 10px">

        <form id="form1" method="get" action="/client/requestlist">
            <div class="col-lg-2">
                <select id="requestUri" name="requestUri" class="form-control" onchange="document.getElementById('form1').submit()">
                    <option value="" <c:if test="${page.pageParams.requestUri == ''}">selected</c:if> >全部请求</option>
                    <option value="/app/latest" <c:if test="${page.pageParams.requestUri == '/app/latest'}">selected</c:if>>请求版本信息</option>
                    <option value="/cmp/getcmpskus" <c:if test="${page.pageParams.requestUri == '/cmp/getcmpskus'}">selected</c:if>>请求比价列表</option>
                </select>
            </div>
        </form>

    </div>

    <div class="row">
        <div class="col-lg-12">
            <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                <thead>
                <tr>
                    <td>请求时间</td>
                    <td>客户端名称</td>
                    <td>请求</td>
                    <td>请求参数</td>
                    <td>电商APP</td>
                    <td>分辨率</td>
                    <td>系统版本/APP版本</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${requestLogs}" var="log">
                    <tr>
                        <td><fmt:formatDate value="${log.createTime}"
                                            pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                        <td>
                            <a href="/client/detail/${log.deviceInfo.id}" target="_blank">
                                    ${log.deviceInfo.deviceName}</a>
                        </td>
                        <td>
                                ${log.requestUri}
                        </td>
                        <td style="width:500px;">
                            <c:forEach items="${log.queryParams}" var="queryParam">
                                <p>${queryParam}</p>
                            </c:forEach>
                        </td>
                        <td>${log.deviceInfo.shopApp}</td>
                        <td>${log.deviceInfo.screen}</td>
                        <td>${log.deviceInfo.osVersion}/${log.deviceInfo.appVersion}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <jsp:include page="../include/page.jsp"/>
</div>

<jsp:include page="../include/footer.jsp"/>