<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<style>
    .stat_count_right {
        text-align: right;
    }

    .tb_text_center {
        text-align: center;
    }
    .refresh_{
        display: none;
    }
</style>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">客户端访问统计
                <div style="margin-top: 10px;margin-bottom: 10px">
                    <h4>只统计Android4.3(含)以上的设备</h4>
                </div>
            </h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>

    <div class="row">
        <div class="col-lg-6">
            <div class="panel panel-primary">
                <div class="panel-heading">装有电商APP的设备统计</div>
                <div class="panel-body">
                    <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                        <thead>
                        <tr>
                            <td class="tb_text_center">总设备数</td>
                        </tr>
                        </thead>
                        <tr>
                            <td class="tb_text_center">${totalVisit.newDeviceWithShop}</td>
                        </tr>
                        </tbody>
                    </table>

                    <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                        <thead>
                        <tr>
                            <td class="tb_text_center">日期</td>
                            <td class="tb_text_center">活跃设备(老/新)</td>
                            <td class="tb_text_center">比价设备数(老/新)</td>
                            <td class="tb_text_center">更新时间</td>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${dayVisits}" var="dayVisit">
                            <tr onmouseover="rowCtrl.on('${dayVisit.ymd}')" onmouseout="rowCtrl.out('${dayVisit.ymd}')">
                                <td class="tb_text_center">
                                        ${dayVisit.ymd}
                                    <a href="/client/statnow?ymd=${dayVisit.ymd}" class="refresh_" id="refresh_${dayVisit.ymd}"><span
                                            class="fa fa-refresh"></span></a>
                                </td>
                                <td class="stat_count_right">${dayVisit.aliveDeviceWithShop}
                                    (${dayVisit.aliveDeviceWithShop-dayVisit.newDeviceWithShop}/${dayVisit.newDeviceWithShop})
                                </td>
                                <td class="stat_count_right">${dayVisit.visitDevice}/
                                    (${dayVisit.visitDevice - dayVisit.visitDeviceNew}/${dayVisit.visitDeviceNew})
                                </td>
                                <td class="tb_text_center">${dayVisit.updateTime}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="col-lg-6">
            <div class="panel panel-info">
                <div class="panel-heading">全部设备统计</div>
                <div class="panel-body">
                    <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                        <thead>
                        <tr>
                            <td class="tb_text_center">总设备数</td>
                        </tr>
                        <tr>
                            <td class="tb_text_center">${totalVisit.newDevice}</td>
                        </tr>
                        </tbody>
                    </table>

                    <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                        <thead>
                        <tr>
                            <td class="tb_text_center">日期</td>
                            <td class="tb_text_center">活跃设备(老/新)</td>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${dayVisits}" var="dayVisit">
                            <tr>
                                <td class="tb_text_center">${dayVisit.ymd}</td>
                                <td class="stat_count_right">${dayVisit.aliveDevice}
                                    (${dayVisit.aliveDevice-dayVisit.newDevice}/${dayVisit.newDevice})
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    var rowCtrl = {
        on: function (ymd) {
            $(".refresh_").hide();
            $("#refresh_" + ymd).show();
        },
        out: function (ymd) {
            $(".refresh_").hide();
        }
    }
</script>

<jsp:include page="../include/footer.jsp"/>