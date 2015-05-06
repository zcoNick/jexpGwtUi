package com.javexpress.gwt.library.ui.map.model;

public class UserPosition extends GeoLocation {

	private int	accuracy;

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public UserPosition() {
		super();
	}

	public UserPosition(double latitude, double longitude) {
		super(latitude, longitude);
	}

	public UserPosition(String latLng) {
		super(latLng);
	}

}