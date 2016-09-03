<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>
<script type="text/javascript">
    console.info('${webSite}');
    console.info(${webSite=='flipkart'  or webSite=='FLIPKART'});
</script>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">订单统计</h1>
        </div>
        <div class="col-lg-12" style="margin: 5px;">
            <div>
                <form action="/showstat/listOrderReport" method="get">
                    <div class="col-lg-2">
                        <div class="input-group">
                            <span class="input-group-addon">站点</span>
                            <select id="webSiteSelect" class="form-control" name="webSite">
                                <option value="ALL">全部站点</option>
                                <option value="flipkart" <c:if test="${webSite=='flipkart'}">selected</c:if>>flipkart
                                </option>
                                <option value="snapdeal" <c:if test="${webSite=='snapdeal'}">selected</c:if>>snapdeal
                                </option>
                                <option value="paytm" <c:if test="${webSite=='paytm'}">selected</c:if>>paytm</option>
                                <option value="shopclues" <c:if test="${webSite=='shopclues'}">selected</c:if>>
                                    shopclues
                                </option>
                                <option value="amazon" <c:if test="${webSite=='amazon'}">selected</c:if>>amazon</option>
                                <option value="ebay" <c:if test="${webSite=='ebay'}">selected</c:if>>ebay</option>
                                <option value="infibeam" <c:if test="${webSite=='infibeam'}">selected</c:if>>infibeam
                                </option>
                            </select>
                        </div>
                    </div>

                    <div class="col-lg-2" style="min-width: 200px">
                        <div class="input-group">
                            <span class="input-group-addon">起始时间</span>
                            <input size="16" type="text" class="form-control form_datetime" id="startTime"
                                   name="startTime"
                                   value="${startTime}">
                        </div>
                        <script>
                            $("#startTime").datepicker({dateFormat: 'yy-mm-dd'});
                        </script>
                    </div>

                    <div class="col-lg-2" style="min-width: 200px">
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
                </form>
            </div>
            <div style="float: left">
                <form action="/orderStats/updateOrderReport" method="get">
                    <div class="col-lg-2">
                        <button type="submit" class="btn btn-primary">手动更新</button>
                    </div>
                </form>
            </div>
        </div>
        <div class="col-lg-12" style="margin: 5px"></div>

        <div class="row">
            <div class="col-lg-12">
                <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                    <thead>
                    <tr>
                        <td>日期</td>
                        <td>总数</td>
                        <td>新用户总数</td>
                        <td>老用户总数</td>
                        <td>未知用户总数</td>
                        <td>闪传总数(总/老/新/未知)</td>
                        <td>GoogleAPP总数(总/老/新/未知)</td>
                        <td>9APP总数(总/老/新/未知)</td>
                        <td>未知渠道总数</td>
                        <%--<td>比价订单数</td>--%>
                        <%--<td>劫持订单数</td>--%>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${orderList}" var="order">
                        <tr>
                            <td>${order.dateTime}</td>
                                <%--<td>${order.orderId}</td>--%>
                                <%--<td>${order.webSite}</td>--%>
                                <%--<td>${order.channel}</td>--%>
                            <td>${order.sumCount}</td>
                            <td>${order.newUserCount}</td>
                            <td>${order.oldUserCount}</td>
                            <td>${order.noneUserCount}</td>
                            <td>${order.shanchuanChannel}</td>
                            <td>${order.googleChannel}</td>
                            <td>${order.nineAppChannel}</td>
                            <td>${order.noneChannel}</td>
                                <%--<td>${order.rediCount}</td>--%>
                                <%--<td>${order.shopCount}</td>--%>
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