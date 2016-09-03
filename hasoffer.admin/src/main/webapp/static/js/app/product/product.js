/**
 * Created by caoyaqi on 2015/11/20.
 */
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
		$("#img_sku").attr("src", "/image/upload.png");
		return;
	}

	var obj = eval('(' + result + ')');
	$("#img_sku").attr("src", obj.fileUrl);
	$("#imagePath").val(obj.fileUrl);
}

$(document).ready(function() {
	$("#skuImg").change(function() {
		for (var i=0; i<this.files.length;i++)
		{
			var file = this.files[i];
			var reader = new FileReader();
			reader.onload = function(file) {
				return function(e) {
					var img = new Image();
					img.src = e.target.result;
					$('#img_sku').attr("src", "/image/uploading.png");
					uploadFile(file, categoryId);

				};
			}(file);
			reader.readAsDataURL(file);
		}
	});

});
