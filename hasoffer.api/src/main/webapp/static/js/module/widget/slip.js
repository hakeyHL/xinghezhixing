/**
 * Created by Administrator on 2015/11/1.
 */
define(function(){
	//yao--w:总宽度;num:显示标签的数量;ele:需要操作的控件;prev:prev控件;next:next控件;total:元素总数
	function slip(data){
		//yao--总宽度
		this.width=data.w||0;
		//yao--模块数量
		this.num=data.num||1;
		//yao--每次滑动的宽度
		this.slide_width=this.width/this.num;
		//yao--初始向右次数
		this.clicks=0;
		//yao--最大点击次数
		this.max_clicks=data.total-this.num;
		//yao--如果控件总数小于需要显示的数量,则不进行操作
		if(this.max_clicks<0){
			data.prev.hide();
			data.next.hide();
			return;
		}
		//yao--需要滑动的控件
		this.ele=$(data.ele);
		var _this=this;
		if(data.prev){
			$(data.prev).click(function(){_this.left();});
		}
		if(data.next){
			$(data.next).click(function(){_this.right();});
		}
	};
	slip.prototype={
		left:function(){
			this.clicks==0?this.clicks=this.max_clicks:--this.clicks;
			var r=this.slide_width*this.clicks;
			this.ele.animate({right:r+"px"},100);
		},
		right:function(){
			this.clicks==this.max_clicks?this.clicks=0:++this.clicks;
			var r=this.slide_width*this.clicks;
			this.ele.animate({right:r+"px"},100);
		}
	};
	return slip;

});