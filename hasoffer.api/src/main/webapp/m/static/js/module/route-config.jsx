/**
 * Created by Administrator on 2015/11/5.
 */
/**
 * Created by Administrator on 2015/11/4.
 */
var Home = require("./../../component/page/Home.jsx"),
    CategoryList = require("./../../component/page/CategoryList.jsx");
var ReactRouter = require('react-router'),
    Router = ReactRouter.Router,
    Route = ReactRouter.Route,
DefaultRoute=ReactRouter.DefaultRoute;
var config=(
    <Route path="/" handler={Home}>
        <Route path="/cat" handler={CategoryList}/>
        <DefaultRoute name="home" handler={Home}></DefaultRoute>
    </Route>
);
module.exports=config