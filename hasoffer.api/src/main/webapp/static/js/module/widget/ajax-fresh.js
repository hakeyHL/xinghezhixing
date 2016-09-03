/**
 * Created by Administrator on 2015/10/26.
 */
define(['jquery.history','dialog'], function(History,   dialog) {
var History=require('jquery.history');

	//yao--ajax无跳转刷新(监控url)
	function AjaxFresh(eleStr,url,data) {
		this.data=data||{};
		this.url=url;
		this.loading;
		//ajax请求对象,每当新一次的请求时,把老的注销掉
		this.hrx=null;
		var _this=this;
		History.Adapter.bind(window, 'statechange', function() { // Note: We are using statechange instead of popstate
			if(_this.loading){
				_this.loading.remove();
				_this.loading=null;
			}
			_this.loading=new dialog.Loading($(eleStr));
			_this.loading.show();
			var State = History.getState(); // Note: We are using History.getState() instead of event.state
			if(_this.hrx){
				_this.hrx.abort();
				_this.hrx==null;
			}
			_this.hrx=$.ajax({
				url : State.url,
				success : function(data) {
					_this.loading.remove();
					$(eleStr).hide().html($(data).find(eleStr).html()).fadeIn();
				}
			});
		});
	};
	AjaxFresh.prototype={
		//yao--将data转换为url参数并放置到浏览器 地址输入框,以激活statechange事件
		//yao--如果isClearPn设置为false,表示有分页点击;反之由其他地方点击,初始化分页数据
		updateUrl:function(isClearPn){
			if(isClearPn&&typeof this.data.pn!="undefined"){
				delete this.data.pn;
				delete this.data.ps;
			}
			var mUrl=this.url;
			$.each(this.data,function(k,v){
				if(v instanceof Array){
					$.each(v,function(vk,vv){
						mUrl+=k+"="+vv+"&";
					});
				}
				else{
					mUrl+=k+"="+v+"&";
				}
			});
			mUrl=mUrl.charAt(mUrl.length-1)=="&"?mUrl.substr(0,mUrl.length-1):mUrl;
			History.replaceState(null, null, mUrl);
		},
		//yao--数组专用,删除数组变量
		del:function(name){
			var _this=this;
			if(name instanceof Array){
				$.each(name,function(k,v){
					delete(_this._del(v));;
				});
			}
			else{
				delete(_this._del(name));;
			}
			this.updateUrl(true);
		},
		_del:function(name){
			if(this.data[name] instanceof Array){
				this.data[name]=[];
			}
			else{
				delete this.data[name];
			}
		},
		//yao--判断data中的name变量是否多选(数组为多选)
		isDult:function(name){
			name=name instanceof Array?name[0]:name;
			return this.data[name] instanceof Array;
		},
		//yao--更新数据函数
		updateOrAdd:function(name,val){
			if(name instanceof Array){
				val=val||[];
				var _this=this;
				$.each(name,function(k,v){
					_this._update(v,val[k]);
				});
			}
			else{
				this._update(name,val);
			}
			this.updateUrl(true);
		},
		//yao--内部更新数据函数,如果更新对象是字符型则直接更新,如果是数组,则push
		_update:function(name,val){
			if(this.data[name] instanceof Array){
				this.data[name].push(val);
			}else{
				this.data[name]=val;
			}
		},
		//yao--如果是删除的数据是数组,则pop掉;如果是字符,则delete变量
		removeValue:function(name,val){
			if(name instanceof Array){
				var _this=this;
				$.each(name,function(k,v){
					_this._remove(v,val[k]);
				});
			}
			else{
				this._remove(name,val);
			}
			this.updateUrl(true);
		},
		_remove:function(name,val){
			var _this=this;
			if(this.data[name] instanceof Array){
				$.each(this.data[name],function(k,v){
					if(v==val){
						_this.data[name].splice(k,1);
					}
				});
			}
			else{
				delete(this.data[name]);
			}
		},
		//yao--分页更新
		hrefUpdate:function(pn,ps){
			this.data.pn=pn;
			this.data.ps=ps||"";
			this.updateUrl(false);
		},
		//yao--通过url更新
		urlUpdate:function(url){
			History.pushState(null, null, url
			);
		}
	}

	return {
		AjaxFresh : AjaxFresh
	}
});
