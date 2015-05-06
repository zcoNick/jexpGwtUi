package com.javexpress.gwt.library.ui.map.model;

import java.io.Serializable;

public class GeoLocation implements Serializable {

	private double	latitude, longitude;

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

	public GeoLocation() {
	}

	public GeoLocation(String latLng) {
		String[] ll = latLng.split(",");
		latitude = Double.valueOf(ll[0]);
		longitude = Double.valueOf(ll[1]);
	}

	public GeoLocation(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String asLatLng() {
		return latitude + "," + longitude;
	}

	public GeoLocation createCopy() {
		return new GeoLocation(latitude, longitude);
	}

}