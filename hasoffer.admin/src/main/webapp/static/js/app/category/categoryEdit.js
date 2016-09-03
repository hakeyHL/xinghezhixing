/**
 * Created by Administrator on 2015/11/3.
 */
var categoryId = null;

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

function uploadMobileFile(file) {
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
				setResultMobile(this.responseText);
			}
		}
	}();
}

function setResult(result) {
	if (result == '') {
		alert('图片上传失败');
		$("#img_category").attr("src", "/image/upload.png");
		return;
	}

	var obj = eval('(' + result + ')');
	$("#img_category").attr("src", obj.fileUrl);
	$("#imagePath").val(obj.fileUrl);
}

function setResultMobile(result) {
	if (result == '') {
		alert('图片上传失败');
		$("#img_category_mobile").attr("src", "/image/upload.png");
		return;
	}

	var obj = eval('(' + result + ')');
	$("#img_category_mobile").attr("src", obj.fileUrl);
	$("#imageMobilePath").val(obj.fileUrl);
}

$(document).ready(function() {
	$("#categoryImg").change(function() {
		for (var i=0; i<this.files.length;i++)
		{
			var file = this.files[i];
			var reader = new FileReader();
			reader.onload = function(file) {
				return function(e) {
					var img = new Image();
					img.src = e.target.result;
					$('#img_category').attr("src", "/image/uploading.png");
					uploadFile(file, categoryId);

				};
			}(file);
			reader.readAsDataURL(file);
		}
	});

	$("#categoryImgMobile").change(function() {
		for (var i=0; i<this.files.length;i++)
		{
			var file = this.files[i];
			var reader = new FileReader();
			reader.onload = function(file) {
				return function(e) {
					var img = new Image();
					img.src = e.target.result;
					$('#img_category_mobile').attr("src", "/image/uploading.png");
					uploadMobileFile(file, categoryId);

				};
			}(file);
			reader.readAsDataURL(file);
		}
	});
});