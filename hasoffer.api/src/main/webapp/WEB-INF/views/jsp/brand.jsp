<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/11/11
  Time: 17:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
	<title>Compare Allbuy_${categoryName}</title>
	<%@ include file="include/common-head.jsp" %>
</head>
<body>
<%@ include file="include/header.jsp" %>
<link rel="stylesheet" href="/static/css/app/brand/brand.css"/>
<div class="bread-wrap clearfix">
	<ol class="wrap bread-li">
		<li>
			<a href="/home">Home</a>
		</li>
		<li class="last">
			<span>${categoryName}</span>
		</li>
	</ol>
</div>
<div class="wrap mt10 clearfix">
	<c:set var="selectedItemId" value="${empty _parameterMap['cat']?'':_parameterMap['cat'][0]}"></c:set>
	<div class="brand-bar">
		<div class="brand-cat J_brand_con">
			<c:forEach items="${categories}" var="item">
				<h2 class="brand-first-cat J_brand_fisrt"
				    <c:if test="${empty item.subCategories}">style="cursor:not-allowed"</c:if>
						>${item.name}
				</h2>
				<ul class="J_brand_second">
					<c:forEach items="${item.subCategories}" var="second">
						<c:choose>
							<c:when test="${selectedItemId==second.id}">
								<li>
									<a href="/brand?cat=${second.id}" class="brand-second-cat-item brand-second-active">
											${second.name}
									</a>
								</li>
							</c:when>
							<c:otherwise>
								<li>
									<a href="/brand?cat=${second.id}" class="brand-second-cat-item">
											${second.name}
									</a>
								</li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>
			</c:forEach>
		</div>

	</div>
	<c:if test="${! empty selectedItemId}">
		<div class="brand-main">
			<div class="brand-desc">
				<h1>
						${categoryName}
				</h1>

				<div class="brand-row clearfix">

					<c:forEach items="${brandFacet}" var="item">
						<c:if test="${item.name!='All'}">
							<div class="brand-col-3">
								<a href="/search?brand=${item.id}" class="brand-item">${item.name}<span class="ml5">(${item.qty})</span></a>
							</div>
						</c:if>
					</c:forEach>
				</div>
			</div>
		</div>
	</c:if>
</div>
<%@ include file="include/footer.jsp" %>
</body>
</html>