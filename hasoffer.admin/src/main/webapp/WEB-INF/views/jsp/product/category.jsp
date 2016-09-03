<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fun" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<style>
    a:link, a:active, a:hover, a:visited {
        text-decoration: none;
        color: #428bca;
    }

    .myactive a {
        color: black;
    }
</style>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">类别管理<br>
                <a href="/cate/testsolr" target="_blank"><h4>test solr</h4></a>
            </h1>
        </div>
    </div>

    <%--<div class="row">
        <div class="col-lg-6">
            <div class="panel panel-default">
                <div class="panel-heading">表单</div>
                <div class="panel-body">
                    当前操作的类目：
                    <span id="selectedCate">
                        <c:forEach items="${routeCates}" var="rc" varStatus="rcs">
                            /-${rc.name}
                        </c:forEach>
                    </span>
                    <input type="hidden" id="sc"
                           value="<c:choose><c:when test="${sc2>0}">${sc2}</c:when><c:when test="${sc1>0}">${sc1}</c:when></c:choose>">
                    <br>

                    <div class="input-group">
                        <lable class="input-group-addon">关键字(keyword, tag):</lable>
                        <input class="form-control" id="selectedCateTag" value="${keyword}">
                    </div>
                    <button class="btn btn-primary" onclick="updateCateKeyword()">更新Tag</button>
                    <button class="btn btn-primary" onclick="statTitles()">统计商品关键字</button>
                </div>
            </div>
        </div>
    </div>--%>

    <div class="row">
        <div class="col-lg-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    一级类目
                    <div class="badge">
                        <a href="javascript:void(0);" onclick="addCate(0,1)">
                            <span class="fa fa-plus"></span>
                        </a>
                    </div>
                </div>
                <ul class="list-group">
                    <c:forEach items="${c1s}" var="c1">
                        <li class="list-group-item <c:if test="${sc1==c1.id}">active myactive</c:if>">
                            <a href="/cate/main?c1=${c1.id}">${c1.name}</a>
                            <span class="badge">
                               <a style="color: greenyellow" href="/cate/detail/${c1.id}" target="_blank">
                                       ${c1.productCount}</a>
                            </span>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <div class="col-lg-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    二级类目
                    <div class="badge">
                        <a href="javascript:void(0);" onclick="addCate(${sc1},2)">
                            <span class="fa fa-plus"></span>
                        </a>
                    </div>
                </div>
                <ul class="list-group">
                    <c:forEach items="${c2s}" var="c2">
                        <li class="list-group-item <c:if test="${sc2==c2.id}">active myactive</c:if>">
                            <a href="/cate/main?c1=${sc1}&c2=${c2.id}">${c2.name}</a>
                            <span class="badge">
                                <a style="color: greenyellow" href="/cate/detail/${c2.id}" target="_blank">
                                        ${c2.productCount}</a>
                            </span>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <div class="col-lg-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    三级类目
                    <div class="badge">
                        <a href="javascript:void(0);" onclick="addCate(${sc2},3)">
                            <span class="fa fa-plus"></span>
                        </a>
                    </div>
                </div>
                <ul class="list-group" id="c3ul">
                    <c:forEach items="${c3s}" var="c3">
                        <li class="list-group-item" id="c3li_${c3.id}">
                            <a href="javascript:void(0);"
                               onclick="selectCate(${sc1}, ${sc2}, ${c3.id}, '${c3.name}', '${c3.keyword}')">
                                    ${c3.name}
                            </a>
                            <span class="badge">
                                <a style="color: greenyellow" href="/cate/detail/${c3.id}" target="_blank">
                                        ${c3.productCount}
                                </a>
                            </span>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</div>

<script>
    var selectCate = function (c1, c2, c3, c3name, c3tag) {
        $("#selectedCate").html(c1 + " / " + c2 + " / " + c3 + " ( " + c3name + " )");
        $("#selectedCateTag").val(c3tag);
        $("#sc").val(c3);
        $("#c3ul").find("li").removeClass("active myactive");
        $("#c3li_" + c3).addClass("active myactive");
    };
    var updateCateKeyword = function () {
        var cateId = $("#sc").val();
        var key = $("#selectedCateTag").val();
        http.doGet("/cate/updatekeyword?cateId=" + cateId + "&key=" + key, function (data) {
            alert("修改成功");
        });
    };
    var addCate = function (parentId, level) {
        if(level >= 2 && parentId <= 0){
            alert('请先选择父级类目');
            return;
        }
        var name = prompt("请输入类目名称");
        if (name == null) {
            return;
        }
        var url = '/cate/create?parentId=' + parentId + '&name=' + name;
        http.doGet(url, function (data) {
            window.location.reload();
        });
    };
    /*var statTitles = function () {
     var url = "/cate/statTitleKeywords?c1=" + ${sc1} +"&c2=" + ${sc2} +"&c3=" + sc3;
     http.doGet(url, function (data) {
     console.log(data);
     })
     };

     var show_modi = function (btn) {
     $("#" + btn).css("display", "block");
     };
     var hide_modi = function (btn) {
     $("#" + btn).css("display", "none");
     }*/
</script>

<jsp:include page="../include/footer.jsp"/>