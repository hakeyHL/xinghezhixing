/**
 * Created by Administrator on 2015/11/11.
 */
//yao--封装dom操作
$.fn.extend({
	getCatConByChild: function () {
		return this.closest(".J_brand_con");
	},
	getFistByParent: function () {
		return this.find(".J_brand_fisrt");
	},
	getSecondByChild: function () {
		return this.getCatConByChild().find(".J_brand_second");
	},
	getOtherCon:function(){
		return this.getCatConByChild().siblings(".J_brand_con");
	},
	getOtherSecondByParent: function () {
		return this.children(".J_brand_second");
	},
	getOtherFirstByParent: function () {
		return this.children(".J_brand_fisrt");
	},
	setCatActiveByFirst:function(){
		var otherCon=this.getOtherCon();
		otherCon.getOtherFirstByParent().removeClass("brand-cat-active");
		otherCon.getOtherSecondByParent().hide();
		this.addClass("brand-cat-active").getSecondByChild().slideDown();
		return this;
	},
	removeCatActiveByFirst:function(){
		this.removeClass("brand-cat-active").getSecondByChild().slideUp();
		return this;
	}
});
$.extend({
	getFirstBySecondActive:function(){
		return $(".brand-second-active").getCatConByChild().getFistByParent();
	}
});
$(document).ready(function(){
	$.getFirstBySecondActive().setCatActiveByFirst();
	$(".J_brand_fisrt").click(function(){
		var _this=$(this);
		if(_this.getSecondByChild().children("*").length==0){
			return;
		}
		_this.hasClass("brand-cat-active")?_this.removeCatActiveByFirst():_this.setCatActiveByFirst();
	})
});