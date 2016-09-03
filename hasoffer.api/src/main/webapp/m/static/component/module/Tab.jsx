var Tab = React.createClass({
	getInitialState : function() {
		return {
			//yao--当前元素索引,在touchEnd中改变
			currentIndex : 0
		};
	},
	render : function() {
		var currentIndex = this.state.currentIndex,
				data = this.props.data,
				head = data.head,
				con = data.con,
				_this = this;
		head.currentIndex = currentIndex;
		head.updateState = function(index) {
			_this.setState({currentIndex : index});
		};
		con.currentIndex = currentIndex;
		con.updateState = function(index) {
			_this.setState({currentIndex : index});
		};
		return (
				<div ref="tab">
					<TabHead data={data.head}/>
					<TabCon data={data.con}>
                    {this.props.children}
					</TabCon>
				</div>
		)
	}
});
var TabHead = React.createClass({
	itemClick : function(e) {
		var tag = e.currentTarget.getAttribute("data-tag");
		this.props.data.updateState(tag);
	},
	render : function() {
		var List = [];
		var data = this.props.data;
		if (data instanceof Array) {
			var s = {
				width : 100 / data + '%'
			}
			for (var i = 0; i < data.length; ++i) {
				List.push(
						<li style={s}>
							<a onClick={this.itemClick} data-tag={i} className={this.props.data.currentIndex == i ?
									"sku-nav-item sku-active" : "sku-nav-item"} href="javascript:void(0);">
                            {data[i]}
							</a>
						</li>
				)
			}
		}
		return (
				<section>
					<ul className="sku-nav clearfix">
                    {List}
					</ul>
				</section>
		);
	}
});
var TabCon = React.createClass({
	getDefaultStyle : function(obj, attribute) { // 返回最终样式函数，兼容IE和DOM，设置参数：元素对象、样式特性
		return obj.currentStyle ? obj.currentStyle[attribute] : document.defaultView.getComputedStyle(obj, false)[attribute];
	},
	touchConfig : {
		//yao--元素子元素个数
		eleLen : 0,
		//yao--页面宽度
		screenW : 0,
		//yao--当手指一动的y轴差小于设定值时认为是横向移动
		y : 7,
		//yao--当认为是横向移动 的时候设置标志位,此后屏蔽浏览器垂直滚动功能
		b : false,
		//yao--已经横向移动的距离
		totalX : 0,
		//yao--最小横向移动值,当totalX大于此值是认为是要切换界面,否则还原
		minX : 80,
		//yao--保存css transition-duration值,在initTouch中初始化
		orginDuration : '0ms',
		//yao--touchStart的初始横坐标
		sx : 0,
		//yao--touchEnd的初始纵坐标
		sy : 0,
		//yao--手指滑动的时候元素跟着滑动
		setOffetX : function(ele, ex, index) {
			//如果手指滑动超过屏幕边界,则不进行操作
			if (ex < 0 || ex > this.screenW) {
				return;
			}
			ele.style.transitionDuration = "0ms";
			var w=ex - this.sx - index * this.screenW;
			this.transForm(ele,w);
		},
		//yao--当滑动距离小于初始距离时,还原元素位置
		backState : function(ele, index) {
			this.transForm(ele,0-index*this.screenW);
		},
		transForm : function(ele, w) {
			ele.style.WebkitTransform = "translate(" + w + "px, 0px)";
			ele.style.MozTransform = "translate(" + w + "px, 0px)";
			ele.style.OTransform = "translate(" + w + "px, 0px)";
			ele.style.MsTransform = "translate(" + w + "px, 0px)";
			ele.style.transform = "translate(" + w + "px, 0px)";
		}
	},
	touchStart : function(e) {
		//yao--初始化标志位
		this.touchConfig.b = false;
		this.touchConfig.sx = e.changedTouches[0].pageX;
		this.touchConfig.sy = e.changedTouches[0].pageY;
	},
	touchEnd : function(e) {
		var ex = e.changedTouches[0].pageX,
				minusX = ex > this.touchConfig.sx ? ex - this.touchConfig.sx : this.touchConfig.sx - ex;
		this.refs.tabCon.style.transitionDuration = this.touchConfig.orginDuration;
		//如果横轴偏移量小于初始偏移量,则返回原来状态;反之则进行图片滚动
		if (minusX < this.touchConfig.minX) {
			this.touchConfig.backState(e.currentTarget, this.props.data.currentIndex);
		}
		else {
			var index = this.props.data.currentIndex;
			if (ex > this.touchConfig.sx) {
				//yao--当元素已经在最左侧是,则不进行滑动
				index > 0 ? this.props.data.updateState(--index) : this.touchConfig.backState(e.currentTarget, index);
			}
			else {
				//yao--当元素已经在最右侧时,则不进行滑动
				index < (this.touchConfig.eleLen - 1) ? this.props.data.updateState(++index) :
						this.touchConfig.backState(e.currentTarget, index);
			}

		}
	},
	touchMove : function(e) {
		var ex = e.changedTouches[0].pageX,
				ey = e.changedTouches[0].pageY,
				minusY = ey > this.touchConfig.sy ? ey - this.touchConfig.sy : this.touchConfig.sy - ey;
		//yao--如果纵轴移动距离minusY小于初始设定距离,或者已经设置为横向一动标志位为true,则屏蔽垂直滚动,并且界面横向一动
		if (this.touchConfig.b || minusY < this.touchConfig.y) {
			e.preventDefault();
			this.touchConfig.b = true;
			this.touchConfig.setOffetX(e.currentTarget, ex, this.props.data.currentIndex);
		}
	},
	//yao  设置tabCon高度;
	//isNow如果为true则立即执行,为false延时执行
	setminHeight : function(isNow) {
		var tabEle = this.refs['tabCon'],
				index = this.props.data.currentIndex,
				tmpEles = tabEle.children, eles = [];

		for (var i = 0; i < tmpEles.length; ++i) {
			if (tmpEles[i].nodeType == 1) {
				eles.push(tmpEles[i]);
			}
		}
		//延时执行,当滑动结束以后才设置高度,延时时间为css设置transform时间
		var orgin = parseInt(this.props.data.orginDuration);
		if (isNaN(orgin)) {
			orgin = 350;
		}
		//yao 设置con标签高度,当con标签高度小于剩下部分高度的时候,高度设置为剩下部分高度
		var eleOffset = A.getOffSet(tabEle).top,
				screenHeight = window.screen.height;
		//yao 兼容安卓浏览器,获取实际css高度,通过screen.width的宽度与元素宽度的比例缩放screenHeight
		screenHeight=screenHeight*this.touchConfig.screenW/window.screen.width;
		var minHeight = screenHeight - eleOffset;
		var maxHeight = minHeight > eles[index].offsetHeight ? minHeight : eles[index].offsetHeight;
		var setHeight = function() {
			if (eles.length > index) {
				tabEle.style.height = maxHeight + "px";
			}
			ele = null;
			tabEle = null;
		};
		if (isNow) {
			setHeight();
		}
		else {
			setTimeout(setHeight, orgin);
		}
	},
	componentDidUpdate : function() {
		this.setminHeight(false);
	},
	//yao--初始化参数,在元素第一次touchStart设置
	componentDidMount : function() {
		this.touchConfig.screenW =this.refs['tabCon'].offsetWidth/this.props.data.sum;
		this.touchConfig.eleLen = this.props.data.sum;
		this.touchConfig.orginDuration = this.props.data.orginDuration;
		this.setminHeight(true);
	},
	render : function() {
		var index = this.props.data.currentIndex,
				sum = this.props.data.sum,
				screew = this.touchConfig.screenW;
		var s = {
			transform : "translate(" + (0 - index * screew) + "px, 0px)",
			MsTransform : "translate(" + (0 - index * screew) + "px, 0px)",
			MozTransform : "translate(" + (0 - index * screew) + "px, 0px)",
			WebkitTransform : "translate(" + (0 - index * screew) + "px, 0px)",
			OTransform : "translate(" + (0 - index * screew) + "px, 0px)",
			width : 100 * sum + "%"
		};
		return (
				<div id="tabCon" ref="tabCon" className="page-tab" onTouchStart={this.touchStart} onTouchEnd={this.touchEnd}
						onTouchMove={this.touchMove} style={s}>
                {this.props.children}
				</div>
		)
	}
});
module.exports = Tab;