package com.javexpress.gwt.library.ui.form.keyboard;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyEvent;

public enum KeyCode {

	// http://www.cambiaresearch.com/articles/15/javascript-char-codes-key-codes
	CLOSE('X', 27), EDIT('D', 68), APPEND('E', 69), FILTER('F', 70), SAVE('K',
			75), LOG('L', 76),
	PRINT('P', 80), DELETE('Q', 81), DUPLICATE('V', 86), REFRESH('Y', 89), IE_HELP(
			'?', 223), NONIE_HELP('?', 170);

	public char	key;
	public int	code;

	private KeyCode(final char key, final int code) {
		this.key = key;
		this.code = code;
	}

	public boolean is(final int nativeKeyCode) {
		return code == nativeKeyCode;
	}

	public static boolean isEnterKey(KeyEvent event) {
		return isKeyEventEquals(event, KeyCodes.KEY_ENTER);
	}

	private static boolean isKeyEventEquals(KeyEvent event, int key) {
		return event.getNativeEvent().getKeyCode() == key;
	}
}