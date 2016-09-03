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

	module.exports = __webpack_require__(10);


/***/ },
/* 1 */,
/* 2 */,
/* 3 */,
/* 4 */,
/* 5 */,
/* 6 */,
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

/***/ },
/* 10 */
/***/ function(module, exports, __webpack_require__) {

	var Home=__webpack_require__(11);
	ReactDOM.render(
			React.createElement(Home, null),
			document.getElementById("app")
	);

/***/ },
/* 11 */
/***/ function(module, exports, __webpack_require__) {

	var Head = __webpack_require__(12),
			Body = __webpack_require__(13),
			Error = __webpack_require__(15),
			Footer = __webpack_require__(16),
			Loading = __webpack_require__(8),
			CategoryFirstList = __webpack_require__(18);
	var Home = React.createClass({displayName: "Home",
		getInitialState : function() {
			return {data : {}};
		},
		componentDidMount : function() {
			var _this = this;
			minAjax(
					{
						url : "category",//request URL
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
			var List;
			var com;
			if (this.state.data.categories) {
				List = (
						React.createElement(CategoryFirstList, {data: this.state.data.categories})
				);
				com = (
						React.createElement("div", null, 
							React.createElement(Head, null), 
							React.createElement(Body, {child: List}), 
							React.createElement(Footer, null)
						)
				);
			}
			else {
				com=React.createElement(Loading, null);
			}
			return com
		}
	});
	module.exports = Home;

/***/ },
/* 12 */
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(7);
	var Head = React.createClass({displayName: "Head",
	    render: function () {
	        return (
	            React.createElement("div", {className: "hd-wrap"}, 
	                React.createElement("div", {className: "hd-search"}, 
	                    React.createElement("a", {className: "iconfont fi-serach"}), 
	                    React.createElement("input", {type: "text", placeholder: "Search any product to find lowest price!"})
	                )
	            )
	        )
	    }
	})
	module.exports = Head;

/***/ },
/* 13 */
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(14);
	var Body = React.createClass({displayName: "Body",
		render : function() {
			var style = {};
			if (this.props.noHead) {
				style.top = "0px";
			}
			if (this.props.noFooter) {
				style.bottom = "0px";
			}
			return (
					React.createElement("div", {className: "bd-wrap", style: style}, 
	            this.props.child
					)
			);
		}
	});
	module.exports = Body;

/***/ },
/* 14 */
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },
/* 15 */
/***/ function(module, exports) {

	var Error = React.createClass({displayName: "Error",
		render: function () {
			return(
					React.createElement("div", null
					)
			)
		}
	});
	module.exports = Error;

/***/ },
/* 16 */
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(17);
	var Footer = React.createClass({displayName: "Footer",
	    render: function () {
	        return (
	            React.createElement("div", {className: "ft-wrap"}, 
	                React.createElement("ul", {className: "ft-tab-con"}, 
	                    React.createElement("li", {className: "ft-tab-item"}, 
	                        React.createElement("a", {className: "ft-tab-a"}, 
	                            React.createElement("span", {className: "ft-sprite ft-sprite-hm"}), 

	                            React.createElement("p", null, "Product")
	                        )
	                    ), 
	                    React.createElement("li", {className: "ft-tab-item"}, 
	                        React.createElement("a", {herf: "/", className: "ft-tab-a"}, 
	                            React.createElement("span", {className: "ft-sprite ft-sprite-deal"}), 

	                            React.createElement("p", null, "Latest Deals")
	                        )
	                    ), 
	                    React.createElement("li", {className: "ft-tab-item"}, 
	                        React.createElement("a", {href: "", className: "ft-tab-a"}, 
	                            React.createElement("span", {className: "ft-sprite ft-sprite-more"}), 

	                            React.createElement("p", null, "More")
	                        )
	                    )
	                )
	            )
	        )
	    }
	});
	module.exports = Footer;

/***/ },
/* 17 */
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },
/* 18 */
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(19);
	var CategoryFirstList = React.createClass({displayName: "CategoryFirstList",
		render : function() {
			var List = [];
			var data=this.props.data||[];
			this.props.data.forEach(function(item) {
				var url=A.baseUrl+"category.html?cat="+item.id;
				if(item){
				List.push(
						React.createElement("div", {className: "col-6"}, 
							React.createElement("a", {href: url, className: "catf-item"}, 
								React.createElement("img", {className: "catf-item-img", src: item.mobImageUrl}), 
								React.createElement("div", {className: "catf-cover"}, 
									React.createElement("img", {src: item.mobImageUrl, className: "catf-cover-img"}), 
									React.createElement("div", {className: "catf-mask-con"}, 
										React.createElement("span", {className: "catf-mask"}, 
									item.name
										)
									)
								)
							)
						));
				}
			});
			return (
					React.createElement("div", {className: "catf-wrap"}, 
						React.createElement("div", {className: "row clearfix"}, 
						List
						)
					)
			);
		}
	});
	module.exports = CategoryFirstList;

/***/ },
/* 19 */
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ }
/******/ ]);