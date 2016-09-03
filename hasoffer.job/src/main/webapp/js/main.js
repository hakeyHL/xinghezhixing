$(function () {
    var width = $(window).width();

    $('iframe.main_frame').width(width - 201);

    $('#nav_list').find('li').click(function () {
        $(this).addClass("hover").siblings("li").removeClass("hover");
        $('#nav_list').find("dl").slideUp(300);
        $(this).find("dl").slideDown(300);
    })
});