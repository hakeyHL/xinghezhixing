<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<style>
    .td_text_center {
        text-align: center;
        width: 150px;
    }

    .td_text_right {
        width: 120px;
        text-align: right;
    }

    .row_btn {
        display: none;
    }
</style>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">系统监视</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <div class="row">
        <div class="col-lg-5">
            <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                <tr>
                    <td class="td_text_center">日期</td>
                    <td class="td_text_right">价格更新数量</td>
                    <td></td>
                </tr>
                <c:forEach items="${results}" var="r">
                    <tr onmouseover="onTr('${r.id}')" onmouseout="outTr('${r.id}')">
                        <td class="td_text_center">${r.id}
                            <a href="javascript:void(0);" onclick="restat('${r.id}')"
                               class="row_btn row_btn_${r.id}">刷新</a>
                        </td>
                        <td class="td_text_right">${r.count}</td>
                        <td><a href="javascript:void(0);" onclick="restatAll('${r.id}')"
                               class="row_btn row_btn_${r.id} ">从当天开始刷新到昨天</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

<script>
    var onTr = function (ymd) {
        $(".row_btn_" + ymd).css("display", "block");
    };
    var outTr = function (ymd) {
        $(".row_btn_" + ymd).css("display", "none");
    };
    var restat = function (ymd) {
        var url = "/stat/cmpskupriceupdate/restat?day=" + ymd;
        http.doGet(url, function (data) {
            window.location.reload();
        });
    };
    var restatAll = function (ymd) {
        var url = "/stat/cmpskupriceupdate/restat?all=true&day=" + ymd;
        http.doGet(url, function (data) {
            window.location.reload();
        });
    };
</script>

<jsp:include page="../include/footer.jsp"/>