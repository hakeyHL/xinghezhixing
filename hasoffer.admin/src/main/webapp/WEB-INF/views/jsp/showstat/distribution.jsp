<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<div id = "page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">日活分布</h1>
        </div>

        <form action="/admin/distribution" method="get">

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">渠道</span>
                    <select id="marketChannelSelect" class="form-control" name="marketChannelString">
                        <option value="">选择渠道</option>
                        <option value="OFFICIAL" <c:if test="${marketChannelString=='OFFICIAL'}">selected</c:if>>官网</option>
                        <option value="GOOGLEPLAY" <c:if test="${marketChannelString=='GOOGLEPLAY'}">selected</c:if>>GOOGLEPLAY</option>
                        <option value="PREASSEMBLE" <c:if test="${marketChannelString=='PREASSEMBLE'}">selected</c:if>>预装</option>
                        <option value="WANGMENG" <c:if test="${marketChannelString=='WANGMENG'}">selected</c:if>>网盟</option>
                        <option value="SHANCHUAN" <c:if test="${marketChannelString=='SHANCHUAN'}">selected</c:if>>闪传</option>
                        <option value="DUOBAO" <c:if test="${marketChannelString=='DUOBAO'}">selected</c:if>>夺宝</option>
                    </select>
                </div>
            </div>

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">Campaign</span>
                    <select id="campaign" class="form-control" name="campaign">
                        <option value="">选择Campaign</option>
                        <option value="OFFICIAL" <c:if test="${marketChannelString=='OFFICIAL'}">selected</c:if>>官网</option>
                        <option value="GOOGLEPLAY" <c:if test="${marketChannelString=='GOOGLEPLAY'}">selected</c:if>>GOOGLEPLAY</option>
                        <option value="PREASSEMBLE" <c:if test="${marketChannelString=='PREASSEMBLE'}">selected</c:if>>预装</option>
                        <option value="WANGMENG" <c:if test="${marketChannelString=='WANGMENG'}">selected</c:if>>网盟</option>
                        <option value="SHANCHUAN" <c:if test="${marketChannelString=='SHANCHUAN'}">selected</c:if>>闪传</option>
                        <option value="DUOBAO" <c:if test="${marketChannelString=='DUOBAO'}">selected</c:if>>夺宝</option>
                    </select>
                </div>
            </div>

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">ADset</span>
                    <select id="aDset" class="form-control" name="aDset">
                        <option value="">选择ADset</option>
                        <option value="OFFICIAL" <c:if test="${marketChannelString=='OFFICIAL'}">selected</c:if>>官网</option>
                        <option value="GOOGLEPLAY" <c:if test="${marketChannelString=='GOOGLEPLAY'}">selected</c:if>>GOOGLEPLAY</option>
                        <option value="PREASSEMBLE" <c:if test="${marketChannelString=='PREASSEMBLE'}">selected</c:if>>预装</option>
                        <option value="WANGMENG" <c:if test="${marketChannelString=='WANGMENG'}">selected</c:if>>网盟</option>
                        <option value="SHANCHUAN" <c:if test="${marketChannelString=='SHANCHUAN'}">selected</c:if>>闪传</option>
                        <option value="DUOBAO" <c:if test="${marketChannelString=='DUOBAO'}">selected</c:if>>夺宝</option>
                    </select>
                </div>
            </div>


            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">查看日期</span>
                    <input size="16" type="text" class="form-control form_datetime" id="baseDate" name="baseDate" value="${baseDate}">
                </div>
                <script>
                    $("#baseDate").datepicker({dateFormat : 'yy-mm-dd'});
                </script>
            </div>

            <div class="col-lg-2">
                <button type="submit" class="btn btn-primary">查询</button>
            </div>

    <div class="col-lg-12" style="margin: 5px"></div>

    <div class="row">
        <div class="col-lg-2">当前符合条件数据 ${page.recordCount} </div>

        <div class="col-lg-2">查看日期存活设备总数 ${deviceNum}</div>

        <div class="col-lg-2">查看日期比价设备总数 ${ratioNum}</div>
    </div>

    <div class="col-lg-12" style="margin: 5px"></div>

    <div class="row" >
        <div class="col-lg-2" style="width: 600px" > 排序 :
            <input type="hidden" id="sort" name = "sort" value="${sort}">
            <span style="margin-right: 20px"><a id="time_desc" style="cursor: pointer;color: #808080" >默认(时间降序)</a></span>
            <span style="margin-right: 20px"><a id="time_asc" style="cursor: pointer">时间升序</a></span>
            <span><a id="other_sort" style="cursor: pointer">其他排序</a></span>

        </div>

    </div>

    </form>

    <div class="col-lg-12" style="margin: 5px"></div>

    <div class="row">
        <div class="col-lg-12">
            <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                <thead>
                <tr>
                    <td>下载日期</td>
                    <td>存活设备数</td>
                    <td>占查看日期存活比例</td>
                    <td>比价设备数</td>
                    <td>占查看日期比价比例</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${data}" var="map">
                    <tr>
                        <td>${map.date.toString().substring(0,10)}</td>
                        <td>${map.alives}</td>
                        <td>${map.alivesPercent}</td>
                        <td>${map.ratios}</td>
                        <td>${map.ratioPercent}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

    </div>

    <jsp:include page="../include/page.jsp"/>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        var sort = $('#sort');
        var time_asc = $('#time_asc');
        var time_desc = $('#time_desc');
        var other_sort = $('#other_sort');

        if(sort.val() == ''){
            sort.val('0');
        }
        if(sort.val() == '0'){
            time_asc.css('color', '#72ACE3');
            time_desc.css('color','grey');
            other_sort.css('color','#72ACE3');
        }else if(sort.val() == '1'){
            time_asc.css('color', 'grey');
            time_desc.css('color','#72ACE3');
            other_sort.css('color','#72ACE3');
        }else{
            time_asc.css('color', '#72ACE3');
            time_desc.css('color','#72ACE3');
            other_sort.css('color','grey');
        }
        $('#time_desc').click(function(){
            $('#sort').val('0');
            time_asc.css('color', '#72ACE3');
            time_desc.css('color','grey');
            other_sort.css('color','#72ACE3');
        });

        $('#time_asc').click(function(){
            sort.val('1');
            time_asc.css('color', 'grey');
            time_desc.css('color','#72ACE3');
            other_sort.css('color','#72ACE3');
            console.info(sort.val());
        });


        $('#other_sort').click(function(){
            sort.val('2');
            time_asc.css('color', '#72ACE3');
            time_desc.css('color','#72ACE3');
            other_sort.css('color','grey');
        });

    });


</script>

<jsp:include page="../include/footer.jsp"/>