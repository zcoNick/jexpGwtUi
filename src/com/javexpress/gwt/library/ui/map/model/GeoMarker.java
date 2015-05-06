package com.javexpress.gwt.library.ui.map.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;

public class GeoMarker {

	private long				id;
	private double				latitude	= Double.MIN_VALUE,
											longitude = Double.MIN_VALUE;
	private boolean				draggable;
	private JavaScriptObject	map;
	private String				title;
	private GeoMarkerListener	listener;
	private String				icon;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public JavaScriptObject getMap() {
		return map;
	}

	@Deprecated
	public void setMap(JavaScriptObject map) {
		this.map = map;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLatLng(String latLng) {
		String[] ll = latLng.split(",");
		this.latitude = Double.valueOf(ll[0]);
		this.longitude = Double.valueOf(ll[1]);
	}

	public String getLatLng() {
		return this.latitude + "," + this.longitude;
	}

	public GeoMarkerListener getListener() {
		return listener;
	}

	public void setListener(GeoMarkerListener listener) {
		this.listener = listener;
	}

	public void fireClicked() {
		if (listener != null)
			listener.markerClicked();
	}

	public void fireDoubleClicked() {
		if (listener != null)
			listener.markerDoubleClicked();
	}

	public void fireDeleted() {
		if (listener != null)
			listener.markerDeleted();
	}

	public void fireDragged(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		if (listener != null)
			listener.markerDragged(latitude, longitude);
	}

	public void fireContextMenu(double latitude, double longitude) {
		Window.alert(title);
	}

}