package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

public class StepsPanel extends BaseResponsivePanel {

	public static class Step {
		private int		step;
		private String	title;

		public int getStep() {
			return step;
		}

		public void setStep(int step) {
			this.step = step;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

	private int	activeStep	= 0;
	private int	maxStep		= 0;

	public StepsPanel() {
		super(DOM.createElement("ul"));
		getElement().setClassName("jexpStepsPanel steps");
	}

	public void addStep(Step step) {
		addStep(step.getStep(), step.getTitle());
	}

	public void addStep(int step, String title) {
		Element li = DOM.createElement("li");
		li.setAttribute("step", String.valueOf(step));
		Element span = DOM.createSpan();
		span.setClassName("step");
		span.setInnerHTML(String.valueOf(step));
		li.appendChild(span);
		span = DOM.createSpan();
		span.setClassName("title");
		span.setInnerHTML(title);
		li.appendChild(span);
		getElement().appendChild(li);
		maxStep = Math.max(maxStep, step);
	}

	public void setActiveStep(int step) {
		for (int i = 0; i < getElement().getChildCount(); i++) {
			Element el = (Element) getElement().getChild(i);
			int cand = Integer.valueOf(el.getAttribute("step"));
			if (cand == step) {
				el.setClassName("active");
				activeStep = step;
			} else if (cand < step) {
				el.setClassName("complete");
			} else {
				el.removeClassName("active");
				el.removeClassName("complete");
			}
		}
	}

	public int getActiveStep() {
		return activeStep;
	}

	public boolean hasPriorStep() {
		return activeStep > 1;
	}

	public boolean hasNextStep() {
		return activeStep < maxStep;
	}

}