require("./../../less/category/categorySecondHead.less");
var CategorySecondHead=React.createClass({
	render:function(){
		var data=this.props.data||{};
		return (
				<div className="cat-sh-wrap">
					<img className="cat-sh-img" src={data.imageUrl} alt={data.name}/>
					<div className="cat-sh-cover">
						<p className="cat-sh-mask">
						{data.name}
						</p>
					</div>
				</div>
		)
	}
});
module.exports=CategorySecondHead;