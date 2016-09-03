<link rel="stylesheet" href="/static/css/module/iconfont.css"/>
<link rel="stylesheet" href="/static/css/module/base.css"/>
<link rel="stylesheet" href="/static/css/module/public.css"/>
<script>
	var PATH = "";
</script>
<script src="/static/js/require.2.1.9-jquery.1.10.2-underscore.1.5.2-backbone.1.0.0.js"></script>
<script src="/static/js/common.js"></script>
<%--yao--setLocal设置本地国家 --%>
<fmt:setLocale value="en_US"/>
<c:set var="queryStr" value="${empty _parameterMap['q']?'':_parameterMap['q'][0]}"></c:set>
<%--<div class="site-nav">--%>
<%--<div class="wrap">--%>
<%--<a href="/home">Home</a>--%>
<%--</div>--%>
<%--</div>--%>
<div class="head-b">
	<div class="wrap">
		<div class="search-wrap  clearfix">
			<a href="/home" class="head-sprite fl sprite-logo"></a>

			<form class="search-fm fr" action="/search" method="get">
				<div class="search-main">
					<i class="search-i iconfont fi-serach"></i>
					<input name="q" type="text" class="search-input" placeholder="Search any product to find lowest price!" autocomplete="off" value="${queryStr}"/>
				</div>
				<div class="search-bar">
					<input type="submit" class="search-submit" value="SEARCH"/>
				</div>
			</form>
		</div>
	</div>
</div>
<div class="head-category">
	<div class="wrap">
		<div class="cat-head-wrap">
				<p>
					<a href="javascript:void(0);" class="J_cat_title cat-name">
						<span class="head-sprite cat-sprite-menu"></span>
						<span>ALL CATEGORIES</span>
					</a>
				</p>
				<ul class="cat-first-list J_first_cat">
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
	</div>
</div>
<script>
		$(".J_first_cat").find(".J_second_cat").each(function(k) {
			var index = k + 1;
			var mySecond = $(this).find("a").length;
			if (mySecond >= index) {
				$(this).css("top", "0px");
			}
			else {

			}
		});
</script>