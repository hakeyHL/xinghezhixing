require("./../../less/module/body.less");
var Body = React.createClass({
	render : function() {
		var style = {};
		if (this.props.noHead) {
			style.top = "0px";
		}
		if (this.props.noFooter) {
			style.bottom = "0px";
		}
		return (
				<div className="bd-wrap" style={style}>
            {this.props.child}
				</div>
		);
	}
});
module.exports = Body;