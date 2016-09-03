<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    String contextPath = request.getContextPath();
%>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>


<div id="page-wrapper">

    <div class="col-lg-12" style="margin: 20px"></div>
    <form class="form-horizontal" action="<%=contextPath%>/deal/edit" enctype="multipart/form-data" id="form_edit"
          method="post" onsubmit="return dosubmit()">

        <input type="hidden" name="id" value="${deal.id}">
        <input type="hidden" name="listPageImage" value="${deal.listPageImage}">
        <input type="hidden" name="infoPageImage" value="${deal.infoPageImage}">
        <input type="hidden" name="website" value="${deal.website}">

        <div class="form-group">
            <label class="col-sm-3 control-label">Deal标题：</label>

            <div class="col-sm-7">
                <input type="text" name="title" class="form-control" value="${deal.title}" placeholder="Deal标题">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">Deal跳转链接：</label>

            <div class="col-sm-7">
                <input type="text" name="linkUrl" class="form-control" value="${deal.linkUrl}" placeholder="Deal跳转链接">
            </div>
        </div>


        <div class="form-group">
            <label class="col-sm-3 control-label">Deal图片：</label>

            <div class="col-sm-7">
                <div class="control-group">
                    <div class="controls" style="width: 300px">
                        <div class="fileupload fileupload-new" data-provides="fileupload"><input type="hidden" value=""
                                                                                                 name="">

                            <div class="fileupload-new thumbnail" style="width: 200px; height: 150px;">
                                <img src="${deal.imageUrl}" alt="" id="image_url">
                                <input name="imageUrl" value="${imagePath}" id="imageUrl" type="hidden">
                            </div>
                            <div class="fileupload-preview fileupload-exists thumbnail"
                                 style="max-width: 200px; max-height: 150px; line-height: 20px;"
                                 id="dealImagePreview"></div>
                            <div>
                                        <span class="btn btn-file"><span class="fileupload-new">选择图片</span>
                                        <span class="fileupload-exists">更换</span>
                                        <input type="file" class="default" id="upload_img" name="dealFile"
                                               img_url="false"></span>
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
            <label class="col-sm-3 control-label">Banner图片：</label>

            <div class="col-sm-7">
                <div class="control-group">
                    <div class="controls" style="width: 300px">
                        <div class="fileupload fileupload-new" data-provides="fileupload"><input type="hidden" value=""
                                                                                                 name="">

                            <div class="fileupload-new thumbnail" style="width: 200px; height: 150px;">
                                <img src="${bannerImageUrl}" alt="" name="banner_image_url" id="banner_image_url">
                                <input name="bannerImageUrl" type="hidden" value="${bannerImageUrl}"
                                       id="bannerImageUrl">
                            </div>
                            <div class="fileupload-preview fileupload-exists thumbnail"
                                 style="max-width: 200px; max-height: 150px; line-height: 20px;"
                                 id="bannerImagePreview"></div>
                            <div>
                                        <span class="btn btn-file"><span class="fileupload-new">选择图片</span>
                                        <span class="fileupload-exists">更换</span>
                                        <input type="file" class="default" id="banner_upload_img" name="bannerFile"
                                               banner_img_url="false"></span>
                            </div>
                        </div>
                    </div>
                    <div id="banner_tip_div"
                         style="margin: 10px; width: 200px; color: rgb(255, 0, 0); display: none; position:absolute;top:60px;left:226px">
                        请选择图片
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">推送到banner展示：</label>

            <div class="col-sm-7">

                <label class="radio-inline">
                    <input type="radio" name="push" value="true" id="inlineRadio1"> 展示
                </label>
                <label class="radio-inline">
                    <input type="radio" name="push" value="false" id="inlineRadio2"> 不展示
                </label>

            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">是否显示到前端：</label>

            <div class="col-sm-7">

                <label class="radio-inline">
                    <input type="radio" name="display" value="true" id="inlineRadio3"> 显示
                </label>
                <label class="radio-inline">
                    <input type="radio" name="display" value="false" id="inlineRadio4"> 不显示
                </label>

            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">折扣额度：</label>

            <div class="col-sm-6">
                <input type="text" name="discount" id="discount" value="${deal.discount}"
                       class="form-control">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">生效时间：</label>

            <div class="col-sm-6">
                <input type="text" name="createTime" id="createTime" value="${fn:substring(deal.createTime, 0, 10)}"
                       class="form-control form_datetime">
            </div>
        </div>
        <%--<script>--%>
        <%--$("#createTime").datetimepicker();--%>
        <%--</script>--%>

        <div class="form-group">
            <label class="col-sm-3 control-label">失效时间：</label>

            <div class="col-sm-6">
                <input type="text" name="expireTime" id="expireTime" value="${fn:substring(deal.expireTime, 0, 10)}"
                       class="form-control form_datetime">
            </div>
        </div>
        <%--<script>--%>
        <%--$("#expireTime").datetimepicker();--%>
        <%--</script>--%>

        <div class="form-group">
            <label class="col-sm-3 control-label">价格描述：</label>

            <div class="col-sm-7">
                <textarea class="form-control" id="priceDescription" name="priceDescription"
                          rows="5">${deal.priceDescription}</textarea>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">deal描述：</label>

            <div class="col-sm-7">
                <textarea class="form-control" name="description"
                          rows="5">${deal.description}</textarea>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                <button type="submit" class="btn btn-default" id="button_submit">确定</button>
            </div>
        </div>

    </form>


</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    提示消息:
                </h4>
            </div>
            <div class="modal-body">
                当Deal设置为前端显示时价格描述不能为空 !
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary"
                        data-dismiss="modal">关闭
                </button>
            </div>
        </div>
    </div>
</div>
<script>

    $().ready(function () {
        var push = "${deal.push}";
        var display = "${deal.display}";
        var inlineRadio1 = $("#inlineRadio1");
        var inlineRadio2 = $("#inlineRadio2");
        var inlineRadio3 = $("#inlineRadio3");
        var inlineRadio4 = $("#inlineRadio4");

        if (push == "true") {
            inlineRadio1.attr("checked", "checked");
        } else {
            inlineRadio2.attr("checked", "checked");
        }
        if (display == "true") {
            inlineRadio3.attr("checked", "checked");
        } else {
            inlineRadio4.attr("checked", "checked");
        }

        var img = $("#image_url");
        var imgUrl = "${deal.imageUrl}";
        if (imgUrl != "") {
            $("#upload_img").attr("img_url", true);
            img.attr("src", imgUrl);
        }
        inlineRadio2.on("click", function () {
            inlineRadio1.attr("checked", false);
            inlineRadio2.attr("checked", "checked");
            $("#tip_div").hide();
        });

        inlineRadio1.on("click", function () {
            inlineRadio2.attr("checked", false);
            inlineRadio1.attr("checked", "checked");
        });
        inlineRadio4.on("click", function () {
            inlineRadio3.attr("checked", false);
            inlineRadio4.attr("checked", "checked");
            $("#tip_div").hide();
        });

        inlineRadio3.on("click", function () {
            inlineRadio4.attr("checked", false);
            inlineRadio3.attr("checked", "checked");
        });
    });

    function dosubmit() {
        var inlineRadio1 = $("#inlineRadio1");
        var inlineRadio3 = $("#inlineRadio3");
        var checked = inlineRadio1.attr("checked");
        var checked3 = inlineRadio3.attr("checked");
        var dealImagePreviewImg = $("#dealImagePreview img").length;
        if (dealImagePreviewImg > 0) {
            $("#upload_img").attr("img_url", true);
            $("#imageUrl").val("");
        } else {
            if ($("#imageUrl").val() != "") {
                $("#upload_img").attr("img_url", true);
            } else {
                $("#upload_img").attr("img_url", false);
            }
        }
        if (checked == "checked") {
            var bannerImagePreviewImg = $("#bannerImagePreview img").length;
            if (bannerImagePreviewImg > 0) {
                //已从本地选择图片则清空bannerImageUrl值以传送文件为准
                $("#bannerImageUrl").val("");
                $("#banner_upload_img").attr("banner_img_url", true);
            } else {
                if ($("#bannerImageUrl").val() != "") {
                    //有值则默认使用原图
                    $("#banner_upload_img").attr("banner_img_url", true);
                } else {
                    $("#banner_upload_img").attr("banner_img_url", false);
                }
            }
            var img = $("#banner_upload_img").attr("banner_img_url");
            if (img == "false") {
                $("#banner_tip_div").show();
                return false;
            }
        }
        if (checked3 == "checked") {
            var priDesLength = $("#priceDescription").val().length;
            if (priDesLength < 1) {
                $("#myModal").modal('show');
                return false;
            }
            var imgurl = $("#upload_img").attr("img_url");
            if (imgurl == "false") {
                $("#tip_div").show();
                return false;
            }
        }
        var button_submit = $("#button_submit");
        button_submit.attr("disabled", true);
        return true;
    }

</script>

<jsp:include page="../include/footer.jsp"/>
