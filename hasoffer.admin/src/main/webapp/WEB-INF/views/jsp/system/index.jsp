<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<style>
    .p_detail_image {
        width: 60px;
        max-height: 100px;
    }

    .p_detail_image_div {
        float: left;
        margin: 20px;
    }

    .p_detail_name_td {
        width: 35%;
        text-align: right;
    }
</style>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">系统管理 - 选项</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <div class="row">
        <div class="col-lg-5">
            <div class="panel panel-default">
                <div class="panel-heading">商品索引</div>
                <div class="panel-body">
                    <button class="btn btn-primary" onclick="solrCtrl.reimportsolr()">重新导入</button>
                    <button class="btn btn-primary" onclick="solrCtrl.appendsolr()">增量导入</button>
                    <button class="btn btn-primary" onclick="solrCtrl.updateall()">全部更新</button>
                </div>
            </div>
        </div>

        <div class="col-lg-5">
            <div class="panel panel-default">
                <div class="panel-heading">类目索引</div>
                <div class="panel-body">
                    <button class="btn btn-primary" onclick="solrCtrl.reimportsolrcategory()">重新导入</button>
                </div>
            </div>
        </div>

        <div class="col-lg-5">
            <div class="panel panel-default">
                <div class="panel-heading">抓取任务</div>
                <div class="panel-body">
                    <a class="btn btn-primary" href="/t/toCreate"> 创建任务</a>
                    <a class="btn btn-primary" href="/t/list"> 任务列表</a>
                    <a class="btn btn-primary" href="/stat/monitor"> 比价更新统计</a>
                </div>
            </div>
        </div>

        <div class="col-lg-5">
            <div class="panel panel-default">
                <div class="panel-heading">FIX</div>
                <div class="panel-body">
                    <a class="btn btn-primary" href="/fix/website">刷新比价网站</a>
                    <a class="btn btn-primary" href="/dat/loadwebsites">加载网站</a>
                    <a class="btn btn-primary" href="/fix/restatvisit">重新统计访问</a>
                    <a class="btn btn-primary" href="/stat/cmpskupriceupdate/restat?all=true">重新统计价格更新</a>
                    <a class="btn btn-primary" href="/cate/fixlevel">刷新类目级别</a>
                    <a class="btn btn-primary" href="/fix/cmpskuurls">fix-比价url</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    var solrCtrl = {
        reimportsolrcategory: function () {
            if (confirm("重新导入将会删除旧的索引，继续吗？")) {
                var url = "/solr/category/reimport";
                http.doGet(url, function (data) {
                    alert(data.result);
                });
            }
        },

        reimportsolr: function () {
            if (confirm("重新导入将会删除旧的索引，继续吗？")) {
                var url = "/solr/product/reimport";
                http.doGet(url, function (data) {
                    alert(data.result);
                });
            }
        },

        appendsolr: function () {
            if (confirm("增量导入，继续吗？")) {
                var url = "/solr/product/append";
                http.doGet(url, function (data) {
                    alert(data.result);
                });
            }
        },

        updateall: function () {
            if (confirm("全部更新，继续吗？")) {
                var url = "/solr/product/updateall";
                http.doGet(url, function (data) {
                    alert(data.result);
                });
            }
        }
    }
</script>

<jsp:include page="../include/footer.jsp"/>