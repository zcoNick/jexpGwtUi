package com.javexpress.gwt.library.ui.data.datatable;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.ui.data.datatable.BoolCell.BoolSafeHtmlRenderer;

/** A {@link Cell} used to render text. */
public class NumberCell extends AbstractSafeHtmlCell<Number> {

	static class NumberSafeHtmlRenderer implements SafeHtmlRenderer<Number> {

		private static NumberSafeHtmlRenderer	instance;

		public static NumberSafeHtmlRenderer getInstance() {
			if (instance == null) {
				instance = new NumberSafeHtmlRenderer();
			}
			return instance;
		}

		private NumberSafeHtmlRenderer() {
		}

		@Override
		public SafeHtml render(Number object) {
			return (object != null) ? SafeHtmlUtils.fromString(JexpGwtUser.formatNumber(object)) : SafeHtmlUtils.EMPTY_SAFE_HTML;
		}

		@Override
		public void render(Number object, SafeHtmlBuilder appendable) {
			appendable.append(render(object));
		}
	}

	/** Constructs a TextCell that uses a {@link BoolSafeHtmlRenderer} to render
	 * its text. */
	public NumberCell() {
		super(NumberSafeHtmlRenderer.getInstance());
	}

	/** Constructs a TextCell that uses the provided {@link SafeHtmlRenderer} to
	 * render its text.
	 * 
	 * @param renderer
	 *            a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance */
	public NumberCell(SafeHtmlRenderer<Number> renderer) {
		super(renderer);
	}

	@Override
	public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(value);
		}
	}

}