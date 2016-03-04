package com.javexpress.gwt.library.ui.data.datatable;

import java.math.BigDecimal;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.ui.data.datatable.BoolCell.BoolSafeHtmlRenderer;

/** A {@link Cell} used to render text. */
public class BigDecimalCell extends AbstractSafeHtmlCell<BigDecimal> {

	static class BigDecimalSafeHtmlRenderer implements SafeHtmlRenderer<BigDecimal> {

		private static BigDecimalSafeHtmlRenderer	instance;

		public static BigDecimalSafeHtmlRenderer getInstance() {
			if (instance == null) {
				instance = new BigDecimalSafeHtmlRenderer();
			}
			return instance;
		}

		private BigDecimalSafeHtmlRenderer() {
		}

		@Override
		public SafeHtml render(BigDecimal object) {
			return (object != null) ? SafeHtmlUtils.fromString(JexpGwtUser.formatDecimal(object)) : SafeHtmlUtils.EMPTY_SAFE_HTML;
		}

		@Override
		public void render(BigDecimal object, SafeHtmlBuilder appendable) {
			appendable.append(render(object));
		}
	}

	/** Constructs a TextCell that uses a {@link BoolSafeHtmlRenderer} to render
	 * its text. */
	public BigDecimalCell() {
		super(BigDecimalSafeHtmlRenderer.getInstance());
	}

	/** Constructs a TextCell that uses the provided {@link SafeHtmlRenderer} to
	 * render its text.
	 * 
	 * @param renderer
	 *            a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance */
	public BigDecimalCell(SafeHtmlRenderer<BigDecimal> renderer) {
		super(renderer);
	}

	@Override
	public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(value);
		}
	}

}