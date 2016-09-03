/**
 * Created by Administrator on 2015/11/24.
 */
(function($){
	$.fn.extend({
		setEllipse:function(line){
			var wid=this[0].offsetWidth;
			for(var i=0;i<this.length;++i){
				var html=this[i].innerHTML;
				var ele=document.createElement("div");
				ele.style.width=wid+"px";
				ele.style.lineHeight="1px";
				ele.style.fontFamily=$(this[i]).css("font-family");
				ele.style.fontSize=$(this[i]).css("font-size");
				ele.innerHTML=html;
				document.body.appendChild(ele);
				var count=0;
				if(ele.offsetHeight>line){
					//yao--以20字符为标准进行删除,直到行数小于等于line
					var cutNum=20;
					while(ele.offsetHeight>line){
						ele.innerHTML=html.substr(0,html.length-cutNum* ++count)+"...";
					}
					count=count*cutNum;
					//yao--进行增加字符,直到行数大于line
					while(ele.offsetHeight<=line){
						ele.innerHTML=html.substr(0,html.length- --count)+"...";
					}
					this[i].title=html;
					//yao--剪切html,因为第二个while循环导致最后加的字符导致行数大于line,所以排除掉
					html=html.substr(0,html.length- ++count)+"...";
				}
				this[i].innerHTML=html;
				ele.parentNode.removeChild(ele);
			}
		}
	})
})(jQuery);
