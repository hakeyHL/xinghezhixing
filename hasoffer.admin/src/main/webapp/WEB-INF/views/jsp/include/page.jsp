<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${page.pageCount > 1}">
	<div class="row">
		<div class="col-lg-12">
			<nav>
				<ul class="pagination">
					<c:if test="${page.pageNum > 1}">
						<li>
							<a href="${page.requestUrl}?page=<c:choose><c:when test="${page.pageNum-1 > 0}">${page.pageNum-1}</c:when><c:otherwise>1</c:otherwise></c:choose>&size=${page.pageSize}<c:forEach items="${page.pageParams}" var="entry">&${entry.key}=${entry.value}</c:forEach>"
							   class="ib pre">&lt;</a></li>
					</c:if>

					<c:choose>
						<c:when test="${page.pageNum == 1}">
							<li class="active"><a>1</a></li>
						</c:when>
						<c:otherwise>
							<li>
								<a href="${page.requestUrl}?page=1&size=${page.pageSize}<c:forEach items="${page.pageParams}" var="entry">&${entry.key}=${entry.value}</c:forEach>"
								   class="ib">1</a></li>
						</c:otherwise>
					</c:choose>

					<c:if test="${page.pageNum > 4}">
						<li><a>...</a></li>
					</c:if>

					<c:if test="${(page.pageNum-2 > 1) && (page.pageNum-2 < page.pageCount)}">
						<li>
							<a href="${page.requestUrl}?page=${page.pageNum-2}&size=${page.pageSize}<c:forEach items="${page.pageParams}" var="entry">&${entry.key}=${entry.value}</c:forEach>"
							   class="ib">${page.pageNum-2}</a></li>
					</c:if>
					<c:if test="${(page.pageNum-1 > 1) && (page.pageNum-1 < page.pageCount)}">
						<li>
							<a href="${page.requestUrl}?page=${page.pageNum-1}&size=${page.pageSize}<c:forEach items="${page.pageParams}" var="entry">&${entry.key}=${entry.value}</c:forEach>"
							   class="ib">${page.pageNum-1}</a></li>
					</c:if>
					<c:if test="${page.pageNum > 1 && page.pageNum < page.pageCount}">
						<li class="active"><a class="ib">${page.pageNum}</a></li>
					</c:if>
					<c:if test="${(page.pageNum+1 > 1) && (page.pageNum+1 < page.pageCount)}">
						<li>
							<a href="${page.requestUrl}?page=${page.pageNum+1}&size=${page.pageSize}<c:forEach items="${page.pageParams}" var="entry">&${entry.key}=${entry.value}</c:forEach>"
							   class="ib">${page.pageNum+1}</a></li>
					</c:if>
					<c:if test="${(page.pageNum+2 > 1) && (page.pageNum+2 < page.pageCount)}">
						<li>
							<a href="${page.requestUrl}?page=${page.pageNum+2}&size=${page.pageSize}<c:forEach items="${page.pageParams}" var="entry">&${entry.key}=${entry.value}</c:forEach>"
							   class="ib">${page.pageNum+2}</a></li>
					</c:if>

					<c:if test="${page.pageCount - page.pageNum >= 4}">
						<li><a>...</a></li>
					</c:if>
					<c:choose>
						<c:when test="${page.pageNum == page.pageCount}">
							<li class="active"><a>${page.pageNum}</a></li>
						</c:when>
						<c:otherwise>
							<li>
								<a href="${page.requestUrl}?page=${page.pageCount}&size=${page.pageSize}<c:forEach items="${page.pageParams}" var="entry">&${entry.key}=${entry.value}</c:forEach>"
								   class="ib">${page.pageCount}</a></li>
						</c:otherwise>
					</c:choose>

					<c:if test="${page.pageNum != page.pageCount}">
						<li>
							<a href="${page.requestUrl}?page=<c:choose><c:when test="${page.pageNum+1 > page.pageCount}">${page.pageCount}</c:when><c:otherwise>${page.pageNum+1}</c:otherwise></c:choose>&size=${page.pageSize}<c:forEach items="${page.pageParams}" var="entry">&${entry.key}=${entry.value}</c:forEach>"
							   class="ib next">&gt;</a></li>
					</c:if>
				</ul>
			</nav>
			第${page.pageNum}页， 每页显示${page.pageSize}条 ，共${page.recordCount}条记录
		</div>
	</div>
</c:if>