function editing_keydown(e, args) {
	if (!e.shiftKey && !e.altKey && !e.ctrlKey) {
		if (e.which==13||(e.which >= 37 && e.which <= 40)) {
			e.stopImmediatePropagation();
		}
	}
}
function jexpDisableGridCellNavigation(args) {
	args.grid.onKeyDown.subscribe(editing_keydown);
};
function jexpEnableGridCellNavigation(args) {
	args.grid.onKeyDown.unsubscribe(editing_keydown);
};
//-- DECIMALCELL START
function JexpDecimalCellEditor(args) {
	var $input, currentValue, scope = this;
	
	this.init = function() {
		$input = $("<INPUT type='text' class='jexpEditGridEditorCell jexpRightAlign' style='border:none;'/>");
		$input.appendTo(args.container).autoNumeric('init', args.column.options).bind("keydown.nav",function(e) {
			if (e.keyCode === $.ui.keyCode.LEFT
					|| e.keyCode === $.ui.keyCode.RIGHT) {
				e.stopImmediatePropagation();
				e.stopPropagation();
			}
		}).focus().select();
	};
	
	this.destroy = function() {
		$input.autoNumeric('destroy');
		$input.off().remove();
	};
	
	this.focus = function() {
		$input.focus();
	};
	
	this.loadValue = function(item) {
		currentValue = item[args.column.field];
		if (!currentValue&&args.column.options.defaultZero){
			currentValue = 0;
			item[args.column.field] = currentValue;
		}
		if (currentValue){
			$input.autoNumeric('set',currentValue);
			$input.select();
		}
	};
	
	this.serializeValue = function() {
		var n = parseFloat($input.autoNumeric('get'));
		return isNaN(n)?null:n;
	};
	
	this.applyValue = function(item,state) {
		item[args.column.field] = state;
	};
	
	this.isValueChanged = function() {
		return (parseFloat($input.autoNumeric('get')) != currentValue);
	};
	
	this.validate = function() {
		var v = !isNaN(parseFloat($input.autoNumeric('get'))) || ($input.val()==''&&!args.column.required);
		return {
			valid: v,
			msg: (v?null:args.column.emptyMessage)
		};
	};
	
	this.init();
}
//-- DECIMALCELL END
//-- MASKCELL START
function JexpMaskEditor(args) {
	var $input;
	var defaultValue;
	var scope = this;
	var calendarOpen = false;

	this.init = function() {
		$input = $("<INPUT type=text class='editor-text' />");
		$input.appendTo(args.container);
		$input.focus().select();
		$input.inputmask(args.column.inputmask,{ placeholder:args.column.placeholder });
	};

	this.destroy = function() {
		$input.remove();
	};

	this.show = function() {
	};

	this.hide = function() {
	};
	
	this.position = function(position) {
	};

	this.focus = function() {
		$input.focus();
	};

	this.loadValue = function(item) {
		defaultValue = item[args.column.field];
		$input.val(defaultValue);
		$input[0].defaultValue = defaultValue;
		$input.select();
	};

	this.serializeValue = function() {
		return $input.val();
	};

	this.applyValue = function(item, state) {
		item[args.column.field] = state;
	};

	this.isValueChanged = function() {
		return (!($input.val() == "" && defaultValue == null))
				&& ($input.val() != defaultValue);
	};

	this.validate = function() {
		var v = ((!$input.val() && !args.column.required) || $input.val().length>0);
		return {
			valid: v,
			msg: (v?null:args.column.emptyMessage)
		};
	};

	this.init();
}
// -- MASKCELL END
//-- DATECELL START
function JexpDateEditor(args) {
	var $input;
	var defaultValue;
	var scope = this;
	var calendarOpen = false;
	
	this.init = function() {
		$input = $("<INPUT type=text class='editor-text' />");
		$input.appendTo(args.container);
		$input.focus().select();
		$input.inputmask("date",{
			mask : args.column.inputmask,
			separator : '.',
			placeholder : " ",
			"clearIncomplete":true
		});
	};
	
	this.destroy = function() {
		$input.remove();
	};
	
	this.show = function() {
	};
	
	this.hide = function() {
	};
	
	this.position = function(position) {
	};
	
	this.focus = function() {
		$input.focus();
	};
	
	this.loadValue = function(item) {
		defaultValue = item[args.column.field];
		$input.val(defaultValue);
		$input[0].defaultValue = defaultValue;
		$input.select();
	};
	
	this.serializeValue = function() {
		return $input.val();
	};
	
	this.applyValue = function(item, state) {
		item[args.column.field] = state;
	};
	
	this.isValueChanged = function() {
		return (!($input.val() == "" && defaultValue == null))
		&& ($input.val() != defaultValue);
	};
	
	this.validate = function() {
		var v = ((!$input.val() && !args.column.required) || $input.val().length==10);
		return {
			valid: v,
			msg: (v?null:args.column.emptyMessage)
		};
	};
	
	this.init();
}
// -- DATECELL END
// -- LONGTEXT START
function JexpLongTextEditor(args) {
	var $input, $wrapper;
	var defaultValue;
	var scope = this;

	this.init = function() {
		var $container = $("body");

		$wrapper = $(
				"<DIV id='"+args.column.baseId + "_ctrl"+"' style='z-index:10000;position:absolute;background:white;padding:5px;border:3px solid gray;-moz-border-radius:10px;border-radius:10px;'/>")
				.appendTo($container);

		$input = $(
				"<TEXTAREA hidefocus rows=5 style='backround:white;width:200px;height:80px;border:0;outline:0'>")
				.appendTo($wrapper);

		$(
				"<DIV style='text-align:left'><small><BUTTON>"+args.column.saveText+"</BUTTON><BUTTON>"+args.column.cancelText+"</BUTTON></small></DIV>")
				.appendTo($wrapper);

		$wrapper.find("button:first").bind("click", this.save);
		$wrapper.find("button:last").bind("click", this.cancel);
		$input.bind("keydown", this.handleKeyDown);

		scope.position(args.position);
		$input.focus().select();
	};

	this.handleKeyDown = function(e) {
		if (e.which == $.ui.keyCode.ENTER && e.ctrlKey) {
			scope.save();
		} else if (e.which == $.ui.keyCode.ESCAPE) {
			e.preventDefault();
			scope.cancel();
		} else if (e.which == $.ui.keyCode.TAB && e.shiftKey) {
			e.preventDefault();
			args.grid.navigatePrev();
		} else if (e.which == $.ui.keyCode.TAB) {
			e.preventDefault();
			args.grid.navigateNext();
		}
	};

	this.save = function() {
		args.commitChanges();
	};

	this.cancel = function() {
		$input.val(defaultValue);
		args.cancelChanges();
	};

	this.hide = function() {
		$wrapper.hide();
	};

	this.show = function() {
		$wrapper.show();
	};

	this.position = function(position) {
		$wrapper.css("top", position.top - 5).css("left", position.left - 5)
	};

	this.destroy = function() {
		$wrapper.remove();
	};

	this.focus = function() {
		$input.focus();
	};

	this.loadValue = function(item) {
		$input.val(defaultValue = item[args.column.field]);
		$input.select();
	};

	this.serializeValue = function() {
		return $input.val();
	};

	this.applyValue = function(item, state) {
		item[args.column.field] = state;
	};

	this.isValueChanged = function() {
		return args.column.required || ((!($input.val() == "" && defaultValue == null))
				&& ($input.val() != defaultValue));		
	};

	this.validate = function() {
		var v = !args.column.required||($input.val()==''&&!args.column.required)||($input.val()!=''&&args.column.required);
		return {
			valid: v,
			msg: (v?null:args.column.emptyMessage)
		};
	};

	this.init();
}
// -- LONGTEXT END
// -- SELECTCELL START
function JexpSelectCellEditor(args) {
    var $select, defaultValue, scope = this;

    this.init = function() {
    	if (!args.column.lazyinited && args.column.lazyInitializer){
    		args.column.lazyInitializer.call(this,args.column,args.item);
    	}
    	if (args.column.editorValues){
        	var opt_labels = args.column.editorLabels.split('é');
        	var opt_values = args.column.editorValues.split('é');
        	var opt_datas = args.column.editorDatas.split('é');
            var option_str = "";
            for( var i=0;i<opt_values.length;i++ ){
              var v = opt_values[i];
              if (!v && !args.column.useEmpty)
            	  continue;
              var l = opt_labels[i];
              var d = opt_datas[i];
              if ( !defaultValue && v && args.column.selectFirst )
            	  defaultValue = v;
            	  
              if (!args.column.itemShowing||args.column.itemShowing.call(this, args.column, args.item, v, l, d))
            	  option_str += "<OPTION value='"+(v?v:"")+"' d='"+(d?d:"")+"'>"+l+"</OPTION>";
            }
    	}
        $select = $("<SELECT class='jexpEditGridEditorCell' style='border:none'>"+ option_str +"</SELECT>");
        $select.appendTo(args.container);
        $select.focus();
        if (args.column.selectFirst && defaultValue)
            $select.val(defaultValue);
		jexpDisableGridCellNavigation(args);
    };

    this.destroy = function() {
		jexpEnableGridCellNavigation(args);
        $select.remove();
    };

    this.focus = function() {
        $select.focus();
    };

    this.loadValue = function(item) {
        defaultValue = item[args.column.field];
        $select.val(defaultValue);
    };

    this.serializeValue = function() {
    	var val = $select.val();
    	var data = $select.find(":selected").attr("d");
    	var item = {v:(val&&val!=''?val:null),d:(data&&data!=''?data:null)};
        return item;
    };

    this.applyValue = function(item,state) {
        item[args.column.field] = state.v;
        if (args.column.selectListener)
        	args.column.selectListener.call(this, args.column, item, state.v, state.d);
    };

    this.isValueChanged = function() {
        return ($select.val() != defaultValue);
    };

    this.validate = function() {
		var v = $select.val() || !args.column.required;
		return {
			valid: v,
			msg: (v?null:args.column.emptyMessage)
		};
    };

    this.init();
}
//-- SELECTCELL END
// -- ROWSELECTCELL START
function JexpRowSelectCellEditor(args) {
	var $select, rowMap, defaultValue, scope = this;
	
	this.init = function() {
		var option_str = "";
		if (args.column.rowDataField){
			rowMap = args.item[args.column.rowDataField];
			if (rowMap){
				option_str = scope.fillItems(rowMap);
			}
		}
		$select = $("<SELECT class='jexpEditGridEditorCell' style='border:none'>"+ option_str +"</SELECT>");
		$select.appendTo(args.container);
		$select.focus();
		jexpDisableGridCellNavigation(args);
	};
	
	this.refreshItems=function(){
		$select.empty();
		$select[0].innerHTML=scope.fillItems(args.item[args.column.rowDataField]);
		scope.loadValue(args.item);
	}
	
	this.fillItems=function(rowMap){
		var option_str = "<OPTION value=''></OPTION>";
		for(key in rowMap){
		    var v = rowMap[key];
		    var l = v["label"];
			option_str += "<OPTION value='"+key+"'>"+l+"</OPTION>";
		}
		return option_str;
	}
	
	this.destroy = function() {
		rowMap = null;
		jexpEnableGridCellNavigation(args);
		$select.remove();
	};
	
	this.focus = function() {
		$select.focus();
	};
	
	this.loadValue = function(item) {
		defaultValue = item[args.column.field];
		$select.val(defaultValue);
	};
	
	this.serializeValue = function() {
		return $select.val();
	};
	
	this.applyValue = function(item,state) {
		item[args.column.field] = state;
		if (rowMap&&args.column.selectListener){
			var extraData = rowMap[state];
			args.column.selectListener.call(this, args.column, item, state, extraData);
		}
	};
	
	this.isValueChanged = function() {
		return ($select.val() != defaultValue);
	};
	
	this.validate = function() {
		var v = $select.val() || !args.column.required;
		return {
			valid: v,
			msg: (v?null:args.column.emptyMessage)
		};
	};
	
	this.init();
}
//-- ROWSELECTCELL END
//-- AUTOCOMPLETE START
function JexpAutoCompleteEditor(args) {
	var lastItem, $input, defaultValue, $div, scope = this;

	this.init = function() {
		args.column.editorOptions.id = args.column.baseId + "_menu";
		args.column.editorOptions.select = function(event, ui) {
			lastItem = ui.item;
			return true;
		};
		args.column.editorOptions.search = function(event, ui) {
			lastItem = null;
		}
		if (args.column.sourceListener){
			args.column.editorOptions.source = function(request, response) {
				args.column.sourceListener.call(this, args.column, args.item, request);
				$.post(args.column.editorOptions.url, request, function(data) {
					response(data);
				}, "json");
			}
		}
		$div = $("<div class='input-group jexpEditGridEditorCell'><span class='input-group-addon jexpHandCursor'><i class='fa fa-search'></i></span><div>");
		$div.appendTo(args.container);
		$input = $("<INPUT type='text' class='jexpEditGridAutoCompleteCell'/>");
		$input.prependTo($div).focus().select()
			.jexpautocomplete(args.column.editorOptions)
			.bind("keydown.nav",function(e) {
					if (e.keyCode === $.ui.keyCode.LEFT
							|| e.keyCode === $.ui.keyCode.RIGHT) {
						e.stopImmediatePropagation();
						e.stopPropagation();
					} else if (e.keyCode === $.ui.keyCode.UP
							|| e.keyCode === $.ui.keyCode.DOWN) {
						e.stopImmediatePropagation();
						e.stopPropagation();
					}
				});
		$(".input-group-addon", $div).click(function(e){
			if (args.column.buttonListener)
				args.column.buttonListener.call(this, args.column, args.item, lastItem, args.grid.getActiveCell());			
		});
		jexpDisableGridCellNavigation(args);
	};

	this.destroy = function() {
		$input.jexpautocomplete('destroy');
		$(".input-group-addon", $div).off();
		$div.remove();
		jexpEnableGridCellNavigation(args);
	};

	this.focus = function() {
		$input.focus();
	};

	this.serializeValue = function() {
		if ($input.val().trim()=="")
			return lastItem = null;
		return lastItem;
	};
	
	this.applyValue = function(item, state) {
		if (args.column.selectableListener){
			if (!args.column.selectableListener.call(this, args.column, item, state?state.data:null))
				return;			
		}
		item[args.column.field] = state?state.label:null;
		item[args.column.editorOptions.valueField] = state?state.id:null;
		if (args.column.selectListener)
			args.column.selectListener.call(this, args.column, item, state?state.data:null);
	};
	
	this.loadValue = function(item) {
		lastItem = {label:item[args.column.field],id:item[args.column.editorOptions.valueField]};
		defaultValue = lastItem.id || "";
		$input[0].defaultValue = defaultValue;
		$input.val(lastItem.label);
		$input.select();
	};

	this.isValueChanged = function() {
		if (lastItem==null)
			return defaultValue!=null;
		else if ($input.val()!=lastItem.label)
			return true;			
		return (lastItem.id != defaultValue);
	};

	this.validate = function() {
		var v = (lastItem==null && !args.column.required) || ((lastItem && lastItem.id) || !args.column.required);
		return {
			valid: v,
			msg: (v?null:args.column.emptyMessage)
		};
	};

	this.init();
}
//-- AUTOCOMPLETE END
//-- CHECKBOX START
function JexpCheckboxEditor(args) {
    var $select;
    var defaultValue;
    var scope = this;

    this.init = function () {
      $select = $("<INPUT type=checkbox value='true' class='editor-checkbox' hideFocus>");
      $select.appendTo(args.container);
      $select.focus();
    };

    this.destroy = function () {
      $select.remove();
    };

    this.focus = function () {
      $select.focus();
    };

    this.loadValue = function (item) {
      defaultValue = !!item[args.column.field];
      if (defaultValue) {
        $select.prop('checked', true);
      } else {
        $select.prop('checked', false);
      }
    };

    this.serializeValue = function () {
      return $select.prop('checked');
    };

    this.applyValue = function (item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function () {
      return (this.serializeValue() !== defaultValue);
    };

    this.validate = function () {
		var v = true;
		return {
			valid: v,
			msg: (v?null:args.column.emptyMessage)
		};
    };

    this.init();
}
//-- CHECKBOX END