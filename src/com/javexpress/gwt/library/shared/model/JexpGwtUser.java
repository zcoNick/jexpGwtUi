package com.javexpress.gwt.library.shared.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;

public class JexpGwtUser implements Serializable {

	public static transient JexpGwtUser	instance;

	private Long						id;
	private Long						orgUnitId;
	private String						fullName;
	private String						cultureCode;
	private String						currencySymbol;
	private String						dateFormat;
	private String						timeStampFormat;
	private String						timeStampFormatLong;
	private String						decimalFormat;
	private char						currencyGroupChar;
	private char						currencyDecimalChar;
	private String						sessionId;
	private String						companyName;
	private Map<String, Serializable>	clientVariables	= new HashMap<String, Serializable>();

	public Map<String, Serializable> getClientParams() {
		return clientVariables;
	}

	public void setClientVariables(Map<String, Serializable> clientVariables) {
		this.clientVariables = clientVariables;
	}

	public static Serializable getVariable(long moduleId, String key) {
		return instance.clientVariables.get(moduleId + "é" + key);
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(final String fullName) {
		this.fullName = fullName;
	}

	public String getCultureCode() {
		return cultureCode;
	}

	public void setCultureCode(final String cultureCode) {
		this.cultureCode = cultureCode;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(final String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public void setDateFormat(final String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setTimeStampFormat(final String timeStampFormat) {
		this.timeStampFormat = timeStampFormat;
	}

	public void setCurrencyGroupChar(final char currencyGroupChar) {
		this.currencyGroupChar = currencyGroupChar;
	}

	public void setCurrencyDecimalChar(final char currencyDecimalChar) {
		this.currencyDecimalChar = currencyDecimalChar;
	}

	public static char getCurrencyGroupChar() {
		return instance.currencyGroupChar;
	}

	public static char getCurrencyDecimalChar() {
		return instance.currencyDecimalChar;
	}

	public static String getDateFormat() {
		if (instance != null)
			return instance.dateFormat;
		String fmt = LocaleInfo.getCurrentLocale().getDateTimeConstants().dateFormats()[0].toUpperCase();
		if (fmt.indexOf("M") < fmt.indexOf("D"))
			fmt = "MM.dd.yyyy";
		else
			fmt = "dd.MM.yyyy";
		return fmt;
	}

	public static String getTimeStampFormat() {
		return instance.timeStampFormat;
	}

	public Long getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Long orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public JexpGwtUser() {
	}

	public JexpGwtUser(String sessionId, final Long id, final Long orgUnitId, final String fullName, final String cultureCode, final String currencySymbol, final char currencyGroupChar, final char currencyDecimalChar, final String dateFormat, final String timeStampFormat, final String timeStampFormatLong) {
		this.sessionId = sessionId;
		this.id = id;
		this.orgUnitId = orgUnitId;
		this.fullName = fullName;
		this.cultureCode = cultureCode;
		this.currencySymbol = currencySymbol;
		this.currencyGroupChar = currencyGroupChar;
		this.currencyDecimalChar = currencyDecimalChar;
		this.dateFormat = dateFormat;
		this.timeStampFormat = timeStampFormat;
		this.timeStampFormatLong = timeStampFormatLong;
		this.decimalFormat = "###" + currencyGroupChar + "###" + currencyDecimalChar + "##";
	}

	public static String formatDate(final Date cand) {
		return cand == null ? null : DateTimeFormat.getFormat(instance.dateFormat).format(cand);
	}

	public static String formatTimestamp(final Date cand) {
		return cand == null ? null : DateTimeFormat.getFormat(instance.timeStampFormat).format(cand);
	}

	public static String formatTimestampLong(final Date cand) {
		return cand == null ? null : DateTimeFormat.getFormat(instance.timeStampFormatLong).format(cand);
	}

	public static Date parseDate(String text) {
		if (text != null && text.length() > instance.dateFormat.length())
			text = text.substring(0, instance.dateFormat.length());
		return text == null ? null : DateTimeFormat.getFormat(instance.dateFormat).parse(text);
	}

	public static Date parseTimestamp(final String text) {
		return text == null ? null : DateTimeFormat.getFormat(instance.timeStampFormat).parse(text);
	}

	public static BigDecimal parseDecimal(String text) {
		if (getCurrencyDecimalChar() != '.') //TR ise
			text = text.replaceAll("\\.", "").replaceAll(",", ".");
		else
			text = text.replaceAll(",", ".");
		return new BigDecimal(text);
	}

	public static String formatDecimal(final BigDecimal cand) {
		NumberFormat nf = NumberFormat.getFormat("###,##0.00");
		String s = nf.format(cand);
		return s;
	}

	public static String formatDecimal(final BigDecimal cand, double precisionSize) {
		String precision = "0";
		for (int i = 1; i < precisionSize; i++)
			precision += "0";
		NumberFormat nf = NumberFormat.getFormat("###,##0." + precision);
		String s = nf.format(cand);
		return s;
	}

	public static void clear() {
		instance = null;
	}

	public static boolean is(Long id) {
		return id != null && instance.getId().equals(id);
	}

}