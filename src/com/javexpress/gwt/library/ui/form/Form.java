package com.javexpress.gwt.library.ui.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RequiresResize;
import com.javexpress.application.model.item.BpmAction;
import com.javexpress.application.model.item.FormDef;
import com.javexpress.application.model.item.type.Pair;
import com.javexpress.gwt.fw.client.GwtBootstrapApplication;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.container.panel.SimplePanelFocusable;
import com.javexpress.gwt.library.ui.dialog.NewJiraIssueDialog;
import com.javexpress.gwt.library.ui.form.keyboard.KeyCode;
import com.javexpress.gwt.library.ui.js.JexpCallback;
import com.javexpress.gwt.library.ui.js.JsCache;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class Form extends SimplePanelFocusable implements IWindow, IUIComposite {

	public final static JsCache<String, String>	formRights				= new JsCache<String, String>(30);

	private IWindowContainer					window;
	protected Form								that;
	private String								rights;
	private boolean								highlightInputElement	= !JsUtil.isBrowserChrome();
	private List<Command>						onLoadCommands;
	private List<Command>						onUnloadCommands;
	private HandlerRegistration					keyDownHandlerRegistration;

	private boolean								showing;

	@Override
	public void setCloseHandler(Command command) {
	}

	public IWindowContainer getWindow() {
		return window;
	}

	@Override
	public abstract FormDef getFormDef();

	public Form() {
		that = this;
		addStyleName("jexpBorderBox");
	}

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public boolean isMaximizable() {
		return true;
	}

	/** DESIGNER:VALUEFUNCTION{PROPERTY=PROP_TITLE,OF=this} */
	@Override
	public abstract String getHeader();

	/** DESIGNER:VALUEFUNCTION{PROPERTY=PROP_WIDTH,OF=this} */
	@Override
	public String getWidth() {
		return "400px";
	}

	/** DESIGNER:VALUEFUNCTION{PROPERTY=PROP_HEIGHT,OF=this} */
	@Override
	public String getHeight() {
		return "300px";
	}

	public Form(final String id) {
		this();
		JsUtil.ensureId(null, that, WidgetConst.FORM_PREFIX, id);
	}

	protected abstract void createGUI() throws Exception;

	@Override
	public String getId() {
		return getElement().getId();
	}

	public boolean isHighlightInputElement() {
		return highlightInputElement;
	}

	public void setHighlightInputElement(boolean highlightInputElement) {
		this.highlightInputElement = highlightInputElement;
	}

	@Override
	public void setWindowContainer(final IWindowContainer window) {
		this.window = window;
	}

	public void closeWindow() {
		IWindowContainer container = getWindow();
		if (container == null)
			return;
		if (onUnloadCommands != null)
			for (Command command : onUnloadCommands)
				command.execute();
		container.close();
	}

	protected static Long toLong(final Serializable extraData) {
		return extraData == null ? null : Long.valueOf(extraData.toString());
	}

	protected static String toStr(final Serializable extraData) {
		return extraData == null ? null : extraData.toString();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		Pair<Long, String> authKey = getAuthKey();
		if (authKey != null && authKey.getLeft() != Long.MIN_VALUE && authKey.getRight().equals(" ")) {//if not anonymous
			final String key = authKey.getLeft() + ":" + authKey.getRight();
			String cached = formRights.get(key);
			if (cached == null)
				GwtBootstrapApplication.sistemClient.getService().formYetkiListesi(authKey.getLeft(), authKey.getRight(), new JexpCallback<List<String>>() {
					@Override
					protected void onResult(final List<String> result) {
						String r = JsUtil.join(result, ",");
						formRights.put(key, r);
						applyFormRights(r);
					}
				});
			else
				applyFormRights(cached);
		} else
			applyFormRights(null);
		if (onLoadCommands != null)
			for (Command cmd : onLoadCommands)
				cmd.execute();
		keyDownHandlerRegistration = addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(final KeyDownEvent event) {
				if (KeyCode.CLOSE.is(event.getNativeKeyCode()))
					closeWindow();
				else if (event.isAltKeyDown()) {
					if (handleAltSpecialKey(event.getNativeKeyCode())) {
						event.preventDefault();
						event.stopPropagation();
					}
				} else if (event.isControlKeyDown())
					if (handleCtrlSpecialKey(event.getNativeKeyCode())) {
						event.preventDefault();
						event.stopPropagation();
					}
			}
		});
	}

	@Override
	protected void nullify() {
		keyDownHandlerRegistration = null;
		window = null;
		that = null;
		onLoadCommands = null;
		onUnloadCommands = null;
		super.nullify();
	}

	@Override
	protected void onUnload() {
		if (highlightInputElement)
			JsUtil.detachHighlightListeners(getElement());
		keyDownHandlerRegistration.removeHandler();
		super.onUnload();
	}

	protected void applyFormRights(final String rights) {
		this.rights = rights != null ? rights : "@";
	}

	protected void assertHasRight(final String key) throws Exception {
		if ("@".equals(rights))
			return;
		if (rights == null || ("," + rights + ",").indexOf("," + key + ",") == -1)
			throw new Exception(IFormFactory.nlsCommon.yetkiliDegilsiniz());
	}

	protected boolean handleCtrlSpecialKey(final int nativeKeyCode) {
		return false;
	}

	protected boolean handleAltSpecialKey(final int nativeKeyCode) {
		if (JsUtil.isBrowserIE() && KeyCode.IE_HELP.is(nativeKeyCode) ||
				!JsUtil.isBrowserIE() && KeyCode.NONIE_HELP.is(nativeKeyCode)) {
			openHelp();
			return true;
		}
		return false;
	}

	public void openHelp() {
		String url = GWT.getHostPageBaseURL() + "showHelp?i=" + getHelpIndex();
		JsUtil.openWindow(url, null);
	}

	protected void openJiraIssue(IJiraEnabledForm form) {
		NewJiraIssueDialog.open(form);
	}

	protected Pair<Long, String> getAuthKey() {
		return null;
	}

	protected void setHeader(final String header) {
		getWindow().setHeader(header);
	}

	@Override
	public void onResize() {
		if (getWidget() instanceof RequiresResize)
			((RequiresResize) getWidget()).onResize();
	}

	@Override
	public void onShow() {
		showing = true;
		if (highlightInputElement)
			JsUtil.attachHighlightListeners(getElement());
	}

	@Override
	public void onHide() {
		showing = false;
	}

	public boolean isShowing() {
		return showing;
	}

	@Override
	public ICssIcon getIcon() {
		return null;
	}

	@Override
	public String getHelpIndex() {
		return getFormDef() != null ? getFormDef().getHelpIndex() : null;
	}

	@Override
	public void addOnLoadCommand(Command command) {
		if (onLoadCommands == null)
			onLoadCommands = new ArrayList<Command>();
		onLoadCommands.add(command);
	}

	@Override
	public void addOnUnloadCommand(Command command) {
		if (onUnloadCommands == null)
			onUnloadCommands = new ArrayList<Command>();
		onUnloadCommands.add(command);
	}

	@Override
	public BpmAction getBpmAction() {
		return null;
	}

	@Override
	public boolean isDraggable() {
		return true;
	}

	@Override
	public boolean isSupportsAction(byte actInsertrecord) {
		return false;
	}

	@Override
	public void performAction(byte action) throws Exception {
	}

}