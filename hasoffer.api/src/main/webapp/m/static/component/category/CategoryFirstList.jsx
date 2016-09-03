require("./../../less/category/categoryFirstList.less");
var CategoryFirstList = React.createClass({
	render : function() {
		var List = [];
		var data=this.props.data||[];
		this.props.data.forEach(function(item) {
			var url=A.baseUrl+"category.html?cat="+item.id;
			if(item){
			List.push(
					<div className="col-6">
						<a href={url} className="catf-item">
							<img className="catf-item-img" src={item.mobImageUrl}/>
							<div className="catf-cover">
								<img src={item.mobImageUrl} className="catf-cover-img"/>
								<div className="catf-mask-con">
									<span className="catf-mask">
								{item.name}
									</span>
								</div>
							</div>
						</a>
					</div>);
			}
		});
		return (
				<div className="catf-wrap">
					<div className="row clearfix">
					{List}
					</div>
				</div>
		);
	}
});
module.exports = CategoryFirstList;