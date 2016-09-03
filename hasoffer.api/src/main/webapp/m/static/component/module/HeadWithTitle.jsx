require("./../../less/module/head.less");
var HeadWithTitle = React.createClass({
	onBack : function() {
		history.back();
	},
	render : function() {
		var wrapClass = "hd-title-wrap",
				titleDom = "";
		if (typeof this.props.data != "undefined") {
			if (this.props.data.noTitle) {
				wrapClass += " hd-opacity";
			}
			if (this.props.data.title) {
				titleDom = (
						<div className="hd-title">
						{this.props.data.title}
						</div>
				);
			}
		}
		return (
				<div className={wrapClass}>
					<div className="hd-head">
						<a onTouchStart={this.onBack} className="hd-back">
							<i className="iconfont fi-back"></i>
						</a>
					</div>
				{titleDom}
				</div>
		)
	}
});
module.exports = HeadWithTitle;