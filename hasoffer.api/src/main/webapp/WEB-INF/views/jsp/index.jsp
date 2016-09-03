<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
	<title>Compare Allbuy</title>
	<%@ include file="include/common-head.jsp" %>
</head>
<body>
<%@ include file="include/header.jsp" %>

<link rel="stylesheet" href="/static/css/app/index/index.css"/>
<link rel="stylesheet" href="/static/css/module/widget/carousel.css"/>

<div class="wrap">
	<%--yao 轮播图 start--%>
	<%--<div class="carousel mt20">--%>
	<%--<ul class="carousel-inner">--%>
	<%--<c:forEach items="${banners}" var="item" varStatus="idxStatus">--%>
	<%--<li class="item" <c:if test="${idxStatus.index==0}">style="display:block"</c:if>>--%>
	<%--<a href="${item.url}" class="J_Promotion_Detail"--%>
	<%--style="background-image: url(${item.image})">--%>
	<%--</a>--%>
	<%--</li>--%>
	<%--</c:forEach>--%>
	<%--</ul>--%>
	<%--</div>--%>
	<%--yao 轮播图 end--%>
	<%--<div class="hm-category">--%>
	<%--<div class="row clearfix">--%>
	<%--<c:forEach items="${categories}" var="cat_item">--%>
	<%--<div class="col-4">--%>
	<%--<a href="/category/${cat_item.id}" class="hm-cate-a">--%>
	<%--<img class="hm-cat-img" src="${cat_item.imageUrl}" alt="${cat_item.name}"/>--%>

	<%--<div class="hm-cate-cover">--%>
	<%--<img class="hm-blur" src="${cat_item.imageUrl}" alt=""/>--%>

	<%--<p class="hm-mask">${cat_item.name}</p>--%>
	<%--</div>--%>
	<%--</a>--%>
	<%--</div>--%>
	<%--</c:forEach>--%>
	<%--</div>--%>
	<%--</div>--%>
	<div class="hm-main-bar clearfix">
		<div class="hm-bar">
			<div class="hm-cover-category clearfix">
				<ul class="cat-first-list J_home_first_cat">
					<c:forEach items="${categories}" var="item">
						<li>
								<%--yao--check first category has second category if or not--%>
							<c:set var="hasSecond" value="${! empty item.subCategories}"></c:set>
							<c:set var="h" value="javascript:void(0);"></c:set>
							<a href="${h}" class="cat-first-a">
								<span class="cat-sprite cat-first-${item.id}"></span>
								<span class="cat-first-name">${item.name}</span>
							</a>
							<c:if test="${hasSecond}">
								<span class="f0">&nbsp;</span><div class="cat-second-list J_second_cat">
									<c:forEach items="${item.subCategories}" var="subItem">
										<a class="cat-second-item" href="/search?cat=${subItem.id}">
										<span class="cat-second-item-img">
											<img src="${subItem.imageUrl}" alt=""/>
										</span>
											<span class="cat-second-name">${subItem.name}</span>
										</a>
									</c:forEach>
								</div>
							</c:if>
						</li>
					</c:forEach>
				</ul>
			</div>
			<div class="hm-brand-con">
				<div class="hm-brand-title">
					<h2>TOP BRAND PRICE LIST</h2>
				</div>
				<ul class="hm-brand-list J_slip_list">
					<c:forEach items="${topBrands}" var="item" varStatus="stat">
						<c:if test="${stat.count<8}">
							<li>
								<a class="hm-brand-a" href="/search?brand=${item.id}">
									<img title="${item.name}" src="${item.logo}" alt="${item.name}"/>
								</a>
							</li>
						</c:if>
					</c:forEach>
				</ul>
				<div class="hm-brand-foot">
					<a href="/brand?cat=64">VIEW ALL</a>
				</div>
			</div>
		</div>
		<div class="hm-main">
			<div class="hm-top-title">
				<a href="/search?cat=64" class="hm-more fr">MORE<span class="hm-sprite-more hm-sprite"></span></a>

				<h1>POPULAR MOBILE PHONES</h1>
			</div>
			<!--yao goodList start-->
			<div class="container">
				<div class="hm-row clearfix">
					<c:forEach items="${topSkus}" var="item">
						<div class="hm-col4">
							<div class="hm-item">
								<a href="/item/${item.id}" class="hm-item-img">
									<img src="${item.masterImage}" alt="${item.title}"/>
								</a>

								<p>
									<c:choose>
										<c:when test="${false&&not empty item.brandName&&not empty item.model}">
											<c:set var="skuShow" value="${item.brandName}&nbsp;${item.model}"></c:set>
											<c:forEach items="${item.saleAttributes}" var="attrItem">
												<c:set var="skuShow" value="${skuShow}&nbsp;${attrItem.strValue}"></c:set>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<c:set var="skuShow" value="${item.title}"></c:set>
										</c:otherwise>
									</c:choose>
									<a class="hm-item-title" title="${skuShow}" href="/item/${item.id}">${skuShow}</a>
								</p>

								<p class="hm-item-price">
									<strong><span></span>US <fmt:formatNumber value="${item.price}" type="currency"/></strong>
								</p>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
			<!--yao goodList end-->
		</div>

	</div>
</div>
<%@ include file="include/footer.jsp" %>
<script>
	$(".J_home_first_cat").find(".J_second_cat").each(function(k) {
		var index = k + 1;
		var mySecond = $(this).find("a").length;
		if (mySecond >= index) {
			$(this).css("top", "0px");
		}
		else {

		}
	});
</script>
</body>
</html>