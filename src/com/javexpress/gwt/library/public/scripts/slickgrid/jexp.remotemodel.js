(function ($) {
  function RemoteModel(options,data) {
    // private
    var PAGESIZE = options.rowsPerPage;
    var searchstr = null;
    var sortCols = null;
    var loadedPages = {};
    var lastSelectedRows = null;
    var disableSelectEventFire = false;
    // events
    var onDataLoading = new Slick.Event();
    var onDataLoaded = new Slick.Event();

    function init() {
    }

    function isDataLoaded(from, to) {
      for (var i = from; i <= to; i++) {
        if (data[i] == undefined || data[i] == null) {
          return false;
        }
      }

      return true;
    }


    function clear() {
      for (var key in data) {
        delete data[key];
      }
      data.length = 0;
      lastSelectedRows = null;
      loadedPages = {};
    }


    function ensureData(from, to) {
    	var fromPage = PAGESIZE==0?1:Math.ceil(from / PAGESIZE);
    	var toPage = PAGESIZE==0?1:Math.ceil(to / PAGESIZE);
    	var isToLoaded = loadedPages[toPage]; 
    	var isFromLoaded = loadedPages[from==0?1:fromPage]; 
    	if (PAGESIZE>0&&isToLoaded && isFromLoaded)
	    	return;
    	if (PAGESIZE>0&&isToLoaded!=undefined&&isFromLoaded!=undefined)
    		return;
        //console.debug("requested from "+from+" to "+to+" @ "+fromPage+"-"+toPage);
	    if (from < 0)
	        from = 0;
	      
	    if (data.length > 0)
	    	to = Math.min(to, data.length - 1);
	    
	    var startPage = -1;
	    if (from==0)
	    	startPage=1;
	    else {
	        for (var i = fromPage; i <= toPage; i++)
	        	if (loadedPages[i]==undefined){
	        		loadedPages[i] = false;
	        		if (startPage==-1)
	        			startPage = i;
	        	}
	        if (startPage==-1)
	        	return;
	    }
	    var url = options.dataURL + "?page=" + startPage + "&rows="+PAGESIZE+"&fetch="+Math.max((toPage-startPage+1)*PAGESIZE,PAGESIZE);
	    
	    if (sortCols&&sortCols.length>0){
	    	var sortDef = sortCols[0];
	    	url += "&sidx=" + sortDef.sortCol.field + "&sord="+(sortDef.sortAsc ? "asc" : "desc");
	    }

	    var h_request = setTimeout(function () {
	    	onDataLoading.notify({from: from, to: to});
	    	
	    	var postData = {};
	    	if (options.onBeforeDataRequest)
	    		options.onBeforeDataRequest.call(this, postData);
	    	
	    	var req = $.ajax({
				url: url,
				cache : false,
				dataType : "json",
				data: JexpUI.AjaxUtils.serialize(postData),
				success: onSuccess,
				error: function (jqXHR, textStatus, errorThrown) {
					onError(jqXHR.status, startPage, toPage, url)
				},
				complete:function(jqXHR,textStatus){
				}
			});
			req.fromPage = startPage;
			req.toPage = toPage;
			req.to = to;
	    }, 10);
    }

    function onError(status, fromPage, toPage, url) {
    	alert("Error loading pages " + fromPage + " to " + toPage +" from "+url);
    }

    function onSuccess(resp,textStatus,xhr) {
    	if (resp.count==0){
    		onDataLoaded.notify({ from: 0, to: 0 });
    		return;
    	}
      //var from = resp.request.start, to = from + resp.results.length;
      //data.length = Math.min(parseInt(resp.hits),1000); // limitation of the API
      var from = (xhr.fromPage-1) * PAGESIZE, to = from+resp.data.length-1;
      //console.debug("setting data from "+from+" to "+(from+resp.data.length-1));
      data.length = resp.count;

      for (var i = 0; i < resp.data.length; i++) {
    	  var item = resp.data[i];

          // Old IE versions can't parse ISO dates, so change to universally-supported format.
          //item.create_ts = item.create_ts.replace(/^(\d+)-(\d+)-(\d+)T(\d+:\d+:\d+)Z$/, "$2/$3/$1 $4 UTC");
          //item.create_ts = new Date(item.create_ts);

          data[from + i] = item;
          data[from + i].index = from + i;    	  
      }
      for (var i = xhr.fromPage; i <= xhr.toPage; i++) {
    	  loadedPages[i]=true;
      }
      onDataLoaded.notify({ from: from, to: to });
    }

    function reloadData(from, to) {
      for (var i = from; i <= to; i++)
        delete data[i];
      ensureData(from, to);
    }

    function setSort(sortColsArr) {
    	sortCols = sortColsArr;
    }

    function setSearch(str) {
      searchstr = str;
      clear();
    }


    init();

    return {
      // properties
      "data": data,

      // methods
      "clear": clear,
      "isDataLoaded": isDataLoaded,
      "ensureData": ensureData,
      "reloadData": reloadData,
      "setSort": setSort,
      "setSearch": setSearch,

      // events
      "onDataLoading": onDataLoading,
      "onDataLoaded": onDataLoaded
    };
  }

  // Slick.Data.RemoteModel
  $.extend(true, window, { Slick: { Data: { RemoteModel: RemoteModel }}});
})(jQuery);