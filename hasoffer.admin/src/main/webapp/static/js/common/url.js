/**
 * 存放所有的 用于 ajax请求的 URL
 * @type {{}}
 */
var abUrl = {
	host : "",
	getUUID : "/common/uuid", //获取uuid，params=（count | 获取uuid的数量，如果不传或非数字，返回1个uuid的数组）
	downloadFile : "/common/download-file" //文件下载，返回json，含文件链接
}

abUrl.srm = {};
