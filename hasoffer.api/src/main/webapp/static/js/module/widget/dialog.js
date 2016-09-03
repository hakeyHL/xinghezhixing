/**
 * Created by Administrator on 2015/10/26.
 */
define(function(){
	var loadingStr=PATH+"/static/image/module/loading.gif",
	    loadingTpl='<div class="dia-load-panel"><img src="'+loadingStr+'"></div>';
	function Loading(ele){
		this.loading=$(loadingTpl);
		ele.append(this.loading);
	}
	Loading.prototype={
		show:function(){
			this.loading.show();
		},
		hide:function(){
			this.loading.hide();
		},
		remove:function(){
			this.loading.remove();
		}
	}

	return {
		Loading:Loading
	};
})