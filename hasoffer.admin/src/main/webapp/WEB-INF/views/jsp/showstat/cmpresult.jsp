<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">比价质量统计</h1>
        </div>

        <div class="col-lg-12" style="margin: 5px"></div>

        <div class="row">
            <div class="col-lg-12">
                <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                    <thead>
                    <tr>
                        <td rowspan="2">日期</td>
                        <td rowspan="2">搜索匹配率</td>
                        <td colspan="7">比价列表中SKU数量( onsale 状态)</td>
                        <td rowspan="2">统计时间</td>
                    </tr>
                    <tr>
                        <td>0个</td>
                        <td>1个</td>
                        <td>2个</td>
                        <td>3个</td>
                        <td>4-10个</td>
                        <td>11-50个</td>
                        <td>51+</td>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${stats}" var="stat">
                        <tr>
                            <td>${stat.ymd}</td>
                            <td>${stat.matchedCount} / ${stat.noMatchedCount}</td>
                            <td>${stat.skuCount0}&nbsp;&nbsp;(<fmt:formatNumber
                                    value="${100 * stat.skuCount0 / stat.totalCount}" pattern="#.##"/> %)
                            </td>
                            <td>${stat.skuCount1}&nbsp;&nbsp;(<fmt:formatNumber
                                    value="${100 * stat.skuCount1 / stat.totalCount}" pattern="#.##"/> %)
                            </td>
                            <td>${stat.skuCount2}&nbsp;&nbsp;(<fmt:formatNumber
                                    value="${100 * stat.skuCount2 / stat.totalCount}" pattern="#.##"/> %)
                            </td>
                            <td>${stat.skuCount3}&nbsp;&nbsp;(<fmt:formatNumber
                                    value="${100 * stat.skuCount3 / stat.totalCount}" pattern="#.##"/> %)
                            </td>
                            <td>${stat.skuCount4}&nbsp;&nbsp;(<fmt:formatNumber
                                    value="${100 * stat.skuCount4 / stat.totalCount}" pattern="#.##"/> %)
                            </td>
                            <td>${stat.skuCount11}&nbsp;&nbsp;(<fmt:formatNumber
                                    value="${100 * stat.skuCount11 / stat.totalCount}" pattern="#.##"/> %)
                            </td>
                            <td>${stat.skuCount51}&nbsp;&nbsp;(<fmt:formatNumber
                                    value="${100 * stat.skuCount51 / stat.totalCount}" pattern="#.##"/> %)
                            </td>
                            <td><fmt:formatDate value="${stat.createTime}"
                                                pattern="yyyy/MM/dd HH:mm:ss"></fmt:formatDate></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

        </div>

        <%--<jsp:include page="../include/page.jsp"/>--%>
    </div>

<jsp:include page="../include/footer.jsp"/>