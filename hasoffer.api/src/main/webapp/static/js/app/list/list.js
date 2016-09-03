/**
 * Created by Administrator on 2015/10/26.
 */
require(['ajaxFresh', 'url.search'], function(ajaxFresh, UrlSearch) {
	$(document).ready(function() {
		//yao--界面初始化,根据url设置相应的界面 跟fresh初始化数组
		var search = new UrlSearch(['brand']),
				data = {
					brand : search.get('brand'),
					lp : search.get('lp'),
					hp : search.get('hp'),
					order : search.get('order')
				};
		domInit(data);
		var urlParam = search.getData();
		var fresh = new ajaxFresh.AjaxFresh('.J_fresh_con', listFilter, urlParam);
		//排序点击事件
		$('.J_ajax_str').click(function() {
			var akey = $(this).data('akey'),
					aval = $(this).data('aval');
			$(this).toggleClass("li-sort-active");
			if ($(this).hasClass("li-sort-active")) {
				$(this).siblings().removeClass("li-sort-active");
				fresh.updateOrAdd(akey, aval);
			}
			else {
				fresh.removeValue(akey, aval);
			}
		});
		//左侧brand组合 checkbox勾选事件
		$('.J_ajax_arr').click(function() {
			var isCheck = !$(this).hasClass("li-filter-select"),
					parentLab = $(this),
					akey = $(this).data('akey'),
					aval = $(this).data('aval'),
					keyStr = akey;
			akey = akey ? akey.toString().split("-") : akey;
			aval = aval ? aval.toString().split("-") : aval;

			if (isCheck) {
				//yao--如果是选择all或者组合选择框要求单选,则把fresh相关数组清空,并把所有的label选择样式去掉,勾选框去掉
				if (aval == 'all') {
					var sibl = parentLab.parent().siblings();
					//去除label样式
					sibl.find('.li-filter-select').removeClass('li-filter-select');
					//本label增加样式
					parentLab.addClass("li-filter-select");
					//更新fresh.data数据
					fresh.del(akey);

					//yao--清空块
					clearPiece(keyStr);
				}
				//yao--否则,将本label设置样式,并更新fresh
				else {
					//yao--如果是其他都选,则去掉all的样式
					var sibl = parentLab.parent().siblings();
					var allInput = sibl.find('.J_ajax_arr[data-aval="all"]');
					allInput.removeClass("li-filter-select");

					//yao--如果不允许多选,则把其他激活样式去掉
					if (!fresh.isDult(akey)) {
						//去除label样式
						sibl.find('.li-filter-select').removeClass('li-filter-select');
					}

					parentLab.addClass("li-filter-select");
					fresh.updateOrAdd(akey, aval);

					//yao--更新或者新增块
					addOrUpdatePiece(keyStr, $(this));
				}
			} else {
				if (aval == 'all') {
					return;
				}
				parentLab.removeClass("li-filter-select");
				fresh.removeValue(akey, aval);

				//yao--移除块
				removePiece(keyStr, $(this));
			}
		});

		//filtet筛选框 viewAll点击
		$(".J_filter_all").click(function() {
			$(this).closest(".J_filter").find(".J_filter_li").addClass("li-filter-scroll");
			$(this).parent().hide();
		});

		//分页链接点击
		$(".J_fresh_con").on('click', ".J_fresh_a", function() {
			var url = $(this).attr("href")
			fresh.urlUpdate(url);
			return false;
			//aSearch = new UrlSearch(['brand'], url),
			//		pn=aSearch.get('pn'),
			//		ps=aSearch.get('ps');
			//fresh.hrefUpdate(pn,ps);
		});
		//小方框操作
		function clearPiece(akey) {
			$(".J_piece_" + akey).hide().find("a").remove();
		}

		function removePiece(akey, ele) {
			var aval = $(ele).data('aval');
			aval = "li-pi-" + aval;
			var parent = $("#" + aval).parent();
			;
			$("#" + aval).remove();
			if (parent.find("span").length == 0) {
				parent.hide();
			}
		}

		function addOrUpdatePiece(akey, ele) {
			var parentEle = $(".J_piece_" + akey),
					aval = $(ele).data('aval'),
					text = $(ele).text();
			id = "li-pi-" + aval;
			if (akey == "lp-hp") {
				clearPiece(akey);
			}
			var appendEle = '<a id="' + id +
					'" class="li-piece-a"><span class="li-piece-sp">' + text +
					'</span><i href="javascript:void(0);" data-aval="' + aval +
					'" data-akey="' + akey + '" class="J_clear li-piece-close iconfont fi-close"></i></a>';
			parentEle.append(appendEle);
			parentEle.show();
		}

		//块点击
		$(".J_piece").on('click', ".J_clear", function() {
			var aval = $(this).data("aval"),
					akey = $(this).data("akey");
			$('.J_ajax_arr[data-aval="' + aval + '"][data-akey="' + akey + '"]').trigger("click");
		});

		//yao--页面初始化
		function domInit(data) {
			var lp = data.lp,
					hp = data.hp,
					order = data.order,
					brand = data.brand;
			if (lp != "" || hp != "") {
				var priceArea = lp + "-" + hp;
				var priceEle = $('.J_ajax_arr[data-aval="' + priceArea + '"]');
				priceEle.addClass('li-filter-select');

				//yao--新增price块
				addOrUpdatePiece('lp-hp', priceEle);
			}
			order = order == "" ? "popular_d" : order;
			var orderPrice = $('.J_ajax_str[data-aval="' + order + '"]');
			orderPrice.addClass("li-sort-active");
			if (brand instanceof Array) {
				$.each(brand, function(k, v) {
					var checkEle = $(".J_filter_li").find('.J_ajax_arr[data-aval="' + v + '"]');
					checkEle.addClass("li-filter-select");

					//yao--新增brand块
					addOrUpdatePiece('brand', checkEle);
				});
			}
		}
	});
})