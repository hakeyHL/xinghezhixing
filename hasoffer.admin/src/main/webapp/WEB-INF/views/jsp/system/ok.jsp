<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>
<style>

</style>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">OK</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <div class="alert alert-success" role="alert">${result}</div>
        </div>
    </div>
</div>

<jsp:include page="../include/footer.jsp"/>