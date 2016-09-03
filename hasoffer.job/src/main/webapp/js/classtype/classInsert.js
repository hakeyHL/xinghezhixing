$(document).ready(function () {
    //form校验
    $('#listForm').validationEngine();

    $('#goBack').click(function () {
        window.location.href = ctx + "/course/showClassTypeInit.action";
    });
    //保存FORM
    $("#saveForm").click(function () {
    });
});
function onsub(type) {
    var val = $('input:radio[id="remodeTrue"]:checked').val();
    if (val == "ISREMOTEEDU") {
        var remoteEduResId = $("#remoteEduResId").val();
        if (remoteEduResId == null || remoteEduResId == "") {
            alert("请配置远程教育");
            return;
        }
        if (type == "demo") {
            $("#statusCode").val("CS_UNPUBLISHED");
        } else {
            $("#statusCode").val("CS_ON_SALE");
        }
    } else {
        if (classSum < 1) {
            alert("请配置班型详情");
            return;
        }
        if (type == "demo") {
            $("#statusCode").val("CS_UNPUBLISHED");
        } else {
            if (parseFloat($("#classPrice").val()) < classbzsjSum) {
                if (confirm("班型售价总额为：" + Number(classbzsjSum).toFixed(2) + "元,大于班型定价,是否确定继续?")) {
                    $("#statusCode").val("CS_WAIT");
                } else {
                    return;
                }
            } else {
                $("#statusCode").val("CS_ON_SALE");
            }
        }
        $("#classCost").val(Number(classSum));
    }
    $("#listForm").submit();
}

/**
 * 转化数字
 * @param val
 * @returns
 */
function toNum(val) {
    if (val == null || val == "") {
        return 0;
    }
    else {
        return parseFloat(val);
    }
}

/**
 * 根据下标删除Array
 */
Array.prototype.remove = function (dx) {

    if (isNaN(dx) || dx > this.length) {
        return false;
    }
    for (var i = 0, n = 0; i < this.length; i++) {
        if (this[i] != this[dx]) {
            this[n++] = this[i];
        }
    }
    this.length -= 1;
};

/**
 * 删除当前TR ,并且删除对应的选中标签
 */
function delTrDiv(obj, id, type, selId) {
    delTr(obj);
    cls(id);
    if (type == 'record') {
        recordArray.remove(selId);
    }
    if (type == "book") {
        bookArray.remove(selId);
    }
    if (type == "serv") {
        servArray.remove(selId);
    }
    if (type == "mod") {
        modArray.remove(selId);
    }
    if (type == "share") {
        shmodArray.remove(selId);
    }
    onAllChange();

}

/**
 * 删除当前TR
 */
function delTr(obj) {
    var tr = obj.parentNode.parentNode;
    var tbody = tr.parentNode;
    tbody.removeChild(tr);
}

/**
 * 根据ID删除DIV
 */
function cls(id) {
    var _div = document.getElementById(id);
    if (_div != null) {
        _div.parentNode.removeChild(_div);
    }
}
/**
 * 获取班型利润系数
 */
function getClassCost(id) {
    $.ajax({
        url: ctx + "/prj/AjaxGetClassCostRatio.action",
        type: "post",
        data: "firstProjectId=" + id,
        cache: false,
        async: false,
        success: function (data) {
            if (data.classCost == null) {
                alert("该项目未配置班型利润系数,请先配置班型利润系数");
            } else {
                //classCost = data.classCost.classCostRatio;
                ratioCost = data.classCost;
            }
        }
    });
}
/**
 * 添加选中的录播
 */
function onRecordClick(num, name) {
    var htm = '<div style="float:left;"class="div_red01" id="recNum' + num + '">' + name;
    htm += '<input type="hidden" value="' + num + '" id="recordIds" name="recordIds">';
    htm += '<span style="width:22px;border-width:0px;font-family:webdings;" onclick="cls(\'recNum' + num + '\')">r</span></div>';
    $(".rec_div_green").append(htm);
}

/**
 * 添加选中的教材
 */
function onBookClick(num, name) {
    var htm = '<div style="float:left;"class="div_red01" id="bookNum' + num + '">' + name;
    htm += '<input type="hidden" value="' + num + '" id="bookIds" name="bookIds">';
    htm += '<span style="width:22px;border-width:0px;font-family:webdings;" onclick="cls(\'bookNum' + num + '\')">r</span></div>';
    $(".book_div_green").append(htm);
}
/**
 * 添加选中的服务
 */
function onServClick(num, name) {
    var htm = '<div style="float:left;"class="div_red01" id="servNum' + num + '">' + name;
    htm += '<input type="hidden" value="' + num + '" id="servIds" name="servIds">';
    htm += '<span style="width:22px;border-width:0px;font-family:webdings;" onclick="cls(\'servNum' + num + '\')">r</span></div>';
    $(".serv_div_green").append(htm);
}
/**
 * 添加选中的模块
 */
function onModClick(num, name) {
    var htm = '<div style="float:left;"class="div_red01" id="modNum' + num + '">' + name;
    htm += '<input type="hidden" value="' + num + '" id="modIds" name="modIds">';
    htm += '<span style="width:22px;border-width:0px;font-family:webdings;" onclick="cls(\'modNum' + num + '\')">r</span></div>';
    $(".mod_div_green").append(htm);
}
/**
 * 添加选中的共享模块
 */
function onShareModClick(num, name) {
    var htm = '<div style="float:left;"class="div_red01" id="shareModNum' + num + '">' + name;
    htm += '<input type="hidden" value="' + num + '" id="shareModIds" name="shareModIds">';
    htm += '<span style="width:22px;border-width:0px;font-family:webdings;" onclick="cls(\'shareModNum' + num + '\')">r</span></div>';
    $(".shareMod_div_green").append(htm);
}
/**
 * 计算页面动态值
 */
function onAllChange() {
    //合并数组
    moduleArray = new Array();
    if (modArray != null) {
        moduleArray = moduleArray.concat(modArray);
    }
    if (shmodArray != null) {
        moduleArray = moduleArray.concat(shmodArray);
    }
    //班型计划招生人数
    var planRecruitNum = $("#planRecruitNum").val();
    classSum = 0;
    classbzsjSum = 0;
    var moduleCount = 0;
    //模块
    if (moduleArray != null && moduleArray.length > 0) {
        var innerhtml = "";
        countHours = 0;
        for (var i = 0; i < moduleArray.length; i++) {
            var mod = moduleArray[i];
            //模块总成本
            var modCountCost = toNum(mod.teacherCost) + toNum(mod.classMgrCost) + toNum(mod.attendantCost) + toNum(mod.assistantCost) + toNum(mod.otherCost) + toNum(mod.liveCost);

            if (50 > parseInt(mod.maxStuNum) && parseInt(mod.maxStuNum) > 0) {
                modCountCost = toNum(modCountCost) + toNum(mod.classroomCost1);
            }
            if (100 > parseInt(mod.maxStuNum) && parseInt(mod.maxStuNum) > 49) {
                modCountCost = toNum(modCountCost) + toNum(mod.classroomCost2);
            }
            if (200 > parseInt(mod.maxStuNum) && parseInt(mod.maxStuNum) > 99) {
                modCountCost = toNum(modCountCost) + toNum(mod.classroomCost3);
            }
            if (300 > parseInt(mod.maxStuNum) && parseInt(mod.maxStuNum) > 199) {
                modCountCost = toNum(modCountCost) + toNum(mod.classroomCost4);
            }
            if (parseInt(mod.maxStuNum) > 299) {
                modCountCost = toNum(modCountCost) + toNum(mod.classroomCost5);
            }
            moduleCount = Number(moduleCount) + Number(modCountCost);
            //人均成本
            var rjcb = 0;
            if (Number(mod.maxStuNum) > Number(planRecruitNum)) {
                rjcb = modCountCost / planRecruitNum;
            } else {
                rjcb = modCountCost / mod.maxStuNum;
            }
            //班型成本
            classSum = (Number(classSum) + Number(rjcb)).toFixed(2);
            //标准售价 
            var bzsj = 0;
            if (mod.instructionTypeCode.itemCode == "FACE_TO_FACE") {
                bzsj = rjcb / (1 - ratioCost.costRatioFtf / 100);
            } else {
                bzsj = rjcb / (1 - ratioCost.costRatioLive / 100);
            }
            classbzsjSum = Number(classbzsjSum) + Number(bzsj);
            innerhtml += '<tr class="odd">';
            innerhtml += '<input type="hidden" name="reqClassDTO.moudule[' + mod.moduleId + '].resId" value="' + mod.moduleId + '">';
            innerhtml += '<input type="hidden" name="reqClassDTO.moudule[' + mod.moduleId + '].resTypeCode" value="MODULE">';
            innerhtml += '<input type="hidden" name="reqClassDTO.moudule[' + mod.moduleId + '].resCost" value="' + Number(rjcb).toFixed(2) + '">';
            innerhtml += '<input type="hidden" name="reqClassDTO.moudule[' + mod.moduleId + '].price" value="' + Number(bzsj).toFixed(2) + '">';
            innerhtml += '<input type="hidden" name="reqClassDTO.moudule[' + mod.moduleId + '].refundFlagCode" value="Y">';
            innerhtml += '<input type="hidden" name="reqClassDTO.moudule[' + mod.moduleId + '].isJoinCode" value="Y">';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + mod.name + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + mod.maxStuNum + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + mod.totalClassHour + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + modCountCost + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + Number(rjcb).toFixed(2) + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + Number(bzsj).toFixed(2) + ' </td>';
            if (mod.isShareCode.itemCode == 'Y') {
                innerhtml += '<td style="vertical-align: middle; text-align: center;"><input type="button" value="删除" onclick="delTrDiv(this,\'shareModNum' + mod.moduleId + '\',\'share\',' + i + ')"></td>';
            }
            if (mod.isShareCode.itemCode == 'N') {
                innerhtml += '<td style="vertical-align: middle; text-align: center;"><input type="button" value="删除" onclick="delTrDiv(this,\'modNum' + mod.moduleId + '\',\'mod\',' + i + ')"></td>';
            }
            countHours = Number(mod.totalClassHour) + Number(countHours);
            innerhtml += '</tr>';
        }
        $("#modView").html(innerhtml);
        $("#sumClassHour").val(countHours);

    }
    //服务
    if (servArray != null && servArray.length > 0) {
        var innerhtml = "";
        for (var i = 0; i < servArray.length; i++) {
            var serv = servArray[i];
            //服务成本
            var servCost = 0;
            if (serv.costTypeCode == "N") {
                servCost = moduleCount * (serv.costPercent / 100);
            } else {
                servCost = toNum(serv.cost);
            }
            //人均成本
            var rjcb = servCost / planRecruitNum;
            //班型成本
            classSum = (Number(classSum) + Number(rjcb)).toFixed(2);
            //标准售价
            var bzsj = rjcb / (1 - ratioCost.costRatioServ / 100);
            classbzsjSum = Number(classbzsjSum) + Number(bzsj);
            innerhtml += '<tr class="odd">';
            innerhtml += '<input type="hidden" name="reqClassDTO.service[' + serv.serviceId + '].resId" value="' + serv.serviceId + '">';
            innerhtml += '<input type="hidden" name="reqClassDTO.service[' + serv.serviceId + '].resTypeCode" value="SERVICE">';
            innerhtml += '<input type="hidden" name="reqClassDTO.service[' + serv.serviceId + '].resCost" value="' + Number(rjcb).toFixed(2) + '">';
            innerhtml += '<input type="hidden" name="reqClassDTO.service[' + serv.serviceId + '].price" value="' + Number(bzsj).toFixed(2) + '">';
            if (serv.joinPercentCode.itemCode == "Y") {
                innerhtml += '<input type="hidden" name="reqClassDTO.service[' + serv.serviceId + '].isJoinCode" value="Y">';
            } else {
                innerhtml += '<input type="hidden" name="reqClassDTO.service[' + serv.serviceId + '].isJoinCode" value="N">';
            }
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + serv.name + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + Number(rjcb).toFixed(2) + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + Number(bzsj).toFixed(2) + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;"><select name="reqClassDTO.service[' + serv.serviceId + '].refundFlagCode" class="form-control"  style="width:150px"><option value="Y" selected="selected">是</option> <option value="N">否</option></select></td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;"><input type="button" value="删除" onclick="delTrDiv(this,\'servNum' + serv.serviceId + '\',\'serv\',' + i + ')"></td>';
            innerhtml += '</tr>';
        }
        $("#servView").html(innerhtml);
    }
    //教材教辅
    if (bookArray != null && bookArray.length > 0) {
        var innerhtml = "";
        for (var i = 0; i < bookArray.length; i++) {
            var book = bookArray[i];
            //班型成本
            classSum = (Number(classSum) + Number(book.costPrice)).toFixed(2);
            classbzsjSum = Number(classbzsjSum) + Number(book.price);
            innerhtml += '<tr class="odd">';
            innerhtml += '<input type="hidden" name="reqClassDTO.book[' + book.bookId + '].resId" value="' + book.bookId + '">';
            innerhtml += '<input type="hidden" name="reqClassDTO.book[' + book.bookId + '].resTypeCode" value="BOOK">';
            innerhtml += '<input type="hidden" name="reqClassDTO.book[' + book.bookId + '].resCost" value="' + book.costPrice + '">';
            innerhtml += '<input type="hidden" name="reqClassDTO.book[' + book.bookId + '].price" value="' + book.price + '">';
            innerhtml += '<input type="hidden" name="reqClassDTO.book[' + book.bookId + '].refundFlagCode" value="Y">';
            innerhtml += '<input type="hidden" name="reqClassDTO.book[' + book.bookId + '].isJoinCode" value="N">';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + book.bookName + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + book.barcode + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + book.costPrice + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + book.price + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;"><input type="button" value="删除" onclick="delTrDiv(this,\'bookNum' + book.bookId + '\',\'book\',' + i + ')"></td>';
            innerhtml += '</tr>';
        }
        $("#bookView").html(innerhtml);
    }
    //录播
    if (recordArray != null && recordArray.length > 0) {
        var innerhtml = "";
        for (var i = 0; i < recordArray.length; i++) {
            var record = recordArray[i];
            //服务成本
            var recordCost = toNum(record.price);

            //人均成本
            var rjcb = recordCost / planRecruitNum;
            //班型成本
            classSum = (Number(classSum) + Number(rjcb)).toFixed(2);
            //标准售价
            var bzsj = rjcb / (1 - ratioCost.costRatioRecord / 100);
            classbzsjSum = Number(classbzsjSum) + Number(bzsj);
            innerhtml += '<tr class="odd">';
            innerhtml += '<input type="hidden" name="reqClassDTO.record[' + record.recCourseId + '].resId" value="' + record.recCourseId + '">';
            innerhtml += '<input type="hidden" name="reqClassDTO.record[' + record.recCourseId + '].resTypeCode" value="REMOTE_EDU">';
            innerhtml += '<input type="hidden" name="reqClassDTO.record[' + record.recCourseId + '].resCost" value="' + rjcb + '">';
            innerhtml += '<input type="hidden" name="reqClassDTO.record[' + record.recCourseId + '].refundFlagCode" value="Y">';
            innerhtml += '<input type="hidden" name="reqClassDTO.record[' + record.recCourseId + '].isJoinCode" value="Y">';
            innerhtml += '<input type="hidden" name="reqClassDTO.record[' + record.recCourseId + '].price" value="' + Number(bzsj).toFixed(2) + '">';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + record.name + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + record.uniqueCode + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + Number(rjcb).toFixed(2) + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;">' + Number(bzsj).toFixed(2) + '</td>';
            innerhtml += '<td style="vertical-align: middle; text-align: center;"><input type="button" value="删除" onclick="delTrDiv(this,\'recNum' + record.recCourseId + '\',\'record\',' + i + ')"></td>';
            innerhtml += '</tr>';
        }
        $("#recordView").html(innerhtml);
    }
    $("#lableId").html("班型成本" + classSum);
    $("#lableIdTwo").html("售价总和" + Number(classbzsjSum).toFixed(2));
}


/**
 * 选择共享模块
 */
function showShareMod(type) {
    if ($("#planRecruitNum").val() == null || $("#planRecruitNum").val() == "" || $("#planRecruitNum").val() == undefined) {
        alert("请输入计划招生人数");
        $("#planRecruitNum").focus();
        return;
    }
    if ($("#projectFirst").val() == "" || $("#projectFirst").val() == null || $("#projectFirst").val() == undefined) {
        alert("请选择一级项目");
        return;
    }
    if ($("#projectSecond").val() == null || $("#projectSecond").val() == "" || $("#projectSecond").val() == undefined) {
        alert("请选择二级项目");
        return;
    }
    if (type == 'show') {
        $('#shareModDiv').modal({
            backdrop: false,
            keyboard: false
        });
    } else if (type == 'select') {
        if ($("#shareModeNames").val() == null || $("#shareModeNames").val() == "") {
            alert("模块名称不能为空");
            $("#shareModeNames").focus();
            return;
        }
        var params = $("#shareModForm").serialize() + "&modSecondProject=" + $("#projectSecond").val();
        $.ajax({
            url: ctx + "/course/ajaxGetModuleList.action",
            type: "post",
            data: params,
            cache: false,
            async: false,
            success: function (data) {
                var innerhtml = "";
                for (var i = 0; i < data.result.length; i++) {
                    var mod = data.result[i];
                    innerhtml += '<tr class="odd">';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + mod.proFirstName + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + mod.proSecondName + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + mod.name + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">';
                    if (mod.instructionTypeCode == 'FACE_TO_FACE') {
                        innerhtml += '面授';
                    } else {
                        innerhtml += '直播';
                    }
                    innerhtml += '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + mod.totalClassHour + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;"><input type="button" value="选择" onclick="onShareModClick(' + mod.moduleId + ',\'' + mod.name + '\')"></td>';
                    innerhtml += '</tr>';
                }
                $("#shareModTb").html(innerhtml);
            }
        });
    } else {
        var params = "";
        $("input:hidden[name='shareModIds']").each(function (i, e) {
            params += "modIds=" + $(e).val() + "&";
        });
        if (params == "") {
            alert("请选共享择模块");
            return;
        }
        $.ajax({
            url: ctx + "/course/ajaxSelModuleList.action",
            type: "post",
            data: params,
            cache: false,
            async: false,
            success: function (data) {
                shmodArray = data.result;
                if (classCost != 0) {
                    onAllChange();
                    $('#shareModDiv').modal('hide');
                }
            }
        });
    }
}
/**
 * 选择模块
 */
function showMod(type) {
    if ($("#projectFirst").val() == "" || $("#projectFirst").val() == null || $("#projectFirst").val() == undefined) {
        alert("请选择一级项目");
        return;
    }
    if ($("#planRecruitNum").val() == null || $("#planRecruitNum").val() == "" || $("#planRecruitNum").val() == undefined) {
        alert("请输入计划招生人数");
        $("#planRecruitNum").focus();
        return;
    }
    if (type == 'show') {
        $('#modDiv').modal({
            backdrop: false,
            keyboard: false
        });
    } else if (type == 'select') {
        var params = $("#modForm").serialize();
        $.ajax({
            url: ctx + "/course/ajaxGetModuleList.action",
            type: "post",
            data: params,
            cache: false,
            async: false,
            success: function (data) {
                var innerhtml = "";
                for (var i = 0; i < data.result.length; i++) {
                    var mod = data.result[i];
                    innerhtml += '<tr class="odd">';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + mod.name + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">';
                    if (mod.instructionTypeCode == 'FACE_TO_FACE') {
                        innerhtml += '面授';
                    } else {
                        innerhtml += '直播';
                    }
                    innerhtml += '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + mod.totalClassHour + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;"><input type="button" value="选择" onclick="onModClick(' + mod.moduleId + ',\'' + mod.name + '\')"></td>';
                    innerhtml += '</tr>';
                }
                $("#modTb").html(innerhtml);
            }
        });
    } else {
        var params = "";
        $("input:hidden[name='modIds']").each(function (i, e) {
            params += "modIds=" + $(e).val() + "&";
        });
        if (params == "") {
            alert("请选择模块");
            return;
        }
        $.ajax({
            url: ctx + "/course/ajaxSelModuleList.action",
            type: "post",
            data: params,
            cache: false,
            async: false,
            success: function (data) {
                modArray = data.result;
                if (classCost != 0) {
                    onAllChange();
                    $('#modDiv').modal('hide');
                }

            }
        });
    }
}
/**
 * 选择录播
 */
function showRecord(type) {
    if ($("#projectFirst").val() == "" || $("#projectFirst").val() == null || $("#projectFirst").val() == undefined) {
        alert("请选择一级项目");
        return;
    }
    if ($("#planRecruitNum").val() == null || $("#planRecruitNum").val() == "" || $("#planRecruitNum").val() == undefined) {
        alert("请输入计划招生人数");
        $("#planRecruitNum").focus();
        return;
    }
    if (type == 'show') {
        $('#record').modal({
            backdrop: false,
            keyboard: false
        });
    } else if (type == "select") {
        if ($("#recordNames").val() == null || $("#recordNames").val() == "") {
            alert("录播名称不能为空");
            $("#recordNames").focus();
            return;
        }
        var params = $("#recordForm").serialize();
        $.ajax({
            url: ctx + "/prj/ajaxGetRecordList.action",
            type: "post",
            data: params,
            cache: false,
            async: false,
            success: function (data) {
                var innerhtml = "";
                for (var i = 0; i < data.result.length; i++) {
                    var record = data.result[i];
                    innerhtml += '<tr class="odd">';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + record.recName + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + record.uniqueCode + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + record.price + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;"><input type="button" value="选择" onclick="onRecordClick(' + record.recCourseId + ',\'' + record.recName + '\')"></td>';
                    innerhtml += '</tr>';
                }
                $("#recordTb").html(innerhtml);
            }
        });

    } else {
        var params = "";
        $("input:hidden[name='recordIds']").each(function (i, e) {
            params += "recordIds=" + $(e).val() + "&";
        });
        if (params == "") {
            alert("请选择录播");
            return;
        }
        $.ajax({
            url: ctx + "/prj/ajaxGetRecordByIds.action",
            type: "post",
            data: params,
            cache: false,
            async: false,
            success: function (data) {
                recordArray = data.result;
                if (classCost != 0) {
                    onAllChange();
                    $('#record').modal('hide');
                }

            }
        });
    }
}
/**
 * 选择教材教辅
 */
function showBook(type) {
    if ($("#planRecruitNum").val() == null || $("#planRecruitNum").val() == "" || $("#planRecruitNum").val() == undefined) {
        alert("请输入计划招生人数");
        $("#planRecruitNum").focus();
        return;
    }
    if (type == 'show') {
        $('#bookDiv').modal({
            backdrop: false,
            keyboard: false
        });
    } else if (type == "select") {
        if ($("#bookNames").val() == null || $("#bookNames").val() == "") {
            alert("教材教辅名称不能为空");
            $("#bookNames").focus();
            return;
        }
        var params = $("#bookForm").serialize();
        $.ajax({
            url: ctx + "/course/ajaxGetBookList.action",
            type: "post",
            data: params,
            cache: false,
            async: false,
            success: function (data) {
                var innerhtml = "";
                for (var i = 0; i < data.result.length; i++) {
                    var book = data.result[i];
                    innerhtml += '<tr class="odd">';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + book.projectName + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + book.bookName + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + book.barcode + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + book.price + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;"><input type="button" value="选择" onclick="onBookClick(' + book.bookId + ',\'' + book.bookName + '\')"></td>';
                    innerhtml += '</tr>';
                }
                $("#bookTb").html(innerhtml);
            }
        });
    } else {
        var params = "";
        $("input:hidden[name='bookIds']").each(function (i, e) {
            params += "bookIds=" + $(e).val() + "&";
        });
        if (params == "") {
            alert("请选择教材");
            return;
        }
        $.ajax({
            url: ctx + "/course/ajaxSelBookList.action",
            type: "post",
            data: params,
            cache: false,
            async: false,
            success: function (data) {
                bookArray = data.result;
                onAllChange();
                $('#bookDiv').modal('hide');
            }
        });
    }
}

/**
 * 选择服务
 */
function showServ(type) {
    if ($("#planRecruitNum").val() == null || $("#planRecruitNum").val() == "" || $("#planRecruitNum").val() == undefined) {
        alert("请输入计划招生人数");
        $("#planRecruitNum").focus();
        return;
    }
    if (type == 'show') {
        $('#servDiv').modal({
            backdrop: false,
            keyboard: false
        });
    } else if (type == "select") {
        if ($("#serviceNames").val() == null || $("#serviceNames").val() == "") {
            alert("服务名称不能为空");
            $("#serviceNames").focus();
            return;
        }
        var params = $("#servForm").serialize();
        $.ajax({
            url: ctx + "/course/ajaxGetServList.action",
            type: "post",
            data: params,
            cache: false,
            async: false,
            success: function (data) {
                var innerhtml = "";
                for (var i = 0; i < data.result.length; i++) {
                    var serv = data.result[i];
                    innerhtml += '<tr class="odd">';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + serv.name + '</td>';
                    if (serv.costTypeCode == "Y") {
                        innerhtml += '<td style="vertical-align: middle; text-align: center;">' + serv.cost + '</td>';
                    } else {
                        innerhtml += '<td style="vertical-align: middle; text-align: center;">' + serv.costPercent + '</td>';
                    }
                    innerhtml += '<td style="vertical-align: middle; text-align: center;"><input type="button" value="选择" onclick="onServClick(' + serv.serviceId + ',\'' + serv.name + '\')"></td>';
                    innerhtml += '</tr>';
                }
                $("#servTb").html(innerhtml);
            }
        });
    } else {
        var params = "";
        $("input:hidden[name='servIds']").each(function (i, e) {
            params += "servIds=" + $(e).val() + "&";
        });
        if (params == "") {
            alert("请选择服务");
            return;
        }
        $.ajax({
            url: ctx + "/course/ajaxSelServList.action",
            type: "post",
            data: params,
            cache: false,
            async: false,
            success: function (data) {
                servArray = data.result;
                if (classCost != 0) {
                    onAllChange();
                    $('#servDiv').modal('hide');
                }

            }
        });
    }
}
/**
 * 选择远程教育
 */
function showRemoteEdu(type) {
    if ($("#planRecruitNum").val() == null || $("#planRecruitNum").val() == "" || $("#planRecruitNum").val() == undefined) {
        alert("请输入计划招生人数");
        $("#planRecruitNum").focus();
        return;
    }
    if (type == 'show') {
        $('#remoteEdu').modal({
            backdrop: false,
            keyboard: false
        });
    } else if (type == "select") {
        if ($("#remoteEduNames").val() == null || $("#remoteEduNames").val() == "") {
            alert("远程教育名称不能为空");
            $("#remoteEduNames").focus();
            return;
        }
        var params = $("#remoteEduForm").serialize();
        $.ajax({
            url: ctx + "/prj/ajaxSelectRemoteEduList.action",
            type: "post",
            data: params,
            cache: false,
            async: false,
            success: function (data) {
                var innerhtml = "";
                for (var i = 0; i < data.result.length; i++) {
                    var remoteedu = data.result[i];
                    innerhtml += '<tr class="odd">';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + remoteedu.name + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + remoteedu.tuitionFee + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + remoteedu.serviceFee + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + remoteedu.suggestPrice + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + remoteedu.sessionName + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;"><input type="radio" id="remoteEduId" name="remoteEduId" value="' + remoteedu.remoteEduId + '"></td>';
                    innerhtml += '</tr>';
                }
                $("#remoteeduTb").html(innerhtml);
            }
        });
    } else {
        var remoteEduId = $('input[name="remoteEduId"]:checked').val();
        if (remoteEduId == "" || remoteEduId == undefined) {
            alert("请选择远程教育");
            return;
        }
        $.ajax({
            url: ctx + "/prj/ajaxSelectRemoteEdu.action",
            type: "post",
            data: {remoteEduId: remoteEduId},
            cache: false,
            async: false,
            success: function (data) {
                //远程教育
                if (data.result != null) {
                    var innerhtml = "";
                    var remoteedu = data.result;
                    innerhtml += '<tr class="odd">';
                    innerhtml += '<input type="hidden" id="remoteEduResId" name="reqClassDTO.remoteEdu.resId" value="' + remoteedu.remoteEduId + '">';
                    innerhtml += '<input type="hidden" name="reqClassDTO.remoteEdu.resTypeCode" value="REMOTE_EDU">';
                    innerhtml += '<input type="hidden" name="reqClassDTO.remoteEdu.isJoinCode" value="N">';
                    innerhtml += '<input type="hidden" name="reqClassDTO.remoteEdu.price" value="' + remoteedu.suggestPrice + '">';
                    innerhtml += '<input type="hidden" name="reqClassDTO.remoteEdu.tuitionFee" value="' + remoteedu.tuitionFee + '">';
                    innerhtml += '<input type="hidden" name="reqClassDTO.remoteEdu.serviceFee" value="' + remoteedu.serviceFee + '">';
                    innerhtml += '<input type="hidden" name="reqClassDTO.remoteEdu.refundFlagCode" value="Y">';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + remoteedu.name + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + remoteedu.tuitionFee + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + remoteedu.serviceFee + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;">' + remoteedu.suggestPrice + '</td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;"><input type="text" id="remoteEdure" value="' + (Number(remoteedu.serviceFee) + Number(remoteedu.tuitionFee)) + '" name="reqClassDTO.remoteEdu.refundPrice" readonly="readonly"  class="form-control"></td>';
                    innerhtml += '<td style="vertical-align: middle; text-align: center;"><input type="button" name="remoteEduId" value="删除" onclick="delTr(this)" class="btn btn-danger"></td>';
                    innerhtml += '</tr>';
                    $("#classPrice").val(Number(remoteedu.tuitionFee) + Number(remoteedu.serviceFee));
                    $("#classCost").val(Number(remoteedu.tuitionFee));
                    $("#remoteEduView").html(innerhtml);
                }
                $('#remoteEdu').modal('hide');
            }
        });

    }
}

function remoteProjectName(projectId) {
    if (projectId == '' || projectId == undefined || projectId == null) {
        $("#remoteprojectSecond").find("option").eq(0).nextAll().remove();
        return;
    }
    $.ajax({
        url: ctx + "/prj/ajaxSelectProjectLevel.action",
        type: "post",
        data: {projectId: projectId},
        cache: false,
        async: false,
        success: function (ProjectLevelListJson) {
            $("#remoteprojectSecond").find("option:eq(0)").nextAll().remove();
            for (var i = 0; i < ProjectLevelListJson.projectLevelList.length; i++) {
                var projectType = ProjectLevelListJson.projectLevelList[i];
                var projectLevelListOption = "<option value='" + projectType.projectId + "'>" + projectType.projectName + "</option>";
                $("#remoteprojectSecondOption").after(projectLevelListOption);
            }
        }
    });
}
/**
 * 基本信息一级项目事件
 * 1.更改二级项目
 * 2.更改班型利润系数
 * 3.更新页面信息
 * @param projectId
 */
function changeProjectName(projectId) {
    if (projectId == '' || projectId == undefined || projectId == null) {
        $("#projectSecond").find("option").eq(0).nextAll().remove();
        return;
    }
    getClassCost(projectId);
    onAllChange();
    $.ajax({
        url: ctx + "/prj/ajaxSelectProjectLevel.action",
        type: "post",
        data: {projectId: projectId},
        cache: false,
        async: false,
        success: function (ProjectLevelListJson) {
            $("#projectSecond").find("option:eq(0)").nextAll().remove();
            for (var i = 0; i < ProjectLevelListJson.projectLevelList.length; i++) {
                var projectType = ProjectLevelListJson.projectLevelList[i];
                var projectLevelListOption = "<option value='" + projectType.projectId + "'>" + projectType.projectName + "</option>";
                $("#projectSecondOption").after(projectLevelListOption);
            }
        }
    });
}

function modProjectName(projectId) {
    if (projectId == '' || projectId == undefined || projectId == null) {
        $("#modprojectSecond").find("option").eq(0).nextAll().remove();
        return;
    }
    $.ajax({
        url: ctx + "/prj/ajaxSelectProjectLevel.action",
        type: "post",
        data: {projectId: projectId},
        cache: false,
        async: false,
        success: function (ProjectLevelListJson) {
            $("#modprojectSecond").find("option:eq(0)").nextAll().remove();
            for (var i = 0; i < ProjectLevelListJson.projectLevelList.length; i++) {
                var projectType = ProjectLevelListJson.projectLevelList[i];
                var projectLevelListOption = "<option value='" + projectType.projectId + "'>" + projectType.projectName + "</option>";
                $("#modprojectSecondOption").after(projectLevelListOption);
            }
        }
    });
}
//判断显示哪个

function onremode() {
    var val = $('input:radio[id="remodeTrue"]:checked').val();
    if (val == "ISREMOTEEDU") {
        $("#oneDiv").css('display', 'block');
        $("#moreDiv").css('display', 'none');
        $("#lableIdDiv").css('display', 'none');
        $("#classPrice").attr('readonly', true);
    } else {
        $("#oneDiv").css('display', 'none');
        $("#lableIdDiv").css('display', 'block');
        $("#moreDiv").css('display', 'block');
        $("#classPrice").attr('readonly', false);
    }
}
