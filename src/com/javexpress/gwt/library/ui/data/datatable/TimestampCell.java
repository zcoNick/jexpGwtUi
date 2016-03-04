package com.javexpress.gwt.library.ui.data.datatable;

import java.util.Date;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.ui.data.datatable.BoolCell.BoolSafeHtmlRenderer;

/** A {@link Cell} used to render text. */
public class TimestampCell extends AbstractSafeHtmlCell<Date> {

	static class TimestampHtmlRenderer implements SafeHtmlRenderer<Date> {

		private static TimestampHtmlRenderer	instance;

		public static TimestampHtmlRenderer getInstance() {
			if (instance == null) {
				instance = new TimestampHtmlRenderer();
			}
			return instance;
		}

		private TimestampHtmlRenderer() {
		}

		@Override
		public SafeHtml render(Date object) {
			return (object != null) ? SafeHtmlUtils.fromString(JexpGwtUser.formatTimestamp(object)) : SafeHtmlUtils.EMPTY_SAFE_HTML;
		}

		@Override
		public void render(Date object, SafeHtmlBuilder appendable) {
			appendable.append(render(object));
		}
	}

	/** Constructs a TextCell that uses a {@link BoolSafeHtmlRenderer} to render
	 * its text. */
	public TimestampCell() {
		super(TimestampHtmlRenderer.getInstance());
	}

	/** Constructs a TextCell that uses the provided {@link SafeHtmlRenderer} to
	 * render its text.
	 * 
	 * @param renderer
	 *            a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance */
	public TimestampCell(SafeHtmlRenderer<Date> renderer) {
		super(renderer);
	}

	@Override
	public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(value);
		}
	}

}