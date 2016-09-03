/**
 * Created by Administrator on 2015/10/23.
 */
require(['slider','slip'],function(slider,slip){
	new slider($('.carousel'), {
		length : 2
	});
	new slip({
		w:$(".J_slip_wrap")[0].offsetWidth,
		ele:$(".J_slip_list"),
		num:7,
		prev:$(".J_prev"),
		next:$(".J_next"),
		total:$(".J_slip_list").children("li").length
	});
});
