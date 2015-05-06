package com.javexpress.gwt.library.ui.map;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.ResourceInjector;
import com.javexpress.gwt.library.ui.map.model.GeoLocation;
import com.javexpress.gwt.library.ui.map.model.GeoMarker;
import com.javexpress.gwt.library.ui.map.model.TravelModeEnum;

public class GMaps extends BaseMapPanel {

	//AIzaSyBz8p4t5rq2BpPV0ZxTIyYEKGP1Y_Jb2-s
	private volatile static boolean	API_INJECTED	= false;

	private volatile boolean		mapInited		= false;

	private JavaScriptObject		directionRenderer;

	private MapsListener			listener;

	public MapsListener getListener() {
		return listener;
	}

	public void setListener(MapsListener listener) {
		this.listener = listener;
	}

	public GMaps(Widget parent, String id, boolean fitToParent, String header) {
		super(parent, id, fitToParent, header);
		options.set("panControl", false);
		options.set("zoomControl", true);
		options.set("mapTypeControl", true);
		options.set("scaleControl", true);
		options.set("streetViewControl", false);
		options.set("overviewMapControl", true);
		options.set("drawingTools", false);
	}

	public void setDrawingTools(boolean drawingTools) {
		options.set("drawingTools", drawingTools);
	}

	public boolean isDrawingTools() {
		return options.getBoolean("drawingTools");
	}

	@Override
	protected boolean hasDirectionService() {
		return true;
	}

	private native void attachInitListener(GMaps x) /*-{
		$wnd.gmapInitialized = function() {
			x.@com.javexpress.gwt.library.ui.map.GMaps::fireGMapInjected()();
		}
	}-*/;

	private void fireGMapInjected() {
		API_INJECTED = true;
		detachInitListener();
		assertCenterLocation();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				if (isAttached() && !mapInited) {
					jsMap = createByJs(GMaps.this, getContainer(), options.getJavaScriptObject(), getCenterLocation().getLatitude(), getCenterLocation().getLongitude());
					mapInited = true;
				}
			}
		});
	}

	private native void detachInitListener() /*-{
		$wnd.gmapInitialized = null;
	}-*/;

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!API_INJECTED) {
			attachInitListener(this);
			String libraries = "";
			if (isDrawingTools())
				libraries += "&libraries=drawing";
			String key = listener != null ? "key=" + listener.getApiKey() : "";
			ResourceInjector.injectScript("https://maps.googleapis.com/maps/api/js?" + key + "&sensor=true&language=" + LocaleInfo.getCurrentLocale().getLocaleName() + libraries + "&callback=gmapInitialized", new Callback<Void, Exception>() {
				@Override
				public void onSuccess(Void result) {
				}

				@Override
				public void onFailure(Exception reason) {
				}
			});
		} else {
			assertCenterLocation();
			jsMap = createByJs(GMaps.this, getContainer(), options.getJavaScriptObject(), getCenterLocation().getLatitude(), getCenterLocation().getLongitude());
			mapInited = true;
		}
	}

	private native JavaScriptObject createByJs(GMaps x, Element element, JavaScriptObject options, double plat, double plng) /*-{
		options.center = {
			lat : plat,
			lng : plng
		};
		if (options.mapTypeControl)
			mapTypeControlOptions = {
				style : $wnd.google.maps.MapTypeControlStyle.DROPDOWN_MENU
			};
		if (options.zoomControl)
			zoomControlOptions = {
				style : $wnd.google.maps.ZoomControlStyle.SMALL
			};

		var map = new $wnd.google.maps.Map(element, options);
		
		if (options.drawingTools){
	        var polyOptions = {
	        	strokeWeight: 0,
	          	fillOpacity: 0.45,
	          	editable: true
	        };
			var drawingManager = new $wnd.google.maps.drawing.DrawingManager({
          		drawingMode: $wnd.google.maps.drawing.OverlayType.POLYGON,
    			drawingControl: true,
    			drawingControlOptions: {
      				position: $wnd.google.maps.ControlPosition.TOP_CENTER,
      				drawingModes: [
				        $wnd.google.maps.drawing.OverlayType.MARKER,
				        $wnd.google.maps.drawing.OverlayType.CIRCLE,
				        $wnd.google.maps.drawing.OverlayType.POLYGON,
				        $wnd.google.maps.drawing.OverlayType.POLYLINE,
				        $wnd.google.maps.drawing.OverlayType.RECTANGLE
      				]
    			},     
    			markerOptions: {
		            draggable: true
	          	},
		        polylineOptions: {
		            editable: true
		        },
          		rectangleOptions: polyOptions,
          		circleOptions: polyOptions,
          		polygonOptions: polyOptions,
          		map: map
			});
			
      		var selectedShape;

      		function clearSelection() {
        		if (selectedShape) {
          			selectedShape.setEditable(false);
          			selectedShape = null;
        		}
      		}

      		function setSelection(shape) {
        		clearSelection();
        		selectedShape = shape;
        		shape.setEditable(true);
      		}

      		function deleteSelectedShape() {
        		if (selectedShape){
          			selectedShape.setMap(null);
          			selectedShape = null;
        		}
      		}
			
	        $wnd.google.maps.event.addListener(drawingManager, 'overlaycomplete', function(event) {
	        	$wnd.console.debug(event);
	            if (event.type != $wnd.google.maps.drawing.OverlayType.MARKER) {
		            // Switch back to non-drawing mode after drawing a shape.
		            drawingManager.setDrawingMode(null);
	
		            // Add an event listener that selects the newly-drawn shape when the user
		            // mouses down on it.
		            var newShape = event.overlay;
		            newShape.type = event.type;
		            $wnd.google.maps.event.addListener(newShape, 'click', function() {
						setSelection(newShape);
		            });
		            setSelection(newShape);
					x.@com.javexpress.gwt.library.ui.map.GMaps::fireNewShapeAdded(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(event,newShape);
	          	}
	        });
		}

		$wnd.google.maps.event.addListener(map, 'rightclick', function(event) {
			var latLng = event.latLng;
			x.@com.javexpress.gwt.library.ui.map.GMaps::fireContextMenu(DD)(latLng.lat(),latLng.lng());
			event.stop();
		});
		return map;
	}-*/;

	@Override
	public void onResize() {
		if (mapInited)
			_resize(jsMap);
	}

	private native void _resize(JavaScriptObject map) /*-{
		$wnd.google.maps.event.trigger(map, "resize");
	}-*/;

	@Override
	protected void onUnload() {
		if (markers != null)
			for (JavaScriptObject jsMarker : markers.values())
				deleteMarker(jsMarker);
		destroyByJs(getContainer(), directionRenderer, jsMap);
		directionRenderer = null;
		super.onUnload();
	}

	private native void destroyByJs(Element element, JavaScriptObject directionRenderer, JavaScriptObject map) /*-{
		if (directionRenderer) {
			$wnd.google.maps.event.clearInstanceListeners(directionRenderer);
			delete directionRenderer;
		}
		$wnd.google.maps.event.clearInstanceListeners(map);
		$wnd.$(element).empty().off();
		delete map;
	}-*/;

	@Override
	protected JavaScriptObject renderMarker(GeoMarker marker) {
		if (!mapInited)
			return null;
		return _renderMarker(jsMap, marker, marker.getLatitude(), marker.getLongitude(), marker.getTitle(), marker.isDraggable(), marker.getIcon());
	}

	private native JavaScriptObject _renderMarker(JavaScriptObject map, GeoMarker marker, double plat, double plng, String ptitle, boolean pdraggable, String picon) /*-{
		var opts = {
			position : new $wnd.google.maps.LatLng(plat, plng),
			map : map,
			draggable : pdraggable,
			title : ptitle
			//animation : $wnd.google.maps.Animation.BOUNCE
		};
		if (picon)
			opts.icon = picon;
		var m = new $wnd.google.maps.Marker(opts);
		$wnd.google.maps.event.addListener(m, 'click', function(event) {
			marker.@com.javexpress.gwt.library.ui.map.model.GeoMarker::fireClicked()();
		});
		$wnd.google.maps.event.addListener(m, 'rightclick', function(event) {
			var latLng = event.latLng;
			marker.@com.javexpress.gwt.library.ui.map.model.GeoMarker::fireContextMenu(DD)(latLng.lat(),latLng.lng());
			event.stop();
		});
		$wnd.google.maps.event.addListener(m, 'dblclick', function(event) {
			marker.@com.javexpress.gwt.library.ui.map.model.GeoMarker::fireDoubleClicked()();
		});
		$wnd.google.maps.event.addListener(m, 'dragend', function(event) {
			marker.@com.javexpress.gwt.library.ui.map.model.GeoMarker::fireDragged(DD)(event.latLng.lat(), event.latLng.lng());
		});
		return m;
	}-*/;

	@Override
	protected void deleteMarker(JavaScriptObject jsMarker) {
		_deleteMarker(jsMarker);
	}

	private native JavaScriptObject _deleteMarker(JavaScriptObject jsMarker) /*-{
		$wnd.google.maps.event.clearInstanceListeners(jsMarker);
		jsMarker.setMap(null);
		delete jsMarker;
	}-*/;

	@Override
	protected GeoLocation getMarkerPosition(JavaScriptObject jsMarker) {
		JsonMap latlng = new JsonMap(_getMarkerPosition(jsMarker));
		return new GeoLocation(latlng.getDouble("lat"), latlng.getDouble("lng"));
	}

	private native JavaScriptObject _getMarkerPosition(JavaScriptObject jsMarker) /*-{
		var p = jsMarker.getPosition();
		return {
			lat : p.lat(),
			lng : p.lng()
		};
	}-*/;

	@Override
	protected void panTo(GeoLocation location) {
		_panTo(jsMap, location.getLatitude(), location.getLongitude());
	}

	private native JavaScriptObject _panTo(JavaScriptObject map, double lat, double lng) /*-{
		map.panTo(new $wnd.google.maps.LatLng(lat, lng));
	}-*/;

	@Override
	protected void clearDirections() {
		_clearDirectionListener(directionRenderer);
		directionRenderer = null;
		super.clearDirections();
	}

	private native void _clearDirectionListener(JavaScriptObject directionRenderer) /*-{
		if (directionRenderer) {
			$wnd.google.maps.event.clearInstanceListeners(directionRenderer);
			delete directionRenderer;
		}
	}-*/;

	@Override
	protected void showDirections(double fromLat, double fromLng, double toLat, double toLng, TravelModeEnum mode) {
		directionRenderer = _showDirections(this, jsMap, getDirectionsPanel(), directionRenderer, fromLat, fromLng, toLat, toLng, mode.toString());
	}

	private native JavaScriptObject _showDirections(GMaps x, JavaScriptObject jsMap, Element directionsPanel, JavaScriptObject jsDirectionRenderer, double fromLat, double fromLng, double toLat, double toLng, String travelMode) /*-{
		if (!$wnd.gmapDirectionsService)
			$wnd.gmapDirectionsService = new $wnd.google.maps.DirectionsService();

		//directionsRendererOptions.draggable=false;
		//directionsRendererOptions.hideRouteList=true;
		//directionsRendererOptions.suppressMarkers=false;
		//directionsRendererOptions.preserveViewport=false;
		var directionsDisplay = jsDirectionRenderer
				|| new $wnd.google.maps.DirectionsRenderer();
		directionsDisplay.setMap(jsMap);
		//if (directionsPanel)
		//directionsDisplay.setPanel(directionsPanel);

		var request = {
			origin : new $wnd.google.maps.LatLng(fromLat, fromLng),
			destination : new $wnd.google.maps.LatLng(toLat, toLng),
			travelMode : $wnd.google.maps.TravelMode[travelMode]
		};
		$wnd.gmapDirectionsService.route(request, function(response, status) {
			if (status == $wnd.google.maps.DirectionsStatus.OK) {
				directionsDisplay.setDirections(response);
			}
		});
		$wnd.google.maps.event
				.addListener(
						directionsDisplay,
						'directions_changed',
						function() {
							var result = directionsDisplay.getDirections();
							var total = 0;
							var myroute = result.routes[0];
							for (var i = 0; i < myroute.legs.length; i++) {
								total += myroute.legs[i].distance.value;
							}
							x.@com.javexpress.gwt.library.ui.map.GMaps::fireDirectionsChanged(I)(total);
						});

		return directionsDisplay;
	}-*/;

	public JavaScriptObject drawPolygon(JsArray<JsArrayNumber> path) {
		return _drawPolygon(jsMap, path);
	}

	private native JavaScriptObject _drawPolygon(JavaScriptObject map, JsArray<JsArrayNumber> path) /*-{
		var polygon = map.drawPolygon({
			paths : path, // pre-defined polygon shape
			strokeColor : '#BBD8E9',
			strokeOpacity : 1,
			strokeWeight : 3,
			fillColor : '#BBD8E9',
			fillOpacity : 0.6
		});
		return polygon;
	}-*/;

	//----EVENTS
	private void fireNewShapeAdded(JavaScriptObject event, JavaScriptObject shape) {
		if (listener != null)
			listener.onNewShapeAdded(event, shape);
	}

}