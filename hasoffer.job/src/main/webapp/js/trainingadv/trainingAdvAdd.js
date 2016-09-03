/**
 * @author 杨海洋
 * 功能描述：
 * 1.完成"新增招简"页面数据初始化
 * 2.绑定页面操作事件
 */
/* 针对招简声明脚本对象*/
var tadv = {};
$(document).ready(function () {
    //form校验
    $('#trainingAdv_form').validationEngine();
    $('#trainingAdvClass_form').validationEngine();

    $('#goBack').click(function () {
        window.location.href = contextPath1 + "/course/showTrainingAdvListInfo.action";
    });

    //保存FORM
    $("#saveForm").click(function () {
        if ($("#trainingAdv_form").validationEngine("validate")) {
            $("#trainingAdv_form").submit();
        }
    });


    /**
     * 级联选择二级项目
     */
    $("#proFirstId").change(function () {
        $.ajax({
            type: "POST",
            url: contextPath1 + "/course/ajaxSelectProSeconds.action",
            cache: false,
            async: false,
            data: {proFirstId: $("#proFirstId").val()},
            success: function (jsonList) {

                $("#proSecondId").find("option:eq(0)").nextAll().remove();
                for (var i = 0; i < jsonList.proSecondList.length; i++) {
                    $("#proSecondId").append("<option value='" + jsonList.proSecondList[i].projectId + "'>" + jsonList.proSecondList[i].projectName + "</option>");
                }
            }
        });
    });

});

/**
 *    选择班型
 */
function sltCls() {
    var sltCls = tadv.sltClsType();
    sltCls.initPage();
}