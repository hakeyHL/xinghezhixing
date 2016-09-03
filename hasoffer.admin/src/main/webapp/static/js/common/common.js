var host = "";
var host2 = "";

var domain_saler_center = "http://www.allbuy.com/saler-center";

var TIME_PATTERN = "yyyy/MM/dd HH:mm";

function getRandomString() {
	return new Date().getTime();
}

var rule_image = {
	size : 2097152,
	width : 215,
	height : 215,
	detail_width : 639,
	detail_height : 639
};

function checkImage(file, src, type) {
	return true;
};

function isNull(x) {
	if (x == undefined || x == null) {
		return true;
	} else {
		return false;
	}
}

function GetRequest() {
	var url = location.search; //获取url中"?"符后的字串
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for (var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
	}
	return theRequest;
}


function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
		return unescape(r[2]);
	}
	return null;
}

function getParam(name) {
	var value = getQueryString(name);
	return value;
}

function parseHM(x) {
	if (x < 10) {
		return "0" + x;
	}
	return x;
}

//传入两个毫秒值，第一个作为开始时间，第二个作为结束时间
//返回当前时刻（客户端的时刻）在传入时间段的关系： -1|0|1
//即在开始时刻前返回-1,在该时间段内返回0,在结束时间后返回1
function timeStatus(stime, etime) {
	var timeNow = new Date();
	if (timeNow > etime) {
		return 1;//已结束
	} else if (timeNow < stime) {
		return -1;
	}
	return 0;
}

function getStatus(stime, etime) {
	var now = new Date();
	var nows = now.getTime();
	var status_txt = "";
	var day_count = 1000 * 60 * 60 * 24;
	var day, hours, mins, count;
	if (nows < stime) {
		status_txt = "距开始还有" + getZNtime(nows, stime);
	} else if (nows >= stime && nows <= etime) {
		status_txt = "距结束还有" + getZNtime(nows, etime);
	} else if (nows > etime) {
		status_txt = "已结束";
	}
	return status_txt;
}

//根据传入的毫秒值，计算相差的天数，小时数，分钟数，返回文本
function getZNtime(beginTime, endTime) {
	var day_count = 1000 * 60 * 60 * 24;
	var count, day, hours, mins;
	count = (endTime - beginTime) / day_count;

	day = parseInt(count);// 整数天数
	count = (count - day) * 24; // 小数表示的小时数
	hours = parseInt(count); // 小时数
	mins = parseInt((count - hours) * 60); // 分钟数

	var text = "";
	if (day > 0) {
		text += day + "天";
	}
	if (hours > 0) {
		text += hours + "小时";
	}
	if (mins > 0) {
		text += mins + "分钟";
	}
	return text;
}

function getStringDate(time) {
	var dateTime = new Date(time);
	var timeStr = dateTime.getFullYear() + "/" + (dateTime.getMonth() + 1) + "/" + dateTime.getDate() + " "
			+ dateTime.getHours() + ":" + dateTime.getMinutes();
	return timeStr;
}
