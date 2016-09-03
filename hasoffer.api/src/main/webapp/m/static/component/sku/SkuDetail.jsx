require("./../../less/sku/skuDetail.less")
var SkuDetail = React.createClass({
	componentDidMount:function(){
		A.setEllipse(document.getElementsByTagName("h1"),2);
	},
	render:function(){
		var classTitle="sku-wrap ";
		return (
				<section className={typeof this.props.data.classTitle=="undefined"?classTitle:classTitle+this.props.data.classTitle} >
					<p className="sku-p-img">
						<img src={this.props.data.sku.masterImage} alt=""/>
					</p>

					<div className="sku-detail">
						<h1>{this.props.data.sku.title}</h1>

						<p className="sku-price">
							<small>From</small>
							<strong>
								${this.props.data.lowestPriceSku.price}
							</strong>
						</p>
					</div>
				</section>
		)
	}
});
module.exports=SkuDetail;