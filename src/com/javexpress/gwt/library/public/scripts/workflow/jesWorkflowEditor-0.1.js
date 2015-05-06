(function( $ ){
var methods = {
	init: function( options ) {
		return this.each(function(){
			var $this = $(this),
				data = $this.data('workflowEditor');
		
			function inspect(reg,dto,el){
				$(".wf-selected",$this).removeClass("wf-selected");
				var pnl = $(".wf-attrs",$this);
				var old = pnl.attr("src");
				if (old&&old==dto.id)
					return;
				else
					pnl.empty();
				var t;
				var tbl = $("<table cellpadding=0 cellspacing=0 border=0 style='width:100%'></table>").appendTo(pnl);
				for (var i=0;i<reg.length;i++){
					t = reg[i];
					var tr = $("<tr></tr>").appendTo(pnl);
					$("<td align='right'>"+t.label+" :</td>").appendTo(tr);
					var ctl = null;
					if (t.type=="check"){
						ctl = $("<input type='check' name='"+t.prop+"'>");
					} else 
						ctl = $("<input type='text' name='"+t.prop+"'>");
					ctl.val(dto[t.prop]);
					ctl.bind("change",function(){
						dto[t.prop]=ctl.val();
					});
					$("<td></td>").append(ctl).appendTo(tr);
				}
				pnl.attr("src",dto.id);
				if (el)
					el.addClass("wf-selected");
			};
			var index=0;
			function createNode(el,isSource,isTarget){
				el = el.appendTo($this);
				var eid = $this.attr("id")+"_"+(index++);
				el.data("data",{id:eid});
				el.addClass("wf-node");
				var pp = el.parent().position();
				var ep = el.position();
				var id = $this.attr("id")+"_"+el.index();
				el.css("left",ep.left-pp.left).css("top",ep.top-pp.top).attr("id",id);
				el.removeClass().addClass("wf-node").addClass($this.attr("id"));
				jsPlumb.draggable(el, { containment:$this.attr("id") });
				el.bind("click",function(){
					var d = $(this).data("data");
					inspect(data.attributeRegistry["node"],d,el);
				});
				
				if (isSource){
					jsPlumb.addEndpoint(id, { anchor:"BottomCenter" }, data.sourceoptions);
					/*jsPlumb.makeSource(id, {
						anchor:"Continuous",
						paintStyle:{ fillStyle:"red" }
					});*/
				}

				if (isTarget){
					jsPlumb.makeTarget(id, {
						anchor:"Continuous",
						paintStyle:{ fillStyle:"red" }
					});			
					jsPlumb.addEndpoint(id, { anchor:"TopCenter" }, data.targetoptions);
				}
				return el;
			};

			function connectionClicked(conn){
				inspect(data.attributeRegistry["conn"],conn);
			};
					
			// If the plugin hasn't been initialized yet
			if ( ! data ) {
				$this.addClass("ui-helper-reset ui-helper-clear ui-widget");
				var plt = $("<div style='display:inline-block;position:absolute;top:10px;left:10px;width:220px;height:200px;overflow:auto'></div>").appendTo($this);
				plt.addClass($this.attr("id")+" ui-widget-content ui-corner-top");
				$("<h6 class='ui-widget-header' style='margin:0'></h6>").html("Ara�lar").appendTo(plt);
				var pltContainer = $("<div style='width:auto;height:auto' class='ui-widget ui-widget-container'></div>").appendTo(plt);
				$this.find(".wf-palette-item").each(function(){
					var el = $(this);
					el.addClass("ui-corner-all");
					$("<h6></h6>").html(el.attr("title")).prependTo(el);
					el.appendTo(pltContainer).draggable({
					appendTo: "body",
					helper: "clone",
				});
				var attrs = $("<div style='display:inline-block;position:absolute;top:215px;left:10px;width:220px;bottom:10px;overflow:auto' class='wf-attrs ui-widget-content ui-corner-bottom'></div>").appendTo($this);
				var cnvs = $("<div style='display:inline-block;position:absolute;top:10px;left:235px;right:10px;bottom:10px;overflow:auto'></div>").appendTo($this);
				cnvs.addClass($this.attr("id")+" wfcanvas ui-widget-content ui-corner-right");
				
				data = { target : $this,
					workflowEditor : $this,
					paletteContainer:pltContainer,
					canvasPanel:cnvs,
					attributePanel:attrs,
					attributeRegistry:{},
					sourceoptions:{endpoint:"Rectangle",paintStyle:{ width:15, height:15, fillStyle:'#666' },isSource:true,
						connectorStyle : { strokeStyle:"orange", lineWidth:5 }},
					targetoptions:{endpoint:"Rectangle",paintStyle:{ width:15, height:15, fillStyle:'#666' },isTarget:true}
				};
				cnvs.droppable({
					accept: "."+$this.attr("id"),
					drop: function( event, ui ) {
						if (!ui.draggable.hasClass("wf-palette-item"))
							return false;
						var el = ui.helper.clone();
						el = createNode(el, ui.draggable.hasClass("source"), ui.draggable.hasClass("target"));
						inspect(data.attributeRegistry["node"],el.data("data"),el);
						return false;
					}
				});
				$(this).data('workflowEditor', data);
	
				jsPlumb.bind("jsPlumbConnection", function(info) {
					info.connection.bind("click", function(conn) {
						connectionClicked(conn);
					});
					inspect(data.attributeRegistry["conn"],info.connection);
				});
			}
		});
    },
    destroy : function( ) {
		return this.each(function(){
			var $this = $(this),
             data = $this.data('workflowEditor');
			// Namespacing FTW
			$(window).unbind('.workflowEditor');
			data.workflowEditor.remove();
			$this.removeData('workflowEditor');
		});
	},
	addAttributeRegisry : function(name,attrs){
		var data = this.data('workflowEditor');
		data.attributeRegistry[name]=attrs;
	}
};
$.fn.workflowEditor = function( method ) {
    if ( methods[method] ) {
      return methods[method].apply( this, Array.prototype.slice.call( arguments, 1 ));
    } else if ( typeof method === 'object' || ! method ) {
      return methods.init.apply( this, arguments );
    } else {
      $.error( 'Method ' +  method + ' does not exist on jQuery.workflowEditor' );
    }    
};
})( jQuery );