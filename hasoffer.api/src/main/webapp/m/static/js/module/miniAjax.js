/**
 * Created by Administrator on 2015/11/4.
 */
/*|--minAjax.js--|
 |--(A Minimalistic Pure JavaScript Header for Ajax POST/GET Request )--|
 |--Author : flouthoc (gunnerar7@gmail.com)(http://github.com/flouthoc)--|
 |--Contributers : Add Your Name Below--|
 */
var miniAjaxBaseUrl = "http://192.168.1.128:8080/";

//yao--全局初始化
(function(w) {
	w.A = w.A || {};
	w.A.baseUrl = "http://192.168.1.128:8080/m/static/tpl/";
	w.A.getDefaultStyle = function(obj, attribute) { // 返回最终样式函数，兼容IE和DOM，设置参数：元素对象、样式特性
		return obj.currentStyle ? obj.currentStyle[attribute] : document.defaultView.getComputedStyle(obj, false)[attribute];
	};
	w.A.setEllipse = function(eles, line) {
		var wid = eles[0].offsetWidth;
		for (var i = 0; i < eles.length; ++i) {
			var html = eles[i].innerHTML;
			var ele = document.createElement("div");
			ele.style.width = wid + "px";
			ele.style.lineHeight = "1px";
			ele.style.fontFamily = this.getDefaultStyle(eles[0], "font-family");
			ele.style.fontSize = this.getDefaultStyle(eles[0], "font-size");
			ele.innerHTML = html;
			document.body.appendChild(ele);
			var count = 0;
			var len = html.length;
			if (ele.offsetHeight > line) {
				//yao--以20字符为标准进行删除,直到行数小于等于line
				var cutNum = 20;
				while (ele.offsetHeight > line) {
					ele.innerHTML = html.substr(0, len - cutNum * ++count) + "...";
				}
				count = count * cutNum;
				//yao--进行增加字符,直到行数大于line
				while (ele.offsetHeight <= line) {
					ele.innerHTML = html.substr(0, len - --count) + "...";
				}
				//yao--剪切html,因为第二个while循环导致最后加的字符导致行数大于line,所以排除掉
				html = html.substr(0, len - ++count - 5) + "...";
			}
			eles[i].innerHTML = html;
			ele.parentNode.removeChild(ele);
		}
	};
	w.A.getTerminalText=function(text,css){

	};
	w.A.getOffSet=function (obj) {
		var pos = {top:0, left:0};
		if (obj.offsetParent){
			while (obj.offsetParent){
				pos.top += obj.offsetTop;
				pos.left += obj.offsetLeft;
				obj = obj.offsetParent;
			}
		}else if(obj.x){
			pos.left += obj.x;
			pos.top += obj.y;
		}
		return pos;
	}
})(window);

function getElementsByClass(searchClass, node, tag) {var classElements = new Array();if (node == null) {node = document;}if (tag == null) {tag = '*';}var els = node.getElementsByTagName(tag);var elsLen = els.length;var pattern = new RegExp('(^|\\s)' + searchClass + '(\\s|$)');for (i = 0, j = 0; i < elsLen; i++) {if (pattern.test(els[i].className)) {classElements[j] = els[i];j++;}}return classElements;};
var query = 'iphone';var main = getElementsByClass('pslmain');var data = '';for (var mi = 0; mi < main.length; mi++) {var ele = getElementsByClass('pstl', main[mi])[0];var title = ele.innerHTML.replace(/(\W)+/g, ' ');var brand = '';var arr = title.split(' ');for (var ai = 0; ai < arr.length; ++ai) {if (query.toLocaleUpperCase() == arr[ai].toLocaleUpperCase()) {brand = arr[ai + 1] + ' ' + arr[ai + 2] + ' ' + arr[ai + 3];}}data += (getElementsByClass('price', main[mi])[0].innerHTML.split('&nbsp;')[1] + ',' + ele.innerHTML + ',' + ele.href + ',' + brand + ',' + query + '\n');}
String.prototype.trim = function() { return this.replace(/(^\s*)|(\s*$)/g, ""); };
function initXMLhttp() {
	var xmlhttp;
	if (window.XMLHttpRequest) {
		//code for IE7,firefox chrome and above
		xmlhttp = new XMLHttpRequest();
	} else {
		//code for Internet Explorer
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}

	return xmlhttp;
}

function minAjax(config) {

	/*Config Structure
	 url:"reqesting URL"
	 type:"GET or POST"
	 method: "(OPTIONAL) True for async and False for Non-async | By default its Async"
	 debugLog: "(OPTIONAL)To display Debug Logs | By default it is false"
	 data: "(OPTIONAL) another Nested Object which should contains reqested Properties in form of Object Properties"
	 success: "(OPTIONAL) Callback function to process after response | function(data,status)"
	 */

	if (!config.url) {

		if (config.debugLog == true) {
			console.log("No Url!");
		}
		return;

	}

	if (!config.type) {

		if (config.debugLog == true) {
			console.log("No Default type (GET/POST) given!");
		}
		return;

	}

	if (!config.method) {
		config.method = true;
	}


	if (!config.debugLog) {
		config.debugLog = false;
	}

	var xmlhttp = initXMLhttp();

	xmlhttp.onreadystatechange = function() {

		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

			if (config.success) {
				var jsonArr = eval("(" + xmlhttp.responseText + ")");
				config.success(jsonArr, xmlhttp.readyState);
			}

			if (config.debugLog == true) {
				console.log("SuccessResponse");
			}
			if (config.debugLog == true) {
				console.log("Response Data:" + xmlhttp.responseText);
			}

		} else {

			if (config.debugLog == true) {
				console.log("FailureResponse --> State:" + xmlhttp.readyState + "Status:" + xmlhttp.status);
			}
		}
	}

	var sendString = [],
			sendData = config.data;
	if (typeof sendData === "string") {
		var tmpArr = String.prototype.split.call(sendData, '&');
		for (var i = 0, j = tmpArr.length; i < j; i++) {
			var datum = tmpArr[i].split('=');
			sendString.push(encodeURIComponent(datum[0]) + "=" + encodeURIComponent(datum[1]));
		}
	} else if (typeof sendData === 'object' && !( sendData instanceof String || (FormData && sendData instanceof FormData) )) {
		for (var k in sendData) {
			var datum = sendData[k];
			if (Object.prototype.toString.call(datum) == "[object Array]") {
				for (var i = 0, j = datum.length; i < j; i++) {
					sendString.push(encodeURIComponent(k) + "[]=" + encodeURIComponent(datum[i]));
				}
			} else {
				sendString.push(encodeURIComponent(k) + "=" + encodeURIComponent(datum));
			}
		}
	}
	sendString = sendString.join('&');
	config.url = miniAjaxBaseUrl + config.url;

	if (config.type == "GET") {
		xmlhttp.open("GET", config.url + "?" + sendString, config.method);
		if (typeof config.head != "undefined") {
			xmlhttp.setRequestHeader(config.head.key, config.head.val);
		}
		xmlhttp.send();

		if (config.debugLog == true) {
			console.log("GET fired at:" + config.url + "?" + sendString);
		}
	}
	if (config.type == "POST") {
		xmlhttp.open("POST", config.url, config.method);
		if (typeof config.head != "undefined") {
			xmlhttp.setRequestHeader(config.head.key, config.head.val);
		}
		xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		xmlhttp.send(sendString);

		if (config.debugLog == true) {
			console.log("POST fired at:" + config.url + " || Data:" + sendString);
		}
	}


}