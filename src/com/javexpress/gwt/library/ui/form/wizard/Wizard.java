package com.javexpress.gwt.library.ui.form.wizard;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Focusable;
import com.javexpress.application.model.item.ControllerAction;
import com.javexpress.application.model.item.Result;
import com.javexpress.application.model.item.type.Pair;
import com.javexpress.gwt.fw.client.library.form.EditForm;
import com.javexpress.gwt.library.ui.ClientModule;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.JqIcon;
import com.javexpress.gwt.library.ui.SilkIcon;
import com.javexpress.gwt.library.ui.container.buttonbar.ButtonBar;
import com.javexpress.gwt.library.ui.container.layout.DivPanelInfo;
import com.javexpress.gwt.library.ui.data.IDataChangeListener;
import com.javexpress.gwt.library.ui.form.button.Button;
import com.javexpress.gwt.library.ui.js.JexpCallback;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class Wizard<CM extends ClientModule<?, ?, ?>, PK extends Serializable, T extends Serializable> extends EditForm<CM, PK, T> {

	private WizardPage					current;
	private Map<String, Serializable>	variables;
	private Button						btBack;
	private Button						btNext;
	private Button						btFinish;

	public Wizard(final String id, final String editingId, final ControllerAction mode, final Serializable extraData, final Pair<Long, String> overrideAuthKeyWith, final IDataChangeListener dataListener, final CM module) throws Exception {
		super(id, mode, overrideAuthKeyWith, dataListener, module);
	}

	@Override
	public ICssIcon getIcon() {
		return SilkIcon.wand;
	}

	@Override
	protected DivPanelInfo createCenterWidget() {
		current = getFirstPage();
		return DivPanelInfo.create(current);
	}

	protected abstract WizardPage getFirstPage();

	protected abstract Result canFinish(T dto);

	@Override
	protected DivPanelInfo createBottomWidget() {
		ButtonBar bb = new ButtonBar();
		btBack = new Button(that, "geri", "Geri");
		btBack.setIcon(JqIcon.arrowthick_1_w);
		btBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				goBack();
			}
		});
		bb.add(btBack);
		btNext = new Button(that, "ileri", "İleri");
		btNext.setIcon(JqIcon.arrowthick_1_e);
		btNext.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				goNext();
			}
		});
		bb.add(btNext);
		btFinish = new Button(that, "kaydet", "Kaydet");
		btFinish.setIcon(JqIcon.check);
		btFinish.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				Result result = canFinish(editing);
				if (result.isSucceded())
					try {
						finish();
					} catch (Exception e) {
						JsUtil.handleError(getId(), e);
					}
				else
					JsUtil.handleResult(getId(), result);
			}
		});
		bb.add(btFinish);
		Button btClose = new Button(that, "kapat", "Kapat");
		btClose.setIcon(JqIcon.close);
		btClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				closeWindow();
			}
		});
		bb.add(btClose);
		return DivPanelInfo.createEM(bb, 2);
	}

	private void goBack() {
		WizardPage newPage = null;
		if (current.hasBack())
			newPage = current.getBackPage();
		if (newPage != null) {
			setActivePage(newPage);
		}
		synchronizeButtons();
	}

	private void goNext() {
		WizardPage newPage = null;
		if (!current.canGoNext())
			return;
		if (current.hasNext())
			newPage = current.getNextPage();
		if (newPage != null) {
			setActivePage(newPage);
		}
		synchronizeButtons();
	}

	protected void finish() throws Exception {
		current.onPageDeactivated();
		updateDto(editing);
		requestPersist(editing, editingMode, new JexpCallback<PK>(btFinish) {
			@Override
			protected void onResult(final PK result) {
				if (dataListener != null)
					dataListener.refresh(result);
				closeWindow();
			}
		});
	}

	@Override
	protected void loadEditing(final String editingId) {
		super.loadEditing(editingId);
		synchronizeButtons();
	}

	private void synchronizeButtons() {
		btBack.setVisible(current.hasBack());
		btNext.setVisible(current.hasNext());
		btFinish.setVisible(!current.hasNext());
	}

	private void setActivePage(final WizardPage newPage) {
		current.onPageDeactivated();
		setCenterWidget(DivPanelInfo.create(newPage, false));
		current = newPage;
		current.onPageActivated();
		onPageChanged();
	}

	@Override
	protected void updateGUI(final T dto) {
		if (current != null)
			current.onPageActivated();
	}

	protected void onPageChanged() {
		Focusable f = current.getFocusWidget();
		if (f != null)
			f.setFocus(true);
	}

	public T getEditing() {
		return editing;
	}

	public void setVariable(final String key, final Serializable value) {
		if (variables == null)
			variables = new HashMap<String, Serializable>();
		variables.put(key, value);
	}

	public Serializable getVariable(final String key) {
		if (variables == null)
			return null;
		return variables.get(key);
	}

	@Override
	protected Focusable getFocusWidget() {
		return current.getFocusWidget();
	}

	@Override
	protected void onUnload() {
		current = null;
		variables = null;
		btBack = null;
		btNext = null;
		btFinish = null;
		super.onUnload();
	}

}