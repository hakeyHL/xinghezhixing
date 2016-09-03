/**
 * Created by Administrator on 2015/12/9.
 */
COMP = {
	BASE_URL : "http://price.allbuy.com"
};
(function(w) {
	w = w || {};
	var eleObj = {};
	//doc元素对象初始化,解决不同网站获取方式不一样问题
	(function eleObjInit(obj) {

	})(eleObj);
	var tmplPrice = '<ul class="compare-plugin-list">\
						<li>\
						<span class="compare-plugin-sprite compare-plugin-logo"></span>\
						</li>\
						{{if historyPrice}}\
						<li class="compare-plugin-item">\
							<a class="compare-plugin-item-a" href="javascript:void(0);" {{if !hasLower}}style="cursor:default;"{{/if}}>\
							<span class="compare-plugin-sprite compare-plugin-history-icon"></span>\
							<span>Drop Price</span>\
							</a>\
							<div class="compare-plugin-history compare-plugin-hover">\
								<p class="compare-plugin-history-title">Price history</p>\
								<div id="compare-plugin-history" style="display:none;" class="compare-plugin-history-price">\
									<span class="compare-plugin-arrow-down"></span>\
									<div id="compare-plugin-price-contain" class="contain">\
									</div>\
								</div>\
								<canvas id="compare-plugin-arc" width="460" height="190"></canvas>\
								<canvas id="price-axes" width="460" height="190"></canvas>\
							</div>\
						</li>\
						{{/if}}\
						{{if sameSkus}}\
						<li class="compare-plugin-item  {{if historyPrice}}compare-plugin-has-history{{/if}}">\
							<a class="compare-plugin-item-a" href="{{lowestSku.url}}" target="_blank" {{if !hasLower}}style="cursor:default;"{{/if}}>\
							<span class="compare-plugin-sprite compare-plugin-price-icon"></span>\
							{{if hasLower}}\
							<span>Lower Price:&nbsp;</span>\
							<strong class="compare-plugin-price-title">${{lowestSku.currentPrice}}</strong>\
							{{else}}\
							<span>other <strong class="compare-plugin-price-title">{{sameSkus.length}}</strong> Store Prices</span>\
							{{/if}}</a>\
							<ul class="compare-plugin-price-list compare-plugin-hover">\
								{{each sameSkus}}\
								{{if $index<10}}\
								<li {{if $index%2==0}} class="compare-plugin-odd"{{/if}}>\
									<a target="_blank" href="{{$value.url}}" class="compare-plugin-price-item">\
										<span class="compare-plugin-key">{{$value.website}}</span>\
										<span class="compare-plugin-value" {{if $index==0}} style="color:#f60;"{{/if}}>${{$value.currentPrice}}</span>\
									</a>\
								</li>\
								{{/if}}\
								{{/each}}\
								<li class="compare-plugin-last">\
									{{if sameSkus.length>9}}\
									<a target="_blank" href="{{abUrl}}" class="compare-plugin-more">More price<span class="compare-plugin-sprite compare-plugin-more-icon"></span></a>\
									{{/if}}\
								</li>\
							</ul>\
						</li>\
						{{/if}}\
					</ul>';
	//yao  图片插件
	var chart = function(ctx, x, y, historyData,currentPrice) {
		historyData.maxPrice=historyData.maxPrice>currentPrice?historyData.maxPrice:currentPrice;
		historyData.minPrice=historyData.minPrice<currentPrice?historyData.minPrice:currentPrice;
		this.ctxArc = document.getElementById('compare-plugin-arc').getContext('2d');
		this.ctx = ctx;
		this.x = x;
		this.y = y;
		//xy标注
		var num = 4;
		var mod1 = 1,
				mod2 = 1;
		//姚 将最低价格最高价格前两位取整
		var maxPrice = historyData.maxPrice,
				minPrice = historyData.minPrice;
		while (maxPrice / mod1 >= 100) {
			mod1 *= 10;
		}
		while (minPrice / mod2 >= 100) {
			mod2 *= 10;
		}
		var minest = Math.floor(minPrice / mod2) * mod2;
		var d = Math.ceil((Math.ceil(maxPrice / mod1) * mod1 - minest) / num);
		//如果d小于10则直接按d去跨度
		if (d > 10) {
			//如果大于10则要求d的前两位必须为5的倍数
			var mod = 1;
			while (d / mod > 100) {
				mod *= 10;
			}
			d = Math.ceil(d / mod / 5) * 5 * mod;
		}
		this.ctx.fillStyle = "#666";
		this.ctx.textAlign = "right";
		this.fillText("$" + minest, -10, 0);
		for (var i = 1; i <= num; ++i) {
			var row = 40;
			this.fillText("$" + (minest + d * i), -10, i * row - 5);
		}
		var piexesEveryPrice = 160 / d / num;
		var piexesEveryDay = 365 / this.getDays(historyData.curTime, historyData.startTime);
		this.maxPriceY = (historyData.maxPrice - minest) * piexesEveryPrice;
		this.minPriceY = (historyData.minPrice - minest) * piexesEveryPrice;
		this.currentPriceY = (currentPrice - minest) * piexesEveryPrice;
		this.maxPrice = historyData.maxPrice;
		this.minPrice = historyData.minPrice;
		this.currentPrice = currentPrice;
		this.startTime = historyData.startTime;
		this.ctx.textAlign = "center";
		var timeArr = [0, 61, 122, 183, 244, 305, 365];
		for (var i=0;i<timeArr.length;++i) {
			var date = this.getAddDate(historyData.startTime, timeArr[i]/piexesEveryDay);
			this.fillText(date, timeArr[i], -17);
		}
		//yao 将历史价格列表放到对象中
		this.data = [];
		for (var i=0;i<historyData.list.length;++i) {
			var o = {};
			o.price = historyData.list[i].price;
			o.time = this.getDate(historyData.list[i].time);
			o.y = (parseFloat(o.price) - minest) * piexesEveryPrice;
			o.x = this.getDays(historyData.startTime, historyData.list[i].time) * piexesEveryDay;
			this.data.push(o);
		}


		var o = {
			price : currentPrice,
			time : this.getDate(historyData.curTime),
			y : (currentPrice - minest) * piexesEveryPrice,
			x : this.getDays(historyData.startTime, historyData.curTime) * piexesEveryDay
		};
		//yao 如果历史价格列表的最后一条数据不是当前日期的数据,则把当前价格数据放到对象中
		if(this.getDays(historyData.curTime,historyData.list[historyData.list.length-1].time)!=0){
			this.data.push(o);
		}
		//yao 如果历史价格列表的最后一条数据是当前日期的数据,则把当最后一条数据改成当前价格数据
		else{
			this.data[this.data.length-1]=o;
		}
	};
	chart.prototype = {
		getDate : function(str) {
			var d = new Date(str);
			var month = d.getMonth() + 1;
			var day = d.getDate();
			var year = d.getFullYear();
			month = month < 10 ? "0" + month : month;
			day = day < 10 ? "0" + day : day;
			return year + "-" + month + "-" + day;
		},
		getAddDate : function(str, days) {
			str = new Date(str).getTime();
			str = parseInt(str + days * 1000 * 60 * 60 * 24);
			var d = new Date(str);
			var month = d.getMonth() + 1;
			var day = d.getDate();
			month = month < 10 ? "0" + month : month;
			day = day < 10 ? "0" + day : day;
			return month + "-" + day;
		},
		getDays : function(strDateStart, strDateEnd) {
			var strDateS = new Date(strDateStart);
			var strDateE = new Date(strDateEnd);
			iDays = parseInt(Math.abs(strDateS - strDateE) / 1000 / 60 / 60 / 24);
			return iDays;
		},
		lineTo : function(x1, y1, x2, y2) {
			this.ctx.moveTo(this.x + x1, this.y - y1);
			this.ctx.lineTo(this.x + x2, this.y - y2);
		},
		lineWithBack : function(x1, y1, x2, y2) {
			this.ctx.moveTo(this.x + x1, this.y - y1);
			this.ctx.lineTo(this.x + x2, this.y - y2);
			this.ctx.lineTo(this.x + x1, this.y - y1);
		},
		virtualLine : function(x1, y1, x2, y2, w, space) {
			var deg = Math.atan((y2 - y1) / (x2 - x1));
			wx = w * Math.cos(deg),
					wy = w * Math.sin(deg),
					spaceX = space * Math.cos(deg),
					spaceY = space * Math.sin(deg);
			while (x1 < x2 || y1 < y2) {
				this.ctx.moveTo(this.x + x1, this.y - y1);
				this.ctx.lineTo(this.x + x1 + wx, this.y - y1 + wy);
				x1 += wx + spaceX;
				y1 += wy + spaceY;
				x1 = x1 > x2 ? x2 : x1;
				y1 = y1 > y2 ? y2 : y1;
			}
		},
		stroke : function() {
			this.ctx.stroke();
		},
		fillText : function(text, x, y) {
			this.ctx.fillText(text, this.x + x, this.y - y);
		},
		drawPrice : function() {
			var data = this.data;
			for (var i=0;i<data.length;++i) {
				if (i > 0) {
					this.ctx.lineTo(this.x + data[i].x - 1, this.y - data[i - 1].y);
				} else {
					if (data[i].x > 0) {
						this.virtualLine(0, data[i].y, data[i].x, data[i].y, 3, 3);
					}
				}
				this.ctx.lineTo(this.x + data[i].x, this.y - data[i].y);
			}
		},
		drawVirtualLine : function() {
			this.ctx.textAlign = "left";
			this.virtualLine(0, this.minPriceY, 365, this.minPriceY, 3, 3);
			this.fillText("$" + this.minPrice, 370, this.minPriceY - 5);
			this.virtualLine(0, this.maxPriceY, 365, this.maxPriceY, 3, 3);
			this.fillText("$" + this.maxPrice, 370, this.maxPriceY - 5);
			this.virtualLine(0, this.currentPriceY, 365, this.currentPriceY, 5, 5);
			this.fillText("$" + this.currentPrice, 370, this.currentPriceY - 5);

		},
		drawAxes : function() {
			this.lineTo(0, 0, 0, 160);
			this.lineTo(0, 0, 365, 0);
			this.lineTo(365, 0, 365, 160);
			this.lineTo(0, 40, 365, 40);
			this.lineTo(0, 80, 365, 80);
			this.lineTo(0, 120, 365, 120);
			this.lineTo(0, 160, 365, 160);
			this.lineTo(61, 0, 61, 160);
			this.lineTo(122, 0, 122, 160);
			this.lineTo(183, 0, 183, 160);
			this.lineTo(244, 0, 244, 160);
			this.lineTo(305, 0, 305, 160);
		},
		getNearestData : function(x) {
			x = this.getAxesX(x);
			var data = this.data,
					nearest,
					minDistance = 9999;
			for (var i=0;i<data.length;++i) {
				if (Math.abs(x - data[i].x) < minDistance) {
					minDistance = Math.abs(x - data[i].x);
					nearest = data[i];
				}
			}
			return nearest;
		},
		getAxesX : function(x) {
			return x - this.x;
		},
		getAbsolutePos : function(data) {
			var pos = {x : 0, y : 0};
			pos.x = this.x + data.x;
			pos.y = this.y - data.y;
			return pos;
		},
		drawArc : function(data) {
			this.ctxArc.clearRect(0, 0, 800, 800);
			this.ctxArc.beginPath();
			this.ctxArc.strokeStyle = "#fff";
			this.ctxArc.arc(this.x + data.x + 1, this.y - data.y, 4, 0, 2 * Math.PI);
			this.ctxArc.fillStyle = "#f60";
			this.ctxArc.fill();
			this.ctxArc.stroke();
		},
		clearArc : function() {
			this.ctxArc.clearRect(0, 0, 800, 800);
		}
	}
	var drawHistoryPrice = function(price,currentPrice,wid) {
		var everyW=1;
		var canvas = document.getElementById("price-axes");
		var ctx = canvas.getContext('2d');
		var draw = new chart(ctx, 40, 170, price,currentPrice);

		//缩放
		//ctx.scale(wid/365,1);
//画线
		ctx.save();
		ctx.translate(0.5, 0.5);
		ctx.beginPath();
//坐标线
		draw.drawAxes();
//虚线
		draw.drawVirtualLine();
		ctx.lineWidth = everyW;
		ctx.strokeStyle = "#eaeaea";
		draw.stroke();
		ctx.restore();

//折线图
		ctx.beginPath();
		ctx.strokeStyle = "#f60";
		ctx.lineWidth = 2*everyW;
		draw.drawPrice();
		ctx.stroke();
		var historyPrice = document.getElementById("compare-plugin-history");
		var historyPriceContainer = document.getElementById('compare-plugin-price-contain');
		var setPricePos = function(e) {
			var data = draw.getNearestData(e.clientX - canvas.getBoundingClientRect().left);
			var pos = draw.getAbsolutePos(data);
			draw.drawArc(data);
			var text = data.time + " $" + data.price;
			historyPriceContainer.innerHTML = text;
			historyPrice.style.marginLeft = (pos.x - 10)+"px";
			historyPrice.style.marginTop = (pos.y + 17)+"px";
			historyPrice.style.display = "block";
		};
		var hidePrice = function() {
			draw.clearArc();
			historyPrice.style.display = "none";
		}
		var canvasRound = document.getElementById('compare-plugin-arc');

		if (canvasRound.addEventListener) {
			canvasRound.addEventListener('mousemove', setPricePos);
			canvasRound.addEventListener('mouseout', hidePrice);
		} else {
			canvasRound.attachEvent('onmousemove', setPricePos);
			canvasRound.addEventListener('onmouseout', hidePrice);
		}
	}
	w.test = function(data) {
		console.log(data);
		var insertEle = document.createElement("div");
		insertEle.className = "compare-plugin compare-plugin-price-wrap";
		var render = template.compile(tmplPrice);
		//yao--如果有比价数据或者历史价格则显示插件content
		if ((typeof data.sameSkus != "undefined" && data.sameSkus.length > 0) || typeof data.historyPrice != "undefined") {
			try {
				//yao 有比价数据
				if (typeof data.sameSkus != "undefined" && data.sameSkus.length > 0) {
					//yao--设置最低价,当前最低价为数组第一项
					data.lowestSku = data.sameSkus[0];
					//yao--设置网站链接
					data.abUrl = w.BASE_URL + data.abUrl;

					var configArr = data.pluginConf.split('@@@');
					//yao--获取要元素要插入的位置(在此之后插入)
					var ele = eval(configArr[0]);
					//yao--判断当前网站是否是最低价
					var price = parseFloat(eval(configArr[1]));
					data.hasLower = isNaN(price) || price >= parseFloat(data.sameSkus[0].currentPrice);
				}

				//yao--渲染模板
				insertEle.innerHTML = render(data);
				//yao--插入元素
				if (ele) {
					ele.nextSibling ? ele.parentNode.insertBefore(insertEle, ele.nextSibling) : ele.parentNode.appendChild(insertEle);
				}
			} catch (e) {
				console.log('get element failed.')
			}
			//yao--有历史价格,canvas画图
			if (typeof data.historyPrice != "undefined") {
				drawHistoryPrice(data.historyPrice,price,305);
			}
		}
	};
	function loadJs(src) {
		var s = document.createElement("script");
		s.src = src;
		document.body.appendChild(s);
	}

	loadJs(w.BASE_URL + "/sameJp?srcUrl=" + location.href + "&callback=COMP.test");


	function $(selector) {
		var arr = selector.split(" ");
		for (var i=0;i<arr.length;++i) {
			console.log(arr[i]);
		}
	}
})(COMP);

