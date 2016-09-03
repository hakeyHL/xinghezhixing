/**
 * @author 杨海洋
 * 主要功能：
 * 1.初始化可选班型弹出窗口
 * 2.将查询结果以列表形式展示在弹窗中
 * 3.操作待选列表对应【选中】按钮后，在新增招简页面“已选班型列表”下动态增加一条被选中的班型
 * */
tadv.sltClsType = function () {
    function initPage() {
        $("#selectableClasses").modal({
            backdrop: false,
            keyboard: false
        });
        // 绑定相关事件
        bindEvent();
    }

    function initData(re) {

    }

    function bindEvent() {

        /**
         * 弹出层选择班型-级联选择二级项目
         */
        $("#_proFirstId").change(function () {
            $.ajax({
                type: "POST",
                url: contextPath1 + "/course/ajaxSelectProSeconds.action",
                cache: false,
                async: false,
                data: {proFirstId: $("#_proFirstId").val()},
                success: function (jsonList) {

                    $("#_proSecondId").find("option:eq(0)").nextAll().remove();
                    for (var i = 0; i < jsonList.proSecondList.length; i++) {
                        $("#_proSecondId").append("<option value='" + jsonList.proSecondList[i].projectId + "'>" + jsonList.proSecondList[i].projectName + "</option>");
                    }
                }
            });
        });
    }

    return {
        initPage: initPage
    };
};