var http = {
    //get action
    doGet: function (url, callBack) {
        /*$.get(url).success(function(data) {
         http.requestSuccess(data, callBack);
         }).error(function(data) {
         http.hasError(data);
         });*/
        $.ajax({
            url: url,
            success: function (data) {
                http.requestSuccess(data, callBack)
            },
            dataType: "json"
        });
    },
    //post action
    doPost: function (url, postData, callBack) {
        /*$.post(url, postData).success(function(data) {
         http.requestSuccess(data, callBack);
         }).error(function(data) {
         http.hasError(data);
         });*/
        $.ajax({
            url: url,
            method: 'post',
            data: postData,
            success: function (data) {
                http.requestSuccess(data, callBack)
            },
            dataType: "json"
        });
    },
    // upload file
    uploadFile: function (url, file, data, callBack) {
        var formdata = new FormData();
        formdata.append("file", file);
        for (var key in data) {
            formdata.append(key, data[key]);
        }
        var xhr = new XMLHttpRequest();
        xhr.open("POST", url, true);
        xhr.send(formdata);
        xhr.onload = function (e) {
            if (this.status == 200) {
                var obj = eval('(' + this.responseText + ')');
                http.requestSuccess(obj, callBack);
            } else {
                http.hasError(this);
            }
        }
    },
    /**
     * 上传图片
     * @param file
     * @param resourceId 图片resourceID
     * @param type 图片类型
     * @param callback 回调函数
     */
    uploadImage: function (file, resourceId, type, callback) {
        var url = host2 + "/common/image-upload";
        var formdata = new FormData();
        formdata.append("file", file);
        formdata.append("type", type);
        formdata.append("resourceId", resourceId);
        var xhr = new XMLHttpRequest();
        xhr.open("POST", url, true);
        xhr.send(formdata);
        xhr.onload = function () {
            return function () {
                if (this.status == 200) {
                    callback(this.responseText);
                }
            }
        }();
    },
    //return success
    requestSuccess: function (data, callBack) {
        if (data.error) {
            abMessage.show(data.message);
        }
        if (data.result) {
            if (data.result.status == -1) {
                alert(data.result.msg);
            }
        }
        callBack(data);
    },
    //request error
    hasError: function (data) {
    }
};