var Tab = require("./../module/Tab.jsx"),
		SkuDetail = require("./../sku/SkuDetail.jsx"),
		Error = require("./../module/Error.jsx"),
		Loading = require("./../module/Loading.jsx"),
		HeadWithTitle = require("./../module/HeadWithTitle.jsx"),
		SkuExpandHead = require("./../sku/SkuExpandHead.jsx"),
		SkuExpandList = require("./../sku/SkuExpandList.jsx"),
		SkuExpandDes = require("./../sku/SkuExpandDes.jsx");

var Sku = React.createClass({
	getInitialState : function() {
		return {data : true};
	},
	componentDidMount : function() {
		var _this = this;
		var cat = new UrlSearch().get("item");
		var url = "item/" + cat;
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
		var com = <Loading />;
		if (this.state.data) {
			if (this.state.data.product) {
				var tabData = {
					head:['Online Price','Specs'],
					con:{
						sum: 2,
						orginDuration: "350ms"
					}
				};
				var childS = {
					width: 100 / 2 + "%"
				};
				com = (
						<div>
							<HeadWithTitle data={{title : "Detail"}}/>
							<SkuDetail  data={{
								sku : this.state.data.sku,
								lowestPriceSku : this.state.data.lowestPriceSku,
								classTitle : 'has-title'
							}}/>
							<Tab data={tabData}>
								<SkuExpandList data={{comparedSkus : this.state.data.comparedSkus}} style={childS}/>
								<SkuExpandDes data={{sku : this.state.data.sku}} style={childS}/>
							</Tab>
						</div>
				);
			}
		} else {
			com = <Error />
		}
		return com;
	}
});
module.exports = Sku;