function JexpPercentCompleteBarFormatter(row, cell, value, columnDef, dataContext) {
    if (value == null || value === "")
    	return "";
    var color;
    if (value < 25) {
    	color = "red";
    } else if (value < 50) {
		color = "orange";
    } else if (value < 75) {
    	color = "silver";
    } else {
    	color = "green";
    }
    return "<span class='percent-complete-bar' style='background:" + color + ";width:" + value + "%'></span>";
}
function JexpBoolFormatter(row, cell, value, columnDef, data) {
	return value?"&#10004;":"";
}
function JexpMapFormatter(row, cell, value, columnDef, data) {
	if (value == null || value === "")
		return "";
	return columnDef.map[value];
}
function JexpComboFormatter(row, cell, value, columnDef, data) {
	if (value == null || value === "")
	  return "";
	if (!columnDef.lazyinited && columnDef.lazyInitializer){
		columnDef.lazyInitializer.call(this,columnDef,data);
	}
	if (columnDef.editorValues){
		var vals = columnDef.editorValues.split("é");
		for (var i=0;i<vals.length;i++)
			if (vals[i]==value){
				return columnDef.editorLabels.split("é")[i];
			}
	}
	return "";
}
function JexpRowComboFormatter(row, cell, value, columnDef, data) {
	if (value == null || value === "")
		return "";
	if (columnDef.rowDataField){
		var rowMap = data[columnDef.rowDataField];
		if (rowMap&&rowMap[value])
			return rowMap[value]["label"];
		else
			return data[columnDef.labelField];
	}
	return "";
}
function JexpDateFormatter(row, cell, value, columnDef, data) {
	if (value == null || value === "")
		return "";
	if (!value.getMonth)
		return value.substring(0,10);
	return JexpUI.formatDate(value,columnDef.dateFormat);
}
function JexpTimeStampFormatter(row, cell, value, columnDef, data) {
	if (value == null || value === "")
		return "";
	if (!value.getMonth)
		return value;
	return JexpUI.formatDate(value,columnDef.timestampFormat);
}
function JexpDecimalFormatter(row, cell, value, columnDef, data) {
	if (value == null || value === "")
		return "";
	return numeral(value).format(columnDef.numeralFormat);	
}
function JexpCurrencyFormatter(row, cell, value, columnDef, data) {
	if (value == null || value === "")
		return "";
	return numeral(value).format(columnDef.numeralFormat);	
}
function JexpLinkFormatter(row, cell, value, columnDef, data) {
	if (value == null || value === "" || (!columnDef.renderOnNew && value.toString().substring(0,1)=="é"))
		return "";
	var icon = null;
	if (columnDef.iconModifier)
		icon = columnDef.iconModifier.call(this,columnDef,value,data);
	if (!icon)
		icon = columnDef.linkIconClass?columnDef.linkIconClass+" ":"";
	return "<span class=\""+icon+" ui-cursor-hand jexpDataGridLinkItem\" title=\""+(columnDef.linkTitle?columnDef.linkTitle:"")+"\" onclick=\"$('#"+columnDef.linkOwner+"').trigger('linkclicked'," +
			"[$(this),"+row+","+cell+",'"+columnDef.field+"',"+columnDef.columnKey+",'"+value+"']);return false;\">"+(columnDef.linkText?columnDef.linkText:"")+"</span>";
}
//--AGGREGATE FORMATTERS
function JexpAvgFormatter(totals, columnDef) {
	var val = totals.avg && totals.avg[columnDef.field];
	if (val != null) {
		return "avg: " + Math.round(val);
	}
	return "";
}
function JexpSumFormatter(totals, columnDef) {
	var val = totals.sum && totals.sum[columnDef.field];
	if (val != null) {
		val = numeral(val).format("0,0");//http://numeraljs.com/ English & Turkish compatible format
		return "&Sigma; : " + val;
	}
	return "";
}
function JexpDecimalSumFormatter(totals, columnDef) {
	var val = totals.sum && totals.sum[columnDef.field];
	if (val != null) {
		val = numeral(val).format(columnDef.numeralFormat);
		return "&Sigma; : " + val;
	}
	return "";
}
