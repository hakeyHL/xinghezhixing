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
            <h1 class="page-header">SolrTest - 类目</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <div class="row" style="margin-bottom: 20px;">
        <form action="/cate/testsolr" method="get">

            <div class="col-lg-12" style="margin:5px"></div>

            <div class="col-lg-4">
                <div class="input-group">
                    <span class="input-group-addon">关键字</span>
                    <input type="text" name="q" class="form-control" placeholder="Search for..."
                           value="${q}">
                </div>
                <!-- /input-group -->
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
                    <td>ID</td>
                    <td>名称</td>
                    <td>Tag</td>
                    <td>level</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${cms}" var="cm">
                    <tr>
                        <td>${cm.id}</td>
                        <td><a href="/cate/detail/${cm.id}" target="_blank">${cm.name}</a></td>
                        <td>${cm.tags}</td>
                        <td>${cm.level}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
</script>

<jsp:include page="../include/footer.jsp"/>