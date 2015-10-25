package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

public class StepsPanel extends BaseResponsivePanel {

	public static class Step {
		private String	step;
		private String	title;

		public String getStep() {
			return step;
		}

		public void setStep(String step) {
			this.step = step;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

	private boolean	showStepNumbers	= true;
	private String	activeStep;

	public boolean isShowStepNumbers() {
		return showStepNumbers;
	}

	public void setShowStepNumbers(boolean showStepNumbers) {
		this.showStepNumbers = showStepNumbers;
	}

	public StepsPanel() {
		super(DOM.createElement("ul"));
		getElement().setClassName("jexpStepsPanel steps");
	}

	public void addStep(Step step) {
		addStep(step.getStep(), step.getTitle());
	}

	public void addStep(String step, String title) {
		insertStep(-1, step, title);
	}

	public void insertStep(int index, String step, String title) {
		Element li = DOM.createElement("li");
		li.setAttribute("step", step);
		Element indexSpan = showStepNumbers ? DOM.createSpan() : null;
		if (indexSpan != null) {
			indexSpan.setClassName("step");
			li.appendChild(indexSpan);
		}
		Element span = DOM.createSpan();
		span.setClassName("title");
		span.setInnerHTML(title);
		li.appendChild(span);
		if (index == -1) {
			if (showStepNumbers)
				indexSpan.setInnerHTML(String.valueOf(getElement().getChildCount() + 1));
			getElement().appendChild(li);
		} else {
			if (showStepNumbers) {
				for (int i = index; i < getElement().getChildCount() - 1; i++) {
					Element nl = (Element) getElement().getChild(index);
					Element ns = (Element) nl.getChild(0);
					ns.setInnerHTML(String.valueOf(i + 1));
				}
			}
			getElement().insertBefore(li, getElement().getChild(index));
		}
	}

	public void setActiveStep(String step) {
		boolean asCompleted = true;
		for (int i = 0; i < getElement().getChildCount(); i++) {
			Element el = (Element) getElement().getChild(i);
			String cand = el.getAttribute("step");
			if (cand.equals(step)) {
				el.setClassName("active");
				activeStep = step;
				asCompleted = false;
			} else if (asCompleted) {
				el.setClassName("complete");
			} else {
				el.removeClassName("active");
				el.removeClassName("complete");
			}
		}
	}

	public String getActiveStep() {
		return activeStep;
	}

	public boolean hasPriorStep() {
		return getPriorStep() != null;
	}

	public boolean hasNextStep() {
		return getNextStep() != null;
	}

	public String getNextStep() {
		String next = null;
		for (int i = getElement().getChildCount() - 1; i > -1; i--) {
			Element el = (Element) getElement().getChild(i);
			String cand = el.getAttribute("step");
			if (cand.equals(activeStep))
				return next;
			else
				next = cand;
		}
		return next;
	}

	public String getFirstStep() {
		if (getElement().getChildCount() == 0)
			return null;
		Element el = (Element) getElement().getChild(0);
		return el.getAttribute("step");
	}

	public String getPriorStep() {
		String prior = null;
		for (int i = 0; i < getElement().getChildCount(); i++) {
			Element el = (Element) getElement().getChild(i);
			String cand = el.getAttribute("step");
			if (cand.equals(activeStep))
				return prior;
			else
				prior = cand;
		}
		return prior;
	}

}