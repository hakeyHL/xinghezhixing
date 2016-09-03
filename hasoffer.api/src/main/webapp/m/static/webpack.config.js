var webpack = require('webpack');
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var jsPath=__dirname + '/js/app/';
var cssPath=__dirname + '/css/';
var lessPath=__dirname + '/less/';
var imagePath=__dirname + '/image/';
var tplPath=__dirname + '/tpl/';
var componentPath=__dirname + '/component/';
var reactjsPath=__dirname + '/reactjs/';
var outputPublicPath = 'http://localhost:3000/scripts/';

var resolveCommon = {
    // Allow to omit extensions when requiring these files
    extensions: ['', '.js', '.jsx']
};

var moduleCommon = {
    loaders: [
        // Pass *.css files through css-loader and style-loader transforms
        {
            test: /\.css$/,
            loader: ExtractTextPlugin.extract("style-loader", "css-loader")
        },
        // Optionally extract less files
        // or any other compile-to-css language
        {
            test: /\.less$/,
            loader: ExtractTextPlugin.extract("style-loader", "css-loader!less-loader")
        },
        // Pass *.jsx files through jsx-loader transform
        { test: /\.jsx$/, loader: 'jsx-loader?harmony' },

        {test: /\.html$/, loader: 'raw'}
    ]
};

module.exports = [
    {

        // Entry point for static analyzer:
        entry: {
            home: [
                reactjsPath+"home/index.js"
            ],
            category:reactjsPath+"category/index.js",
            sku:reactjsPath+"sku/index.js",
            skuList:reactjsPath+"skuList/index.js"

        },

        output: {
            // Where to put build results when doing production builds:
            path: jsPath,
            chunkFilename: "[id].js",
            // JS filename you're going to use in HTML
            filename: '[name].js'
        },

        plugins: [
            new ExtractTextPlugin("../../css/app/[name].css", {
                allChunks: true
            })
        ],

        resolve: resolveCommon,

        module: moduleCommon
    }
];
