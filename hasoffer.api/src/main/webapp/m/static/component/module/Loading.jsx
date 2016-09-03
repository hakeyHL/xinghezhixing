require("./../../less/module/loading.less");
var Loading = React.createClass({
	render:function(){
		return (
		<div className="load">
			<img src="../image/module/loading.gif" alt=""/>
		</div>
		);
	}
});
module.exports=Loading;