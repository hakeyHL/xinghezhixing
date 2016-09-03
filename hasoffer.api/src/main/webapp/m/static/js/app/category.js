/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};

/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {

/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;

/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};

/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);

/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;

/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}


/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;

/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;

/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";

/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ function(module, exports, __webpack_require__) {

	/**
	 * Created by Administrator on 2015/11/7.
	 */
	var CategoryList=__webpack_require__(1);
	ReactDOM.render(
			React.createElement(CategoryList, null),
			document.getElementById("app")
	);

/***/ },
/* 1 */
/***/ function(module, exports, __webpack_require__) {

	var CategorySecondList = __webpack_require__(2),
			CategorySecondHead = __webpack_require__(4),
			HeadWithTitle = __webpack_require__(6),
			Loading = __webpack_require__(8);
	var CategoryList = React.createClass({displayName: "CategoryList",
		getInitialState : function() {
			return {data : false};
		},
		componentDidMount : function() {
			var _this = this;
			var cat = new UrlSearch().get("cat");
			var url = "category/" + cat;
			minAjax(
					{
						url : url,//request URL
						type : "GET",//Request type GET/POST
						head : {
							key : "Accept", val : "application/json"
						},
						//CALLBACK FUNCTION with RESPONSE as argument
						success : function(data) {
							_this.setState({data : data});
						},
						error : function() {
							_this.setState({data : false});
						}
					}
			);
		},
		render : function() {
			var com;
			if(this.state.data){
				com=(
						React.createElement("div", null, 
							React.createElement(HeadWithTitle, {data: {noTitle : true}}), 
							React.createElement(CategorySecondHead, {data: this.state.data.category}), 
							React.createElement(CategorySecondList, {data: this.state.data.category.subCategories})
						)
				);
			}
			else{
				com=React.createElement(Loading, null);
			}
			return com;
		}
	});
	module.exports = CategoryList;

/***/ },
/* 2 */
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(3);
	var CategorySecondList = React.createClass({displayName: "CategorySecondList",
		render : function() {
			var List = [];
			var data = this.props.data || [];
			data.forEach(function(v) {
				if (v) {
					var url = A.baseUrl + "skulist.html?cat=" + v.id;
					List.push(
							React.createElement("li", null, 
								React.createElement("a", {className: "cats-item", href: url}, 
									React.createElement("span", {className: "cats-img"}, 
										React.createElement("img", {src: v.mobImageUrl})
									), 
									React.createElement("span", {className: "cats-name"}, v.name), 
									React.createElement("i", {className: "iconfont fi-chevron-right cats-i"})
								)
							)
					);
				}
			})
			return (
					React.createElement("ul", {className: "cats-list"}, 
					List
					)
			)
		}
	});
	module.exports = CategorySecondList;

/***/ },
/* 3 */
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },
/* 4 */
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(5);
	var CategorySecondHead=React.createClass({displayName: "CategorySecondHead",
		render:function(){
			var data=this.props.data||{};
			return (
					React.createElement("div", {className: "cat-sh-wrap"}, 
						React.createElement("img", {className: "cat-sh-img", src: data.imageUrl, alt: data.name}), 
						React.createElement("div", {className: "cat-sh-cover"}, 
							React.createElement("p", {className: "cat-sh-mask"}, 
							data.name
							)
						)
					)
			)
		}
	});
	module.exports=CategorySecondHead;

/***/ },
/* 5 */
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },
/* 6 */
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(7);
	var HeadWithTitle = React.createClass({displayName: "HeadWithTitle",
		onBack : function() {
			history.back();
		},
		render : function() {
			var wrapClass = "hd-title-wrap",
					titleDom = "";
			if (typeof this.props.data != "undefined") {
				if (this.props.data.noTitle) {
					wrapClass += " hd-opacity";
				}
				if (this.props.data.title) {
					titleDom = (
							React.createElement("div", {className: "hd-title"}, 
							this.props.data.title
							)
					);
				}
			}
			return (
					React.createElement("div", {className: wrapClass}, 
						React.createElement("div", {className: "hd-head"}, 
							React.createElement("a", {onTouchStart: this.onBack, className: "hd-back"}, 
								React.createElement("i", {className: "iconfont fi-back"})
							)
						), 
					titleDom
					)
			)
		}
	});
	module.exports = HeadWithTitle;

/***/ },
/* 7 */
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },
/* 8 */
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(9);
	var Loading = React.createClass({displayName: "Loading",
		render:function(){
			return (
			React.createElement("div", {className: "load"}, 
				React.createElement("img", {src: "../image/module/loading.gif", alt: ""})
			)
			);
		}
	});
	module.exports=Loading;

/***/ },
/* 9 */
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ }
/******/ ]);