$(function () {
    $(window).resize(function () {
        $('#frameContent').height($('#mainFrame').height());
    });
    // IE下taarget的bug
    $('a[target="frameContent"]').live('click', function () {
        var url = $(this).attr('href');
        $('#frameContent').attr('src', url);
        return false;
    });
    // 空连接
    $('a[href="javascript:;"]').live('click', function () {
        $(this).next('ul').slideToggle(300);
    });
});