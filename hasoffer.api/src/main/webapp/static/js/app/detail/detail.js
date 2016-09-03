$(document).ready(function(){
$(".compare-body-nav").find("li").click(function(){
	$(this).addClass("compare-active").siblings().removeClass("compare-active");
    $(".J_body").children("*").hide();
	var item=$(this).data("item");
	$(".J_"+item).show();
});
	$("h1").setEllipse(2);
});