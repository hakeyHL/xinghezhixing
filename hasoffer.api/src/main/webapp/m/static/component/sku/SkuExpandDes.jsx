require("./../../less/sku/skuExpandHead.less");
var SkuExpandDes = React.createClass({
	render : function() {
		var List = [];
		this.props.data.sku.basicAttributes.forEach(function(item) {
			if(item) {
				List.push(
						<tr>
							<td>
								<span>Service Provider</span>
							</td>
							<td className="tr">
								<strong>not specified</strong>
							</td>
						</tr>
				);
			}
		});
		return (
				<div className="sku-desc-tbl" style={this.props.style}>
					<table>
						<thead>
							<tr>
								<th className="lab"></th>
								<th className="value"></th>
							</tr>
						</thead>
						<tbody>
						{List}
						</tbody>
					</table>
					<div className="sku-desc-div">
					{this.props.data.sku.desc}
					</div>
				</div>
		)
	}
});
module.exports = SkuExpandDes;
