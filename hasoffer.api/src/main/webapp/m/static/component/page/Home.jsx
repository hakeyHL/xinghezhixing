var Head = require("./../module/Head.jsx"),
		Body = require("./../module/Body.jsx"),
		Error = require("./../module/Error.jsx"),
		Footer = require("./../module/Footer.jsx"),
		Loading = require("./../module/Loading.jsx"),
		CategoryFirstList = require("./../category/CategoryFirstList.jsx");
var Home = React.createClass({
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
					<CategoryFirstList data={this.state.data.categories}/>
			);
			com = (
					<div>
						<Head />
						<Body child={List} />
						<Footer />
					</div>
			);
		}
		else {
			com=<Loading />;
		}
		return com
	}
});
module.exports = Home;