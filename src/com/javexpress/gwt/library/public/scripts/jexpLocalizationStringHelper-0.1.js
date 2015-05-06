//http://stackoverflow.com/questions/11933577/javascript-convert-unicode-string-to-title-case
// locale specific chars
// IMPORTANT: name of locale must be always in lower case (for "tr-TR" locale - "tr-tr") !!!
var localeInfos={
		"en": { lower: { i:"I" },
            	upper: { I:"i" } 
		},
		"tr": { lower: { i:"İ", ı:"I", ş:"Ş", ğ:"Ğ", ü:"Ü", ç:"Ç", ö:"Ö" },
                upper: { İ:"i", I:"ı", Ş:"ş", Ğ:"ğ", Ü:"ü", Ç:"ç", Ö:"ö" } 
		}
    },
    localeInfo;
// helper vars
var mask="\\s:\\-", // add additional delimeters chars to the mask if needed
    rg=new RegExp("([^"+mask+"])([^"+mask+"]*)","g");
var fnToLocaleLower=function(s){ return localeInfo.upper[s]; },
    fnToLocaleUpper=function(s){ return localeInfo.lower[s]; },
    fnToProper=function($0,$1,$2){
        if(localeInfo){
            if(localeInfo.lower.hasOwnProperty($1))$1=localeInfo.lower[$1];
            $2=$2.replace(localeInfo.upperSearchRegExp,fnToLocaleLower);
        }
        return $1.toUpperCase()+$2.toLowerCase();
    };
// helper calculations
var localeInfosKeys=Object.keys(localeInfos);
for(var i=0;localeInfo=localeInfos[localeInfosKeys[i]];i++){
    localeInfo.lowerSearchRegExp=new RegExp("["+Object.keys(localeInfo.lower).join("")+"]","g");
    localeInfo.upperSearchRegExp=new RegExp("["+Object.keys(localeInfo.upper).join("")+"]","g");
}
// extending String.prototype
function toLocaleTitleCase(str,locale){
    localeInfo=localeInfos[arguments.length?locale.toLowerCase():null];
    return str.replace(rg,fnToProper);
};
function toLocaleLowerCase(str,locale){
    return ((localeInfo=localeInfos[arguments.length?locale.toLowerCase():null]) ?
            str.replace(localeInfo.upperSearchRegExp,fnToLocaleLower):
            str).toLowerCase();
};
function toLocaleUpperCase(str,locale){
    return ((localeInfo=localeInfos[arguments.length?locale.toLowerCase():null]) ?
            str.replace(localeInfo.lowerSearchRegExp,fnToLocaleUpper) :
            str).toUpperCase();
};