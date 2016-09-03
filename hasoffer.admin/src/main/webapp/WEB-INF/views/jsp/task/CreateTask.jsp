<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<style>
    .input-field {
        width: 800px;
        height: 50px;
    }
    .input-group-addon{
        width: 50px;
        height: 50px;
    }
</style>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">新建任务</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>

    <div class="row">
        <div class="col-lg-12">
            <form id="taskCreateForm" action="/t/create" method="post">

                <div class="input-group">
                    <span class="input-group-addon" id="basic-addon1">*</span>
                    <input type="text" class="input-field" name="urlTemplate" placeholder="--请输入抓取的链接--" aria-describedby="basic-addon1">
                </div>
                <br/>

                <jsp:include page="../include/module/category.jsp"/>
                <br/><br/><br/>

                <div class="input-group">
                    <span class="input-group-addon" id="basic-addon3">*</span>
                    <input type="text" class="input-field" name="start" placeholder="--请输入起始数字--" aria-describedby="basic-addon1">
                </div>
                <br/>

                <div class="input-group">
                    <span class="input-group-addon" id="basic-addon4">*</span>
                    <input type="text" class="input-field" name="size" placeholder="--请输入抓取数量--" aria-describedby="basic-addon1">
                </div>
                <br/>

                <div class="btn-group" role="group" aria-label="...">
                    <button type="button" class="btn btn-primary" onclick="submit()">新建</button>
                </div>

            </form>
        </div>
    </div>
    <jsp:include page="../include/page.jsp"/>
</div>
<jsp:include page="../include/footer.jsp"/>
<script>

    $(document).ready(function () {
        categoryManager.load('${page.pageParams.category1}', '${page.pageParams.category2}', '${page.pageParams.category3}');
    });

    function submit(){
        //todo 表单数据校验
        $("#taskCreateForm").submit();
    }

</script>