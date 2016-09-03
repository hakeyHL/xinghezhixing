<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script src="/static/js/jquery/jquery-2.1.1.min.js"></script>
<script src="/static/js/bootstrap/bootstrap-typeahead.min.js"></script>

<div class="form-group">
	<label class="col-sm-3 col-lg-1 col-md-2 control-label">商品品牌</label>
	<div class="col-sm-4 col-lg-2 col-md-2">
		<input name="brand" id="brand" class="form-control" placeholder="品牌" data-provide="typeahead" autocomplete="off" value="${brand}">
	</div>
	<label class="col-sm-3 col-lg-1 col-md-2 control-label">商品型号</label>
	<div class="col-sm-6 col-lg-2 col-md-2">
		<input name="model" id="model" class="form-control" placeholder="型号" data-provide="typeahead" autocomplete="off" value="${model}">
	</div>
</div>

<script>
	var brandList = new Array();
	var modelList = new Array();
	var currentBrand = "";

	$(document).ready(function(){
		$.get('/content/getBrands', function(data){
			if (data != null)
			{
				brandList.length = 0;
				for (var i = 0; i < data.length; i++){
					brandList.push(data[i]);
				}
			}
		});

		if ($("#brand").val() != '') {
			$.get('/content/getModels?brandName='+$("#brand").val(), function(data){
				modelList.length = 0;
				if (data != null)
				{
					for (var i = 0; i < data.length; i++){
						modelList.push(data[i]);
					}
				}
			});
		}

		$("#brand").typeahead({source:brandList,
			updater:function(item){
				var curValue = item;
				curValue = curValue.replace(/(^\s*)|(\s*$)/g, "");
				if (curValue == currentBrand) {
					return;
				} else {
					currentBrand = curValue;
					modelList.length = 0;
					if (curValue == ''){
					} else{
						$.get('/content/getModels?brandName='+curValue, function(data){
							if (data != null)
							{
								for (var i = 0; i < data.length; i++){
									modelList.push(data[i]);
								}
							}
						});

					}
				}

				return item;
			}});

		$("#model").typeahead({source:modelList});
	});
</script>