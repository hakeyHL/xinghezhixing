require("./../../less/sku/skuExpandHead.less");
var SkuExpandHead = React.createClass({
	itemClick:function(e){
		timeCount.start();
		var tag=e.currentTarget.getAttribute("data-tag");
		this.props.data.updateState(tag);
	},
	render : function() {
		return (
				<section>
					<ul className="sku-nav clearfix">
						<li>
							<a onClick={this.itemClick} data-tag="0" className={this.props.data.currentIndex==0?"sku-nav-item sku-active":"sku-nav-item"} href="javascript:void(0);">
								Online Price
							</a>
						</li>
						<li>
							<a onClick={this.itemClick} data-tag="1" className={this.props.data.currentIndex==1?"sku-nav-item sku-active":"sku-nav-item"} href="javascript:void(0);">
								Specs
							</a>
						</li>
					</ul>
				</section>
		);
	}
});
module.exports=SkuExpandHead;