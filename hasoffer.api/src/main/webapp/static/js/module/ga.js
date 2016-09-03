/**
 * Created by Administrator on 2015/10/16.
 */

(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
})(window,document,'script','//www.google-analytics.com/analytics.js','ga');

ga('create', 'UA-68938374-1', 'auto');
ga('send', 'pageview');
(function(w){
	w.A=w.A||{};
	w.A.ga={
		page:null,
		init:function(page){
			this.page=page;
		},
		_get:function(){
			return this.page||'index';
		},
		event:function(act,lab){
			ga('send','event',this._get(),act,lab);
		}
	}
})(window);
$(document).ready(function(){
	$(".J_ga_in_a").click(function(){
		var act="inner_jump";
		var lab=$(this).data('ga-lab')||"other site";
		A.ga.event(act,lab);
	});
	$(".J_ga_out_a").click(function(){
		var act="out_jump";
		var lab=$(this).data('ga-lab')||"other url";
		A.ga.event(act,lab);
	});
	$(".J_ga_btn").click(function(){
		var act="click";
		var lab=$(this).data('ga-lab')||"view";
		A.ga.event(act,lab);
	});
});