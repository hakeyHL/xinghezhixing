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
            <h1 class="page-header">购买统计</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>

    <div class="row">
        <div class="col-lg-12">
            <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                <thead>
                <tr>
                    <td>id</td>
                    <td>标题</td>
                    <td>购买次数</td>
                    <td>最近更新时间</td>
                    <td>查看搜索日志</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${statUserBuyList}" var="userBuyVo">
                    <tr>
                    <td>${userBuyVo.id}</td>
                    <td>
                        <a href="/p/cmp/${userBuyVo.id}">${userBuyVo.title}</a>
                    </td>
                    <td>${userBuyVo.count}</td>
                    <td>${userBuyVo.updateTime}</td>
                    <td>
                        <a href="/userbuy/getsrmlogs/${userBuyVo.id}">查看搜索日志</a>
                    </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <jsp:include page="../include/page.jsp"/>

</div>

<jsp:include page="../include/footer.jsp"/>