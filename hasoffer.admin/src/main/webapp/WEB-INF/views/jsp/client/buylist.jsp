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
            <h1 class="page-header">客户端购买记录</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>

    <div class="row" style="margin-bottom: 10px">

        <form action="/client/buy" method="get">

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">来源网站</span>
                    <select id="fromWebsite" class="form-control" name="fromWebsite">
                        <option value="">选择网站</option>
                        <c:forEach items="${fromapps}" var="fromapp">
                            <option value="${fromapp}" <c:if test="${fromWebsite==app}">selected</c:if>>${fromapp}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">跳转网站</span>
                    <select id="toWebsite" class="form-control" name="toWebsite">
                        <option value="">选择网站</option>
                        <c:forEach items="${toapps}" var="toapp">
                            <option value="${toapp}" <c:if test="${toWebsite==app}">selected</c:if>>${toapp}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="col-lg-12" style="margin: 5px"></div>

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">起始日期</span>
                    <input size="16" type="text" class="form-control form_datetime"
                           id="startTime" name="startTime" value="${startTime}">
                </div>
                <script>
                    $("#startTime").datepicker();
                </script>
            </div>

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">结束日期</span>
                    <input size="16" type="text" class="form-control form_datetime"
                           id="endTime" name="endTime" value="${endTime}">
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
                    <td>创建时间</td>
                    <td>来源网站</td>
                    <td>跳转网站</td>
                    <td>商品</td>
                    <td>客户端</td>
                    <td>电商APP</td>
                    <td>APP版本</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${requestLogs}" var="requestLog">
                    <tr>
                        <td><fmt:formatDate value="${requestLog.createTime}"
                                            pattern="yyyy/MM/dd HH:mm:ss"></fmt:formatDate></td>
                        <td>${requestLog.fromWebsite}</td>
                        <td>${requestLog.toWebsite}</td>
                        <td>
                            <a href="/p/cmp/${requestLog.proId}">
                                    ${requestLog.title}
                            </a>
                        </td>
                        <td><a href="/client/detail/${requestLog.deviceId}">${requestLog.deviceId}</a></td>
                        <td>${requestLog.shopApp}</td>
                        <td>${requestLog.appType}_${requestLog.marketChannel}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <jsp:include page="../include/page.jsp"/>
</div>

<jsp:include page="../include/footer.jsp"/>
