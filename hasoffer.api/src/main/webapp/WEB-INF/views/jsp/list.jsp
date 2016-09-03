<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="yaoUrl" value="/list"></c:set>
<!DOCTYPE html>
<html>
<head>
	<title>Search any product to get the best price !</title>
	<%@ include file="include/common-head.jsp" %>
</head>
<body>
<%@ include file="include/header.jsp" %>
<link rel="stylesheet" href="/static/css/app/list/list.css"/>
<script>
	var listFilter = "/list?";
</script>
<div class="li-main-table clearfix wrap">
	<div class="li-bar">
		<div>
			<div class="li-filter J_filter">
				<p class="li-filter-title">Brand</p>
				<ul class="li-filter-list J_filter_li">
					<li class="li-filter-item"><%--yao--激活样式:li-filter-select--%>
						<a href="javascript:void(0);" class="li-filter-lab J_ajax_arr" data-akey="brand" data-aval="all"><span class="li-sprite li-sprite-filter"></span>All</a>
					</li>
					<c:forEach items="${brands}" var="item">
						<li class="li-filter-item">
							<a href="javascript:void(0);" class="li-filter-lab J_ajax_arr" data-akey="brand" data-aval="${item.id}"><span class="li-sprite li-sprite-filter"></span>${item.name}
							</a>
						</li>
					</c:forEach>
				</ul>

				<%--yao--如果列表过长,则显示view all--%>
				<c:if test="${fn:length(brands)>10}">
					<p class="li-filter-all">
						<a href="javascript:void(0);" class="J_filter_all">View All</a>
					</p>
				</c:if>
			</div>
			<div class="li-filter mt15">
				<p class="li-filter-title">Price</p>
				<ul class="li-filter-list">
					<li class="li-filter-item">
						<%--yao--激活样式:li-filter-select--%>
						<a href="javascript:void(0);" class="li-filter-lab J_ajax_arr" data-akey="lp-hp" data-aval="all"><span class="li-sprite li-sprite-filter"></span>All</a>
					</li>
					<li class="li-filter-item">
						<a href="javascript:void(0);" class="li-filter-lab J_ajax_arr" data-akey="lp-hp" data-aval="-10"><span class="li-sprite li-sprite-filter"></span>Under $10</a>
					</li>
					<li class="li-filter-item">
						<a href="javascript:void(0);" class="li-filter-lab J_ajax_arr" data-akey="lp-hp" data-aval="10-25"><span class="li-sprite li-sprite-filter"></span>$10 - $25</a>
					</li>
					<li class="li-filter-item">
						<a href="javascript:void(0);" class="li-filter-lab J_ajax_arr" data-akey="lp-hp" data-aval="25-45"><span class="li-sprite li-sprite-filter"></span>$25 - $45</a>
					</li>
					<li class="li-filter-item">
						<a href="javascript:void(0);" class="li-filter-lab J_ajax_arr" data-akey="lp-hp" data-aval="45-65"><span class="li-sprite li-sprite-filter"></span>$45 - $65</a>
					</li>
					<li class="li-filter-item">
						<a href="javascript:void(0);" class="li-filter-lab J_ajax_arr" data-akey="lp-hp" data-aval="65-100"><span class="li-sprite li-sprite-filter"></span>$65 - $100</a>
					</li>
					<li class="li-filter-item">
						<a href="javascript:void(0);" class="li-filter-lab J_ajax_arr" data-akey="lp-hp" data-aval="100-"><span class="li-sprite li-sprite-filter"></span>Over $100</a>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="li-main">
		<div class="pl15 clearfix">
			<div class="li-head clearfix">
				<h1 class="fl">ProductList</h1>

				<p class="fr">
					<span class="c6 f12">Sort by:</span>
					<%--yao--激活样式:li-sort-active--%>
					<a href="javascript:void(0);" class="li-sort J_ajax_str" data-akey="order" data-aval="popular_d">Popular<i class="iconfont fi-arrow-down li-sort-font"></i></a>
					<a href="javascript:void(0);" class="li-sort J_ajax_str" data-akey="order" data-aval="price_a">Low
						Price<i class="iconfont fi-arrow-down li-sort-font"></i></a>
					<a href="javascript:void(0);" class="li-sort J_ajax_str" data-akey="order" data-aval="price_d">High
						Price<i class="iconfont fi-arrow-up  li-sort-font"></i></a>
				</p>
			</div>
			<div class="mt10 J_piece">
				<p class="J_piece_brand li-piece-wrap clearfix" style="display:none;">
					<strong>Brand:</strong>
				</p>
				<p class="J_piece_lp-hp li-piece-wrap clearfix" style="display:none;">
					<strong>Price:</strong>
				</p>
			</div>
			<div class="container J_fresh_con">
				<div class="row clearfix">
					<c:choose>
						<c:when test="${pageableResult.data!=null&&fn:length(pageableResult.data)!=0}">
							<c:forEach items="${pageableResult.data}" var="item">
								<div class="col-m4">
									<div class="li-item">
										<a href="/item/${item.id}" class="li-item-img">
											<img src="${item.masterImage}" alt="${item.title}"/>
										</a>

										<p class="pt5">
											<c:choose>
												<c:when test="${not empty item.brandName&&not empty item.model}">
													<c:set var="skuShow" value="${item.brandName}&nbsp;${item.model}"></c:set>
													<c:forEach items="${item.saleAttributes}" var="attrItem">
														<c:set var="skuShow" value="${skuShow}&nbsp;${attrItem.strValue}"></c:set>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<c:set var="skuShow" value="${item.title}"></c:set>
												</c:otherwise>
											</c:choose>
											<a class="clamp2" title="${skuShow}" href="/item/${item.id}">${skuShow}</a>
										</p>

										<p class="li-item-price">
											<span class="c6">From</span>
											<strong>US <fmt:formatNumber value="${item.price}" type="currency"/></strong>
										</p>

										<p>
											<i class="iconfont fi-house li-see"></i><a href="/item/${item.id}" class="li-see-all li-see">SeeAll ${item.comparedQty}
											Stores</a>
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

				<div class="mt30">
					<%@ include file="include/page.jsp" %>
				</div>
			</div>
		</div>
	</div>
</div>
<%@ include file="include/footer.jsp" %>
<script src="/static/js/app/list/list.js"></script>
</body>
</html>