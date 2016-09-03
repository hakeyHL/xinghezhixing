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
            <h1 class="page-header">任务查看</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>

    <div class="row">
        <div class="col-lg-12">
            <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                <thead>
                <tr>
                    <td>类目Id</td>
                    <td>类目名称</td>
                    <td>网站名称</td>
                    <%--<td>--%>
                        <%--被搜索次数--%>
                        <%--<a href="/s/list?sort=count_desc" class="btn-default">--%>
                            <%--<span style="float: right;">排序</span>--%>
                        <%--</a>--%>
                    <%--</td>--%>
                    <td>数据量</td>
                    <%--<td>--%>
                        <%--<a href="/s/list?sort=time_desc" class="btn-default">--%>
                            <%--<span style="float: right;">排序</span>--%>
                        <%--</a>--%>
                    <%--</td>--%>
                    <td>上次执行时间</td>
                    <td>操作</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${thdFetchTaskList}" var="sl">
                    <tr>
                        <td>${sl.ptmCateId}</td>
                        <td>
                            ${sl.categoryName}
                        </td>
                        <td>
                           ${s1.website}
                        </td>
                        <td>${sl.count}</td>
                        <td>${sl.lastProcessTime}</td>
                        <td>
                            <a type="button" href="/替换路径/${sl.id}" target="_blank">开始抓取</a>
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