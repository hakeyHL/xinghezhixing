<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="col-lg-2 col-sm-4">
	<select id="category1" name="category1" class="form-control" onchange="categoryManager.selectCategory(1)">
	</select>
</div>

<div class="col-lg-2 col-sm-4">
	<select id="category2" name="category2" class="form-control" onchange="categoryManager.selectCategory(2)" style="display: none">
	</select>
</div>
<div class="col-lg-2 col-sm-4">
	<select id="category3" name="category3" class="form-control" onchange="categoryManager.selectCategory(3)" style="display: none">
	</select>
</div>

<script>
	var categoryManager = {
		load : function(c1, c2, c3) {
			categoryManager.getDataAndSet(0, 0, c1, function() {
				categoryManager.getDataAndSet(1, c1, c2, function() {
					categoryManager.getDataAndSet(2, c2, c3, function() {});
				});
			});
		},

		getDataAndSet : function(level, id, select, call) {
			var url = "/cate/list/" + id;
			http.doGet(url, function(data) {
				categoryManager.setSubCategoies(level + 1, data.categories, select);
				call();
			});
		},

		selectCategory : function(level) {
			var id = $("#category" + level).val();
			categoryManager.clear(level);
			categoryManager.getDataAndSet(level, id, "", function() {});
		},

		setSubCategoies : function(level, categories, select) {
			var len = categories.length;
			if (len > 0) {
				$("#category" + level).show();
			} else {
				$("#category" + level).hide();
			}
			var html = "<option value='" + -1 + "' >--请选择分类--</option>";
			for (var i = 0; i < len; i++) {
				var category = categories[i];
				var val = category.id;
				if (select == val) {
					html += "<option value='" + val + "' selected>" + category.name + "</option>";
				} else {
					html += "<option value='" + val + "'>" + category.name + "</option>";
				}
			}
			$("#category" + level).html(html);
		},

		clear : function(level) {
			if (level == 2) {
				$("#category3").empty();
				$("#category3").hide();
			}
			if (level == 1) {
				$("#category2").hide();
				$("#category3").hide();
				$("#category2").empty();
				$("#category3").empty();
			}
		}
	};
</script>