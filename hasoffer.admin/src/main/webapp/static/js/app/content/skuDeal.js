/**
 * Created by caoyaqi on 2015/11/24.
 */
var recommendReason = null;
var skuSpecifics = null;

function uploadFile(file) {
	var url = "/uploadfile";
	var formdata = new FormData();
	formdata.append("file", file);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Accept", "application/json");
	xhr.send(formdata);
	//xhr.se
	xhr.onload = function() {
		return function(e) {
			if (this.status == 200) {
				setResult(this.responseText);
			}
		}
	}();
}

function setResult(result) {
	if (result == '') {
		alert('图片上传失败');
		$("#img_banner").attr("src", "/image/upload.png");
		return;
	}

	var obj = eval('(' + result + ')');
	$("#img_banner").attr("src", obj.fileUrl);
	$("#bannerImgPath").val(obj.fileUrl);
}

$(document).ready(function(){
if(recommendReason == null){
	recommendReason = CKEDITOR.replace("recommendReason", {
		language : 'zh-cn',
		allowedContent : true,
		height : 300
	});
}

	if (skuSpecifics == null){
		skuSpecifics = CKEDITOR.replace("skuSpecifics", {
			language : 'zh-cn',
			allowedContent : true,
			height : 300
		});
	}

	//setInterval(hideBars, 0);
	/*$("#createD").click(function(){

	});*/
	if (_.isEmpty($("#bannerImgPath").val())){
		$("#img_banner").attr("src", "/image/upload.png");
	}

	$("#bannerImg").change(function() {
		for (var i=0; i<this.files.length;i++)
		{
			var file = this.files[i];
			var reader = new FileReader();
			reader.onload = function(file) {
				return function(e) {
					var img = new Image();
					img.src = e.target.result;
					//uploadFile(file);
					if (img.width == 710 && img.height == 230){
						$('#img_banner').attr("src", "/image/uploading.png");
						uploadFile(file);
					} else {
						alert('图片尺寸不合乎规范！');
					}
				};
			}(file);
			reader.readAsDataURL(file);
		}
	});

	$("form").submit(function(){
		if ($("#slide").is(':checked')){
			if (_.isEmpty($("#bannerImgPath").val())){
				alert("当前deal选择了首页轮播，请添加轮播图！");
				return false;
			}
		}
	});
});

function hideBars(){
	$('.cke_bottom.cke_reset_all').css('display', 'none');
	$('.cke_top.cke_reset_all').css('display', 'none');
}
