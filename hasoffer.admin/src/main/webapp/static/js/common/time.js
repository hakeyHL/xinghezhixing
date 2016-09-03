/**
 * Created by CWZ on 2014/12/2.
 */

function getTime4US() {
	var dd1 = new Date();
	dd1.setMinutes(dd1.getMinutes() + dd1.getTimezoneOffset() - 300);
	return '美国东部时间:' + dd1.getFullYear() + "/" + (dd1.getMonth() + 1) + "/" + dd1.getDate() + " " + dd1.getHours() + ":" +
			dd1.getMinutes() + ":" + dd1.getSeconds();
}

function getTime4CN() {
	var dd2 = new Date();
	dd2.setMinutes(dd2.getMinutes() + dd2.getTimezoneOffset() + 13 * 60 - 300);
	return '中国北京时间:' + dd2.getFullYear() + "/" + (dd2.getMonth() + 1) + "/" + dd2.getDate() + " " + dd2.getHours() + ":" +
			dd2.getMinutes() + ":" + dd2.getSeconds();
}

/**
 * 返回ymd格式的日期
 * 以当天为日期，offset为当天往前的几天
 * @param offset
 */
function getYMD(offset) {
	var date = new Date();
	var targetDate = new Date(date.getTime() - offset * 86400000);
	var y = targetDate.getFullYear();
	var m = targetDate.getMonth() + 1;
	var d = targetDate.getDate();
	var mm = m, dd = d;
	if (m < 10) {
		mm = "0" + m;
	}
	if (d < 10) {
		dd = "0" + d;
	}
	return y + "" + mm + "" + dd;
}

function getDate(offset) {
	var date = new Date();
	return new Date(date.getTime() - offset * 86400000);
}