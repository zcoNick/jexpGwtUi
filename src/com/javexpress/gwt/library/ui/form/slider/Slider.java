package com.javexpress.gwt.library.ui.form.slider;

import java.beans.Beans;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.JexpWidget;
import com.javexpress.gwt.library.ui.form.slider.event.SliderEvent;
import com.javexpress.gwt.library.ui.form.slider.event.SliderListener;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class Slider extends JexpWidget {

	private JSONObject options;
    private List<SliderListener> listeners;

	public Slider(final Widget parent, final String id, final JSONObject options) {           
	    super();
	    JsUtil.ensureId(parent, this, WidgetConst.SLIDER_PREFIX, id);
    
		this.options = options;
		if (this.options == null) {
			this.options = getOptions(0, 100, new int[] { 0 });
		}
	}
	
	public Slider(final Widget parent, final String id, final int min, final int max, final int defaultValue)
	{
	    this(parent, id, min, max, new int[]{defaultValue});
	}
	    
	public Slider(final Widget parent, final String id, final int min, final int max, final int[] defaultValues)
	{           
	    this(parent, id, getOptions(min, max, defaultValues));
	}
	
    @Override
    protected void onLoad()
    {		
    	if (!Beans.isDesignTime()){
	        if (options == null) 
	            options = new JSONObject();
	        createSliderJS(this, getElement().getId(), options.getJavaScriptObject());
    	}
        super.onLoad();
    }
    
    private native void createSliderJS(Slider x, String id, JavaScriptObject options) /*-{
        options.start = function(event, ui) {
            x.@com.javexpress.gwt.library.ui.form.slider.Slider::fireOnStartEvent(Lcom/google/gwt/user/client/Event;Lcom/google/gwt/core/client/JsArrayInteger;)(event, ui.values);
        };
        options.slide = function(event, ui) {
            return x.@com.javexpress.gwt.library.ui.form.slider.Slider::fireOnSlideEvent(Lcom/google/gwt/user/client/Event;Lcom/google/gwt/core/client/JsArrayInteger;)(event, ui.values);
        };
        options.change = function(event, ui) {
            var has = event.originalEvent ? true : false;
            x.@com.javexpress.gwt.library.ui.form.slider.Slider::fireOnChangeEvent(Lcom/google/gwt/user/client/Event;Lcom/google/gwt/core/client/JsArrayInteger;Z)(event, ui.values, has);                
        };
        options.stop = function(event, ui) {
            x.@com.javexpress.gwt.library.ui.form.slider.Slider::fireOnStopEvent(Lcom/google/gwt/user/client/Event;Lcom/google/gwt/core/client/JsArrayInteger;)(event, ui.values);
        };
        $wnd.$("#" + id).slider(options);
    }-*/;
    
    private void fireOnStartEvent(final Event evt, final JsArrayInteger values)
    {
    	if (listeners==null||listeners.isEmpty())
    		return;
        int[] vals = JsUtil.jsArrayIntegerToIntArray(values);
        SliderEvent e = new SliderEvent(evt, this, vals);
        
		for (SliderListener l : listeners) {
            l.onStart(e);
        }
    }
    
    private boolean fireOnSlideEvent(final Event evt, final JsArrayInteger values)
    {
    	if (listeners==null||listeners.isEmpty())
    		return true;
        int[] vals = JsUtil.jsArrayIntegerToIntArray(values);
        SliderEvent e = new SliderEvent(evt, this, vals);
        
        for (SliderListener l : listeners)
            l.onStart(e);
        
        boolean ret = true;
        
        for (SliderListener l : listeners) {
            if (!l.onSlide(e)) {
                //if any of the listeners returns false, return false,
                //but let them all do their thing
                ret = false;
            }
        }
        return ret;
    }
    
    private void fireOnChangeEvent(final Event evt, final JsArrayInteger values, final boolean hasOriginalEvent)
    {
    	if (listeners==null||listeners.isEmpty())
    		return;
        int[] vals = JsUtil.jsArrayIntegerToIntArray(values);        
        SliderEvent e = new SliderEvent(evt, this, vals, hasOriginalEvent);
        for (SliderListener l : listeners) {
            l.onChange(e);
        }
    }

	private void fireOnStopEvent(final Event evt, final JsArrayInteger values)
    {
    	if (listeners==null||listeners.isEmpty())
    		return;
        int[] vals = JsUtil.jsArrayIntegerToIntArray(values);
        SliderEvent e = new SliderEvent(evt, this, vals);
        for (SliderListener l : listeners) {
            l.onStop(e);
        }
    }
    
    public int getValue()
    {
        return getValueAtIndex(0);
    }
    
    public void setValues(final int[] values)
    {
        JSONArray vals = intArrayToJSONArray(values);
        setValuesJS(getElement().getId(), vals.getJavaScriptObject());
    } 
        
    public int getValueAtIndex(final int index)
    {
        return getValueJS(getElement().getId(), index);
    }
    
    private native void setValuesJS(String id, JavaScriptObject values) /*-{
        $wnd.$("#" + id).slider("option", "values", values);
    }-*/;
    
    private native int getValueJS(String id, int index) /*-{
        return $wnd.$("#" + id).slider("values", index);
    }-*/;
    
    @Override
    protected void onUnload()
    {
    	options = null;
    	listeners = null;
        destroySliderJS(this, getElement().getId());
        super.onUnload();        
    }
    
    private native void destroySliderJS(Slider x, String id) /*-{
        $wnd.$("#" + id).slider("destroy");
    }-*/;
    
    /**
     * A convenient way to create an options JSONObject.  Use SliderOption 
     * for keys.
     *
     * @param min - default minimum of the slider
     * @param max - default maximum of the slider
     * @param defaultValues - default points of each anchor
     * @return a JSONObject of Slider options
     */
    public static JSONObject getOptions(final int min, final int max, final int[] defaultValues) 
    {
        JSONObject options = new JSONObject();
        options.put(SliderOption.MIN.toString(), new JSONNumber(min));
        options.put(SliderOption.MAX.toString(), new JSONNumber(max));
        JSONArray vals = intArrayToJSONArray(defaultValues);
        options.put(SliderOption.VALUES.toString(), vals);
        return options;
    }

    private static JSONArray intArrayToJSONArray(final int[] values)
    {
        JSONArray vals = new JSONArray(); 
        for (int i = 0, len = values.length; i < len; i++) {
            vals.set(i, new JSONNumber(values[i]));
        }
        return vals;
    }
    
    private static enum SliderOption {
    	MIN("min"),MAX("max"),VALUES("values");
    	private String opt;
    	private SliderOption(final String opt){
    		this.opt = opt;
    	}
    	@Override
    	public String toString() {
    		return opt;
    	}
    }

}