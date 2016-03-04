package com.javexpress.gwt.library.ui.data.datatable;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;

/** A {@link Cell} used to render text. */
public class BoolCell extends AbstractSafeHtmlCell<Boolean> {

	static class BoolSafeHtmlRenderer implements SafeHtmlRenderer<Boolean> {

		private static BoolSafeHtmlRenderer	instance;

		public static BoolSafeHtmlRenderer getInstance() {
			if (instance == null) {
				instance = new BoolSafeHtmlRenderer();
			}
			return instance;
		}

		private BoolSafeHtmlRenderer() {
		}

		@Override
		public SafeHtml render(Boolean object) {
			return (object != null && object.booleanValue()) ? SafeHtmlUtils.fromString("&#10004;") : SafeHtmlUtils.EMPTY_SAFE_HTML;
		}

		@Override
		public void render(Boolean object, SafeHtmlBuilder appendable) {
			appendable.append(render(object));
		}
	}

	/** Constructs a TextCell that uses a {@link BoolSafeHtmlRenderer} to render
	 * its text. */
	public BoolCell() {
		super(BoolSafeHtmlRenderer.getInstance());
	}

	/** Constructs a TextCell that uses the provided {@link SafeHtmlRenderer} to
	 * render its text.
	 * 
	 * @param renderer
	 *            a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance */
	public BoolCell(SafeHtmlRenderer<Boolean> renderer) {
		super(renderer);
	}

	@Override
	public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(value);
		}
	}

}