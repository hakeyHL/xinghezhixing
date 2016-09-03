require("./../../less/category/categorySecondList.less");
var CategorySecondList = React.createClass({
	render : function() {
		var List = [];
		var data = this.props.data || [];
		data.forEach(function(v) {
			if (v) {
				var url = A.baseUrl + "skulist.html?cat=" + v.id;
				List.push(
						<li>
							<a className="cats-item" href={url}>
								<span className="cats-img">
									<img src={v.mobImageUrl}/>
								</span>
								<span className="cats-name">{v.name}</span>
								<i className="iconfont fi-chevron-right cats-i"></i>
							</a>
						</li>
				);
			}
		})
		return (
				<ul className="cats-list">
				{List}
				</ul>
		)
	}
});
module.exports = CategorySecondList;