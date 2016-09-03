<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<style>
    table {
        font-size: 12px;
    }
</style>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">匹配结果 - <a href="/p/cmp/${result.relatedProId}"
                                              target="_blank">${result.relatedProId}</a></h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <div class="row">
        <div class="panel panel-default">
            <!-- Default panel contents -->
            <div class="panel-heading">基本信息</div>
            <!-- Table -->
            <table class="table">
                <tr>
                    <td>标题</td>
                    <td>价格</td>
                    <td>来源</td>
                </tr>
                <tr>
                    <%--<td><fmt:formatDate value="${result.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>--%>
                    <td>${result.title}</td>
                    <td>${result.price}</td>
                    <td>${result.fromWebsite}</td>
                </tr>
            </table>
        </div>
    </div>

    <div class="row">
        <div class="panel panel-default">

            <div class="panel-heading">搜索到的SKU
                <button onclick="rematch('${result.id}')">Rematch</button>
            </div>

            <div class="panel-body">

                <div class="panel panel-default">
                    <div class="panel-body">
                        <ul class="nav nav-pills">
                            <c:forEach items="${result.sitePros}" var="sitePro">
                                <li role="presentation" id="btn1_${sitePro.key}" name="btn1">
                                    <a href="javascript:void(0);"
                                       onclick="click_skus_btn('${sitePro.key}')"
                                       <c:if test="${sitePro.value.productList.size() > 0}">style="color: red" </c:if>>
                                            ${sitePro.key}(${sitePro.value.productList.size()})</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>

                <c:forEach items="${result.sitePros}" var="sitePro">
                    <table id="table1_${sitePro.key}" name="siteskutable" class="table table-bordered table-condensed"
                           style="display: none">
                        <tr>
                            <td>SourceID</td>
                            <td>Title</td>
                            <td>Price</td>
                        </tr>
                        <c:forEach items="${sitePro.value.productList}" var="sku">
                            <tr>
                                <td><a href="${sku.url}">${sku.sourceId}</a></td>
                                <td>${sku.title}</td>
                                <td>${sku.price}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:forEach>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="panel panel-default">

            <div class="panel-heading">分析结果</div>
            <div class="panel-body">

                <div class="panel panel-default">
                    <div class="panel-body">
                        <ul class="nav nav-pills">
                            <c:forEach items="${result.finalSkus}" var="finalSku">
                                <li role="presentation" id="btn2_${finalSku.key}" name="btn2">
                                    <a href="javascript:void(0);"
                                       onclick="click_final_skus_btn('${finalSku.key}')">${finalSku.key}(${finalSku.value.size()})</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>

                <c:forEach items="${result.finalSkus}" var="finalSku">
                    <table id="table2_${finalSku.key}" name="finalskutable" class="table table-bordered table-condensed"
                           style="display: none">
                        <tr>
                            <td>SourceID</td>
                            <td>Title</td>
                            <td>Title-Score</td>
                            <td>Price</td>
                            <td>Price-Score</td>
                        </tr>
                        <c:forEach items="${finalSku.value}" var="sku">
                            <tr>
                                <td><a href="${sku.url}">${sku.sourceId}</a></td>
                                <td>${sku.title}</td>
                                <td>${sku.titleScore}</td>
                                <td>${sku.price}</td>
                                <td>${sku.priceScore}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:forEach>
            </div>
        </div>
    </div>

    <script>
        function rematch(logId) {
            var url = "/s/rematch/" + logId;

            http.doGet(url, function (data) {
                if (data.result == "ok") {
                    window.location.reload();
                }
            });
        }

        function click_final_skus_btn(site) {
            $("table[name='finalskutable']").css("display", "none");
            $("li[name='btn2']").removeClass("active");

            var tb = "#table2_" + site;
            $(tb).css("display", "block");

            var btn = "#btn2_" + site;
            $(btn).addClass("active");
        }

        function click_skus_btn(site) {
            $("table[name='siteskutable']").css("display", "none");
            $("li[name='btn1']").removeClass("active");

            var tb = "#table1_" + site;
            $(tb).css("display", "block");

            var btn = "#btn1_" + site;
            $(btn).addClass("active");
        }
    </script>

    <jsp:include page="../include/page.jsp"/>
</div>

<jsp:include page="../include/footer.jsp"/>