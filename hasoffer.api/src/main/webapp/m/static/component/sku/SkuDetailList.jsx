require("./../../less/sku/skuDetailList.less");
var SkuDetailList=React.createClass({
	componentDidMount:function(){
	A.setEllipse(document.getElementsByTagName("h2"),2);
	},
	componentDidUpdate:function(){
		console.log(1);
	},
	render:function(){
		var List=[];
		var data=this.props.data.skus||[];
		data.forEach(function(item){
			if(item) {
				var url = A.baseUrl + "sku.html?item=" + item.id;
				List.push(
						<li>
							<a href={url}>
								<p className="skus-p-img">
									<img src={item.masterImage} alt=""/>
								</p>

								<div className="skus-detail">
									<h2 ref="detailItem">{item.title}</h2>
									<p className="skus-price">
										<small>From</small>
										<strong>
											${item.price}
										</strong>
									</p>
								</div>
							</a>
						</li>
				);
			}
		});
		var classTitle="skus-wrap ";
		return (
				<section>
					<ul className={typeof this.props.data.classTitle=="undefined"?classTitle:classTitle+this.props.data.classTitle} >
					{List}
					</ul>
				</section>
		)
	}
});
module.exports=SkuDetailList;