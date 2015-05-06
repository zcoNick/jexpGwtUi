jQuery.extend($.fn.fmatter, {
    bool:function(cellvalue, options, rowdata) {
	    return cellvalue?"&#10004;":"";
	}
});
jQuery.extend($.fn.fmatter.bool,{
    unformat : function(cellvalue, options) {
	    return cellvalue=="&#10004;";
	}
});