<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
	<title></title>
	<%@ include file="include/common-head.jsp" %>
</head>
<body>
<%@ include file="include/header.jsp" %>
<link rel="stylesheet" href="/static/css/app/deal/recommand.css"/>
<div class="rec-wrap wrap">
	<!-- yao 上一半 start-->
	<div class="rec-head">
		<!-- yao--左侧图片 start-->
		<p class="rec-good-pic"><img src="${detail.coverUrl}" alt="good img" title="${detail.title}"/></p>
		<!-- yao--左侧图片 end-->

		<!-- yao 右侧app down界面 start-->
		<div class="rec-app-down">
			<a href="" class="deal-sprite rec-apple-down"></a>
			<a href="" class="deal-sprite rec-and-down"></a>
			<span class="deal-sprite rec-down-img"></span>
		</div>
		<!-- yao 右侧app down界面 end-->
		<!-- yao 中间描述 start-->
		<div class="rec-good-main">
			<h1>
				<strong>
					${detail.recommendTitle}
				</strong>${detail.title}
			</h1>

			<div class="rec-good-p">
				<p><fmt:formatDate value="${detail.createTime}" pattern="MM/dd/yyyy hh:mm"/></p>

				<p>Store: ${detail.site}</p>
			</div>
			<div class="rec-good-buy">
				<a href="${detail.targetUrl}" class="rec-good-a">
					Buy Now
				</a>
			</div>
		</div>
		<!-- yao 中间描述 end-->
	</div>
	<!-- yao 上一半 end-->
	<!-- yao 下一半 start-->
	<div class="rec-main-bar clearfix">
		<!-- yao 下一半左侧 start-->
		<div class="rec-main">
			<!-- yao deal details start-->
			<div class="rec-title-wrap">
				<div class="rec-expand-title">

					<h2>Deal Details </h2>
				</div>
			</div>
			<div class="rec-expand-li">
				${detail.recommendReason}
				<p class="rec-expand-item">
					<a href="/item/${detail.cmpSkuId}" class="rec-expand-a">Click here</a>&nbsp;to compare price.
				</p>
			</div>
			<!-- yao deal details end-->
			<!-- yao product features strat-->
			<div class="rec-title-wrap">
				<div class="rec-expand-title">

					<h2>Product Features</h2></div>
			</div>
			<div class="rec-expand-feature pt15">
				${detail.skuSpecifics}
			</div>

			<p class="rec-expand-feature pt5">
				<a href="" class="rec-expand-a">Click here</a>&nbsp;to view all info about the item.
			</p>
			<!-- yao product features end-->
		</div>
		<!-- yao 下一半左侧 end-->
		<!--yao 下一半右侧 start-->
		<div class="rec-bar">
			<p class="rec-rel-deal"><strong>Related Deals</strong><a href="" class="rec-rel-more">More ></a></p>
			<ul>
				<c:forEach items="${detail.relatedDeals}" var="item" varStatus="stat">
					<li class="rec-rel-item">
						<a href="${item.targetUrl}" class="rec-rel-img"><img src="${item.coverUrl}" alt=""/></a>

						<div class="rec-rel-right">
							<a href="${item.targetUrl}" class="rec-rel-1">
									${item.title}
							</a>

							<p><strong class="rec-rel-off">${item.recommendTitle}</strong></p>

							<p class="rec-rel-site"><span class="fl">${item.site}</span><span
									class="fr"><fmt:formatDate value="${item.createTime}" pattern="MM/dd/yyyy hh:mm"/> </span></p>
						</div>
					</li>
				</c:forEach>
			</ul>
		</div>
		<!--yao 下一半右侧 end-->

	</div>
	<div class="rec-expand-desc">
		<c:if test="${!empty detail.previous}">
			<p>
				<span class="rec-expand-lab">Previous:</span>
				<a href="/deal/${detail.previous.id}" class="rec-expand-val">${detail.previous.title}</a>
			</p>
		</c:if>
		<c:if test="${!empty detail.next}">
			<p class="pt20">
				<span class="rec-expand-lab">Next:</span>
				<a href="/deal/${detail.next.id}" class="rec-expand-val">${detail.next.title}</a>
			</p>
		</c:if>
	</div>
	<!-- yao 下一半 end-->
</div>
<%--insert code--%>
<%@ include file="include/footer.jsp" %>
<script>
	$("h1").setEllipse(2);
</script>
</body>
</html>