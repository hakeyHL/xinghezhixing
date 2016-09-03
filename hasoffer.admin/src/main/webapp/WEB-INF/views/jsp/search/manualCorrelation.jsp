<%--
  Date: 2016/2/14
  用来手动修正搜索历史中，搜索出现偏差的条目
--%>
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

    textarea {
        resize: none;
    }
</style>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">手动关联</h1>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12">
            <h2>${keywords}</h2>
        </div>
    </div>


    <div class="row">
        <div class="col-lg-12">
            <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                <tr>
                    <td class="website">网站名称</td>
                    <td>商品详情页地址</td>
                </tr>
                <tr>
                    <td><a target='_blank' href="${flipkartSearchUrl}">FLIPKART</a></td>
                    <td>
                        <input type="url" id="url0" style="width: 730px;" placeholder="请输入商品在FLIPKART详情页的地址"
                               onfocus="clearInfo(this)" onblur="checkWebsite(this)"/><span></span><br/>
                    </td>
                    </td>
                </tr>
                <tr>
                    <td><a target='_blank' href="${snapdealSearchUrl}">SNAPDEAL</a></td>
                    <td>
                        <input type="url" id="url1" style="width: 730px;" placeholder="请输入商品在SNAPDEAL详情页的地址"
                               onfocus="clearInfo(this)" onblur="checkWebsite(this)"/><span></span><br/>
                    </td>
                </tr>
                <tr>
                    <td><a target='_blank' href="${amazonSearchUrl}">AMAZON</a></td>
                    <td>
                        <input type="url" id="url2" style="width: 730px;" placeholder="请输入商品在AMAZON详情页的地址"
                               onfocus="clearInfo(this)" onblur="checkWebsite(this)"/><span></span><br/>
                    </td>
                </tr>
                <tr>
                    <td><a target='_blank' href="${ebaySearchUrl}">EBAY</a></td>
                    <td>
                        <input type="url" id="url3" style="width: 730px;" placeholder="请输入商品在EBAY详情页的地址"
                               onfocus="clearInfo(this)" onblur="checkWebsite(this)"/><span></span><br/>
                    </td>
                </tr>
                <tr>
                    <td><a target='_blank' href="${shopcluesSearchUrl}">SHOPCLUES</a></td>
                    <td>
                        <input type="url" id="url4" style="width: 730px;" placeholder="请输入商品在SHOPCLUES详情页的地址"
                               onfocus="clearInfo(this)" onblur="checkWebsite(this)"/><span></span><br/>
                    </td>
                </tr>
                <tr>
                    <td><a target='_blank' href="${paytmearchUrl}">PAYTM</a></td>
                    <td>
                        <input type="url" id="url5" style="width: 730px;" placeholder="请输入商品在PAYTM详情页的地址"
                               onfocus="clearInfo(this)" onblur="checkWebsite(this)"/><span></span><br/>
                    </td>
                </tr>
            </table>
            <button class="btn btn-primary" onclick="startQuery('${srmSearchLogId}')">开始抓取</button>
            <div id="loadingImage" style="display: none;">
                <img src="/static/image/loading.gif"/>
            </div>
            <br/>
        </div>
    </div>
    <br/>

    <hr/>
    <div class="row">
        <div class="col-lg-12">
            <form id="addPtm">
                <input name="url" type="hidden">
                <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
                    <tr>
                        <td class="website">来源</td>
                        <td>
                            <input name="website" type="text"/>
                        </td>
                    </tr>
                    <tr>
                        <td>标题</td>
                        <td>
                            <input name="title" type="text" style="width: 730px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>价格</td>
                        <td>
                            <input name="price" type="text"/>
                        </td>
                    </tr>
                    <tr>
                        <td>图片</td>
                        <td>
                            <img name="middleImgPath"/>
                        </td>
                    </tr>
                    <tr>
                        <td>类别</td>
                        <td>
                            <jsp:include page="../include/module/category.jsp"/>
                        </td>
                    </tr>
                    <tr>
                        <td>描述</td>
                        <td>
                            <textarea name="description" rows="10px" cols="30px" style="width: 730px"></textarea>
                        </td>
                    </tr>
                    <%--特性：--%>
                    <%--sku属性：<textarea name=""/>--%>
                    <%--隐藏域中添加product中的skus和saleAttributeNames--%>
                    <input name="skus" type="hidden"/>
                    <input name="sANs" type="hidden"/>
                    <input name="sourceSid" type="hidden"/>
                </table>
            </form>
        </div>
    </div>
    <hr/>
    <div id="productListInfo" style="display: none;">
        <form id="addCmpPtm" action="/s/addCmpSkus" method="post">
            <input id="ptmProductId" type="hidden" name="productId">
            <input id="srmSearchLogId" type="hidden" name="srmSearchLogId" value="${srmSearchLogId}">
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
    <hr/>
    <button class="btn btn-primary" onclick="submit()" style="width: 80px">保存</button>
</div>

<script>

    $(document).ready(function () {
        categoryManager.load('${page.pageParams.category1}', '${page.pageParams.category2}', '${page.pageParams.category3}');
    });

    function clearInfo(obj) {
        var node = obj;
        var nextNode = node.nextSibling;
        nextNode.innerHTML = "";
    }

    function checkWebsite(obj) {

        var node = obj;
        var urlString = node.value;

        if (urlString == "") {
            return;
        }

        var nextNode = node.nextSibling;

        var url = "/s/checkWebsite"
        http.doPost(url, {'urlString': urlString}, function (data) {
            //解析data
            var info = data.website;
            nextNode.innerHTML = info;
        }, 'json');
    }

    function submit() {
        var url1 = "/p/create";
        var dataSubmit = $("#addPtm").serialize();
        http.doPost(url1, {"data": dataSubmit}, function (data) {
            var ptmProductId = data.id;
            $("#ptmProductId").attr("value", ptmProductId);
            $("#addCmpPtm").submit();
        });

    }

    function startQuery(srmSearchLogId) {
        //表格中数据清空
        $("input[name='website']").attr("value","");
        $("input[name='title']").attr("value","");
        $("input[name='price']").attr("value","");
        $("img[name='middleImgPath']").attr("src","");
        $("textarea[name='description']").innerHTML = "";
        //tbody中的数据清除
        $("#productInfoShow").empty();
        $("#productListInfo").attr("style", "display: none");

        var parameters = new Array(6);
        var count = 0;

        for (var i = 0; i < parameters.length; i++) {
            var id = "url" + i;
            var value = $("#" + id).val();
            if (typeof (value) == "undefined" || value.length == 0) {
                continue;
            }
            parameters[count] = value;
            count++;
        }

        //获取第一个url解析详情
        var url0 = parameters[0];
        parameters.splice(0, 1);

        if (count == 0) {
            alert("请至少输入一个网站的url");
            return;
        }

        //显示loding
        $("#loadingImage").attr("style", "display: block;");

        //获取单个商品的详细信息
        var urlSingle = "/s/getProductInfo"
        var keywords = "${keywords}"

        http.doPost(urlSingle, {"parameter": url0,"keywords":keywords}, function (data) {

            $("input[name='url']").attr("value", url0);
            if (data.flag == 'true') {
                var website = data.website;
                var title = data.title;
                var price = data.price;
                var imageUrl = data.masterImageUrl;
                var description = data.description;

                //将属性添加到input标签中
                $("input[name='website']").attr("value", website);
                $("input[name='title']").attr("value", title);
                $("input[name='price']").attr("value", price);
                $("img[name='middleImgPath']").attr("src", imageUrl);
                $("img[name='middleImgPath']").attr("width", "200px");
                $("img[name='middleImgPath']").attr("height", "200px");
                $("textarea[name='description']").innerHTML = description;
            }else{
                $("input[name='title']").attr("value", keywords);
                alert("商品详情解析失败，请点击保存，保存url");
            }

            //获取多个商品的简单信息
            var urlList = "/s/getProduct";
            // 跳转抓取列表的方法，将url传入
            for (var i = parameters.length - 1; i >= 0; i--) {
                var value = parameters[i];
                if (typeof (value) == "undefined" || value.length == 0) {
                    parameters.splice(i, 1);
                    continue;
                }
            }
            if (parameters.length > 0) {
                http.doPost(urlList, {"parameters": JSON.stringify(parameters)}, function (data) {
                    for (var i = 0; i < data.length; i++) {
                        //解析json，获取各项数据
                        var title = data[i].title;
                        var price = data[i].price;
                        var sourceSid = data[i].sourceSid;
                        var url = data[i].sourceSite;
                        var website = data[i].website;
                        //向表格中填充数据
                        $("#productInfoShow").append("<tr><td class='website'><input type='text' name='website' value='" + website + "'/></td><td class='price'><input type='text' name='price' value='" + price + "'/></td><td><input class='title' type='text' name='title' value='" + title + "'/></td><input type='hidden' name='sourceSid' value='" + sourceSid + "'/><input type='hidden' name='url' value='" + url + "'/></tr>");
                    }
                    //显示表格
                    $("#productListInfo").attr("style", "display: block");

                    //隐藏loding
                    $("#loadingImage").attr("style", "display: none;");
                }, "json");
            }
        });

    }
</script>

<jsp:include page="../include/footer.jsp"/>