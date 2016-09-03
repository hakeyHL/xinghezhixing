<c:if test="${pageableSkus.totalPage>1}">

	<link rel="stylesheet" href="/static/css/module/page.css"/>
	<c:choose>
		<c:when test="${pageableSkus.currentPage>=pageableSkus.totalPage}">
			<c:set var="nextPage" value="${pageableSkus.totalPage}"></c:set>
		</c:when>
		<c:otherwise>
			<c:set var="nextPage" value="${pageableSkus.currentPage+1}"></c:set>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${empty _queryStringWithoutPage}">
			<c:set var="urlParam" value=""></c:set>
		</c:when>
		<c:otherwise>
			<c:set var="urlParam" value="&${_queryStringWithoutPage}"></c:set>
		</c:otherwise>
	</c:choose>

	<div class="page-nav">
		<c:if test="${pageableSkus.currentPage>1}">
			<a class="ib pre J_fresh_a" href="${yaoUrl}?pn=${pageableSkus.currentPage-1}&ps=${pageableSkus.pageSize}${urlParam}">
				&lt; Previous
			</a>
		</c:if>
		<c:choose>
			<c:when test="${pageableSkus.currentPage==1}">
				<span class="cur">1</span>
			</c:when>
			<c:otherwise>
				<a class="J_fresh_a" href="${yaoUrl}?pn=1&ps=${pageableSkus.pageSize}${urlParam}"
				   class="ib">1</a>
			</c:otherwise>
		</c:choose>
		<c:if test="${pageableSkus.currentPage>4}">
			<span class="dot">...</span>
		</c:if>
		<c:forEach var="index" begin="0" end="4">
			<c:choose>
				<c:when test="${index==2&&pageableSkus.currentPage>1&&pageableSkus.currentPage<pageableSkus.totalPage}">
					<span class="cur">${pageableSkus.currentPage}</span>
				</c:when>
				<c:otherwise>
					<c:if test="${(pageableSkus.currentPage-2+index)>1&&(pageableSkus.currentPage-2+index)<pageableSkus.totalPage}">
						<a class="ib J_fresh_a" href="${yaoUrl}?pn=${pageableSkus.currentPage-2+index}&ps=${pageableSkus.pageSize}${urlParam}">
						${pageableSkus.currentPage-2+index}
						</a>
					</c:if>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:if test="${(pageableSkus.totalPage-pageableSkus.currentPage)>=4}">
			<span class="dot">...</span>
		</c:if>
		<c:choose>
			<c:when test="${pageableSkus.currentPage==pageableSkus.totalPage}">
				<span class="cur">${pageableSkus.currentPage}</span>
			</c:when>
			<c:otherwise>
				<a class="J_fresh_a ib" href="${yaoUrl}?pn=${pageableSkus.totalPage}&ps=${pageableSkus.pageSize}${urlParam}">${pageableSkus.totalPage}</a>
			</c:otherwise>
		</c:choose>
		<c:if test="${pageableSkus.currentPage!=pageableSkus.totalPage}">
			<a class="J_fresh_a ib next" href="${yaoUrl}?pn=${nextPage}&ps=${pageableSkus.pageSize}${urlParam}">Next &gt;</a>
		</c:if>
	</div>
</c:if>