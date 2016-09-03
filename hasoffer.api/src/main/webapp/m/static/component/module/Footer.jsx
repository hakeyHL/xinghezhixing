require("./../../less/module/footer.less");
var Footer = React.createClass({
    render: function () {
        return (
            <div className="ft-wrap">
                <ul className="ft-tab-con">
                    <li className="ft-tab-item">
                        <a  className="ft-tab-a">
                            <span className="ft-sprite ft-sprite-hm"></span>

                            <p>Product</p>
                        </a>
                    </li>
                    <li className="ft-tab-item">
                        <a herf="/" className="ft-tab-a">
                            <span className="ft-sprite ft-sprite-deal"></span>

                            <p>Latest Deals</p>
                        </a>
                    </li>
                    <li className="ft-tab-item">
                        <a href="" className="ft-tab-a">
                            <span className="ft-sprite ft-sprite-more"></span>

                            <p>More</p>
                        </a>
                    </li>
                </ul>
            </div>
        )
    }
});
module.exports = Footer;