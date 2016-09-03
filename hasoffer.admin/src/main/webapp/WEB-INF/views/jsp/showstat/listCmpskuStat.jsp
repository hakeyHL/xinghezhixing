<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">设备频率统计</h1>
        </div>

        <form action="/showstat/listCmpskuStat" method="get">

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">渠道</span>
                    <select id="marketChannelSelect" class="form-control" name="market">
                        <option value="ALL">全部渠道</option>
                        <option value="OFFICIAL" <c:if test="${market=='OFFICIAL'}">selected</c:if>>官网
                        </option>
                        <option value="GOOGLEPLAY" <c:if test="${market=='GOOGLEPLAY'}">selected</c:if>>
                            GOOGLEPLAY
                        </option>
                        <option value="PREASSEMBLE" <c:if test="${market=='PREASSEMBLE'}">selected</c:if>>
                            预装
                        </option>
                        <option value="WANGMENG" <c:if test="${market=='WANGMENG'}">selected</c:if>>网盟
                        </option>
                        <option value="SHANCHUAN" <c:if test="${market=='SHANCHUAN'}">selected</c:if>>闪传
                        </option>
                        <option value="DUOBAO" <c:if test="${market=='DUOBAO'}">selected</c:if>>夺宝</option>
                    </select>
                </div>
            </div>

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">起始时间</span>
                    <input size="16" type="text" class="form-control form_datetime" id="startTime" name="startTime"
                           value="${startTime}">
                </div>
                <script>
                    $("#startTime").datepicker({dateFormat: 'yy-mm-dd'});
                </script>
            </div>

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">结束时间</span>
                    <input size="16" type="text" class="form-control form_datetime" id="endTime" name="endTime"
                           value="${endTime}">
                </div>
                <script>
                    $("#endTime").datepicker({dateFormat: 'yy-mm-dd'});
                </script>
            </div>
            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">默认时间</span>
                    <input size="16" type="text" class="form-control form_datetime" id="days" name="days" value="10">
                </div>
            </div>
            <div class="col-lg-2">
                <button type="submit" class="btn btn-primary">查询</button>
            </div>

            <div class="col-lg-12" style="margin: 5px"></div>

            <%--<div class="row">
                <div class="col-lg-12">
                    <div class="col-lg-2">当前符合条件数据 ${page.recordCount} </div>

                    &lt;%&ndash;<div class="col-lg-2">装有电商总设备数 ${deviceWithShop}</div>

                    <div class="col-lg-2">全部总设备数 ${aliveDevices}</div>&ndash;%&gt;
                </div>
            </div>

            <div class="col-lg-12" style="margin: 5px"></div>

            <div class="row">
                <div class="col-lg-12">
                    <div class="col-lg-2" style="width: 600px"> 排序 :
                        <input type="hidden" id="sort" name="sort" value="${sort}">
                    <span style="margin-right: 20px"><a id="time_desc"
                                                        style="cursor: pointer;color: #808080">默认(时间降序)</a></span>
                        <span style="margin-right: 20px"><a id="time_asc" style="cursor: pointer">时间升序</a></span>
                    </div>
                </div>
            </div>--%>

        </form>

        <div class="col-lg-12" style="margin: 5px"></div>

        <div class="row">
            <div class="col-lg-12">
                <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                    <thead>
                    <tr>
                        <td>渠道</td>
                        <td>设备</td>
                        <%--<td>存活设备（All/New）</td>--%>
                        <td>下载时间</td>
                        <td>触发天数</td>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${aliveStats}" var="alive">
                        <tr>
                            <td>${alive.marketChannel}</td>
                            <td>${alive.deviceId}</td>
                                <%--<td>${alive.allAlive} / ${alive.newAlive}</td>--%>
                            <td>${alive.deviceYmd}</td>
                            <td>${alive.useDaySize}</td>

                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

        </div>
        <%--<jsp:include page="../include/page.jsp"/>--%>
        <jsp:include page="../include/page.jsp"/>
    </div>

    <script type="text/javascript">
        $(document).ready(function () {
            var sort = $('#sort');
            var time_asc = $('#time_asc');
            var time_desc = $('#time_desc');
            var other_sort = $('#other_sort');

            if (sort.val() == '') {
                sort.val('0');
            }
            if (sort.val() == '0') {
                time_asc.css('color', '#72ACE3');
                time_desc.css('color', 'grey');
                other_sort.css('color', '#72ACE3');
            } else if (sort.val() == '1') {
                time_asc.css('color', 'grey');
                time_desc.css('color', '#72ACE3');
                other_sort.css('color', '#72ACE3');
            } else {
                time_asc.css('color', '#72ACE3');
                time_desc.css('color', '#72ACE3');
                other_sort.css('color', 'grey');
            }
            $('#time_desc').click(function () {
                $('#sort').val('0');
                time_asc.css('color', '#72ACE3');
                time_desc.css('color', 'grey');
                other_sort.css('color', '#72ACE3');
            });

            $('#time_asc').click(function () {
                sort.val('1');
                time_asc.css('color', 'grey');
                time_desc.css('color', '#72ACE3');
                other_sort.css('color', '#72ACE3');
                console.info(sort.val());
            });


            $('#other_sort').click(function () {
                sort.val('2');
                time_asc.css('color', '#72ACE3');
                time_desc.css('color', '#72ACE3');
                other_sort.css('color', 'grey');
            });

        });


    </script>

<jsp:include page="../include/footer.jsp"/>