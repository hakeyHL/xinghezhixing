<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<style>

</style>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">编辑类目:${currentCate.name}</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <div class="row">
        <div class="col-lg-8">
            <!--
            类目改名，删除类目
            修改关键字，统计类目下商品title关键词
            类目迁移，商品迁移
            -->

            <div class="panel panel-default">
                <div class="panel-heading">基础信息</div>
                <div class="panel-body">
                    <div class="col-lg-12">
                        <ol class="breadcrumb">
                            <c:forEach var="cate" items="${cates}">
                                <li><a href="/cate/detail/${cate.id}">${cate.name}</a></li>
                            </c:forEach>
                        </ol>
                    </div>
                    <form action="/cate/update/${currentCate.id}" method="get">
                        <div class="col-lg-8">
                            <div class="input-group">
                                <span class="input-group-addon">类目名称</span>
                                <input type="text" class="form-control" aria-describedby="basic-addon1"
                                       id="categoryName" name="categoryName" value="${currentCate.name}">
                            </div>
                        </div>
                        <div class="col-lg-3">
                            <button type="submit" class="btn btn-primary">更新</button>
                        </div>
                    </form>

                    <c:if test="${currentCate.productCount == 0}">
                        <div class="col-lg-12" style="margin-top: 10px">
                            <button class="btn btn-danger" onclick="deleteCate()">删除类目</button>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>

        <div class="col-lg-4">
            <div class="panel panel-default">
                <div class="panel-body">

                    <div class="col-lg-12" style="margin-bottom: 10px;">
                        当前类目下有<span style="color:red">${currentCate.productCount}</span>个商品
                    </div>
                    <form action="/cate/update/${currentCate.id}" method="get">
                        <div class="col-lg-8">
                            <div class="input-group">
                                <span class="input-group-addon">Tag</span>
                                <input type="text" class="form-control" aria-describedby="basic-addon1"
                                       id="categoryTag" name="categoryTag" value="${currentCate.keyword}">
                            </div>
                        </div>
                        <div class="col-lg-3">
                            <button type="submit" class="btn btn-primary">更新</button>
                        </div>
                    </form>

                    <c:if test="${currentCate.productCount > 0}">
                        <div class="col-lg-8" style="margin-top: 10px">
                            <div class="input-group">
                                <span class="input-group-addon">关键字密度(≥)</span>
                                <input type="text" class="form-control" aria-describedby="basic-addon1"
                                       id="analysis_percent" value="0.1">
                            </div>
                        </div>

                        <div class="col-lg-4" style="margin-top: 10px">
                            <button class="btn btn-default" onclick="statTitles()">分析类目关键词</button>
                        </div>
                    </c:if>

                    <div class="col-lg-8">
                        <table class="table table-condensed" id="table_keyword" style="margin-top: 10px">
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-8">
            <div class="panel panel-default">
                <div class="panel-heading">迁移</div>
                <div class="panel-body">
                    <form id="moveForm" method="post">
                        <input type="hidden" name="currentCateId" value="${currentCate.id}">

                        <div class="col-lg-12" style="margin-bottom: 10px">
                            <jsp:include page="../include/module/category.jsp"></jsp:include>
                        </div>

                        <div class="col-lg-12">
                            <div class="col-lg-12">
                                <button class="btn-primary" onclick="move('moveProductsToNewCategory')">迁移所有商品到新类目
                                </button>
                                <%--<button class="btn-primary" onclick="move('moveCategoryToNewCategory')">把该类目作为子类目迁移到新类目</button>--%>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        categoryManager.load('${page.pageParams.category1}', '${page.pageParams.category2}', '${page.pageParams.category3}');
    });

    var statTitles = function () {
        var url = "/cate/statTitleKeywords?cateId=" + ${currentCate.id} +"&percent=" + $("#analysis_percent").val();
        http.doGet(url, function (data) {
            var stats = data.stats;
            var h = "";
            for (var name in stats) {
                h += "<tr><td>" + name + "</td><td>" + stats[name] + "</td></tr>";
            }
            $("#table_keyword").html(h);
        })
    };

    var deleteCate = function () {
        if (confirm("将尝试删除类目：${currentCate.name}，请确认！")) {
            window.location = "/cate/delete/${currentCate.id}";
        }
    };

    var move = function (type) {
        var f = document.getElementById("moveForm");
        f.action = "/cate/" + type;
        f.submit();
    };
</script>

<jsp:include page="../include/footer.jsp"/>