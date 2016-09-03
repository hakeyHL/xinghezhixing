<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- loading -->
<div name="loadingDiv" class="loadingDiv1 centerDiv">
	<img src="../../image/allbuy/abLoading.gif">
</div>
<div name="loadingDiv" class="loadingDiv2"></div>

<style>
	.loadingDiv1 {
		position: fixed;
		z-index: 1001;
	}

	.loadingDiv2 {
		position: fixed;
		width: 100%;
		height: 100%;
		left: 0;
		top: 0;
		opacity: 0;
		z-index: 1000
	}

</style>