<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../../include/header.jsp"/>
<jsp:include page="../../include/left.jsp"/>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">top selling 列表</h1>
        </div>

        <div class="row" style="margin-bottom: 10px">

            <form action="/topselling/list" method="get">

                <div class="col-lg-2">
                    <div class="input-group">
                        <span class="input-group-addon">topSelling状态</span>
                        <select id="topSellingStatusString" class="form-control" name="topSellingStatusString">
                            <option value="">选择状态</option>
                            <c:forEach items="${statusList}" var="status">
                                <option value="${status}" <c:if test="${selectstatus==status}">selected</c:if>>
                                    <c:if test="${'WAIT'==status}">等待编辑</c:if>
                                    <c:if test="${'ONLINE'==status}">已经编辑</c:if>
                                    <c:if test="${'OFFLINE'==status}">已经失效</c:if>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
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
                        <td>商品ID</td>
                        <td>图片</td>
                        <td>标题</td>
                        <td>访问次数</td>
                        <td>切换状态</td>
                        <td colspan="3">操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${topSellingVoList}" var="topSellingVo">
                        <tr>
                            <td>${topSellingVo.id}</td>
                            <td><img src="${topSellingVo.imageurl}" height="130" width="100"/></td>
                            <td>
                                <a href="/p/cmp/${topSellingVo.id}">${topSellingVo.name} ${topSellingVo.skuNumber}个比价</a>
                            </td>
                            <td>${topSellingVo.count}</td>
                            <td>
                                <button id="changeStatus" onclick="change(${topSellingVo.id})">切换状态</button>
                            </td>
                            <td><a href="detail/${topSellingVo.id}">修改图片</a></td>
                            <td>
                                <a href="/p/cmp/${topSellingVo.id}">商品关联</a>/<a
                                    href="<c:if test='${""==topSellingVo.logid}'>#</c:if><c:if test='${""!=topSellingVo.logid}'>/s/reSearchByLogKeyword/${topSellingVo.logid}</c:if>">日志关联</a>
                            </td>
                            <td><a href="delete/${topSellingVo.id}">删除</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

        </div>
        <%--<jsp:include page="../include/page.jsp"/>--%>
        <jsp:include page="../../include/page.jsp"/>
    </div>
</div>
<jsp:include page="../../include/footer.jsp"/>
<script>

    //状态切换
    function change(topSellingId) {

        url = "/topselling/changeStatus/" + topSellingId;
        http.doGet(url);

        window.location.href = "/topselling/list?topSellingStatusString=${selectstatus}&tmp=" + Math.random() * 10000000000000000;
    }

</script>
