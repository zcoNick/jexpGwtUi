JexpUI.Dashboard = function(id, config) {
	this.id = id;
	this.config = config;
	this.initConfig();
};
JexpUI.Dashboard.prototype = {
    settings:{
        columns:'.ui-dashboard-column',
        widgetSelector: '.ui-dashboard-widget',
        handleSelector: '.widget-head',
        contentSelector: '.widget-content',
        direction:'ltr',
        widgetDefault:{
            movable: true,
            removable: true,
            editable: false
        }
    },
    initConfig:function () {
        this.addWidgetControls();
        this.makeSortable();
    },
    getWidgetSettings:function (id) {
        var settings = this.settings;
        return (id&&this.config[id]) ? $.extend({},this.settings.widgetDefault,this.config[id]):this.settings.widgetDefault;
    },
    addWidgetControls:function () {
        var _self = this;
        $(this.settings.widgetSelector, $(this.settings.columns)).each(function () {
            var thisWidgetSettings = _self.getWidgetSettings(this.id);
            if (thisWidgetSettings.removable) {
                $('<span class="remove ui-icon ui-icon-close" style="float:'+(_self.config.direction=='rtl'?'left':'right')+'"></span>').mousedown(function (e) {e.stopPropagation();}).click(function () {
                    if(confirm('This widget will be removed, ok?')) {
                        $(this).parents(_self.settings.widgetSelector).animate({opacity: 0},function () {
                        	var el = $(this);
                       		_self.config.onupdate.call(this,el.attr("code"),el.attr("refr"),-1,-1);
                            el.wrap('<div/>').parent().slideUp(function () {
                            	$(this).remove();
                            });
                        });
                    }
                    return false;
                }).appendTo($(_self.settings.handleSelector, this));
            }
            
            if (thisWidgetSettings.editable) {
                $('<span class="edit ui-icon ui-icon-gear" style="float:'+(_self.config.direction=='rtl'?'left':'right')+'"></span>').click(function (e) {
                	var parent = $(this).closest(_self.settings.widgetSelector)
               		_self.config.onsettings.call(this, parent.attr("code"), parent.attr("refr"));
                }).appendTo($(_self.settings.handleSelector,this));
            }
        });        
    },
    makeSortable:function () {
        var dashboard = this,
            settings = this.settings,
            $sortableItems = (function () {
                var notSortable = '';
                $(settings.widgetSelector,$(settings.columns)).each(function (i) {
                    if (!dashboard.getWidgetSettings(this.id).movable) {
                        if(!this.id) {
                            this.id = 'widget-no-id-' + i;
                        }
                        notSortable += '#' + this.id + ',';
                    }
                });
                if (notSortable)
                	return $('> li:not(' + notSortable + ')', settings.columns);
                else
                	return $('> li', settings.columns);
            })();
        
        $sortableItems.find(settings.handleSelector).css({cursor:'move'}).mousedown(function (e) {
            $sortableItems.css({width:''});
            $(this).parent().css({
                width: $(this).parent().width() + 'px'
            });
        }).mouseup(function () {
            if(!$(this).parent().hasClass('dragging')) {
                $(this).parent().css({width:''});
            } else {
                $(settings.columns).sortable('disable');
            }
        });

        $(settings.columns).sortable({
            items: $sortableItems,
            connectWith: $(settings.columns),
            handle: settings.handleSelector,
            placeholder: 'widget-placeholder',
            forcePlaceholderSize: true,
            revert: 300,
            delay: 100,
            opacity: 0.8,
            containment: 'document',
            start:function(e,ui) {
                $(ui.helper).addClass('dragging').width("40px");
            },
            stop:function(e,ui) {
                $(ui.item).css({width:''}).removeClass('dragging');
                $(settings.columns).sortable('enable');
            },
            update:function(e,ui) {
            	if (dashboard.config.onupdate){
            		var el = $(ui.item);
            		dashboard.config.onupdate.call(this,el.attr("code"),el.attr("refr"),parseInt(el.parent().attr("col")),el.index());
            	}
            },
        });
    }
}