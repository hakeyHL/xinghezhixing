<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h1 class="page-header">流量劫持抓取统计</h1>
    </div>

    <form action="/showstat/listsearchloghijacktest" method="get">

      <div class="col-lg-2">
        <div class="input-group">
          <span class="input-group-addon">站点</span>
          <select id="webSiteSelect" class="form-control" name="webSite">
            <option value="">全部渠道</option>
            <option value="flipkart" <c:if test="${webSite=='flipkart'}">selected</c:if>>flipkart</option>
            <option value="snapdeal" <c:if test="${webSite=='snapdeal'}">selected</c:if>>snapdeal</option>
            <option value="shopclues" <c:if test="${webSite=='shopclues'}">selected</c:if>>shopclues</option>
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
        <button type="submit" class="btn btn-primary">查询</button>
      </div>

      <div class="col-lg-12" style="margin: 5px"></div>


    </form>

    <div class="col-lg-12" style="margin: 5px"></div>

    <div class="row">
      <div class="col-lg-12">
        <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
          <thead>
          <tr>
            <td>日期</td>
            <td>站点</td>
            <td>当日应劫持总数</td>
            <td>劫持成功数</td>
            <td>劫持失败数</td>
            <td>因重名失败数</td>
            <td>因no index失败数</td>
            <td>抓取成功数</td>
            <td>抓取失败数</td>
            <td>更新时间</td>
          </tr>
          </thead>
          <tbody>
          <c:forEach items="${countList}" var="count">
            <tr>
              <td>${count.date}</td>
              <td>${count.website}</td>
              <td>${count.totalAmount}</td>
              <td>${count.statusSuccessAmount}</td>
              <td>${count.statusFailAmount}</td>
              <td>${count.differentUrlAmount}</td>
              <td>${count.noIndexAmount}</td>
              <td>${count.noIndexSuccessAmount}</td>
              <td>${count.noIndexFailAmount}</td>
              <td>${count.updateDate}</td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>

    </div>
    <%--<jsp:include page="../include/page.jsp"/>--%>
    <jsp:include page="../include/page.jsp"/>
  </div>

<jsp:include page="../include/footer.jsp"/>