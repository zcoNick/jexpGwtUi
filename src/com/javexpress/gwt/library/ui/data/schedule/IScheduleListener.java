package com.javexpress.gwt.library.ui.data.schedule;

import java.util.Date;

import com.javexpress.gwt.library.ui.js.JsonMap;

public interface IScheduleListener {
	
	public void addItem(Date start, Date end);
	public void modifyItem(String id, String view);
	public void beforeDataRequest(JsonMap postData);
	public void itemMoved(JsonMap data, Long id, Date start, Date end);
	public void itemResized(JsonMap data, Long id, Date start, Date end);

}