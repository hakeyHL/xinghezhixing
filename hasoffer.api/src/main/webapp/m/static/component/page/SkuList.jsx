var SkuDetailList = require("./../sku/SkuDetailList.jsx"),
		Error = require("./../module/Error.jsx"),
		Loading = require("./../module/Loading.jsx"),
		HeadWithTitle = require("./../module/HeadWithTitle.jsx");
var SkuList = React.createClass({
	getInitialState : function() {
		return {data : true};
	},
	componentDidMount : function() {
		var _this = this;
		var cat = new UrlSearch().get("cat");
		var url = "search";
		minAjax(
				{
					url : url,//request URL
					type : "GET",//Request type GET/POST
					data:{cat:cat},
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
		var com = <Loading />;
		if (this.state.data) {
			if (this.state.data.categoryFacet) {
				com = (
						<div>
							<HeadWithTitle data={{title : this.state.data.categoryFacet[0].secondCatQtys[0].name}}/>
							<SkuDetailList data={{skus:this.state.data.pageableSkus.data,classTitle:'has-title'}}/>
						</div>
				);
			}
		} else {
			com = <Error />
		}
		return com;
	}
});
module.exports=SkuList;