JexpUI.TreeTable=function(id,config){
	this.id=id;
	this.config=config;
	this.initConfig();
};
JexpUI.TreeTable.prototype={
	selected:null,
	indent:'&nbsp;&nbsp;',
	initConfig:function(){
		//this.getTable().fixedHeaderTable({});
		this.refresh();
	},
	getTable:function(){
		return $("#"+this.config.sampleRowId).parent();
	},
	parent:function(){
		return this.getTable().parent();
	},
	setGridWidth:function(width){
	},
	setGridHeight:function(height){
	},
	refresh:function(rowid){
		var table=this.getTable();
		$("tr:not(#"+this.config.sampleRowId+")",table).each(function(index, value) {
			if (this.id)
				$(this).remove();
		});
		pd = {};
		if (this.config.onprepareData)
            this.config.onprepareData.call(this, pd);
		var _self=this;
		JexpUI.AjaxRequest(this.config.listUrl,{
			onsuccess:function(json,status,xhr){
				if (!json||!json.data||json.data.length==0)
					return;
				for (var i=0;i<json.data.length;i++){
					_self.createRow(json.data[i]);
				}
				if (rowid)
					_self.select(rowid);
			}
		},pd);
	},
	createRow:function(row){
		var table=this.getTable();
		var tr = $("#"+this.config.sampleRowId).clone().attr("id", row.id);
		if (row.parent)
			tr.attr("pid", row.parent);
		else
			tr.show();
		var _self=this;
		tr.click(function(event){
			if (_self.selected){
				_self.selected.removeClass("ui-state-highlight");
			}
			_self.selected=$(this);
			_self.selected.addClass("ui-state-highlight");
			_self.onSelect(_self.selected.attr("id"));
		}).hover(function(){$(this).addClass("ui-state-hover");},function(){$(this).removeClass("ui-state-hover");});
		$("td", tr).each(function(index, value) {
			var td= $(this);
			if (index==0){
				var sp = "<table cellpadding=0 cellspacing=0><tr>";
				var indent = '';
				for (var z=0;z<row.level;z++)
					indent = _self.indent;
				if (indent!='')
					sp+="<td>"+indent+"</td>";
				sp+="<td><span id='ind"+row.id+"' class='ui-icon ui-icon-"+(!row.isLeaf?(row.expanded?"triangle-1-s":"triangle-1-e"):"radio-off")+"'></td><td>"+row[td.attr("data")]+"</td></tr></table>";
				var icon=$("span", $(sp).appendTo(td));
				if (!row.isLeaf)
					icon.click(function(event){
						var sp=$(this);
						if (sp.hasClass("ui-icon-triangle-1-e")){
							_self.expand(sp.closest("table").closest("tr").attr("id"));
						} else {
							_self.collapse(sp.closest("table").closest("tr").attr("id"));
						}
						return false;
					});
			} else {
				var data = row[td.attr("data")]; 
				if (data)
					td.html(data);
				else {
					var sp = $("span", td);
					if (sp.attr("onclick_sample")){
						data = sp.attr("onclick_sample").replace("{id}",row["id"])+";event.cancelBubble=true;return false;";
						sp.attr("onclick", data).removeAttr("onclick_sample", "");
					}
				}
			}
		});
		tr.appendTo(table);
	},
	select:function(rowid){
		var tr=this.node(rowid);
		tr.show();
		if (tr.attr("pid")){
			var pr=this.node(tr.attr("pid"));
			while (pr[0]){
				this.expand(pr.attr("id"));
				pr=this.node(pr.attr("pid"));
			}
		}
		if (this.selected){
			this.selected.removeClass("ui-state-highlight");
		}
		this.selected=tr;
		this.selected.addClass("ui-state-highlight");
	},
	deselect:function(){
		if (this.selected!=null)
			this.selected.removeClass("ui-state-highlight");
		this.selected=null;
	},
	node:function(rowid){
		return $("tr[id*='"+rowid+"']");
	},
	delRowById:function(rowid){
		this.node(rowid).remove();
	},
	onSelect:function(selectedNode){
		if (!this.config.selection)
			return;
		pd = {};
		pd[this.config.selection]=selectedNode;
		if (this.config.onprepareData)
            this.config.onprepareData.call(this, pd);
		var _self=this;
		JexpUI.AjaxRequest(this.config.selectUrl,{formId:this.config.formId,
			update:this.config.update,
			onsuccess:function(data,xhr,status){
				if (_self.config.selectionOncomplete)
					_self.config.selectionOncomplete.call(this, data,xhr,status);
			}
		},pd);
		pd=null;
	},
	expand:function(id){
		var node=this.node(id);
		node.show();
		$("#ind"+id,node).addClass("ui-icon-triangle-1-s").removeClass("ui-icon-triangle-1-e");
		$("tr[pid*='"+id+"']").each(function(index, value) {
			$(this).show();
		});
	},
	collapse:function(id){
		var node=this.node(id);
		node.show();
		$("#ind"+id,node).addClass("ui-icon-triangle-1-e").removeClass("ui-icon-triangle-1-s");
		$("tr[pid*='"+id+"']").each(function(index, value) {
			$(this).hide();
		});
	}
};