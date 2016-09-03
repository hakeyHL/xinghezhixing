/*
 Syntax:
 var slide = new Slide(element,[,options]);
 options:
 data : [{
 src : '/v2/images/file/index_focus.jpg',
 title : '泛欧西',
 text : '比利时/意大利流行天后 Lara Fabian 新作《Deux Ils, Deux Elles》。<br>Lara Fabian：1970年1月9日出生于比利时-埃尔泰贝克，比利时/意大利国际流行天后，同时拥有加拿大国籍。主要演唱法语、意大利语和英语歌曲，但她还会用西班牙语、葡萄牙语、俄语、希伯来语、希腊语和德语演唱。'
 }],
 effect : 'fade'/'slide'
 */
define(function (require, exports, module) {
    require('jquery.touchswap');
    var juicer=require('juicer');

    var View = Backbone.View.extend({
        options: {
            effect: 'fade',
            duration: 7000
        },
        events: {
            "click .J_toggle": "toggle",
            "click .J_goto": "goto",
            "mouseenter": "pause",
            "mouseleave": "play"
        },
        initialize: function (options) {
            $.extend(this.options, options);
            var context = this;
            this.render(options.length);
            this.current = 0;
            //this.show(this.current);
            this.$el.swipe({
                //Generic swipe handler for all directions
                swipe: function (event, direction, distance, duration, fingerCount) {
                    if (direction == "right") {
                        context.go('prev');
                    } else {
                        context.go('next');
                    }
                },
                excludedElements: [],
                fingers: 'all'
            });
            this.play();
        },
        render: function (length) {
            this.count = length;
            var data = [];
            for (var i = 0; i < length; i++) {
                data.push(i);
            }
            this.$el.append(juicer.to_html(this.tpl, {items: data}));
            var $nav = this.$el.find('.J_nav');
            this.$item = this.$el.find('li');
            this.$pointer = $nav.find('a');
            if (this.options.effect == "slide") {
                this.slideInit();
            }
        },
        tpl: [
            '<a href="javascript:;" class="nav-next J_toggle" data-direction="next"><i class="ico ico-next">Next</i></a>',
            '<a href="javascript:;" class="nav-prev J_toggle" data-direction="prev"><i class="ico ico-prev">Prev</i></a>',
            '<div class="nav J_nav">',
            '{@each items as item,index}',
            '<a href="javascript:;" class="item ib J_goto{@if index==0} active{@/if}" data-index="{{index}}"><i class="ib"></i></a>',
            '{@/each}',
            '</div>'].join(''),
        toggle: function (e) {
            var direction = $(e.currentTarget).data().direction;
            this.go(direction);
        },
        go: function (direction) {
            if (direction == 'prev') {
                var index = this.current - 1;
                if (index < 0) {
                    index = this.count - 1;
                }
            } else {
                index = this.current + 1;
                if (index >= this.count) {
                    index = 0;
                }
            }
            this.show(index, direction);
        },
        goto: function (e) {
            var index = $(e.currentTarget).data().index;
            if (this.current != index) {
                this.show(index, this.current > index ? 'prev' : 'next');
            }
        },
        auto: function () {
            if (this.count > 1) {
                var context = this;
                clearTimeout(this._t);
                if (!this.isPause) {
                    this._t = setTimeout(function () {
                        context.go('next');
                    }, this.options.duration);
                }
            }
        },
        show: function (index, direction) {
            var $prevImg = $(this.$item[this.current]), $nextImg = $(this.$item[index]);
            if (this.options.effect == "fade") {
                this.fade($prevImg, $nextImg);
            } else if (this.options.effect == "slide") {
                if (direction) {
                    this.slide($prevImg, $nextImg, direction);
                } else {
                    $nextImg.show();
                    this.onShowNext($nextImg);
                }
            }
            $(this.$pointer[this.current]).removeClass('active');
            $(this.$pointer[index]).addClass('active');
            this.current = index;
        },
        fade: function ($prevImg, $nextImg) {
            var context = this;
            $prevImg.stop(true, true).fadeOut(600, function () {
                context.onHidePrev($prevImg);
            });

            $nextImg.stop(true, true).fadeIn(600, function () {
                context.onShowNext($(this));
            });
        },
        slide: function ($prevImg, $nextImg, direction) {
            var context = this, $clone = $nextImg.clone().show(), toward;
            if (direction == 'next') {
                $prevImg.after($clone);
                toward = -this.imgWidth;

            } else if (direction == 'prev') {
                $prevImg.before($clone);
                this.$ul.css('margin-left', -this.imgWidth);
                toward = 0;
            }
            this.$ul.stop(true, true).animate({'margin-left': toward}, 600, function () {
                $clone.remove();
                $prevImg.hide();
                $nextImg.show();
                context.$ul.css('margin-left', 0);
                context.onHidePrev($prevImg);
                context.onShowNext($nextImg);
            });
        },
        slideInit: function () {
            this.$ul = this.$el.find('ul');
            this.imgWidth = this.$item.width();
            this.$ul.css('width', this.imgWidth * 2);
        },
        onHidePrev: function ($prevImg) {
            this.findInfo($prevImg).hide();
            this.findTitle($prevImg).stop(true, true).hide();
            this.findText($prevImg).hide();
        },
        onShowNext: function ($nextImg) {
            var context = this;
            this.findInfo($nextImg).show();
            this.findTitle($nextImg).fadeIn().queue(function () {
                var $this = $(this);
                context.findText($nextImg).fadeIn(function () {
                    $this.dequeue();
                })
            });
            this.auto();
        },
        findInfo: function ($Img) {
            return $Img.find('.J_focus_info');
        },
        findTitle: function ($Img) {
            return $Img.find('h3');
        },
        findDesc: function ($Img) {
            return $Img.find('h4');
        },
        findText: function ($Img) {
            return $Img.find('.text');
        },
        pause: function () {
            clearTimeout(this._t);
            this.isPause = true;
        },
        play: function () {
            this.isPause = false;
            this.auto();
        }
    });

    var Slide = function (el, opts) {
        var options = opts || {};
        options.el = el;
        return new View(options);
    };
    module.exports = Slide;
});