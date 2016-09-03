<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="yaoUrl" value="/search"></c:set>
<!DOCTYPE html>
<html>
<head>
	<title>Search any product to get the best price !</title>
	<%@ include file="include/common-head.jsp" %>
</head>
<body>
<%@ include file="include/header.jsp" %>
<link rel="stylesheet" href="/static/css/app/list/list.css"/>
<div class="bread-wrap clearfix">
	<ol class="wrap bread-li">
		<c:choose>
			<c:when test="${empty queryStr}">
				<li class="last">
					<a href="/home">Home</a>
				</li>
			</c:when>
			<c:otherwise>
				<li>
					<a href="/home">Home</a>
				</li>
				<li class="last">
					<span>"${queryStr}"</span>
				</li>
			</c:otherwise>
		</c:choose>
	</ol>
</div>
<div class="li-main-table clearfix wrap">

	<div class="li-bar">
		<div class="query-cat">
			<c:forEach items="${categoryFacet}" var="item">
				<h2 class="query-cat-first">
						${item.name}
				</h2>
				<ul>
					<c:forEach items="${item.secondCatQtys}" var="second">
						<li>

							<a class="query-cat-second-item" href="${second.checkedHref}">
								<span class="query-cat-second-name">${second.name}<span class="c9 ml5">(${second.qty})</span></span>
							</a>
						</li>
					</c:forEach>
				</ul>
			</c:forEach>
		</div>
		<div class="query-cat">
			<h2 class="query-cat-first">
				Brand
			</h2>
			<ul class="J_filter_li">
				<c:forEach items="${brandFacet}" var="item" varStatus="stat">
					<c:if test="${stat.count<=8}">
						<li>
							<c:choose>
								<c:when test="${item.checked}">
									<a class="li-filter-item li-filter-select" href="${item.uncheckedHref}"><span class="li-sprite li-sprite-filter"></span>${item.name}(${item.qty})</a>
								</c:when>
								<c:otherwise>
									<a class="li-filter-item" href="${item.checkedHref}"><span class="li-sprite li-sprite-filter"></span>${item.name}<span class="c9 ml5">(${item.qty})</span></a>
								</c:otherwise>
							</c:choose>
						</li>
					</c:if>
				</c:forEach>
			</ul>
			<c:if test="${fn:length(brandFacet)>8}">
				<a class="J_more" href="javascript:void(0);">+ See more</a>
				&nbsp;<div class="J_brand_more li-filter-brand-more" style="display: none;">
					<ul class="J_filter_li">
						<c:forEach items="${brandFacet}" var="item" varStatus="stat">
							<li>
								<c:choose>
									<c:when test="${item.checked}">
										<a class="li-filter-item li-filter-select" href="${item.uncheckedHref}"><span class="li-sprite li-sprite-filter"></span>${item.name}(${item.qty})</a>
									</c:when>
									<c:otherwise>
										<a class="li-filter-item" href="${item.checkedHref}"><span class="li-sprite li-sprite-filter"></span>${item.name}<span class="c9 ml5">(${item.qty})</span></a>
									</c:otherwise>
								</c:choose>
							</li>
						</c:forEach>
					</ul>
				</div>
			</c:if>
		</div>
		<div class="query-cat">
			<h2 class="query-cat-first">
				Price
			</h2>
			<ul class="J_filter_li">
				<c:forEach items="${priceFacet}" var="item">
					<li>
						<c:choose>
							<c:when test="${item.checked}">

								<a class="li-filter-select li-filter-item" href="${item.uncheckedHref}"><span class="li-sprite li-sprite-filter"></span>${item.text}
								</a>
							</c:when>
							<c:otherwise>
								<a class="li-filter-item" href="${item.checkedHref}"><span class="li-sprite li-sprite-filter"></span>${item.text}
								</a>
							</c:otherwise>
						</c:choose>

					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<div class="li-main">
		<div class="clearfix">
			<%--yao --选择块显示 --%>
			<div class="J_piece">
				<c:forEach items="${filterGroups}" var="item" varStatus="stat">
					<c:if test="${item.text!='q:' && ! empty item.filters}">
						<p class="J_piece_category li-piece-wrap clearfix" style="display:block;">
							<strong>${item.text}</strong>
							<c:forEach items="${item.filters}" var="child">
								<a href="${child.cancelHref}" class="li-piece-a">
									<span class="li-piece-sp">${child.text}</span>
									<i class="li-piece-close iconfont fi-close"></i>
								</a>
							</c:forEach>
						</p>
					</c:if>
				</c:forEach>
			</div>
			<div class="li-head clearfix">
				<p class="fl query-sum-result">${pageableSkus.numFund} Results</p>

				<p class="fr">
					<span class="c6 f12">Sort by:</span>
					<c:forEach items="${orders}" var="item">
						<a href="${item.href}" class="li-sort J_ajax_str
								<c:if test="${item.checked}">
									 li-sort-active
								</c:if>
								">${item.text}<i class="iconfont fi-arrow-down li-sort-font"></i></a>
					</c:forEach>
				</p>
			</div>
			<div class="container J_fresh_con">
				<div class="li-row clearfix">
					<c:choose>
						<c:when test="${pageableSkus.data!=null&&fn:length(pageableSkus.data)!=0}">
							<c:forEach items="${pageableSkus.data}" var="item">
								<div class="li-col4">
									<div class="li-item">
										<a href="/item/${item.id}" class="li-item-img">
											<img src="${item.masterImage}" alt="${item.title}"/>
										</a>

										<p class="pt5">
											<c:choose>
												<c:when test="${false && not empty item.brandName&&not empty item.model}">
													<c:set var="skuShow" value="${item.brandName}&nbsp;${item.model}"></c:set>
													<c:forEach items="${item.saleAttributes}" var="attrItem">
														<c:set var="skuShow" value="${skuShow}&nbsp;${attrItem.strValue}"></c:set>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<c:set var="skuShow" value="${item.title}"></c:set>
												</c:otherwise>
											</c:choose>
											<a class="li-item-title" title="${skuShow}" href="/item/${item.id}">${skuShow}</a>
										</p>

										<p class="li-item-price">
											<strong><span></span>US <fmt:formatNumber value="${item.price}" type="currency"/></strong>
										</p>
									</div>
								</div>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<p class="li-no-data">
								<img src="static/image/module/list-no-data.png" alt=""/>
								<br/>
								Sorry, no products matched your criteria.
								<br/>
								Try deselecting some filter options.
							</p>
						</c:otherwise>
					</c:choose>
				</div>


			</div>
		</div>
	</div>


</div>
<script type="text/javascript" src="/static/js/app/list/search.js"></script>
<%@ include file="include/page.jsp" %>
<%@ include file="include/footer.jsp" %>
</body>
</html>