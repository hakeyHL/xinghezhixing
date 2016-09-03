<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<style>
	.p_detail_image {
		width: 60px;
		max-height: 100px;
	}

	.p_detail_image_div {
		float: left;
		margin: 20px;
	}
	.p_detail_name_td{
		width: 35%;
		text-align: right;
	}
</style>

<div id="page-wrapper">
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">商品: ${product.title}</h1>
		</div>
		<!-- /.col-lg-12 -->
	</div>
	<div class="row">
		<div class="col-lg-6">
			<div class="col-lg-12">
				<p>来源：<a href="${mspJob.url}" target="_blank">${mspJob.url}</a></p>

				<p>创建时间：${mspJob.createTime} </p>

				<p>更新时间:${mspJob.processTime}</p>
			</div>
			<%--<ol class="breadcrumb">
				<c:forEach items="${product.categories}" var="cate" varStatus="vs">
					<li class="${vs.last?"active":""}">${cate.name}</li>
				</c:forEach>
			</ol>--%>

			<div class="col-lg-12">
				<p>类目(category) :
					<c:forEach items="${product.categories}" var="cate" varStatus="vs">
						${cate.name}
						${vs.last?"":"/"}
					</c:forEach>
				</p>
				<p>颜色(color) : ${product.color}</p>

				<p>大小(size) : ${product.size}</p>

				<p>评论(reviews): ${product.rating} </p>
			</div>

			<div class="col-lg-12">
				<c:forEach items="${imageUrls}" var="imageUrl">
					<div class="p_detail_image_div">
						<img src="${imageUrl}" class="p_detail_image">
					</div>
				</c:forEach>
			</div>

			<div class="col-lg-12">
				<div>
					<ol>
						<c:forEach items="${features}" var="feature">
							<li>${feature}</li>
						</c:forEach>
					</ol>
				</div>
				<div>
					${product.description}
				</div>
			</div>
		</div>

		<div class="col-lg-6">
			<div class="col-lg-12">
				<c:forEach items="${basicAttributes}" var="entry">
					<div class="panel panel-default">
						<!-- Default panel contents -->
						<div class="panel-heading">${entry.key}</div>
						<table class="table">
							<c:forEach items="${entry.value}" var="attr">
								<tr>
									<td class="p_detail_name_td">${attr.name}</td>
									<td>${attr.value}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</div>

<script>

</script>

<jsp:include page="../include/footer.jsp"/>