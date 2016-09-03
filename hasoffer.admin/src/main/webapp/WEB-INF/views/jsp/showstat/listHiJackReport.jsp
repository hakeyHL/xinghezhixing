<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">流量劫持统计</h1>
        </div>

        <form action="/showstat/listHiJackReport" method="get">

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">站点</span>
                    <select id="webSiteSelect" class="form-control" name="webSite">
                        <option value="ALL">全部渠道</option>
                        <option value="flipkart" <c:if test="${webSite=='flipkart'}">selected</c:if>>flipkart</option>
                        <option value="snapdeal" <c:if test="${webSite=='snapdeal'}">selected</c:if>>snapdeal</option>
                        <option value="paytm" <c:if test="${webSite=='paytm'}">selected</c:if>>paytm</option>
                        <option value="shopclues" <c:if test="${webSite=='shopclues'}">selected</c:if>>shopclues</option>
                        <option value="amazon" <c:if test="${webSite=='amazon'}">selected</c:if>>amazon</option>
                        <option value="ebay" <c:if test="${webSite=='ebay'}">selected</c:if>>ebay</option>
                        <option value="infibeam" <c:if test="${webSite=='infibeam'}">selected</c:if>>infibeam</option>
                    </select>
                </div>
            </div>

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">起始时间</span>
                    <input size="16" type="text" class="form-control form_datetime" id="startTime" name="startTime"
                           value="${startTime}">
                </div>
                <script>
                    $("#startTime").datepicker({dateFormat: 'yy-mm-dd'});
                </script>
            </div>

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">结束时间</span>
                    <input size="16" type="text" class="form-control form_datetime" id="endTime" name="endTime"
                           value="${endTime}">
                </div>
                <script>
                    $("#endTime").datepicker({dateFormat: 'yy-mm-dd'});
                </script>
            </div>
            <div class="col-lg-2">
                <button type="submit" class="btn btn-primary">查询</button>
            </div>

            <div class="col-lg-12" style="margin: 5px"></div>


        </form>

        <div class="col-lg-12" style="margin: 5px"></div>

        <div class="row">
            <div class="col-lg-12">
                <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                    <thead>
                    <tr>
                        <td>日期</td>
                        <td>站点</td>
                        <td>点击小球</td>
                        <td>劫持成功</td>
                        <td>劫持成功率</td>
                        <td>比价次数</td>
                        <td>比价失败次数</td>
                        <td>商品未收录</td>
                        <td>商品重名</td>
                        <td>其他原因</td>
                        <td>抓取成功/唯一</td>
                        <td>抓取成功/重复</td>
                        <td>抓取失败</td>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${hiJackList}" var="hiJack">
                        <tr>
                            <td>${hiJack.dateStr}</td>
                            <td>${hiJack.webSite}</td>
                            <td>${hiJack.pricelistCount}</td>
                            <td>${hiJack.rediToAffiliateUrlCount}</td>
                            <td>${hiJack.successPerc}%</td>
                            <td>${hiJack.cmpSkuCount}</td>
                            <td>${hiJack.deeplinkCount}</td>
                            <td>${hiJack.deeplinkNullCount}</td>
                            <td>${hiJack.deeplinkDoubleCount}</td>
                            <td>${hiJack.deeplinkExceptionCount}</td>
                            <td>${hiJack.captureSingleSuccess}</td>
                            <td>${hiJack.captureMultipleSuccess}</td>
                            <td>${hiJack.captureFail}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

        </div>
        <%--<jsp:include page="../include/page.jsp"/>--%>
        <jsp:include page="../include/page.jsp"/>
    </div>

<jsp:include page="../include/footer.jsp"/>