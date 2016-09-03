/**
 * Created by Administrator on 2015/11/15.
 */
$(document).ready(function(){
	$(".J_more").click(function(){
		var ele=$(".J_brand_more");
		if(ele.is(":hidden")){
			ele.css("display","inline-block")
			return;
		}
		ele.hide();
	});
	$(document).bind('click',function(e){
		var ele=$(e.target);
		if(ele.hasClass("J_brand_more")||ele.hasClass("J_more")||ele.closest(".J_brand_more").length!=0){
			return;
		}
		$(".J_brand_more").hide();
	})
})