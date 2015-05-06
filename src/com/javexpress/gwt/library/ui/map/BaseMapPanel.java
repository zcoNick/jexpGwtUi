package com.javexpress.gwt.library.ui.map;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.container.panel.ContainerWithBar;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.map.model.GeoLocation;
import com.javexpress.gwt.library.ui.map.model.GeoMarker;
import com.javexpress.gwt.library.ui.map.model.IMapListener;
import com.javexpress.gwt.library.ui.map.model.TravelModeEnum;
import com.javexpress.gwt.library.ui.map.model.UserPosition;

public abstract class BaseMapPanel extends ContainerWithBar implements ISizeAwareWidget {

	public static final long					HOME_MARKER_ID	= Long.MIN_VALUE;

	protected JsonMap							options;
	protected JavaScriptObject					jsMap;
	protected Map<GeoMarker, JavaScriptObject>	markers;

	private IMapListener						mapListener;
	private Element								homeButton;
	private Element								markerButton;
	private Element								directionsPanel;

	private GeoLocation							centerLocation;
	private GeoLocation							home;
	private int									allowedMarkers	= 0;

	private com.google.gwt.user.client.Element	topPanel;

	public IMapListener getMapListener() {
		return mapListener;
	}

	public void setMapListener(IMapListener mapListener) {
		this.mapListener = mapListener;
	}

	public GeoLocation getCenterLocation() {
		return centerLocation;
	}

	public void setCenterLocation(GeoLocation centerLocation) {
		this.centerLocation = centerLocation;
		if (isAttached())
			panTo(centerLocation);
	}

	public GeoLocation getHome() {
		return home;
	}

	public void setHome(GeoLocation home) {
		this.home = home;
		if (isAttached()) {
			addHomeButton();
		}
	}

	public void addToolItem(MapToolItem ti) {
		addToolItemElement(ti.getId(), ti.getIcon(), ti.getCaption(), ti.getHint(), ti.getIconClass(), true);
	}

	public int getAllowedMarkers() {
		return allowedMarkers;
	}

	public void setAllowedMarkers(int allowedMarkers) {
		this.allowedMarkers = allowedMarkers;
	}

	public Map<GeoMarker, JavaScriptObject> getMarkers() {
		return markers;
	}

	public void addMarker(GeoMarker marker) {
		if (marker.getLatitude() == Double.MIN_VALUE || marker.getLongitude() == Double.MIN_VALUE)
			return;
		if (markers == null)
			markers = new LinkedHashMap<GeoMarker, JavaScriptObject>();
		if (marker.getIcon() == null)
			marker.setIcon(JsUtil.getImagesPath() + "mapMarker.png");
		JavaScriptObject jsMarker = renderMarker(marker);
		if (jsMarker != null) {
			markers.put(marker, jsMarker);
		}
	}

	public void removeMarker(GeoMarker marker) {
		if (markers == null)
			return;
		JavaScriptObject jsMarker = markers.get(marker);
		if (jsMarker != null) {
			deleteMarker(jsMarker);
			marker.fireDeleted();
			markers.remove(marker);
		}
	}

	protected abstract JavaScriptObject renderMarker(GeoMarker marker);

	protected abstract void deleteMarker(JavaScriptObject jsMarker);

	protected abstract void panTo(GeoLocation location);

	protected void addHomeMarker() {
		GeoMarker m = getMarker(HOME_MARKER_ID);
		if (m != null)
			return;
		m = new GeoMarker();
		m.setId(HOME_MARKER_ID);
		m.setDraggable(true);
		m.setLatitude(home.getLatitude());
		m.setLongitude(home.getLongitude());
		m.setIcon(JsUtil.getImagesPath() + "homeFlag.png");
		addMarker(m);
	}

	public GeoMarker getMarker(long id) {
		if (markers == null)
			return null;
		for (GeoMarker m : markers.keySet())
			if (m.getId() == id)
				return m;
		return null;
	}

	protected void addHomeButton() {
		if (home == null || homeButton != null)
			return;
		homeButton = addToolItemElement("home", FaIcon.home, null, null, "green", true);
		_bindToElement(new Command() {
			@Override
			public void execute() {
				panTo(home);
			}
		}, homeButton);
	}

	protected void addAppendMarkerButton() {
		if (markerButton != null)
			return;
		markerButton = addToolItemElement("addmarker", FaIcon.plus, null, null, "purple", true);
		_bindToElement(new Command() {
			@Override
			public void execute() {
				GeoLocation l = centerLocation != null ? centerLocation : home;
				fireAddMarker(l != null ? l.getLatitude() : Double.MIN_VALUE, l != null ? l.getLongitude() : Double.MIN_VALUE);
			}
		}, markerButton);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		addHomeButton();
	}

	protected void fireAddMarker(double latitude, double longitude) {
		if (mapListener == null)
			return;
		int count = 0;
		if (markers != null) {
			count = markers.size();
			if (home != null)
				count--;
		}
		if (count < allowedMarkers) {
			mapListener.createNewMarker(latitude != Double.MIN_VALUE ? latitude : null, longitude != Double.MIN_VALUE ? longitude : null);
		}
	}

	public GeoLocation getMarkerPosition(long id) {
		GeoMarker m = getMarker(id);
		if (m == null)
			return null;
		return getMarkerPosition(markers.get(m));
	}

	protected abstract boolean hasDirectionService();

	protected abstract GeoLocation getMarkerPosition(JavaScriptObject jsMarker);

	public void showDirections(long fromMarkerId, long toMarkerId, TravelModeEnum mode) {
		GeoMarker from = getMarker(fromMarkerId);
		GeoMarker to = getMarker(toMarkerId);
		showDirections(from, to, mode);
	}

	protected void clearDirections() {
		if (directionsPanel != null)
			directionsPanel.removeFromParent();
		directionsPanel = null;
		fireDirectionsChanged(-1);
	}

	public void showDirections(GeoMarker from, GeoMarker to, TravelModeEnum mode) {
		clearDirections();
		if (!hasDirectionService())
			return;
		directionsPanel = DOM.createDiv();
		showDirections(from.getLatitude(), from.getLongitude(), to.getLatitude(), to.getLongitude(), mode);
	}

	protected Element getDirectionsPanel() {
		return directionsPanel;
	}

	protected abstract void showDirections(double fromLat, double fromLng, double toLat, double toLng, TravelModeEnum mode);

	protected native void _bindToElement(Command cmd, Element el) /*-{
		$wnd.$(el).click(function(e) {
			cmd.@com.google.gwt.user.client.Command::execute()();
		});
	}-*/;

	public BaseMapPanel(Widget parent, final String id, boolean fitToParent, String header) {
		super(fitToParent);
		JsUtil.ensureId(parent, this, WidgetConst.MAPPANEL_PREFIX, id);

		topPanel = header != null ? DOM.createDiv() : null;
		if (topPanel != null) {
			topPanel.setClassName("grid-header");
			topPanel.getStyle().setPosition(Position.ABSOLUTE);
			topPanel.getStyle().setDisplay(Display.BLOCK);
			topPanel.getStyle().setTop(0, Unit.PX);
			topPanel.getStyle().setLeft(0, Unit.PX);
			topPanel.getStyle().setRight(0, Unit.PX);
			topPanel.getStyle().setHeight(18, Unit.PX);
			setHeader(header);
		}

		createPanels();

		getElement().addClassName("jexpMapsContainer");
		getToolContainer().addClassName("jexpGridToolbar");
		createDefaultOptions();
	}

	@Override
	protected int createTopPanel() {
		return topPanel != null ? 18 : 0;
	}

	public void setHeader(String title) {
		if (topPanel != null)
			topPanel.setInnerHTML("<label>" + title + "</label>");
	}

	private JsonMap createDefaultOptions() {
		options = new JsonMap();
		setZoom(15);
		return options;
	}

	public void setZoom(int zoom) {
		options.setInt("zoom", zoom);
	}

	public int getZoom() {
		return options.getInt("zoom");
	}

	protected UserPosition getUserLocationDefault(double latitude, double longitude) {
		try {
			return getUserLocation();
		} catch (Exception e) {
			return new UserPosition(latitude, longitude);
		}
	}

	protected UserPosition getUserLocation() throws Exception {
		JsonMap gl = new JsonMap(_getUserLocation());
		if (gl.containsKey("error"))
			throw new Exception(gl.getString("error"));
		/*
		coords.latitude 	The latitude as a decimal number
		coords.longitude 	The longitude as a decimal number
		coords.accuracy 	The accuracy of position
		coords.altitude 	The altitude in meters above the mean sea level
		coords.altitudeAccuracy 	The altitude accuracy of position
		coords.heading 	The heading as degrees clockwise from North
		coords.speed 	The speed in meters per second
		timestamp 	The date/time of the response
		 */
		JsonMap coords = new JsonMap(gl.get("coords").isObject().getJavaScriptObject());
		UserPosition l = new UserPosition();
		l.setLatitude(coords.getLong("latitude"));
		l.setLongitude(coords.getLong("longitude"));
		l.setAccuracy(coords.getInt("accuracy"));
		return l;
	}

	private native JavaScriptObject _getUserLocation() /*-{
		var result = {};
		var fSuccess = function(position) {
			$wnd.console.debug("geoposition:", position);
			result.timestamp = position.timestamp;
			result.coords = position.coords;
		};
		var fError = function(error) {
			$wnd.console.debug("geoError:", error);
			switch (error.code) {
			case error.PERMISSION_DENIED:
				result.error = "PERMISSION_DENIED";
				break;
			case error.POSITION_UNAVAILABLE:
				result.error = "POSITION_UNAVAILABLE";
				break;
			case error.TIMEOUT:
				result.error = "TIMEOUT";
				break;
			case error.UNKNOWN_ERROR:
				result.error = "UNKNOWN_ERROR";
				break;
			}
		};
		if ($wnd.navigator.geolocation)
			$wnd.navigator.geolocation.getCurrentPosition(fSuccess, fError);
		return result;
	}-*/;

	protected void assertCenterLocation() {
		if (centerLocation == null) {
			if (home != null)
				centerLocation = home.createCopy();
			else
				centerLocation = getUserLocationDefault(39.934486, 32.853241);
		}
	}

	@Override
	protected void onUnload() {
		options = null;
		jsMap = null;
		markers = null;
		homeButton = null;
		markerButton = null;
		mapListener = null;
		super.onUnload();
	}

	public void fireContextMenu(double latitude, double longitude) {
		Window.alert(latitude + "," + longitude);
	}

	public void fireDirectionsChanged(int totalDistance) {
		Window.alert("Total " + totalDistance);
	}

	public void clearMarkers(boolean keepHomeMarker) {
		if (markers != null)
			for (GeoMarker m : markers.keySet())
				if (m.getId() != HOME_MARKER_ID)
					removeMarker(m);
	}

	public String getMarkerLatLng(long markerId) {
		GeoMarker marker = getMarker(markerId);
		if (marker == null)
			return null;
		return marker.getLatLng();
	}

}