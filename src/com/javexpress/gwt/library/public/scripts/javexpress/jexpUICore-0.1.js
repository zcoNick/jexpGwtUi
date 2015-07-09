/**
 * JavExpress
 */
var jexpDateFormat = function () {
	var	token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
		timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
		timezoneClip = /[^-+\dA-Z]/g,
		pad = function (val, len) {
			val = String(val);
			len = len || 2;
			while (val.length < len) val = "0" + val;
			return val;
		};

	// Regexes and supporting functions are cached through closure
	return function (date, mask, utc) {
		var dF = jexpDateFormat;

		// You can't provide utc if you skip other args (use the "UTC:" mask prefix)
		if (arguments.length == 1 && Object.prototype.toString.call(date) == "[object String]" && !/\d/.test(date)) {
			mask = date;
			date = undefined;
		}

		// Passing date through Date applies Date.parse, if necessary
		date = date ? new Date(date) : new Date;
		if (isNaN(date)) throw SyntaxError("invalid date");

		mask = String(dF.masks[mask] || mask || dF.masks["default"]);

		// Allow setting the utc argument via the mask
		if (mask.slice(0, 4) == "UTC:") {
			mask = mask.slice(4);
			utc = true;
		}

		var	_ = utc ? "getUTC" : "get",
			d = date[_ + "Date"](),
			D = date[_ + "Day"](),
			m = date[_ + "Month"](),
			y = date[_ + "FullYear"](),
			H = date[_ + "Hours"](),
			M = date[_ + "Minutes"](),
			s = date[_ + "Seconds"](),
			L = date[_ + "Milliseconds"](),
			o = utc ? 0 : date.getTimezoneOffset(),
			flags = {
				d:    d,
				dd:   pad(d),
				ddd:  dF.i18n.dayNames[D],
				dddd: dF.i18n.dayNames[D + 7],
				m:    m + 1,
				mm:   pad(m + 1),
				mmm:  dF.i18n.monthNames[m],
				mmmm: dF.i18n.monthNames[m + 12],
				yy:   String(y).slice(2),
				yyyy: y,
				h:    H % 12 || 12,
				hh:   pad(H % 12 || 12),
				H:    H,
				HH:   pad(H),
				M:    M,
				MM:   pad(M),
				s:    s,
				ss:   pad(s),
				l:    pad(L, 3),
				L:    pad(L > 99 ? Math.round(L / 10) : L),
				t:    H < 12 ? "a"  : "p",
				tt:   H < 12 ? "am" : "pm",
				T:    H < 12 ? "A"  : "P",
				TT:   H < 12 ? "AM" : "PM",
				Z:    utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
				o:    (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
				S:    ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
			};

		return mask.replace(token, function ($0) {
			return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
		});
	};
}();
jexpDateFormat.i18n = {
	dayNames: [
		"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
		"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
	],
	monthNames: [
		"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
		"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
	]
};
JexpUI={
	jqId : function(id){
		return id.replace(/:/g,"\\:");
	},
	jqEl : function(id){
		return $("#" + this.jqId(id));
	},
	jqEl : function(id,parent){
		return $("#" + this.jqId(id),parent);
	},
	firstChild: function(el){
		return el.find(":nth-child(1)");
	},
	hide:function(id){
		this.jqEl(id).hide();
	},
	hideMulti:function(ids){
		for(var i=0;i<ids.length;i++)
			if (ids[i].length>0)
				this.hide(ids[i]);
	},
	show:function(id){
		this.jqEl(id).show();
	},
	showMulti:function(ids){
		for(var i=0;i<ids.length;i++)
			if (ids[i].length>0)
				this.show(ids[i]);
	},
    getCookie : function(name) {
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    },
    formatMoney : function(n, c, d, t){
    	//c desimal hane 2
    	//d desimal ayraci
    	//t binler ayraci
    	var c = isNaN(c = Math.abs(c)) ? 2 : c, d = d == undefined ? "," : d, t = t == undefined ? "." : t, s = n < 0 ? "-" : "", i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "", j = (j = i.length) > 3 ? j % 3 : 0;
    	return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
    },
    formatDate:function(v, f){
    	return jexpDateFormat(v,f);
    }
};

JexpUI.AjaxUtils = {
    serialize: function(params) {
        var serializedParams = '';
        for(var param in params) {
        	if (!param || !params[param])
        		continue;
        	if ($.isPlainObject(params[param]))
        		serializedParams = serializedParams + "&" + param + "=" + encodeURI(JSON.stringify(params[param]));
        	else
        		serializedParams = serializedParams + "&" + param + "=" + encodeURI(params[param]);
        }
        return serializedParams;
    },
    replaceElement: function(id, content) {
        JexpUI.jqEl(id).replaceWith(content);
    },
    setListener:function(sessionInvalidlistener){
    	if (sessionInvalidlistener)
	        $.ajaxSetup({
	            statusCode:
	            {
	                901: sessionInvalidlistener
	            }
	        });
    }
};

JexpUI.AjaxRequest = function(actionURL, cfg, params) {
    var requestParams = "";
    if(cfg.formId) {
        requestParams = JexpUI.jqEl(cfg.formId).serialize();
    }
    if(params)
        requestParams = requestParams + (jQuery.isPlainObject(params)?JexpUI.AjaxUtils.serialize(params):"&"+params);
    var xhrOptions = {
        url : actionURL,
        type : "POST",
        cache : false,
        dataType : "json",
        data : requestParams,
        beforeSend: function(xhr) {
           xhr.setRequestHeader('Jes-Request', 'jes/ajax');
           if (cfg.indicatorId)
        	   JexpUI.jqEl(cfg.indicatorId).show();
           if(cfg.onstart!=null)
               cfg.onstart.call(this, xhr);
        },
        success : function(data, status, xhr) {
            if(data!=null && cfg.onsuccess) {
                var value = cfg.onsuccess.call(this, data, status, xhr);
                if(value === false)
                    return;
            }
        },
        complete : function(xhr, status) {
            if(cfg.oncomplete!=null) {
                cfg.oncomplete.call(this, xhr, status, this.args);
            }
            JexpUI.RequestManager.poll();
            if (cfg.indicatorId)
         	   JexpUI.jqEl(cfg.indicatorId).hide();
        }
    };
	
    xhrOptions.global = cfg.global === false ? false : true;
	
    if(!cfg.onerror) {
    	xhrOptions.error=function(xhr, ajaxOptions, thrownError){
    		if (xhr.responseText.substring(0,5)=='<html'){
    			new JexpUI.Message({id:'jes_hata_',message:xhr.responseText,width:'95%'});
    			return;
    		}
    		alert(thrownError);
        };
    } else
    	xhrOptions.error=cfg.onerror;

    if(cfg.async) {
        $.ajax(xhrOptions);
    } else {
        JexpUI.RequestManager.offer(xhrOptions);
    }
    xhrOptions = null;
};

JexpUI.RequestManager = {
    requests : new Array(),
    offer : function(req) {
        this.requests.push(req);
        if(this.requests.length == 1) {
            var retVal = $.ajax(req);
            if(retVal === false)
                this.poll();
        }
    },

    poll : function() {
        if(this.isEmpty()) {
            return null;
        }
 
        var processedRequest = this.requests.shift();
        var nextRequest = this.peek();
        if(nextRequest != null) {
            $.ajax(nextRequest);
        }

        return processedRequest;
    },

    peek : function() {
        if(this.isEmpty()) {
            return null;
        }
        var nextRequest = this.requests[0];
        return nextRequest;
    },
    
    isEmpty : function() {
        return this.requests.length == 0;
    }
};

JexpUI.Message = function(cfg) {
	this.cfg = cfg;
	this.initConfig();
};

JexpUI.Message.prototype = {
		initConfig:function() {
			var pr = "<p>";
			if (this.cfg.icon)
				pr += "<span class='ui-icon ui-icon-"+this.cfg.icon+"' style='float:left;'></span>";
			pr += this.cfg.message+"</p>";
			this.div = $("<div id='"+this.cfg.id+"' title='Bilgi' style='height:85%;overflow:auto'>"+pr+"</div>").appendTo(document.body);
			var _self = this;
			this.div.dialog({autoOpen:true,resizable:false,modal:true,width:this.cfg.width,
				buttons:{'Tamam':function(){
					if ($.isFunction(_self.cfg.onconfirm))
						_self.cfg.onconfirm.call(_self.callee,$(this));
					$(this).dialog("destroy");
				}
				}});
		}
};

function getIFrameParentId(){
	var frameWindow = document.parentWindow || document.defaultView;
	return $(frameWindow.frameElement.parentNode).attr("id");
}

$.fn.centerInWindow = function () {
	console.debug("a"+Math.max(0, (($(window).height() - $(this).outerHeight()) / 2) + $(window).scrollTop()) + "px");
	console.debug(Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) + $(window).scrollLeft()) + "px");
    this.css({position:"absolute",
    	top: Math.max(0, (($(window).height() - $(this).outerHeight()) / 2) + $(window).scrollTop()) + "px",
    	left: Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) + $(window).scrollLeft()) + "px",
    	});
    return this;
}

$.widget( "custom.jexpautocomplete", $.ui.autocomplete, {
	miIndex:0,
	_renderMenu:function(ul,items){
		var that = this;
		ul.attr("id",that.options.id).addClass("ui-shadow");
		$.each(items,function(index,item) {
			that.miIndex=index;
			that._renderItemData(ul,item);
		});
		$( ul ).find("li:even").addClass("evenitem");
	},
	_renderItem:function(ul,item) {
		var that = this;
		return $( "<li>" )
			.append( $( "<a name=\""+ul[0].id+":"+that.miIndex+"\">" ).text( item.label ) )
			.appendTo( ul );
	},
});

//--TESTBENCH SUPPORT
function mouseWheel(element,delta) {
	//Works with Chrome and FireFox
	var evt = document.createEvent("MouseEvents");
	evt.initMouseEvent(
	  'DOMMouseScroll', // in DOMString typeArg,
	   true,  // in boolean canBubbleArg,
	   true,  // in boolean cancelableArg,
	   window,// in views::AbstractView viewArg,
	   delta,   // in long detailArg,
	   0,     // in long screenXArg,
	   0,     // in long screenYArg,
	   0,     // in long clientXArg,
	   0,     // in long clientYArg,
	   0,     // in boolean ctrlKeyArg,
	   0,     // in boolean altKeyArg,
	   0,     // in boolean shiftKeyArg,
	   0,     // in boolean metaKeyArg,
	   0,     // in unsigned short buttonArg,
	   null   // in EventTarget relatedTargetArg
	);
	element.dispatchEvent(evt);
}