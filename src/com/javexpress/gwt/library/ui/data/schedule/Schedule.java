package com.javexpress.gwt.library.ui.data.schedule;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.IJsonServicePoint;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.JexpWidget;
import com.javexpress.gwt.library.ui.data.IDataChangeListener;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class Schedule extends JexpWidget implements IDataChangeListener {

	public static void fillResources(final WidgetBundles wb) {
		wb.addStyleSheet("scripts/schedule/fullcalendar.css");
		wb.addJavaScript("scripts/schedule/fullcalendar-2.0.2.min.js");
	}

	private String				dataUrl;
	protected JsonMap			options;
	private IScheduleListener	listener;

	public Schedule(final Widget parent, final String id, final IJsonServicePoint servicePoint) {
		super();
		setElement(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.SCHEDULE_PREFIX, id);
		getElement().addClassName((JsUtil.USE_BOOTSTRAP ? "" : "jexpBorderBox ") + "jexpSchedule");
		this.dataUrl = JsUtil.getServiceUrl(servicePoint);
		this.options = createDefaultOptions();
	}

	private JsonMap createDefaultOptions() {
		options = new JsonMap();
		options.set("editable", true);
		options.set("selectable", true);
		setFirstHour(7);
		setSlotMinutes(30);
		return options;
	}

	public void setSlotMinutes(final int val) {
		options.setInt("slotMinutes", val);
	}

	public void setFirstHour(final int val) {
		options.setInt("firstHour", val);
	}

	public void setAspectRatio(final double val) {
		options.set("aspectRatio", val);
	}

	public IScheduleListener getListener() {
		return listener;
	}

	public void setListener(final IScheduleListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, getElement(), options.getJavaScriptObject(), dataUrl);
	}

	private native void createByJs(Schedule x, Element element, JavaScriptObject options, String dataUrl) /*-{
		var el = $wnd.$(element);
		var opts = {
			theme : true,
			header : {
				left : 'prev,next today',
				center : 'title',
				right : 'agendaDay,agendaWeek,month'
			},
			editable : options.editable,
			selectable : options.selectable,
			selectHelper : true,
			slotMinutes : options.slotMinutes,
			firstHour : options.firstHour,
			monthNames : [ 'Ocak', 'Şubat', 'Mart', 'Nisan', 'Mayıs',
					'Haziran', 'Temmuz', 'Ağustos', 'Eylül', 'Ekim', 'Kasım',
					'Aralık' ],
			monthNamesShort : [ 'Oca', 'Şub', 'Mar', 'Nis', 'May', 'Haz',
					'Tem', 'Ağu', 'Eyl', 'Eki', 'Kas', 'Ara' ],
			dayNames : [ 'Pazar', 'Pazartesi', 'Salı', 'Çarşamba', 'Perşembe',
					'Cuma', 'Cumartesi' ],
			dayNamesShort : [ 'Paz', 'Pzt', 'Sal', 'Çrş', 'Prş', 'Cum', 'Cmt' ],
			buttonText : {
				today : 'Bugün',
				month : 'Ay',
				week : 'Hafta',
				day : 'Gün'
			},
			defaultView : 'agendaWeek',
			firstHour : 8,
			allDayText : 'Tüm Gün',
			defaultEventMinutes : 60,
			timeFormat : 'H(:mm)',
			eventClick : function(calEvent, jsEvent, view) {
				x.@com.javexpress.gwt.library.ui.data.schedule.Schedule::fireOnEvent(Ljava/lang/String;Ljava/lang/String;)(calEvent.id+"",view.name);
			},
			select : function(s, e, allday) {
				x.@com.javexpress.gwt.library.ui.data.schedule.Schedule::fireOnAdd(Lcom/google/gwt/core/client/JsDate;Lcom/google/gwt/core/client/JsDate;)(s,e);
				el.fullCalendar('unselect');
			},
			eventDrop : function(event, dayDelta, minuteDelta, allDay,
					revertFunc, jsEvent, ui, view) {
				x.@com.javexpress.gwt.library.ui.data.schedule.Schedule::fireOnMove(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JsDate;Lcom/google/gwt/core/client/JsDate;)(event, event.start, event.end);
			},
			eventResize : function(event, dayDelta, minuteDelta, revertFunc,
					jsEvent, ui, view) {
				x.@com.javexpress.gwt.library.ui.data.schedule.Schedule::fireOnResize(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JsDate;Lcom/google/gwt/core/client/JsDate;)(event, event.start, event.end);
			},
			eventSources : [ {
				events : function(start, end, callback) {
					var postData = {
						start : Math.round(start.getTime() / 1000),
						end : Math.round(end.getTime() / 1000)
					};
					x.@com.javexpress.gwt.library.ui.data.schedule.Schedule::preparePostData(Lcom/google/gwt/core/client/JavaScriptObject;)(postData);
					$wnd.$.ajax({
						url : dataUrl,
						type : "POST",
						cache : false,
						dataType : "json",
						data : postData,
						success : function(json) {
							var events = [];
							for (var i = 0; i < json.length; i++) {
								events.push({
									id : json[i].id,
									start : json[i].start,
									end : json[i].end,
									title : json[i].title,
									backgroundColor : json[i].backgroundColor,
									editable : json[i].editable,
									allDay : json[i].allDay
								});
							}
							callback(events);
						}
					});
				}
			} ]
		};
		if (options.aspectRatio)
			opts.aspectRatio = options.aspectRatio;
		else
			opts.contentHeight = el.height() - 42;
		el.fullCalendar(opts);
	}-*/;

	@Override
	public void refresh(final Serializable result) {
		_refresh(getElement());
	}

	private native void _refresh(final Element element) /*-{
		$wnd.$(element).fullCalendar('refetchEvents');
	}-*/;

	@Override
	protected void onUnload() {
		dataUrl = null;
		options = null;
		listener = null;
		destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).fullCalendar("destroy");
	}-*/;

	//---------- EVENTS
	public void preparePostData(final JavaScriptObject jsobject) {
		JsonMap opts = new JsonMap(jsobject);
		if (listener != null) {
			listener.beforeDataRequest(opts);
		}
	}

	public void fireOnEvent(final String id, final String view) {
		if (listener != null)
			listener.modifyItem(id, view);
	}

	public void fireOnAdd(final JsDate start, final JsDate end) {
		if (listener != null) {
			listener.addItem(new Date((long) start.getTime()), new Date((long) end.getTime()));
		}
	}

	public void fireOnMove(final JavaScriptObject event, final JsDate start, final JsDate end) {
		if (listener != null) {
			JsonMap data = new JsonMap(event);
			Long id = data.getLong("id");
			listener.itemMoved(data, id, new Date((long) start.getTime()), new Date((long) end.getTime()));
		}
	}

	public void fireOnResize(final JavaScriptObject event, final JsDate start, final JsDate end) {
		if (listener != null) {
			JsonMap data = new JsonMap(event);
			Long id = data.getLong("id");
			listener.itemResized(data, id, new Date((long) start.getTime()), new Date((long) end.getTime()));
		}
	}

}