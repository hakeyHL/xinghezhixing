<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../../include/header.jsp"/>
<jsp:include page="../../include/left.jsp"/>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">top selling 编辑</h1>
        </div>

        <div class="col-lg-12" style="margin: 20px"></div>

        <form class="form-horizontal" action="/topselling/edit/${topSellingId}" enctype="multipart/form-data"
              id="form_edit" method="post">

            <div class="form-group">
                <label class="col-sm-3 control-label">标题：</label>

                <div class="col-sm-7">
                    <input type="text" class="form-control" value="${title}" readonly="true">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label">图片：</label>

                <div class="col-sm-7">
                    <div class="control-group">
                        <div class="controls" style="width: 300px">
                            <div class="fileupload fileupload-new" data-provides="fileupload">

                                <div class="fileupload-new thumbnail" style="width: 200px; height: 150px;">
                                    <img src="${oriImageUrl}" alt="" id="image_url">
                                </div>
                                <div class="fileupload-preview fileupload-exists thumbnail"
                                     style="max-width: 200px; max-height: 150px; line-height: 20px;"></div>
                                <div>
                                        <span class="btn btn-file"><span class="fileupload-new">选择图片</span>
                                        <span class="fileupload-exists">更换</span>
                                        <input type="file" class="default" id="upload_img" name="file" img_url="false"></span>
                                </div>
                            </div>
                        </div>
                        <div id="tip_div"
                             style="margin: 10px; width: 200px; color: rgb(255, 0, 0); display: none; position:absolute;top:60px;left:226px">
                            请选择图片
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-9">
                    <button type="submit" class="btn btn-default" id="button_submit">确定</button>
                    <button class="btn btn-default" onclick="back()">取消</button>
                </div>
            </div>

        </form>
    </div>
    <%--<jsp:include page="../include/page.jsp"/>--%>
    <jsp:include page="../../include/page.jsp"/>
</div>

<jsp:include page="../../include/footer.jsp"/>

<script>

    function back() {
        history.go(-1);
    }

</script>