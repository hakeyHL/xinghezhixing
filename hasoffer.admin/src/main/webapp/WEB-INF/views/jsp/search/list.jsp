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
            <h1 class="page-header">搜索历史</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <div class="row" style="margin-bottom: 20px;">
        <form action="/s/list" method="get">
            <div class="col-md-1">
                <div class="checkbox">
                    <label>
                        <input type="checkbox" id="ctrlDate" onclick="setDate()" <c:if test="${byDay}">checked</c:if> > 指定日期
                    </label>
                </div>
                <script>
                    function setDate() {
                        var byDate = ${"ctrlDate"}.checked;
                        if (byDate) {
                            $("#createDate").attr("name", "createDate");
                            $("#selectdatebox").show();
                        } else {
                            $("#createDate").attr("name", "");
                            $("#selectdatebox").hide();
                        }
                    }
                </script>
            </div>

            <div class="col-md-2" id="selectdatebox">
                <div class="input-group">
                    <span class="input-group-addon">创建日期</span>
                    <input size="16" type="text" class="form-control form_datetime"
                           id="createDate" name="createDate" <c:if test="${byDay}">value='${date}'</c:if> >
                </div>
                <script>
                    $("#createDate").datepicker();
                    <c:if test="${!byDay}">
                        $("#selectdatebox").hide();
                    </c:if>
                </script>
            </div>
            <div class="col-lg-12" style="margin:5px"></div>
            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">搜索记录</span>
                    <select class="form-control" name="queryType">
                        <option value="0"
                                <c:if test="${page.pageParams.queryType==0}">selected</c:if> >全部
                        </option>
                        <option value="1" <c:if test="${page.pageParams.queryType==1}">selected</c:if>>无结果</option>
                        <option value="2" <c:if test="${page.pageParams.queryType==2}">selected</c:if>>有结果</option>
                    </select>
                </div>
            </div>
            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">结果</span>
                    <select class="form-control" name="precise">
                        <option value="ALL" <c:if test="${page.pageParams.precise=='ALL'}">selected</c:if>>
                            全部
                        </option>
                        <option value="NOCHECK" <c:if test="${page.pageParams.precise=='NOCHECK'}">selected</c:if>>
                            未检查
                        </option>
                        <option value="MANUALSET" <c:if test="${page.pageParams.precise=='MANUALSET'}">selected</c:if>>
                            已人工关联
                        </option>
                        <option value="TIMERSET" <c:if test="${page.pageParams.precise=='TIMERSET'}">selected</c:if>>
                            程序关联
                        </option>
                        <option value="TIMERSET2" <c:if test="${page.pageParams.precise=='TIMERSET2'}">selected</c:if>>
                            程序关联(新)
                        </option>
                    </select>
                </div>
            </div>

            <div class="col-lg-12" style="margin: 5px"></div>

            <div class="col-lg-2">
                <div class="input-group">
                    <span class="input-group-addon">排序</span>
                    <select class="form-control" name="sort">
                        <option value="time_desc" <c:if test="${page.pageParams.sort=='time_desc'}">selected</c:if>>
                            最新搜索
                        </option>
                        <option value="count_desc" <c:if test="${page.pageParams.sort=='count_desc'}">selected</c:if>>
                            搜索次数
                        </option>
                    </select>
                </div>
            </div>

            <div class="col-lg-2">
                <button type="submit" class="btn btn-primary">查询</button>
            </div>
        </form>
    </div>

    <jsp:include page="./list-table.jsp"/>

    <jsp:include page="../include/page.jsp"/>
</div>

<jsp:include page="../include/footer.jsp"/>