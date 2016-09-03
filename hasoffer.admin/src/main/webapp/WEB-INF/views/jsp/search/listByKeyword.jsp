<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<style>
    .p_list_image {
        width: 40px;
        max-height: 60px;
    }

    .website {
        width: 120px;
    }

    .price {
        width: 60px;
    }

    .title {
        width: 350px;
    }
</style>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">手动匹配[${keyword}, (Rs.)${clientPrice}]</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <div class="row" style="margin-bottom: 20px;">
        <form action="/s/reSearchByLogKeyword/${srmSearchLogId}">
            <div class="col-lg-6">
                <div class="input-group">
                    <span class="input-group-addon">关键字</span>
                    <input type="text" name="title" class="form-control" value="${keyword}">
                </div>
            </div>
            <div class="col-lg-2">
                <button type="submit" class="btn btn-primary">搜索</button>
            </div>
            <div class="col-lg-2">
                <button type="button" class="btn btn-primary" onclick="toManualCorrelation()">没有匹配链接，前去添加</button>
            </div>
        </form>
    </div>
    <div class="row">
        <div class="col-lg-12">
            <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                <thead>
                <tr>
                    <td>ID</td>
                    <td>图片</td>
                    <td>标题</td>
                    <td>比价数量</td>
                    <td>最低价</td>
                    <td>最高价</td>
                    <td>status</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${products}" var="product" varStatus="status">
                    <tr onmouseover="pCtrl.onPro('${product.id}')" onmouseout="pCtrl.outPro('${product.id}')">
                        <td>${product.id}</td>
                        <td><img src="${product.masterImageUrl}" class="p_list_image"></td>
                        <td>
                            <a href="/p/cmp/${product.id}" target="_blank">${product.title}</a>

                            <p style="color: #969696;margin-top:10px">
                                <c:forEach items="${product.categories}" var="cate" varStatus="vs">
                                    ${cate.name}
                                    <c:if test="${!vs.last}">
                                        >
                                    </c:if>
                                </c:forEach>
                            </p>
                        </td>
                        <td>${product.skuCount}</td>
                        <td>${product.minPrice}</td>
                        <td>${product.maxPrice}</td>
                        <td>
                                <%--<a href="/s/manualMatch/${product.id}/${website}/${srmSearchLogId}">匹配</a>--%>
                            <span>
                                <c:if test="${status.first}">
                                    <c:if test="${hasRelateProduct}" >
                                        已匹配
                                    </c:if>
                                </c:if>
                            </span>
                            <br/>
                            <span>
                                <a href="javascript:void(0)" onclick="getCmpSku('${product.id}')">关联</a>
                            </span>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Modal -->
    <div id="myModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         style="display: none; ">
        <div class="modal-dialog" role="document">
            <div class="modal-content">

                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="closeModeal()">
                        <span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">手动匹配</h4>
                </div>

                <div class="modal-body">
                    <table class="table table-bordered table-hover table-condensed">
                        <thead>
                        <tr>
                            <td>网站名称</td>
                            <td>商品详情页地址</td>
                        </tr>
                        </thead>
                        <tbody id="websiteUrl">
                        </tbody>
                    </table>
                </div>

                <button class="btn btn-primary" onclick="startQuery()">开始抓取</button>
                <div id="loadingImage" style="display: none;">
                    <img src="/static/image/loading.gif"/>
                </div>

                <div id="productListInfo" class="modal-body" style="display: none;">
                    <form id="addCmpPtm" action="/s/addCmpSkus" method="post">
                        <input id="srmSearchLogId" type="hidden" name="srmSearchLogId">
                        <input id="ptmProductId" type="hidden" name="productId">
                        <table class="table table-bordered table-hover table-condensed">
                            <thead>
                            <tr>
                                <td class="website">来源</td>
                                <td class="price">价格</td>
                                <td class="title">标题</td>
                            </tr>
                            </thead>
                            <tbody id="productInfoShow">
                            </tbody>
                        </table>
                    </form>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal" onclick="closeModeal()">关闭
                    </button>
                    <button type="button" class="btn btn-primary" onclick="submit()">保存</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script>

    function submit() {
        var url = "/s/addCmpSkus";
        http.doPost(url, $("#addCmpPtm").serialize(), function () {
            window.location.href = "/s/reSearchByLogKeyword/${srmSearchLogId}";
        });

    }

    function startQuery() {
        //todo 保证上传的url中，不会出现重复的
        //清空productListShow
        $("#productInfoShow").empty();
        //展开loading
        $("#loadingImage").attr("style", "display: block;");
        var parameters = new Array();
        for (var i = 0; i < 6; i++) {
            var name = "url" + i;
            parameters[i] = $("input[name=" + name + "]").val();
        }
        var url = "/s/getProduct";

        // 跳转抓取的的方法，将url传入
        http.doPost(url, {"parameters": JSON.stringify(parameters)}, function (data) {
            for (var i = 0; i < data.length; i++) {
                //解析json，获取各项数据
                var title = data[i].title;
                var price = data[i].price;
                var sourceSid = data.sourceSid;
                var url = data[i].sourceSite;
                var website = data[i].website;
                //向表格中填充数据
                $("#productInfoShow").append("<tr><td><input class='website' type='text' name='website' value='" + website + "'/></td><td><input class='price' type='text' name='price'value='" + price + "'/></td><td><input class='title' type='text' name='title'value='" + title + "'/></td><input type='hidden' name='sourceSid' value='" + sourceSid + "'/><input type='hidden' name='url' value='" + url + "'/></tr>");
            }
            //隐藏loading
            $("#loadingImage").attr("style", "display: none;");
            //显示表格
            $("#productListInfo").attr("style", "display: block");
        }, "json");
    }

    function toManualCorrelation() {
        var url = "/s/toManualCorrelation/${srmSearchLogId}";
        window.location.href = url;
    }

    function getCmpSku(productId) {

        //先清空数据
        $("#websiteUrl").empty();

        var url = "/s/getCmpSku";
        var srmSearchLogId = '${srmSearchLogId}';
        http.doPost(url, {
            productId: productId,
            srmSearchLogId: srmSearchLogId
        }, function (data) {
            //该count值用来表示input标签中url的个数
            var count = 0;
            for (var i = 0; i < data.length; i++) {
                var website = data[i].website;
                var url = data[i].url;
                var flag = data[i].flag;
                if (flag == "exists") {
                    $("#websiteUrl").append("<tr><td>" + website + "</td><td><a target='_blank' href='"+url+"'>已存在</a></td></tr>");
                } else {
                    $("#websiteUrl").append("<tr><td><a target='_blank' href='" + url + "'>" + website + "</a></td><td><input type='text'placeholder='请输入" + website + "网站该商品的详情页地址' name='url" + count + "' onfocus='clearInfo(this)' onblur='checkWebsite(this)'><span></span></td></tr>");
                    count++;
                }
            }
            //向表单中添加productId
            $("#ptmProductId").val(productId);
            $("#srmSearchLogId").val(srmSearchLogId);
            $("#myModal").modal("show");
        }, "json");
    }

    function clearInfo(obj) {
        var node = obj;
        var nextNode = node.nextSibling;
        nextNode.innerHTML = "";
    }

    function checkWebsite(obj) {

        var node = obj;
        var urlString = node.value;

        if(urlString == ""){
            return ;
        }

        var nextNode = node.nextSibling;
        var url = "/s/checkWebsite"
        http.doPost(url, {'urlString': urlString}, function (data) {
            //解析data
            var info = data.website;
            nextNode.innerHTML = info;
        }, 'json');
    }

    function closeModeal() {
        $("#websiteUrl").empty();
        $("#productInfoShow").empty();
        //隐藏表格
        $("#productListInfo").attr("style", "display: none;");
        $("#myModal").modal("hide");
    }


    var pCtrl = {
        modifyTag: function (id, tag) {
            console.log(id + "\t" + tag);
            var newtag = prompt('编辑tag(不同的tag使用空格分开)', tag);
            if (newtag != null && newtag != tag) {
                // 更新tag
                http.doPost('/p/updateTag', {id: id, tag: newtag}, function (data) {
                    console.log(data);
                });
            }
        },
        onPro: function (id) {
            $("#modifyBtn" + id).css("display", "block");
        },
        outPro: function (id) {
            $("#modifyBtn" + id).css("display", "none");
        }
    };
</script>

<jsp:include page="../include/footer.jsp"/>