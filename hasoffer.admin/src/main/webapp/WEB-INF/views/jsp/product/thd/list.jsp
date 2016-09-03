<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../../include/header.jsp"/>
<jsp:include page="../../include/left.jsp"/>

<style>
    .p_list_image {
        width: 40px;
        max-height: 60px;
    }
</style>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">第三方商品</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <div class="row" style="margin-bottom: 20px;">
        <form id="form1" action="/thd/list" method="get">

            <jsp:include page="../../include/module/category.jsp"/>

            <div class="col-lg-12" style="margin:5px"></div>

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">网站名称</span>
                    <select class="form-control" id="website" name="website">
                        <option value="FLIPKART" <c:if test="${page.pageParams.website=='FLIPKART'}">selected</c:if>>
                            FLIPKART
                        </option>
                        <option value="SNAPDEAL" <c:if test="${page.pageParams.website=='SNAPDEAL'}">selected</c:if>>
                            SNAPDEAL
                        </option>
                    </select>
                </div>
            </div>

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">关联类型</span>
                    <select class="form-control" id="relateType" name="relateType">
                        <c:forEach items="${relateTypes}" var="rt">
                            <option value="${rt}"
                                    <c:if test="${relateType == rt}">selected</c:if> >${rt}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="col-lg-2">
                <button type="submit" class="btn btn-primary">查询</button>
                <c:if test="${page.pageParams.category3 > 0}">
                    <button type="button" class="btn btn-primary" onclick="relateProducts()">关联商品</button>
                </c:if>
            </div>
        </form>
    </div>
    <div class="row">
        <div class="col-lg-10">
            <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                <thead>
                <tr>
                    <td>序号</td>
                    <td>图片</td>
                    <td>标题</td>
                    <td>关联商品</td>
                    <td>关联类型</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${products}" var="thd">
                    <tr>
                        <td>${thd.id}</td>
                        <td><img src="${thd.imageUrl}" class="p_list_image"></td>
                        <td>
                            <span>${thd.title}</span>

                            <p style="color: #969696;margin-top:10px">
                                <span>price : ${thd.price}</span>
                            </p>
                        </td>
                        <td>
                            <a href="/p/cmp/${thd.ptmProductId}" target="_blank">
                                <span>${thd.ptmProductTitle}</span>
                            </a>
                        </td>
                        <td>
                            <span>${thd.relateType}</span>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <jsp:include page="../../include/page.jsp"/>
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

    function relateProducts() {
        var form1 = document.getElementById('form1');
        form1.action = "/thd/relate";
        form1.method = "post";
        form1.submit();
    }
    ;
</script>

<jsp:include page="../../include/footer.jsp"/>