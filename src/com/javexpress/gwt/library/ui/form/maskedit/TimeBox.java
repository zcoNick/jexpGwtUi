package com.javexpress.gwt.library.ui.form.maskedit;

import java.util.Date;

import com.google.gwt.user.client.ui.Widget;

public class TimeBox extends MaskEditBox {

	public TimeBox(final Widget parent, final String id) {
		this(parent, id, false);
	}

	public TimeBox(final Widget parent, final String id, final boolean withSeconds) {
		super(parent, id, "hh:mm" + (withSeconds ? ":ss" : ""));
		setPlaceHolder("__/__" + (withSeconds ? "/__" : ""));
	}

	public void setValueDate(final Date date) {
		if (date == null) {
			setText(null);
			return;
		}
		String h = String.valueOf(date.getHours());
		if (h.length() == 1)
			h = "0" + h;
		String m = String.valueOf(date.getMinutes());
		if (m.length() == 1)
			m = "0" + m;
		h += ":" + m;
		if (getMask().length() > 5) {
			String s = String.valueOf(date.getSeconds());
			if (s.length() == 1)
				s = "0" + s;
			h += ":" + s;
		}
		setText(h);
	}

}