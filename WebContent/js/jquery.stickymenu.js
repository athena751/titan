(function ($) {
    $.fn.stickyMenubar = function (o) {
        o = $.extend({
            top: null,
            floatingDiv: null,
            floatingDivBackground: false,
            onScroll: function () {},
            onLeaveTop: function () {}
        }, o || {});

        function setLiActions(t, lvl) {
            var parent_ul = t.find('ul:first');
            if (lvl > 0) {
                t.addClass('inner_menu').hide()
            } else {
                t.addClass('smenubar_background')
            }
            t.children('li').each(function () {
                var li = $(this);
                var class_parent = '';
                var class_child = '';
                var uls = li.children('ul');
                if (uls.length) {
                    uls.each(function () {
                        setLiActions($(this), lvl + 1)
                    })
                }
                if (uls.length) {
                    if (lvl == 0) {
                        li.find('a:first').append('<span class="arrow_down"></span>')
                    } else {
                        li.find('a:first').append('<span class="arrow_right"></span>')
                    }
                    li.mouseenter(function () {
                        if (lvl == 0) {
                            var inner_ul = li.children('ul');
                            var pos = li.position();
                            var pos_ul = inner_ul.position();
                            var top_c = pos.top + li.height();
                            if (inner_ul.hasClass('show2left')) {
                                var left_c = pos.left - inner_ul.width() + li.width() - 1;
                                class_parent = 'inuseleft';
                                class_child = 'inusechildleft';
                                class_panel = 'left_slide'
                            } else {
                                var left_c = pos.left;
                                class_parent = 'inuse';
                                class_child = 'inusechild';
                                class_panel = 'right_slide'
                            }
                            if (inner_ul.width() < li.width()) {
                                inner_ul.css('width', li.width())
                            }
                            inner_ul.stop(true, true).css({
                                top: top_c,
                                left: left_c
                            }).addClass(class_panel).slideDown(150);
                            li.addClass(class_child)
                        } else {
                            var inner_ul = li.children('ul');
                            var pos = li.position();
                            var pos_ul = inner_ul.position();
                            var top_c = pos.top;
                            if (inner_ul.hasClass('show2left')) {
                                var left_c = pos.left - inner_ul.width() + li.width() - 1;
                                class_parent = 'inuseleft';
                                class_child = 'inusechildleft';
                                class_panel = 'left_slide'
                            } else {
                                var left_c = pos.left + li.width();
                                class_parent = 'inuse';
                                class_child = 'inusechild';
                                class_panel = 'right_slide'
                            }
                            inner_ul.css({
                                top: top_c,
                                left: left_c
                            }).addClass(class_panel).css({
                                'white-space': 'nowrap'
                            }).stop(true, true).animate({
                                width: 'toggle'
                            }, 300);
                            li.addClass(class_child)
                        }
                    });
                    li.mouseleave(function () {
                        li.stop(true, true).removeClass(class_child).find('ul').hide()
                    })
                } else {
                    li.click(function () {});
                    li.mouseenter(function () {
                        li.stop(true, true).addClass(class_parent)
                    });
                    li.mouseleave(function () {
                        li.stop(true, true).removeClass(class_parent).find('ul').hide()
                    });
                    li.mouseout(function () {
                        li.stop(true, true).removeClass(class_parent).find('ul').hide()
                    })
                }
            })
        }
        return this.each(function () {
            var t = $(this);
            t.addClass('smenubar');
            if (!o.floatingDiv) {
                var floatingDiv = t
            } else {
                var floatingDiv = o.floatingDiv;
                floatingDiv.css({
                    width: '100%'
                })
            }
            var top_Y = 0;
            if (!o.top) {
                top_Y = floatingDiv.position().top
            } else {
                top_Y = o.top
            }
            if (o.floatingDivBackground) {
                floatingDiv.addClass('smenubar_background');
                t.css({
                    'border-bottom': 'none'
                })
            }
            $(window).scroll(function () {
                if ($(this).scrollTop() >= top_Y) {
                    floatingDiv.addClass('float_top');
                    o.onLeaveTop.call(this)
                } else {
                    floatingDiv.removeClass('float_top');
                    o.onScroll.call(this)
                }
            });
            setLiActions(t, 0)
        })
    }
})(jQuery);
