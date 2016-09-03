/**
 * Created by Administrator on 2015/10/27.
 */
define(function(){
	function UrlSearch(arr,url)
	{
		this.urlParam={};
		for(var index in arr){
			this.urlParam[arr[index]]=[];
		}
		var name,val;
		var str=url||location.href; //取得整个地址栏
		var num=str.indexOf("?")
		str=str.substr(num+1); //取得所有参数   stringvar.substr(start [, length ]

		var arr=str.split("&"); //各个参数放到数组里
		for(var i=0;i < arr.length;i++){
			num=arr[i].indexOf("=");
			if(num>0){
				name=arr[i].substring(0,num);
				val=arr[i].substr(num+1);
				if(this.urlParam[name]&&this.urlParam[name] instanceof Array){
					this.urlParam.name=this.urlParam.name||[];
					this.urlParam[name].push(val);
				}
				else{
					this.urlParam[name]=val;
				}

			}
		}
	}
	UrlSearch.prototype={
		//获取url参数值,当没有该参数时候返回空字符串
		get:function(name){
			if(typeof this.urlParam[name] =="undefined"){
				return "";
			}
			if(!this.urlParam[name] instanceof Array){
				return this.urlParam[name];
			}
			return this.urlParam[name];
		},
		getData:function(){
			return this.urlParam;
		}
	}
	return UrlSearch;
})