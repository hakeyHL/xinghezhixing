require.config({
	baseUrl : '/static/js/',
	urlArgs : window.urlArgs || '',
	paths : {
		juicer : 'lib/juicer',
		'jquery.touchswap' : 'lib/jquery.touchswap-1.6.3-min',
		slider : 'module/widget/slider',
		dialog : 'module/widget/dialog',
		'ajaxFresh' : 'module/widget/ajax-fresh',
		'jquery.history' : 'lib/jquery.history',
		'url.search':'module/widget/url-search',
		slip:'module/widget/slip'
	},
	map : {
		'*' : {
			'css' : 'lib/require-css.min'
		}
	},
	shim : {
		juicer : {
			exports : 'juicer'
		},
		'jquery.history' : {
			exports : 'History'
		},
		dialog:['css!../css/module/widget/dialog.css']
	}



});