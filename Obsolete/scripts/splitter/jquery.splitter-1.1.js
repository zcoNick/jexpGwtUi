$.widget("jes.splitter", {
	options : {
		collapsedSize:0,
		collapsedChild:-1,
		minAsize : 0, // minimum width/height in PX of the
		// first (A) div.
		maxAsize : 0, // maximum width/height in PX of the
		// first (A) div.
		minBsize : 0, // minimum width/height in PX of the
		// second (B) div.
		maxBsize : 0, // maximum width/height in PX of the
		// second (B) div.
		ghostClass : 'working',// class name for _ghosted
		// splitter and hovered button
		invertClass : 'invert',// class name for invert // splitter button
		splitHorizontal:false
	},
	_create : function() {
		var that = this;
		this.options = $.extend(this.options, this.options.splitHorizontal ? 
			{  // Horizontal
				moving : "top",
				sizing : "height",
				eventPos : "pageY",
				splitbarClass : "splitbarH",
				buttonClass : "splitbuttonH",
				cursor : "n-resize"
			} : {
				// Vertical
				moving : "left",
				sizing : "width",
				eventPos : "pageX",
				splitbarClass : "splitbarV",
				buttonClass : "splitbuttonV",
				cursor : "e-resize"
			});
		this.element.addClass("jesSplitPanel");
		this.dragger = $('<div><span></span></div>').attr({
			"class" : this.options.splitbarClass,
			unselectable : "on"
		}).css({
			"cursor" : this.options.cursor,
			"user-select" : "none",
			"-webkit-user-select" : "none",
			"-khtml-user-select" : "none",
			"-moz-user-select" : "none"
		}).draggable({ containment: "parent", 
			zIndex: 900, 
			axis:(this.options.splitHorizontal?"y":"x"), 
			stop: function(event,ui){
				that.updatePosition(true);
			}
		});
		this._draggerSize = this.options.splitHorizontal?this.dragger.height():this.dragger.width();
		this.options.A.after(this.dragger);
		this._update();
		this.updatePosition();
	},
	_setOption : function(key, value) {
		this.options[key] = value;
		this._update();
	},
	_update : function() {
		if (this.options.closeableto != undefined) {
			var that = this;
			this.button = $('<div></div>').css("cursor", 'pointer').attr({
				"class" : that.options.buttonClass,
				unselectable : "on"
			}).hover(function() {
					$(this).addClass(that.options.ghostClass);
				}, function() {
					$(this).removeClass(that.options.ghostClass);
			}).click(function(e) {
				that.button.toggleClass(that.options.invertClass);
				if (that.options.closeableto==0){
					var collapsed = that.options.collapsedSize;
					if (collapsed>0){
						//expand
						collapsed = that.options.collapsedSize;
						that.options.collapsedSize = 0;
						that.options.collapsedChild=-1;
						if (that.options.splitHorizontal)
							that.options.A.css({height:collapsed});
						else
							that.options.A.css({width:collapsed});
					} else {
						//collapse
						if (that.options.splitHorizontal)
							that.options.collapsedSize = that.options.A.height();
						else
							that.options.collapsedSize = that.options.A.width();
						collapsed = 0;
						that.options.collapsedChild=0;
					}
					that.updatePosition(true);
				}
				return false;
			}).appendTo(this.dragger);
		}
	},
	updatePosition:function(fireEvents){
		var main = $(this.element);
		var effect = false;
		if (this.options.splitHorizontal){
			var total = main.height();
			if (total<5)
				return;
			var poff = main.offset();
			var doff = this.dragger.offset();
			var ah = doff.top-poff.top;
			var th = parseFloat(((ah*100)/total).toFixed(3));
			if (this.options.collapsedChild==-1){
				if (this.options.minAsize){
					var force = Math.max(th,this.options.minAsize);
					effect = th!=force;
					th = force;
				}
				if (!effect&&this.options.maxAsize){
					var force = Math.min(th,this.options.maxAsize);
					effect = th!=force;
					th = force;
				}
				this.options.A.css({height:(th+"%")});
			} else if (this.options.collapsedChild==0)
				this.options.A.css({height:5});
			this.dragger.css({top:""});
			doff = this.dragger.offset();
			th = parseFloat((total-doff.top+poff.top-this._draggerSize).toFixed(1));
			this.options.B.css({height:(th+"px")});
		} else {
			var total = main.width();
			if (total<5)
				return;
			var poff = main.offset();
			var doff = this.dragger.offset();
			var aw = doff.left-poff.left;
			var tw = parseFloat(((aw*100)/total).toFixed(3));
			if (this.options.collapsedChild==-1){
				if (this.options.minAsize){
					var force = Math.max(tw,this.options.minAsize);
					effect = tw!=force;
					tw = force;
				}
				if (!effect&&this.options.maxAsize){
					var force = Math.min(tw,this.options.maxAsize);
					effect = tw!=force;
					tw = force;
				}
				this.options.A.css({width:(tw+"%")});
			} else if (this.options.collapsedChild==0)
				this.options.A.css({width:5});
			this.dragger.css({left:""});
			doff = this.dragger.offset();
			tw = parseFloat((total-doff.left+poff.left-this._draggerSize-1).toFixed(1));
			this.options.B.css({width:(tw+"px")});
		}
		if (fireEvents&&this.options.onResize)
			this.options.onResize.call(this);
		if (effect){
			this.options.A.effect("highlight", {}, 500, null );
		}
	},
	dragged : function( event, ui ) {
		this.updatePosition();
	},
	destroy : function() {
		this.dragger.remove();
		if (this.button)
			this.button.remove();
		this.button=null;
		this.options=null;
		this._draggerSize = null;
		// Call the base destroy function.
		$.Widget.prototype.destroy.call(this);
	}
});