<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
	<title>compare Allbuy category</title>
	<%@ include file="include/common-head.jsp" %>
</head>
<body>
<%@ include file="include/header.jsp" %>
<link rel="stylesheet" href="/static/css/app/category/category.css"/>
<div class="page-brand">

</div>
<div class="cat-con wrap clearfix">
	<div class="cat-bar">
		<p class="cat-filter-title">CATEGORIES</p>
		<ul class="cat-filter-list">
			<c:forEach items="${categories}" var="item">
				<li>
					<c:set var="hasSecond" value="${! empty item.subCategories}"></c:set>
					<c:set var="h" value="javascript:void(0);"></c:set>
					<c:if test="${hasSecond}">
						<c:set var="h" value="/category/${item.id}"></c:set>
					</c:if>
					<a class="cat-filter-item <c:if test="${item.id==category.id}">cat-active</c:if>
							" href="${h}">${item.name}</a>
				</li>
			</c:forEach>
		</ul>
	</div>
	<div class="cat-main">
		<div class="cat-main-bd">
			<h1>${category.name}</h1>

			<div class="row clearfix">
				<c:forEach items="${category.subCategories}" var="item">
					<div class="col-5">
						<div class="cat-sec-list">
							<a href="/list?cat=${item.id}" class="cat-sec-a">
								<img src="${item.mobImageUrl}" alt="${item.name}"/>
							</a>

							<p class="cat-sec-des">
								<a href="">
									${item.name}
								</a>
							</p>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</div>
<%@ include file="include/footer.jsp" %>
</body>
</html>