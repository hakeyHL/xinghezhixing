<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--
  Created by IntelliJ IDEA.
  User: glx
  Date: 2015/10/13
  Time: 11:22
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html>
<head>
	<title>Compare Allbuy_${sku.title}</title>
	<%@ include file="include/common-head.jsp" %>
</head>
<body>
<%@ include file="include/header.jsp" %>
<link rel="stylesheet" href="/static/css/app/detail/detail.css"/>
<div class="bread-wrap clearfix">
	<ol class="wrap bread-li">
		<li>
			<a href="/home">Home</a>
		</li>
		<li>
			<a href="/search?cat=${product.secondCategoryId}">${product.secondCategoryName}</a>
		</li>
		<li class="last">
			<span>${sku.title}</span>
		</li>
	</ol>
</div>
<div class="wrap">
	<div class="compare-head clearfix">
		<a href="javascript:void(0);" class="compare-head-a fl">
			<img src="${sku.masterImage}" alt="${sku.title}"/>
		</a>
		<img class="fr" style="margin-top: -24px;" src="/static/image/tmp/detail-banner.png" alt=""/>

		<div class="compare-head-body">
			<h1>
				${sku.title}
			</h1>

			<p class="compare-head-attr">
				<c:if test="${sku.brandName!=''}">
					<span class="compare-head-sp">Brand:</span>
					<a class="compare-head-model mr30 J_ga_in_a" data-ga-lab="list-brand" href="/search?brand=${sku.brandId}">${sku.brandName}
					</a>
					</span>
				</c:if>
			</p>

			<div class="compare-head-right">
				<a href="${lowestPriceSku.url}" rel="nofollow" target="_blank" class="compare-go fr J_ga_out_a" data-ga-lab="Best">Buy
					Now</a>

				<p class="compare-head-sp">Best price in ${lowestPriceSku.siteName}</p>

				<p class="compare-head-best">US <fmt:formatNumber value="${lowestPriceSku.price}" type="currency"/></p>
			</div>
		</div>
	</div>
	<c:set var="nullDeal" value="${deals==null||fn:length(deals)==0}"></c:set>
	<div class="compare-body">
		<ul class="compare-body-nav">
			<li class="compare-active J_ga_btn" data-ga-lab="bestPrice" data-item="price">
				Prices
			</li>
			<li class="J_ga_btn" data-ga-lab="dealList" data-item="deal"
			    <c:if test="${nullDeal}">style="display:none"</c:if>
					>
				Deals
			</li>
			<li class="J_ga_btn" data-ga-lab="Specifications" data-item="desc">
				Key Specs
			</li>

		</ul>
		<div class="compare-body-panel clearfix">
			<div class="compare-main J_body  <c:if test='${nullDeal}'>compare-all</c:if>">
				<table class="compare-body-tb J_price">
					<thead>
					<tr>
						<th class="picture">Picture</th>
						<th class="online">Online Stores</th>
						<th class="name">Product Name</th>
						<th class="price">Price</th>
						<th class="href"></th>
					</tr>
					</thead>
					<tbody>
					<c:forEach items="${comparedSkus}" var="compareSku">
						<tr>
							<td>
								<div class="compare-sku-img"><img src="${compareSku.masterImageUrl}" alt="${compareSku.masterImageUrl}"/>
								</div>
							</td>
							<td>
								<a class="compare-web-logo J_ga_out_a" data-ga-lab="${compareSku.siteName}" rel=nofollow href='${compareSku.url}'><img src="${compareSku.logoUrl}" alt="${lowestPriceSku.siteName}"/></a>
							</td>
							<td class="tc">
								<a class="c0" target="_blank" href="${compareSku.url}" rel="nofollow">${ compareSku.title}</a>
							</td>
							<td class="compare-price">
								<c:choose>
									<c:when test="${compareSku.lowest}">
										<span class="compare-lowest">
											<span class="compare-lowest-ico"></span>US <fmt:formatNumber value="${compareSku.price}" type="currency"/>
										</span>
									</c:when>
									<c:otherwise>
										<span>US <fmt:formatNumber value="${compareSku.price}" type="currency"/></span>
									</c:otherwise>
								</c:choose>

							</td>
							<td>
								<a target="_blank" href="${compareSku.url}" rel="nofollow" class="compare-width-s J_ga_out_a" data-ga-lab="${compareSku.siteName}">Buy
									Now</a>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				<div class="J_desc compare-desc" style="display:none;">
					<table class="compare-base-attr">
						<thead>
						<tr>
							<th class="key"></th>
							<th class="value"></th>
						</tr>
						</thead>
						<tbody>
						<c:forEach items="${sku.basicAttributes}" var="item" varStatus="stat">
							<tr
									<c:if test="${stat.count%2!=0}">class="odd"</c:if> >
								<td>${item.attributeDefName}</td>
								<td>${item.value}</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
					${sku.desc}
				</div>
			</div>
			<div class="compare-bar" <c:if test="${nullDeal}"> style="display: none;"</c:if> >
				<dl class="compare-body-recent">
					<dt>Recent Deals</dt>
					<dd>
						<p class="compare-recent-time">08/10/2015</p>

						<p>
							good quality,exactly like in the description. rate services.thank you Allbuy!good quality, escription. fast
							shipping
							and grate services.t
						</p>

						<p class="clearfix mt5">
							<span class="fl">www.gearbest.com</span>
							<span class="fr">55Reviews</span>
						</p>
					</dd>
				</dl>
			</div>
		</div>
	</div>
</div>
<%@ include file="include/footer.jsp" %>
<script src="/static/js/app/detail/detail.js"></script>
</body>
</html>
