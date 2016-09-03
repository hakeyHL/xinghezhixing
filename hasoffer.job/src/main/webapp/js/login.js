$(function () {

    $('input[name="j_username"]').poshytip({
        className: 'tip-darkgray',
        showOn: 'none',
        alignTo: 'target',
        alignX: 'inner-right',
        alignY: 'top',
        offsetX: 25,
        offsetY: 0
    });
    $('input[name="j_password"]').poshytip({
        className: 'tip-darkgray',
        showOn: 'none',
        alignTo: 'target',
        alignX: 'inner-right',
        alignY: 'top',
        offsetX: 25,
        offsetY: 0
    });
    // 提交
    $('input[name="j_username"],input[name="j_password"]').focus(function () {
        $(this).poshytip('hide');
    });
    $('#form1').submit(function () {

        if ($('input[name="j_username"]').val().length < 1) {
            $('input[name="j_username"]').poshytip('show');
            return false;
        }
        if ($('input[name="j_password"]').val().length < 1) {
            $('input[name="j_password"]').poshytip('show');
            return false;
        }
    });


    $('#submitBtn').click(function () {

        if ($('input[name="j_username"]').val().length < 1) {
            $('input[name="j_username"]').poshytip('show');
            return false;
        } else {
            $('input[name="j_username"]').poshytip('hide');
        }

        if ($('input[name="j_password"]').val().length < 1) {
            $('input[name="j_password"]').poshytip('show');
            return false;
        } else {
            $('input[name="j_password"]').poshytip('hide');
        }

        var userName = $("input[name='j_username']").val();
        var pwd = $("input[name='j_password']").val();
        //验证用户
        validUser(userName, pwd);
    });


    //设置回车键触发事件
    $(document).keydown(function (event) {
        if (event.keyCode == 13) {
            $('#submitBtn').click();
        }
    });


    //关闭弹出层
    $('.closeTrigger').click(function () {
        $('#selectModalDialog').removeClass("in").hide();
        $('#selectBackdrop').removeClass("in").hide();

        //关闭验证提示
        $('#office').poshytip('hide');
        $('#productLine').poshytip('hide');

    });


    //弹出层确定按钮事件
    $('#seleteBtn').click(function () {

        //验证提示
        $('#office').focus(function () {
            $(this).poshytip('hide');
        });
        if ($('#office').val() == null || $('#office').val().length < 1) {
            $('#office').poshytip('show');
            return false;
        }

        //if($('#productLine').val()==null || $('#productLine').val().length <1 ){
        //	$('#productLine').poshytip('show'); 
        //	return false;
        //}

        //弹出层
        $('#seletedOffice').val($('#office').val());
        $('#seletedProduct').val($('#productLine').val());
        //提交form
        $('#form1')[0].submit();
    });


});


//设置Cookie
function setCookie(c_name, value, expiredays) {
    document.cookie = c_name + "=" + escape(value);
}
// 读取Cookie
function getCookie(c_name) {
    if (document.cookie.length > 0) {
        c_start = document.cookie.indexOf(c_name + "=");
        if (c_start != -1) {
            c_start = c_start + c_name.length + 1;
            c_end = document.cookie.indexOf(";", c_start);
            if (c_end == -1) c_end = document.cookie.length;
            return unescape(document.cookie.substring(c_start, c_end));
        }
    }
    return "";
}


/**
 * 验证用户
 */
function validUser(userId, pwd) {
    $.ajax({
        url: 'authenticate/authenticateUser.action',
        data: {
            userId: userId,
            password: pwd
        },
        type: 'POST',
        dataType: "json",
        success: function (data) {
            if (data.validUser == true) {
                $("#loginMsg").empty();
                $('#form1')[0].submit();
            } else if (data.validUser == false) {
                var loginMsgHtml = "<div id='loginMsg' style='margin-top: -10px;margin-bottom: 10px;' >";
                loginMsgHtml = loginMsgHtml + "<span style='color: red;'>登录失败.</span></div>";
                $("#loginMsg").empty();
                $("#loginMsg").append(loginMsgHtml);
            }
        }
    });

}


