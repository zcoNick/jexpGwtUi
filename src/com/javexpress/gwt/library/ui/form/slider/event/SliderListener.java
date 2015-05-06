package com.javexpress.gwt.library.ui.form.slider.event;

public abstract class SliderListener {

	public abstract void onStart(SliderEvent e);

	public abstract boolean onSlide(SliderEvent e);

	public abstract void onChange(SliderEvent e);

	public abstract void onStop(SliderEvent e);

}