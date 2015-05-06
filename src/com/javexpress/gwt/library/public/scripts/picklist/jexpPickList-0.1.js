JexpUI.PickList = function(id,config) {
	this.id = id;
	this.config = config;
	this.initConfig();
};
JexpUI.PickList.prototype = {
	initConfig:function(){
	},
	moveSelectedOptions:function(fromList,toList){
		var f = JexpUI.jqEl(fromList)[0];
		var fi = f.selectedIndex;
		if (fi==-1) {
			alert('Lütfen taşınacak opsiyonu seçiniz.');
			return false;
		}
		while (fi>-1){
			var opt = f.options[fi];
			$('#'+toList).append(opt);
			fi = f.selectedIndex;
		}
		return false;
	},
	moveAllOptions:function(fromList,toList){
		$('#'+fromList+' option').each(function(){
			$('#'+toList).append(this);
		});
		return false;
	},
	moveSelectedOptionsUpDown:function(fromList,direction){
		$('#'+fromList+' option:selected').each(function(){
			if (direction==-1)
				$(this).insertBefore($(this).prev());
			else
			   $(this).insertAfter($(this).next());
		});
		return false;
	},
	updateSelectedOption:function(fromList,str,delim,end){
		var f = JexpUI.jqEl(fromList);
		var fi = f.selectedIndex;
		if (fi==-1) {
			alert('Lütfen taşınacak opsiyonu seçiniz.');
			return false;
		}
		var opt = f.options[fi];
		var v = opt.value;
		if (v.indexOf(delim)>-1)
			v = v.substring(0,v.indexOf(delim)).trim();
		v += ' '+delim+str+end;
		opt.value=v;
		opt.innerHTML=v;
	},
	updateSelectedOptions:function(fromList,inp){
		var sels = "";
		var f = JexpUI.jqEl(fromList)[0];
		for (var i=0;i<f.options.length;i++){
			sels += (sels.length>0?";":"")+f.options[i].value;
		}
		JexpUI.jqEl(inp).val(sels);
	}
};