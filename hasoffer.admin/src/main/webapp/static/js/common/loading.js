/*if (window.attachEvent) {
 window.attachEvent("onload", removeLoading);
 } else if (window.addEventListener) {
 window.addEventListener("load", removeLoading, false);
 }*/

function removeLoading() {
	$("div[name='loadingDiv']").hide();
}

function showLoading() {
	$("div[name='loadingDiv']").show();
	window.setTimeout(removeLoading, 1000);
}

$("a.loading").bind('click', function() {
	var href = $(this).attr("href");
	if (href.indexOf("javascript") < 0) {
		showLoading();
	}
});

$("button.loading").bind('click', function() {
	showLoading();
});

removeLoading();