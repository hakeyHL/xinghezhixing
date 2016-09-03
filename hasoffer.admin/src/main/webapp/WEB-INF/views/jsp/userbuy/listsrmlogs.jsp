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
      <h1 class="page-header">商品搜索记录</h1>
    </div>
    <!-- /.col-lg-12 -->
  </div>

  <jsp:include page="../search/list-table.jsp"></jsp:include>

  <jsp:include page="../include/page.jsp"/>

</div>

<jsp:include page="../include/footer.jsp"/>