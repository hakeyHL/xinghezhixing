require("./../../less/sku/skuExpandList.less");
var SkuExpandList = React.createClass({
	render : function() {
		var List=[];
		this.props.data.comparedSkus.forEach(function(item){
			if(item) {
				List.push(
						<li>
							<div className="sku-item-tbl">
								<span className="sku-item-cell">
									<div className="sku-item-img">
										<img src={item.masterImageUrl} alt={item.siteName}/>
									</div>
								</span>
								<span className="sku-item-cell sku-item-price">
									US ${item.price}
								</span>
								<span className="sku-item-cell">
									<a className="sku-item-a" href={item.url}>
										<span className="mr15">Shop Now</span>
										<i className="sku-a-icon sku-sprite"></i>
									</a>
								</span>
							</div>
						</li>
				);
			}
		});
		return (
				<div style={this.props.style}>
					<ul className="sku-li">
					{List}
					</ul>
				</div>
		);
	}
});
module.exports=SkuExpandList;