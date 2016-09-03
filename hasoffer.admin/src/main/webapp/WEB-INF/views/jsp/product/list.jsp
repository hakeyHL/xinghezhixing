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
            <h1 class="page-header">商品列表</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <div class="row" style="margin-bottom: 20px;">
        <form action="/p/list" method="get">

            <jsp:include page="../include/module/category.jsp"/>

            <div class="col-lg-12" style="margin:5px"></div>

            <div class="col-lg-4">
                <div class="input-group">
                    <span class="input-group-addon">关键字</span>
                    <input type="text" name="title" class="form-control" placeholder="Search for..."
                           value="${page.pageParams.title}">
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
                    <td>图片</td>
                    <td>标题</td>
                    <td>Tag</td>
                    <%----%>
                    <%--<td>价格(Rs.)</td>--%>
                    <%--<td>评论数</td>--%>

                    <td>来源</td>
                    <td>创建时间</td>
                    <%--<td>颜色</td>--%>
                    <%--<td>大小</td>--%>
                    <%--<td></td>--%>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${products}" var="thd">
                    <tr onmouseover="pCtrl.onPro('${thd.id}')" onmouseout="pCtrl.outPro('${thd.id}')">
                        <td>${thd.id}</td>
                        <td><img src="${thd.masterImageUrl}" class="p_list_image"></td>
                        <td>
                            <a href="/p/cmp/${thd.id}" target="_blank">${thd.title}</a>

                            <p style="color: #969696;margin-top:10px">
                                <c:forEach items="${thd.categories}" var="cate" varStatus="vs">
                                    ${cate.name}
                                    <c:if test="${!vs.last}">
                                        >
                                    </c:if>
                                </c:forEach>
                            </p>
                        </td>
                        <td>${thd.tag}
                            <a id="modifyBtn${thd.id}" href="javascript:void(0);" style="display: none"
                               onclick="pCtrl.modifyTag('${thd.id}', '${thd.tag}')">编辑</a>
                        </td>
                            <%--<td>${product.price}</td>
                            <td>${product.rating}</td>--%>
                        <td>${thd.sourceSite}</td>
                        <td>${thd.createTime}</td>
                            <%--<td>${product.color}</td>
                            <td>${product.size}</td>
                            <td></td>--%>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <jsp:include page="../include/page.jsp"/>
</div>

<script>
    $(document).ready(function () {
        categoryManager.load('${page.pageParams.category1}', '${page.pageParams.category2}', '${page.pageParams.category3}');
    });

    var pCtrl = {
        modifyTag: function (id, tag) {
            console.log(id + "\t" + tag);
            var newtag = prompt('编辑tag(不同的tag使用空格分开)', tag);
            if (newtag != null && newtag != tag) {
                // 更新tag
                http.doPost('/p/updateTag', {id: id, tag: newtag}, function (data) {
                    console.log(data);
                });
            }
        },
        onPro: function (id) {
            $("#modifyBtn" + id).css("display", "block");
        },
        outPro: function (id) {
            $("#modifyBtn" + id).css("display", "none");
        }
    };
</script>

<jsp:include page="../include/footer.jsp"/>