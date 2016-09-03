require("./../../less/module/head.less");
var Head = React.createClass({
    render: function () {
        return (
            <div className="hd-wrap">
                <div className="hd-search">
                    <a className="iconfont fi-serach"></a>
                    <input type="text" placeholder="Search any product to find lowest price!"/>
                </div>
            </div>
        )
    }
})
module.exports = Head;