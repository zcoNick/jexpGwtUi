package com.javexpress.gwt.library.ui.bootstrap;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Focusable;
import com.javexpress.common.model.item.BpmAction;
import com.javexpress.common.model.item.FormDef;
import com.javexpress.common.model.item.type.Pair;
import com.javexpress.gwt.library.ui.AbstractContainerFocusable;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.facet.ProvidesAuthorization;
import com.javexpress.gwt.library.ui.form.IJiraEnabledForm;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.form.IUICompositeView;
import com.javexpress.gwt.library.ui.form.keyboard.KeyCode;
import com.javexpress.gwt.library.ui.js.JexpCallback;
import com.javexpress.gwt.library.ui.js.JsCache;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class UIComposite extends AbstractContainerFocusable implements IUIComposite {

	public final static JsCache<String, String>	viewRights	= new JsCache<String, String>(30);

	private IUICompositeView					attachedTo;

	protected UIComposite						that;
	private String								rights;
	private List<Command>						onLoadCommands;
	private List<Command>						onUnloadCommands;
	private HandlerRegistration					keyDownHandlerRegistration;

	private Command								closeCommand;

	private boolean								showing;

	private String								header;
	private ICssIcon							icon;
	private String								width;
	private Integer								xsSize;
	private Integer								smSize;
	private Integer								mdSize;
	private Integer								lgSize;
	private String								height;
	private String								helpIndex;
	private boolean								resizable	= true;
	private boolean								draggable	= true;
	private boolean								maximizable	= true;
	private FormDef								formDef;
	private Focusable							initialWidget;

	private KeyDownHandler						keyDownHandler;

	public UIComposite(String id) {
		super(DOM.createDiv());
		getElement().setId(id);
		getElement().addClassName("jexp-ui-form");

		that = this;
	}

	@Override
	public IUICompositeView getAttachedTo() {
		return attachedTo;
	}

	@Override
	public void setAttachedTo(IUICompositeView attachedTo) {
		this.attachedTo = attachedTo;
	}

	protected void createGUI() {
	}

	public Integer getXsSize() {
		return xsSize;
	}

	public void setXsSize(Integer xsSize) {
		this.xsSize = xsSize;
		if (isAttached() && attachedTo != null) {
			attachedTo.onResize();
		}
	}

	public Integer getSmSize() {
		return smSize;
	}

	public void setSmSize(Integer smSize) {
		this.smSize = smSize;
		if (isAttached() && attachedTo != null) {
			attachedTo.onResize();
		}
	}

	public Integer getMdSize() {
		return mdSize;
	}

	public void setMdSize(Integer mdSize) {
		this.mdSize = mdSize;
		if (isAttached() && attachedTo != null) {
			attachedTo.onResize();
		}
	}

	public Integer getLgSize() {
		return lgSize;
	}

	public void setLgSize(Integer lgSize) {
		this.lgSize = lgSize;
		if (isAttached() && attachedTo != null) {
			attachedTo.onResize();
		}
	}

	@Override
	public void setCloseHandler(Command command) {
		this.closeCommand = command;
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

	public Focusable getInitialWidget() {
		return initialWidget;
	}

	public void setInitialWidget(Focusable initialWidget) {
		this.initialWidget = initialWidget;
	}

	@Override
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
		if (isAttached() && attachedTo != null) {
			attachedTo.setHeader(icon, header);
		}
	}

	@Override
	public ICssIcon getIcon() {
		return icon;
	}

	public void setIcon(ICssIcon icon) {
		this.icon = icon;
	}

	@Override
	public String getWidth() {
		return width;
	}

	@Override
	public String getHeight() {
		return height;
	}

	@Override
	public void setWidth(String width) {
		this.width = width;
	}

	@Override
	public void setHeight(String height) {
		this.height = height;
	}

	@Override
	public FormDef getFormDef() {
		return formDef;
	}

	public void setFormDef(FormDef formDef) {
		this.formDef = formDef;
	}

	public void setHelpIndex(String helpIndex) {
		this.helpIndex = helpIndex;
	}

	@Override
	public String getHelpIndex() {
		return helpIndex != null ? helpIndex : getFormDef() != null ? getFormDef().getHelpIndex() : null;
	}

	@Override
	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	@Override
	public boolean isDraggable() {
		return draggable;
	}

	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	@Override
	public boolean isMaximizable() {
		return maximizable;
	}

	public void setMaximizable(boolean maximizable) {
		this.maximizable = maximizable;
	}

	protected Pair<Long, String> getAuthKey() {
		return null;
	}

	protected boolean handleCtrlSpecialKey(final int nativeKeyCode) {
		return false;
	}

	protected boolean handleAltSpecialKey(final int nativeKeyCode) {
		if (JsUtil.isBrowserIE() && KeyCode.IE_HELP.is(nativeKeyCode) ||
				!JsUtil.isBrowserIE() && KeyCode.NONIE_HELP.is(nativeKeyCode)) {
			ClientContext.instance.openHelp(that);
			return true;
		}
		return false;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		Pair<Long, String> authKey = getAuthKey();
		if (authKey != null && authKey.getLeft() != Long.MIN_VALUE && authKey.getRight().equals(" ")) {//if not anonymous
			final String key = authKey.getLeft() + ":" + authKey.getRight();
			String cached = viewRights.get(key);
			if (cached == null && ClientContext.instance instanceof ProvidesAuthorization) {
				((ProvidesAuthorization) ClientContext.instance).formYetkiListesi(authKey.getLeft(), authKey.getRight(), new JexpCallback<List<String>>() {
					@Override
					protected void onResult(final List<String> result) {
						String r = JsUtil.join(result, ",");
						viewRights.put(key, r);
						applyFormRights(r);
					}
				});
			} else
				applyFormRights(cached);
		} else
			applyFormRights(null);
		if (onLoadCommands != null)
			for (Command cmd : onLoadCommands)
				cmd.execute();
		keyDownHandler = new KeyDownHandler() {
			@Override
			public void onKeyDown(final KeyDownEvent event) {
				if (event.isAltKeyDown()) {
					if (handleAltSpecialKey(event.getNativeKeyCode())) {
						event.preventDefault();
						event.stopPropagation();
					}
				} else if (event.isControlKeyDown()) {
					if (handleCtrlSpecialKey(event.getNativeKeyCode())) {
						event.preventDefault();
						event.stopPropagation();
					}
				} else if (event.isShiftKeyDown() && event.getNativeKeyCode() == KeyCodes.KEY_F12) {
					if (that instanceof IJiraEnabledForm) {
						event.preventDefault();
						event.stopPropagation();
						((IJiraEnabledForm) that).openJiraIssue();
					}
				}
			}
		};
		keyDownHandlerRegistration = addKeyDownHandler(keyDownHandler);
	}

	@Override
	public KeyDownHandler getKeyDownHandler() {
		return keyDownHandler;
	}

	protected void applyFormRights(final String rights) {
		this.rights = rights != null ? rights : "@";
	}

	protected void assertHasRight(final String key) throws Exception {
		if ("@".equals(rights))
			return;
		if (rights == null || ("," + rights + ",").indexOf("," + key + ",") == -1)
			throw new Exception(ClientContext.nlsCommon.yetkiliDegilsiniz());
	}

	@Override
	protected void onUnload() {
		if (onUnloadCommands != null)
			for (Command cmd : onUnloadCommands)
				cmd.execute();
		header = null;
		icon = null;
		width = null;
		height = null;
		helpIndex = null;
		formDef = null;
		initialWidget = null;
		nullify();
		super.onUnload();
	}

	protected void nullify() {
		that = null;
		closeCommand = null;
		rights = null;
		onLoadCommands = null;
		onUnloadCommands = null;
		keyDownHandlerRegistration = null;
	}

	public void close() {
		if (closeCommand != null)
			closeCommand.execute();
	}

	public boolean isShowing() {
		return showing;
	}

	@Override
	public void onShow() {
		showing = true;
	}

	@Override
	public void onHide() {
		showing = false;
	}

	@Override
	public boolean isSupportsAction(byte action) {
		return false;
	}

	@Override
	public void performAction(byte action) throws Exception {
	}

	@Override
	public BpmAction getBpmAction() {
		return null;
	}

	@Override
	public boolean canClose() {
		return true;
	}

}