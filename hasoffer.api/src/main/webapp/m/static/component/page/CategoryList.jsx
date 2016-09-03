var CategorySecondList = require("./../category/CategorySecondList.jsx"),
		CategorySecondHead = require("./../category/CategorySecondHead.jsx"),
		HeadWithTitle = require("./../module/HeadWithTitle.jsx"),
		Loading = require("./../module/Loading.jsx");
var CategoryList = React.createClass({
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
					<div>
						<HeadWithTitle data={{noTitle : true}}/>
						<CategorySecondHead data={this.state.data.category}/>
						<CategorySecondList data={this.state.data.category.subCategories}/>
					</div>
			);
		}
		else{
			com=<Loading />;
		}
		return com;
	}
});
module.exports = CategoryList;